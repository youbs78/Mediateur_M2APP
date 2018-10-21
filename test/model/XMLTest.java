package model;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class XMLTest {
    private XML srcXML = XML.getInstance();
    private static final String reqMed_1 =  " SELECT Enseignant.ID-Enseignant as id, Enseignant.Nom as nom, " +
                                                    "Enseignant.Prenom as prenom, Enseignant.adresseMail as adresseMail " +
                                            " FROM Enseignant ; ";

    private static final String reqSrc_1 =  null;

    @Test
    public void getInstance() {
        Assert.assertEquals(XML.class, XML.getInstance().getClass());
    }

    @Test
    public void connexion() {
        this.srcXML.connexion();
        Assert.assertNotNull(this.srcXML.getDocXML());
        this.srcXML.deconnexion();
    }

    @Test
    public void deconnexion() {
        // Connexion obligatoire avant
        this.srcXML.connexion();
        this.srcXML.deconnexion();
        Assert.assertNull( this.srcXML.getDocXML());
    }

    @Test
    public void setMediateurReq() {
        String reqMed = "test1";
        this.srcXML.setMediateurReq(reqMed);
        Assert.assertEquals(reqMed.toLowerCase(), this.srcXML.getMedSQL());
    }

    @Test
    public void reqMedtoReqSrc() {
        this.srcXML.setMediateurReq(reqMed_1);
        Assert.assertEquals(reqSrc_1, this.srcXML.reqMedtoReqSrc());
    }

    @Test
    public void executeReq() {
        this.srcXML.connexion();
        this.srcXML.executeReq("reqMed_1");
        this.srcXML.executeReq("reqMed_2");
        this.srcXML.executeReq("reqMed_3");
    /*
        Assert.assertNotNull(this.srcXML.getNodeList());
        // Vérifie s'il existe au moins un résultat
        Assert.assertNotEquals(this.srcXML.getNodeList().getLength(), 0);
    */
        this.srcXML.deconnexion();
    }

    @Test
    public void getResFromExecuteReq() {

    }

    @Test
    public void tradResToMed() {

    }

    @Test
    public void lire_XML() {
        this.srcXML.lire_XML();
    }
}