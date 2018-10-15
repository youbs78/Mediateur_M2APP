package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Pour ajouter des tests unitaires, simplement faire Alt+Enter
// sur le nom de la classe et "Create test"
public class MySQL {
    // Singleton pour bonne pratique
    private static MySQL INSTANCE = new MySQL();
    private Connection conn;

    private static MySQL getInstance(){
        return INSTANCE;
    }

    public boolean connexion(){
        try {
            /*  The newInstance() call is a work around for some
                broken Java implementations */
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }
        catch (ClassNotFoundException ex) {
            System.err.println("Erreur de chargement du driver.");
            return false;
        }
        catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            System.err.println("Erreur de chargement du driver.");
            return false;
        }

        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/id_td1?autoReconnect=true&useSSL=false", "root", "");
            //"jdbc:oracle:thin:@172.19.255.3:1521:MIAGE"
            System.out.println("Connexion réussie");
            return true;
        }
        catch (SQLException ex) {
            System.err.println("Erreur de connexion à la base de données.");
            return false;
        }
    }

    public boolean deconnexion() {
        try {
            this.conn.close();
            System.out.println("Deconnexion réussie");
            return true;
        } catch (SQLException ex) {
            System.err.println("Erreur de deconnexion à la base de données.");
            return false;
        }
    }

}