package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MySQL {
    private Connection conn;

    public MySQL() {
        super();
    }

    public void connexion() throws SQLException, ClassNotFoundException {
        try {
            /*  The newInstance() call is a work around for some
                broken Java implementations */
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException ex) {
            System.err.println("Erreur de chargement du driver.");
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/id_td1?autoReconnect=true&useSSL=false", "root", "");
            //"jdbc:oracle:thin:@172.19.255.3:1521:MIAGE"
            System.out.println("Connexion réussie");

        } catch (SQLException ex) {
            System.err.println("Erreur de connexion à la base de données.");
        }
    }

    public void deconnexion() {
        try {
            this.conn.close();
            System.out.println("Deconnexion réussie");
        } catch (SQLException ex) {
            System.err.println("Erreur de deconnexion à la base de données.");
        }
    }

}