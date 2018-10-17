package model;

import contract.ExtracteurItf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Classe Extracteur/Adaptateur pour la source de donnees Excel
 *
 * @author ayoub
 * @version 0.1
 */
public class Excel implements ExtracteurItf {
    // Singleton pour bonne pratique
    private static Excel INSTANCE = new Excel();
    private Connection conn;    // Objet connexion une fois celle-ci établie
    private Statement statement;    // Objet statement une fois la connexion etablie

    private Excel() {
    }

    public static Excel getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Excel();
        }
        return INSTANCE;
    }

    @Override
    public void connexion() {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            //Class.forName("com.hxtt.sql.excel.ExcelDriver");

        } catch (ClassNotFoundException ex) {
            System.err.println("Erreur de chargement du driver.");
        }
        try {
            this.conn = DriverManager.getConnection("jdbc:odbc:data.Source1", "", "");
            statement = conn.createStatement();
            System.out.println("Connexion etablie");

        } catch (SQLException ex) {
            System.err.println("Excel Erreur de connexion à la base de données.");
        }
    }

    @Override
    public void deconnexion() {
        try {
            this.conn.close();
            System.out.println("Connexion terminée");
        } catch (SQLException ex) {
            System.err.println("Excel Erreur de deconnexion à la base de données");
        }
    }

    @Override
    public void setMediateurReq(String reqMed) {

    }

    @Override
    public String reqMedtoReqSrc(String reqMed) {
        return null;
    }

    @Override
    public void executeReq(String reqSrc) {

    }

    @Override
    public List<HashMap<String, Object>> getResFromExecuteReq() {
        return null;
    }

    @Override
    public List<HashMap<String, Object>> tradResToMed(List<HashMap<String, Object>> resSrc) {
        return null;
    }

    // Affiche les erreurs quand la connexion a echoué
    public void displaySQLErrors(SQLException e) {
        System.out.println("SQLException: " + e.getMessage());
        System.out.println("SQLStatus: " + e.getSQLState());
        System.out.println("CodeErreur: " + e.getErrorCode());
    }


    //Execution d'une requête
    public ResultSet requeteSQL(String requete) {
        ResultSet resultat = null;
        try {
            resultat = statement.executeQuery(requete);
        } catch (SQLException e) {
            displaySQLErrors(e);
        }
        return resultat;
    }
}
