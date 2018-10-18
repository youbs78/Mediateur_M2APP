package model;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class XMLTest {
    private XML srcXML = XML.getInstance();
    private static final String reqMed_1 =  "SELECT Enseignant.ID-Enseignant, Enseignant.Nom, Enseignant.Prenom, Enseignant.adresseMail FROM Enseignant";
    private static final String reqSrc_1 =  "select enseignants.enseignant.numens, enseignants.enseignant.nom, enseignants.enseignant.prenom, enseignants.enseignant.adressemail from enseignant";

    @Test
    public void setMediateurReq() {
        String reqMed = "Test1";
        this.srcXML.setMediateurReq(reqMed);
        Assert.assertEquals(reqMed.toLowerCase(), this.srcXML.getMedSQL());
    }

    @Test
    public void connection() {
        try {
            this.srcXML.connexion();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.println("XMLTest-lire_XML s'est bien déroulé !");
    }

    @Test
    public void reqMedtoReqSrc() {
        this.srcXML.setMediateurReq(reqMed_1);
        Assert.assertEquals(reqSrc_1, this.srcXML.reqMedtoReqSrc());
    }

    @Test
    public void executeReq() throws SQLException {
        this.srcXML.executeReq("select Enseignants.Enseignant.Enseignements.Enseigne.Nb_heures from Enseignant");
    }


    @Test
    public void lire_XML() {
        try {
            this.srcXML.lire_XML("data/Univ_BD_3.xml");
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        // System.out.println("XMLTest-lire_XML s'est bien déroulé !");
    }
}