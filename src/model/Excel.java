package model;

import contract.ExtracteurItf;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
    private Connection conn;    // Objet connexion une fois celle-ci �tablie.
    private Statement statement;    // Objet statement une fois la connexion etablie.
    private ResultSet   rset;   // Repr�sente le r�sultat de la requ�te (recordSet)
    private String      medSQL; // Requ�te SQL envoy� par le mediateur
    
    // Cr�ation de la table de correspondance en static final pour �viter modification
    
    //Nous allons cr�er deux tables de correspondance, l'une pour les requetes du mediateur 
    //et l'autre pour les correspondances entre les colonnes renvoy�s pas les requetes mediateur et les requetes sources 
    private static final HashMap <String, String> TABLE_CORRESPONDANCE_requetes = new HashMap<>();
    static {
        //Table de correspondance repondant � la question 2 : Implantation des requetes SQL
    	TABLE_CORRESPONDANCE_requetes.put( " SELECT Enseignant.ID-Enseignant as id, Enseignant.Nom as nom, Enseignant.Prenom as prenom, SUM(Cours.Heures) as heures " +
                " FROM   Enseignant, Enseigne, Cours " +
                " WHERE  Enseignant.ID-Enseignant = Enseigne.ID-Enseignant " +
                "   AND  Cours.ID-Cours = Enseigne.ID-Cours  " +
                " GROUP BY Enseignant.ID-Enseignant ;", "select Distinct ID, Nom, Prenom, 0 as Heures FROM T_Excel_2006, T_Excel_2007 where Statut='enseignant' ;"
                										 				);
    	TABLE_CORRESPONDANCE_requetes.put(" SELECT COUNT(Etudiant.ID-Etudiant) as nb_etudiant_francais " +
                				 " FROM   Etudiant " +
                				 " WHERE  Etudiant.Provenance = 'France'; ", 
        																	"SELECT count(DISTINCT ID) FROM T_Excel_2006, T_Excel_2007 WHERE Provenance ='France' and Statut='etudiant' ");
				        	
    	TABLE_CORRESPONDANCE_requetes.put("SELECT Cours.Type as type, COUNT(Cours.Id-Cours) as nb_cours_par_type " +
                				 " FROM Cours " +
                				 " GROUP BY Cours.Type; ", 
                				 						  " SELECT Type_Cours , COUNT( Distinct Id_Cours)" +
                				 						  " FROM  T_Excel_2006, T_Excel_2007 " +
        												  " GROUP BY Type_Cours; ");

    }

    private static final HashMap <String, String> TABLE_CORRESPONDANCE = new HashMap<>();
    static {
        // region Table Etudiant
    	TABLE_CORRESPONDANCE.put("enseignant.prenom", "t_excel_2006.prenom");
    	TABLE_CORRESPONDANCE.put("enseignant.nom", "t_excel_2006.nom");
    	TABLE_CORRESPONDANCE.put("enseignant.id-enseignant", "t_excel_2006.id");
    	TABLE_CORRESPONDANCE.put("cours.heures", "heures");
    	
    	
    	TABLE_CORRESPONDANCE.put("etudiant.id-etudiant", "count(distinct id)");
        TABLE_CORRESPONDANCE.put("cours.id-cours", "count(distinct id_cours)");
        TABLE_CORRESPONDANCE.put("cours.type-cours", "t_excel_2006.type_cours");
  
    }

    /** Constructeur red�fini comme �tant priv� pour interdire
     *  son appel et forcer a� passer par la m�thode
     *  Il marche comme un constructeur normal
     */
    private Excel() {
    }

    public static Excel getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Excel();
        }
        return INSTANCE;
    }
    
    public Connection getConn() {
        return conn;
    }
    public Statement getStmt() {
        return statement;
    }
    public ResultSet getRset() {
        return rset;
    }

    public String getMedSQL() {
        return medSQL;
    }

    @Override
    public void connexion() {
        String rootProject = System.getProperty("user.dir").replaceAll("\\\\", "/");
        String partialPath = "/data/Source1.xls";
        String absoluteFilePath = rootProject.concat(partialPath);

    	try
		{
            String driver = "com.hxtt.sql.excel.ExcelDriver";
			Class.forName(driver).newInstance();
		}
		catch (Exception ex)
		{
			System.err.println("Erreur de chargement du driver.");
		}
		try
		{
			String url = "jdbc:Excel:/";
			url = url.concat(absoluteFilePath);
			
			this.conn = DriverManager.getConnection(url,"","");
			statement = conn.createStatement();
			statement.setFetchSize(10);
			
		}
		catch (SQLException ex)
		{
			System.err.println("Excel Erreur de connexion � la base de donn�es.");
		}
		
    }

    @Override
    public void deconnexion() {
		try
		{
			this.statement.close();
			this.conn.close();
			System.out.println("Connexion termin�e avec Source 1");
		
		}
		catch (SQLException ex)
		{
			System.err.println("Excel Erreur de deconnexion � la base de donn�es");
		    
		}
    }
    
	// Affiche les erreurs quand la connexion a echou�
	public void displaySQLErrors(SQLException e)
 {
		System.out.println("SQLException: " + e.getMessage());
		System.out.println("SQLStatus: " + e.getSQLState());
		System.out.println("CodeErreur: " + e.getErrorCode());
 }

	
	//Execution d'une requ�te
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
    	this.medSQL = reqMed;
    	//System.out.println("Requete du mediateur transmis � l'extracteur : " + this.medSQL);
    }

    @Override
    public String reqMedtoReqSrc() {
       // On associe la requete du mediateur a la requete source
        String reqSrc = this.medSQL;

        // Traitement de la requete source : Parcours la table de correspondance afin d'effectuer les correspondances
        for (HashMap.Entry<String, String> entry : TABLE_CORRESPONDANCE_requetes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // Remplace la valeur cl� trouv�
            // par sa valeur correspondante � la source
            reqSrc = reqSrc.replace(key, value);
        }
        System.out.println("Transformation de la requete du mediateur pour l'extracteur : " +reqSrc);
        return reqSrc;
       
    	
 
    }

    @Override
    public void executeReq(String reqSrc) {
    	   try {
               if (this.statement != null) {
                   // Le stmt.close ferme automatiquement le rset.
                   this.statement.close();
               }
               this.statement = this.conn.createStatement();
               this.rset = statement.executeQuery(reqSrc);

           } catch (SQLException e) {
        	   displaySQLErrors(e);
           }
		
    }

    @Override
    public List<HashMap<String, Object>> getResFromExecuteReq() {
        List<HashMap<String, Object>> rows = new ArrayList<>();

        try{
            ResultSetMetaData metaData = this.rset.getMetaData();
            int columnCount = metaData.getColumnCount();
            // Cr�e une liste de hashmap afin d'associer chaque colonne � sa valeur
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
            System.out.println("Resultat de l'extracteur : ");
            for(HashMap<String, Object> element : rows){
                System.out.println(element.toString());
            }

            return rows;

        } catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

	@Override
	public List<HashMap<String, Object>> tradResToMed(List<HashMap<String, Object>> resSrc) {
        List<HashMap<String, Object>> resMed = new ArrayList<>();

        // Loop sur la List<HashMap<>>
        for (HashMap<String, Object> row : resSrc) {
            // Parcours la table de correspondance afin d'effectuer les correspondances
            // On va convertir les noms de colonne source en nom de colonne global
            for (HashMap.Entry<String, String> entry : TABLE_CORRESPONDANCE.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                /*  On retire la ligne avec le nom de colonne correspondant � la source
                    puis on le remet avec le nom de colonne correspondant au mediateur
                    On v�rifie que l'entr�e correspond avant de la retirer */
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
