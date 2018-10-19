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
    private static final String reqSrc_1 =  "  ";  //TODO: Faire la requête XPATH

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
        String reqMed = "Test1";
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
        /*
        this.srcMySQL.connexion();
        this.srcMySQL.executeReq(reqSrc_1);

        Assert.assertNotNull(this.srcMySQL.getResFromExecuteReq());
        // TODO: Vérifier le résultat de la requête exemple également

        this.srcMySQL.deconnexion();
        */
    }

    @Test
    public void tradResToMed() {
        /*
        List<HashMap<String, Object>> res;
        List<HashMap<String, Object>> tradRes;

        this.srcMySQL.connexion();
        this.srcMySQL.executeReq(reqSrc_1);
        res = this.srcMySQL.getResFromExecuteReq();
        tradRes = this.srcMySQL.tradResToMed(res);

        Assert.assertNotEquals(0, tradRes.size());

        for (HashMap<String, Object> row : tradRes) {
            //On compare les colonnes attendues avec celles obtenues
            for(String e : key_tradReq_1){
                // On voti s'il existe une valeur attribué à la clef
                Assert.assertNotNull(row.getOrDefault(e,null));
            }
        }

        this.srcMySQL.deconnexion();
        */
    }

    @Test
    public void lire_XML() {
        try {
            this.srcXML.lire_XML();
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        // System.out.println("XMLTest-lire_XML s'est bien déroulé !");
    }
}