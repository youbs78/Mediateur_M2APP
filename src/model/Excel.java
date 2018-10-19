package model;

import contract.ExtracteurItf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe Extracteur/Adaptateur pour la source de donnees Excel
 *
 * @author Ayoub
 * @version 1.0
 */
public class Excel implements ExtracteurItf {
    // Singleton pour bonne pratique
    private static Excel INSTANCE = new Excel();
    private Connection conn;    // Objet connexion une fois celle-ci Ã©tablie
    private Statement statement;    // Objet statement une fois la connexion etablie.

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
    	try
		{
			Class.forName("com.hxtt.sql.excel.ExcelDriver").newInstance();
		}
		catch (Exception ex)
		{
			System.err.println("Erreur de chargement du driver.");
		}
		try
		{
			String url = "jdbc:Excel:/D:/Source1.xls";
			
			this.conn = DriverManager.getConnection(url,"","");
			statement = conn.createStatement();
			statement.setFetchSize(10);
			System.out.println("Connexion etablie");
			
		}
		catch (SQLException ex)
		{
			System.err.println("Excel Erreur de connexion à la base de données.");
		}
		
    }

    @Override
    public void deconnexion() {
		try
		{
			this.statement.close();
			this.conn.close();
			System.out.println("Connexion terminée");
		
		}
		catch (SQLException ex)
		{
			System.err.println("Excel Erreur de deconnexion à la base de données");
		    
		}
    }
    
	// Affiche les erreurs quand la connexion a echoué
	public void displaySQLErrors(SQLException e)
 {
		System.out.println("SQLException: " + e.getMessage());
		System.out.println("SQLStatus: " + e.getSQLState());
		System.out.println("CodeErreur: " + e.getErrorCode());
 }

	
//Execution d'une requête
public ResultSet requeteSQL(String requete)
{
	ResultSet resultat = null;
	try
	{
		resultat = statement.executeQuery(requete);
	}
	catch(SQLException e)
	{
		displaySQLErrors(e);
	}
	return resultat;
}

    @Override
    public void setMediateurReq(String reqMed) {

    }

    @Override
    public String reqMedtoReqSrc() {
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
		// TODO Auto-generated method stub
		return null;
	}

 



}
