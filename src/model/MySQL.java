package model;

import contract.ExtracteurItf;
import domain.Cours;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Pour ajouter des tests unitaires, simplement faire Alt+Enter
// sur le nom de la classe et "Create test"
public class MySQL implements ExtracteurItf {
    // Singleton pour bonne pratique
    private static MySQL INSTANCE = null;
    private Connection conn;
    // Représente une instruction SQL
    private Statement stmt;

    // SQL Query
    private static final String QUERY_FIND_ALL_COURS = "SELECT * FROM cours";

    private MySQL(){}

    public static MySQL getInstance(){
        if(INSTANCE == null){
            INSTANCE = new MySQL();
        }
        return INSTANCE;
    }

    @Override
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
            return true;
        }
        catch (SQLException ex) {
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

    }

    @Override
    public String reqMedtoReqSrc(String reqMed) {
        return null;
    }

    @Override
    public void executeReq(String reqSrc) {

    }

    @Override
    public List<Object> getResFromExecuteReq(String req) {
        return null;
    }

    @Override
    public List<Object> tradResToMed(List<Object> resSrc) {
        return null;
    }

    public List<Cours> findAllCours(){
        List<Cours> cours = new ArrayList<Cours>();

        try {
            this.stmt = this.conn.createStatement();
            ResultSet rset = this.stmt.executeQuery(MySQL.QUERY_FIND_ALL_COURS);

            while(rset.next()){
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