package client;


import model.MySQL;

import java.sql.SQLException;

public class Mediateur {
    public static void main(String[] args){
        System.out.println("Hello World");

        MySQL sourceMySQL = new MySQL();

        try {
            sourceMySQL.connexion();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        sourceMySQL.deconnexion();

    }
}
