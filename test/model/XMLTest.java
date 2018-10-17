package model;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XMLTest {
    private XML srcXML = XML.getInstance();

    @Test
    public void lire_XML() {
        try {
            this.srcXML.lire_XML("src/data/Univ_BD_3.xml");
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        // System.out.println("XMLTest-lire_XML s'est bien déroulé !");
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
    public void executeReq() {
        try {
            this.srcXML.executeReq("Test");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.println("XMLTest-lire_XML s'est bien déroulé !");
    }
}