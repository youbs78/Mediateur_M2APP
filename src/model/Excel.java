package model;

import contract.ExcelItf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Excel implements ExcelItf{
	private Connection conn;	// Objet connexion une fois celle-ci établie
	private Statement statement;	// Objet statement une fois la connexion etablie
	
	public Excel()
	{
		super();
	}


	@Override
	public boolean connexion()
	{
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			//Class.forName("com.hxtt.sql.excel.ExcelDriver");

		}
		catch (ClassNotFoundException ex)
		{
			System.err.println("Erreur de chargement du driver.");
		}
		try
		{
			this.conn = DriverManager.getConnection("jdbc:odbc:data.Source1","", "");
			statement = conn.createStatement();
			System.out.println("Connexion etablie");
			return true;
		}
		catch (SQLException ex)
		{
			System.err.println("Excel Erreur de connexion à la base de données.");
		}
		return false;
	}

	@Override
	public boolean deconnexion()
	{
		try
		{
			this.conn.close();
			System.out.println("Connexion terminée");
			return true;
		}
		catch (SQLException ex)
		{
			System.err.println("Excel Erreur de deconnexion à la base de données");
		    return false;
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
}
