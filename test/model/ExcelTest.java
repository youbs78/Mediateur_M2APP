package model;


import java.sql.*;
import java.sql.ResultSet;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Excel;



class ExcelTest {

	private Excel srcExcel = new Excel();

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
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
	 System.out.println("ExcelTest-Deconnexion s'est bien déroulé !");
	 
	}
	
	  @Test
	    public void testRequeteSQL() {
		  try {
		  this.srcExcel.connexion();
		  
		  ResultSet rs = this.srcExcel.requeteSQL("select * from test");
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
}
