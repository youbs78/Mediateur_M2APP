package model;

import contract.ExtracteurItf;
import domain.Cours;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private static HashMap <String, String> tableCorrespondance = new HashMap<String, String>() ;
    private Connection  conn;
    private Statement   stmt;   // Représente une instruction SQL
    private ResultSet   rset;   // Représente le résultat de la requête (recordSet)
    private String      medSQL; // Requête SQL envoyé par le mediateur


    /** Constructeur redéfini comme étant privé pour interdire
     *  son appel et forcer à passer par la méthode
     */
    private MySQL() {
        // Initialisation de la table de correspondance une seule fois.
        this.generateTableCorrespondance();
    }

    public static MySQL getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MySQL();
        }
        return INSTANCE;
    }

    /**
     * Méthode pour générer la table de correspondance dans la hashmap,
     * elle sera appelé une fois dans le constructeur
     */
    private void generateTableCorrespondance(){
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
        tableCorrespondance.put("enseignant.adressemail", "'Source 2' as enseignant.adressemail");
        // endregion

        // region Table Cours
        tableCorrespondance.put("cours.id-cours", "cours.numcours");
        tableCorrespondance.put("cours.libele", "cours.libele");
        tableCorrespondance.put("cours.type", "cours.type");
        tableCorrespondance.put("cours.niveau", "cours.niveau");
        tableCorrespondance.put("cours.heures", "0 as cours.heures");
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
        tableCorrespondance.put("enseigne.heures", "0 as enseigne.heures");
        // endregion
    }

    @Override
    public boolean connexion() {
        try {
            /*  The newInstance() call is a work around for some
                broken Java implementations */
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException ex) {
            System.err.println("Erreur de chargement du driver.");
            return false;
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            System.err.println("Erreur de chargement du driver.");
            return false;
        }

        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/id_td1?autoReconnect=true&useSSL=false", "root", "");
            //"jdbc:oracle:thin:@172.19.255.3:1521:MIAGE"
            return true;
        } catch (SQLException ex) {
            System.err.println("Erreur de connexion à la base de données.");
            return false;
        }
    }

    @Override
    public boolean deconnexion() {
        try {
            this.conn.close();
            return true;
        } catch (SQLException ex) {
            System.err.println("Erreur de deconnexion à la base de données.");
            return false;
        }
    }

    @Override
    public void setMediateurReq(String reqMed) {
        this.medSQL = reqMed.toLowerCase();
    }

    @Override
    public String reqMedtoReqSrc(String reqMed) {


    }

    @Override
    public void executeReq(String reqSrc) {
        try {
            this.stmt = this.conn.createStatement();
            this.rset = this.stmt.executeQuery(reqSrc);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (this.stmt != null) {
                try {
                    // Le stmt.close ferme automatiquement le rset.
                    this.stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<Object> getResFromExecuteReq(String req) {
        List<Object> resultList = new ArrayList<Object>();

        return null;
    }

    @Override
    public List<Object> tradResToMed(List<Object> resSrc) {
        return null;
    }

    public List<Cours> findAllCours() {
        List<Cours> cours = new ArrayList<Cours>();

        try {
            this.stmt = this.conn.createStatement();
             rset = this.stmt.executeQuery(MySQL.QUERY_FIND_ALL_COURS);

            while (rset.next()) {
                Cours c = new Cours();
                c.setId_cours(rset.getInt("NumCours"));
                c.setLibele(rset.getString("libele"));
                c.setNiveau(rset.getString("niveau"));
                c.setType(rset.getString("TYPE"));
                cours.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (this.stmt != null) {
                try {
                    // Le stmt.close ferme automatiquement le rset.
                    this.stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return cours;
    }

}