package model;

import contract.ExtracteurItf;

import java.sql.*;
import java.util.*;

/**
 * Classe Extracteur/Adaptateur pour la source de donnees MySQL (originellement Oracle)
 *
 * @author kevin
 * @version 0.1
 */
// Pour ajouter des tests unitaires, simplement faire Alt+Enter
// sur le nom de la classe et "Create test"
public class MySQL implements ExtracteurItf {
    private static MySQL INSTANCE = null; // Singleton pour bonne pratique
    private Connection  conn;
    private Statement   stmt;   // Représente une instruction SQL
    private ResultSet   rset;   // Représente le résultat de la requête (recordSet)
    private String      medSQL; // Requête SQL envoyé par le mediateur
    // Création de la table de correspondance en static final pour éviter modification
    private static final HashMap <String, String> tableCorrespondance = new HashMap<String, String>();
    static {
        // region Table Etudiant
        tableCorrespondance.put("etudiant.id-etudiant", "etudiant.id_etudiant");
        tableCorrespondance.put("etudiant.nom", "etudiant.nom");
        tableCorrespondance.put("etudiant.prenom", "etudiant.prenom");
        tableCorrespondance.put("etudiant.provenance", "etudiant.provenance");
        tableCorrespondance.put("etudiant.paysformationprecedente", "etudiant.pays_formation_precedente");
        tableCorrespondance.put("etudiant.anneedebut", "etudiant.annee_debut");
        tableCorrespondance.put("etudiant.age", "year(curdate())-year(etudiant.datenaissance)");
        tableCorrespondance.put("etudiant.niveauinsertion", "etudiant.niveau_inscription");
        // endregion
        // region Table Enseignant
        tableCorrespondance.put("enseignant.id-enseignant", "enseignant.id_ens");
        tableCorrespondance.put("enseignant.nom", "enseignant.nom");
        tableCorrespondance.put("enseignant.prenom", "enseignant.prenom");
        tableCorrespondance.put("enseignant.adressemail", "'Source 2' as adressemail");
        // endregion
        // region Table Cours
        tableCorrespondance.put("cours.id-cours", "cours.numcours");
        tableCorrespondance.put("cours.libele", "cours.libele");
        tableCorrespondance.put("cours.type", "cours.type");
        tableCorrespondance.put("cours.niveau", "cours.niveau");
        tableCorrespondance.put("cours.heures", "0 as heures");
        // endregion
        // region Table Inscription
        tableCorrespondance.put("inscription.id-etudiant", "inscription.numet");
        tableCorrespondance.put("inscription.id-cours", "inscription.numcours");
        tableCorrespondance.put("inscription.annee", "inscription.annee");
        tableCorrespondance.put("inscription.note", "inscription.note_cours");
        // endregion
        // region Table Enseigne
        tableCorrespondance.put("enseigne.id-enseignant", "enseigne.numens");
        tableCorrespondance.put("enseigne.id-cours", "enseigne.numcours");
        tableCorrespondance.put("enseigne.annee", "enseigne.annee");
        tableCorrespondance.put("enseigne.heures", "0 as heures");
        // endregion
    }

    /** Constructeur redéfini comme étant privé pour interdire
     *  son appel et forcer à passer par la méthode
     *  Il marche comme un constructeur normal
     */
    private MySQL() {
    }

    public static MySQL getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MySQL();
        }
        return INSTANCE;
    }

    public String getMedSQL() {
        return medSQL;
    }

    public Connection getConn() {
        return conn;
    }

    public Statement getStmt() {
        return stmt;
    }

    public ResultSet getRset() {
        return rset;
    }

    @Override
    public void connexion() {
        try {
            /*  The newInstance() call is a work around for some
                broken Java implementations */
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException ex) {
            System.err.println("Erreur de chargement du driver.");
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            System.err.println("Erreur de chargement du driver.");
        }

        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/id_td1?autoReconnect=true&useSSL=false", "root", "");
        } catch (SQLException ex) {
            System.err.println("Erreur de connexion à la base de données.");
        }
    }

    @Override
    public void deconnexion() {
        try {
            if (this.stmt != null) {
                // Le stmt.close ferme automatiquement le rset.
                this.stmt.close();
            }
            this.conn.close();
            this.conn = null;
        } catch (SQLException ex) {
            System.err.println("Erreur de deconnexion à la base de données.");
        }
    }

    @Override
    public void setMediateurReq(String reqMed) {
        this.medSQL = reqMed.toLowerCase();
    }

    @Override
    public String reqMedtoReqSrc(String reqMed) {
        // Mise en minuscule de la requête médiateur pour
        // aider avec la table de correspondance
        String reqSrc = reqMed.toLowerCase();

        // Parcourt la table de correspondance afin d'effectuer les correspondances
        for (HashMap.Entry<String, String> entry : tableCorrespondance.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // Remplace la valeur clé trouvée
            // par sa valeur correspondante à la source
            reqSrc = reqSrc.replace(key, value);
        }

        return reqSrc;
    }

    @Override
    public void executeReq(String reqSrc) {
        try {
            if (this.stmt != null) {
                // Le stmt.close ferme automatiquement le rset.
                this.stmt.close();
            }
            this.stmt = this.conn.createStatement();
            this.rset = this.stmt.executeQuery(reqSrc);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<HashMap<String, Object>> getResFromExecuteReq() {
        List<HashMap<String, Object>> rows = new ArrayList<HashMap<String, Object>>();

        try{
            ResultSetMetaData metaData = this.rset.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Crée une liste de hashmap afin d'associer chaque colonne à sa valeur
            while (this.rset.next()) {
                HashMap<String, Object> columns = new LinkedHashMap<String, Object>();

                for (int i = 1; i <= columnCount; i++) {
                    // le nom de la colonne sera mis en miniscule pour la table de correspondance
                    columns.put(metaData.getColumnLabel(i).toLowerCase(), this.rset.getObject(i));
                }

                rows.add(columns);
            }

            return rows;

        } catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<HashMap<String, Object>> tradResToMed(List<HashMap<String, Object>> resSrc) {
        List<HashMap<String, Object>> resMed = new ArrayList<HashMap<String, Object>>();

        // Loop sur la List<HashMap<>>
        for (HashMap<String, Object> row : resSrc) {
            // Parcourt la table de correspondance afin d'effectuer les correspondances
            // On va convertir les noms de colonne source en nom de colonne global
            for (HashMap.Entry<String, String> entry : tableCorrespondance.entrySet()) {
                // On va ignorer les préfixes de table

                // TODO: A FAIRE
                String key = entry.getKey().split("\\.")[1];
                String value = entry.getValue().split("\\.")[1];
                /*
                    On retire la ligne avec le nom de colonne correspondant à la source
                    puis on le remet avec le nom de colonne correspondant au mediateur
                 */
                Object obj = row.remove(value);
                row.put(key, obj);
            } //endloop: tableCorrespondance.entrySet()

            // Alimente la liste traduite pour le mediateur
            resMed.add(row);
        } //endloop: resSrc

        return resMed;
    }

}