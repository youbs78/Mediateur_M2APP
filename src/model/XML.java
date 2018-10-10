package model;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XML {

    //Connection à la source
    public void lire_XML (String path_fichier) throws FileNotFoundException, SAXException, IOException, ParserConfigurationException{

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        org.w3c.dom.Document doc = builderFactory.newDocumentBuilder().parse(new FileInputStream(path_fichier));
        Node node ;
        Element E, E_1;

        //Liste des étudiants
        NodeList etudiants = ((org.w3c.dom.Document)doc).getElementsByTagName("Etudiant");
        NodeList L ;
        for (int index =0 ; index < etudiants.getLength(); index++)
        {
            //Un étudiant
            E = (Element) etudiants.item(index);
            L = E.getElementsByTagName("NumEt");
            E_1 = (Element) L.item(0); // un seul noeud NumEt
            System.out.println("num etudiant "+E_1.getTextContent());
            L = E.getElementsByTagName("nom");
            E_1 = (Element) L.item(0);
            System.out.println("num etudiant "+E_1.getTextContent());
        }

        //Liste des enseignants

        //Liste des cours
    }
}

//Assurer l'exécution des différentes requêtes

//Permettre l'envoi et la réception de requêtes depuis/vers le médiateur
