package model;



import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.junit.Assert;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;



class ExcelTest {


	 private Excel srcExcel = Excel.getInstance();
	  private static final String reqMed_1 =  " SELECT Enseignant.ID-Enseignant as id, Enseignant.Nom as nom, Enseignant.Prenom as prenom, SUM(Cours.Heures) as heures " +
				 " FROM   Enseignant, Enseigne, Cours " +
				 " WHERE  Enseignant.ID-Enseignant = Enseigne.ID-Enseignant " +
				 "  AND  Cours.ID-Cours = Enseigne.ID-Cours  " +
				 " GROUP BY Enseignant.ID-Enseignant ;";
	private static final String reqSrc_1 =  "Aucune Correspondance avec la Source 0";
	private static final String reqSrc_2 =  "SELECT count(DISTINCT ID) FROM T_Excel_2006, T_Excel_2007 WHERE Provenance ='France' and Statut='etudiant' ";
	private static final List<String> key_tradReq_1 = new ArrayList<>();
	static{
	//region Liste colonne traduite depuis reqSrc_2 attendu
	key_tradReq_1.add("etudiant.id-etudiant");
	//endregion
	}

	@Test
	void connexon() {
		 this.srcExcel.connexion();
	     Assert.assertNotNull(this.srcExcel.getConn());
         System.out.println("ExcelTest-Connexion s'est bien deroule !");
         
	}
	
	@Test
	public void deconnexion() {
	 // Connexion obligatoire avant
	 this.srcExcel.connexion();
	 this.srcExcel.deconnexion();
	 Assert.assertNull(this.srcExcel.getConn());
	 System.out.println("ExcelTest-Deconnexion s'est bien d?roul? !");
	 
	}
	
	  @Test
	public void testRequeteSQL() {
		  try {
		  this.srcExcel.connexion();
		  
		  ResultSet rs = this.srcExcel.requeteSQL("select '' from T_Excel_2006");
		   ResultSetMetaData resultSetMetaData = rs.getMetaData();
           int iNumCols = resultSetMetaData.getColumnCount();
           for (int i = 1; i <= iNumCols; i++) {
               System.out.println(resultSetMetaData.getColumnLabel(i)
                                  + "  " +
                                  resultSetMetaData.getColumnTypeName(i));
           }

           Object colval;
           while (rs.next()) {
               for (int i = 1; i <= iNumCols; i++) {
                   colval = rs.getObject(i);
                   System.out.print(colval + "  ");
               }
               System.out.println();
           }
           
           this.srcExcel.deconnexion();
		  }
		  catch (Exception e) {
	            System.out.println(e.getMessage());
	            e.printStackTrace();
	        }

		 
	  }
	  
	  @Test
	public void setMediateurReq() {
	        String reqMed = "Test1";
	        this.srcExcel.setMediateurReq(reqMed);
	        Assert.assertEquals(reqMed, this.srcExcel.getMedSQL());
	 
	    }
	  
	  @Test
	public void reqMedtoReqSrc() {
	        this.srcExcel.setMediateurReq(reqMed_1);
	        Assert.assertEquals(reqSrc_1, this.srcExcel.reqMedtoReqSrc());
	    }
	  
	@Test
	public void executeReq() throws SQLException {
	        this.srcExcel.connexion();

	        this.srcExcel.executeReq(reqSrc_2);
	        Assert.assertNotNull(this.srcExcel.getStmt());
	        // Vérifie s'il existe au moins un résultat
	        Assert.assertTrue(this.srcExcel.getRset().next());

	        this.srcExcel.deconnexion();
	    }
	  
	@Test
	public void getResFromExecuteReq() {

	        this.srcExcel.connexion();
	        this.srcExcel.executeReq(reqSrc_2);

	        Assert.assertNotNull(this.srcExcel.getResFromExecuteReq());
	        // TODO: V?rifier le r?sultat de la requ?te exemple ?galement
	        this.srcExcel.deconnexion();
	    }
	
	   @Test
	    public void tradResToMed() {
	        List<HashMap<String, Object>> res;
	        List<HashMap<String, Object>> tradRes;

	        this.srcExcel.connexion();
	        this.srcExcel.executeReq(reqSrc_2);
	        res = this.srcExcel.getResFromExecuteReq();
	        tradRes = this.srcExcel.tradResToMed(res);

	        Assert.assertNotEquals(0, tradRes.size());

	        for (HashMap<String, Object> row : tradRes) {
	            //On compare les colonnes attendues avec celles obtenues
	            for(String e : key_tradReq_1){
	                // On voit s'il existe une valeur attribu? ? la clef
	                Assert.assertNotNull(row.getOrDefault(e,null));
	            }
	        }

	        this.srcExcel.deconnexion();
	    }

}
