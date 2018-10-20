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
    private static final HashMap <String, String> TABLE_CORRESPONDANCE = new HashMap<>();
    static {
        // region Table Etudiant
        TABLE_CORRESPONDANCE.put("etudiant.id-etudiant", "etudiant.id_etudiant");
        TABLE_CORRESPONDANCE.put("etudiant.nom", "etudiant.nom");
        TABLE_CORRESPONDANCE.put("etudiant.prenom", "etudiant.prenom");
        TABLE_CORRESPONDANCE.put("etudiant.provenance", "etudiant.provenance");
        TABLE_CORRESPONDANCE.put("etudiant.formationprecedente", "etudiant.formationprecedente");
        TABLE_CORRESPONDANCE.put("etudiant.paysformationprecedente", "etudiant.pays_formation_precedente");
        TABLE_CORRESPONDANCE.put("'france'", "'fr'");
        TABLE_CORRESPONDANCE.put("'allemagne'", "'de'");
        TABLE_CORRESPONDANCE.put("'italie'", "'it'");
        TABLE_CORRESPONDANCE.put("etudiant.anneedebut", "etudiant.annee_debut");
        TABLE_CORRESPONDANCE.put("etudiant.age", "year(curdate())-year(etudiant.datenaissance)");
        TABLE_CORRESPONDANCE.put("etudiant.niveauinsertion", "etudiant.niveau_inscription");
        // endregion
        // region Table Enseignant
        TABLE_CORRESPONDANCE.put("enseignant.id-enseignant", "enseignant.id_ens");
        TABLE_CORRESPONDANCE.put("enseignant.nom", "enseignant.nom");
        TABLE_CORRESPONDANCE.put("enseignant.prenom", "enseignant.prenom");
        TABLE_CORRESPONDANCE.put("enseignant.adressemail", "'Source 2'");
        // endregion
        // region Table Cours
        TABLE_CORRESPONDANCE.put("cours.id-cours", "cours.numcours");
        TABLE_CORRESPONDANCE.put("cours.libele", "cours.libele");
        TABLE_CORRESPONDANCE.put("cours.type", "cours.type");
        TABLE_CORRESPONDANCE.put("cours.niveau", "cours.niveau");
        TABLE_CORRESPONDANCE.put("cours.heures", "0");
        // endregion
        // region Table Inscription
        TABLE_CORRESPONDANCE.put("inscription.id-etudiant", "inscription.numet");
        TABLE_CORRESPONDANCE.put("inscription.id-cours", "inscription.numcours");
        TABLE_CORRESPONDANCE.put("inscription.annee", "inscription.annee");
        TABLE_CORRESPONDANCE.put("inscription.note", "inscription.note_cours");
        // endregion
        // region Table Enseigne
        TABLE_CORRESPONDANCE.put("enseigne.id-enseignant", "enseigne.numens");
        TABLE_CORRESPONDANCE.put("enseigne.id-cours", "enseigne.numcours");
        TABLE_CORRESPONDANCE.put("enseigne.annee", "enseigne.annee");
        TABLE_CORRESPONDANCE.put("enseigne.heures", "0");
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
            String BD_DRIVER = "com.mysql.jdbc.Driver";
            Class.forName(BD_DRIVER).newInstance();
        } catch (ClassNotFoundException ex) {
            System.err.println("Erreur de chargement du driver.");
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            System.err.println("Erreur de chargement du driver.");
        }

        try {
            String BD_URL = "jdbc:mysql://localhost:3306/id_td1?autoReconnect=true&useSSL=false";
            String BD_USER = "root";
            String BD_MDP = "";
            this.conn = DriverManager.getConnection(BD_URL, BD_USER, BD_MDP);
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
    public String reqMedtoReqSrc() {
        // Mise en minuscule de la requête médiateur pour
        // aider avec la table de correspondance
        String reqSrc = this.medSQL.toLowerCase();
        if(reqSrc.isEmpty()){
            System.out.println("Erreur: Pas de requête médiateur chargée !");
            return null;
        }

        // Parcourt la table de correspondance afin d'effectuer les correspondances
        for (HashMap.Entry<String, String> entry : TABLE_CORRESPONDANCE.entrySet()) {
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
        List<HashMap<String, Object>> rows = new ArrayList<>();

        try{
            ResultSetMetaData metaData = this.rset.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Crée une liste de hashmap afin d'associer chaque colonne à sa valeur
            while (this.rset.next()) {
                HashMap<String, Object> columns = new HashMap<>();

                for (int i = 1; i <= columnCount; i++) {
                    // On veut avoir le nom de table pour la correspondance
                    StringBuilder nouvelleCle = new StringBuilder();
                    if(!metaData.getTableName(i).isEmpty()){
                        nouvelleCle.append(metaData.getTableName(i))
                                   .append(".");
                    }
                    nouvelleCle.append(metaData.getColumnLabel(i));
                    // le nom de la colonne sera mis en miniscule pour la table de correspondance
                    columns.put(nouvelleCle.toString().toLowerCase(), this.rset.getObject(i));
                }

                rows.add(columns);
            }

            return rows;

        } catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public List<HashMap<String, Object>> tradResToMed(List<HashMap<String, Object>> resSrc) {
        List<HashMap<String, Object>> resMed = new ArrayList<>();

        // Loop sur la List<HashMap<>>
        for (HashMap<String, Object> row : resSrc) {
            // Parcourt la table de correspondance afin d'effectuer les correspondances
            // On va convertir les noms de colonne source en nom de colonne global
            for (HashMap.Entry<String, String> entry : TABLE_CORRESPONDANCE.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                /*  On retire la ligne avec le nom de colonne correspondant à la source
                    puis on le remet avec le nom de colonne correspondant au mediateur
                    On vérifie que l'entrée correspond avant de la retirer */
                if(row.get(value)!=null){
                    Object obj = row.remove(value);
                    row.put(key, obj);
                }
            } //endloop: TABLE_CORRESPONDANCE.entrySet()

            // Alimente la liste traduite pour le mediateur
            resMed.add(row);
        } //endloop: resSrc

        return resMed;
    }

}