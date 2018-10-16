package model;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XMLTest {
    private XML srcXML = new XML();

    @Test
    public void lire_XML() {
        // TODO: Je ne sais plus comment faire des assert pour faire échouer les tests mdr
        try {
            this.srcXML.lire_XML("src/data/Univ_BD_3.xml");
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        // System.out.println("XMLTest-lire_XML s'est bien déroulé !");
    }
}