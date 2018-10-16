package model;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.Assert.*;

public class XMLTest {
    private XML srcXML = new XML();

    @Test
    public void lire_XML() {
        try {
            this.srcXML.lire_XML("Univ_BD_3.xml");
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        // System.out.println("XMLTest-lire_XML s'est bien déroulé !");
    }
}