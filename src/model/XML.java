package model;

import contract.ExtracteurItf;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Classe Extracteur/Adaptateur pour la source de donnees XML
 *
 * @author arnold
 * @version 0.1
 */
public class XML implements ExtracteurItf {
    private static XML INSTANCE = new XML(); //Singleton pour bonne pratique
    private String medSQL; //Requête SQL envoyé par le médiateur
    private static final HashMap <String, String> TABLE_CORRESPONDANCE = new HashMap<>(); //Table de correspondance en static final pour éviter toutes modifications
    static {
        // region Table Etudiant
        TABLE_CORRESPONDANCE.put("etudiant.id-etudiant","etudiants.etudiant.numet");
        TABLE_CORRESPONDANCE.put("etudiant.nom","etudiants.etudiant.nom");
        TABLE_CORRESPONDANCE.put("etudiant.prenom","'Source 3'");
        TABLE_CORRESPONDANCE.put("etudiant.nom","etudiants.etudiant.provenance");
        TABLE_CORRESPONDANCE.put("etudiant.provenance","etudiants.etudiant.provenance");
        TABLE_CORRESPONDANCE.put("etudiant.formationprecedente","etudiants.etudiant.formationprecedante");
        TABLE_CORRESPONDANCE.put("etudiant.paysformationprecedente","etudiants.etudiant.pays_formation_precedante");
        TABLE_CORRESPONDANCE.put("etudiant.anneedebut","etudiants.etudiant.anneedebut");
        TABLE_CORRESPONDANCE.put("etudiant.age","etudiants.etudiant.dateNaissance");
        TABLE_CORRESPONDANCE.put("etudiant.niveauinsertion","etudiants.etudiant.niveau_insertion");
        //endregion
        // region Table Enseignant
        TABLE_CORRESPONDANCE.put("enseignant.id-enseignant", "enseignants.enseignant.numens");
        TABLE_CORRESPONDANCE.put("enseignant.nom", "enseignants.enseignant.nom");
        TABLE_CORRESPONDANCE.put("enseignant.prenom", "enseignants.enseignant.prenom");
        TABLE_CORRESPONDANCE.put("enseignant.adressemail", "enseignants.enseignant.adressemail");
        //endregion
        // region Table Cours
        TABLE_CORRESPONDANCE.put("cours.id-cours", "cours.cours.id_cours");
        TABLE_CORRESPONDANCE.put("cours.libele", "'Source 3'");
        TABLE_CORRESPONDANCE.put("cours.type", "cours.cours.type");
        TABLE_CORRESPONDANCE.put("cours.niveau", "cours.cours.niveau");
        TABLE_CORRESPONDANCE.put("cours.heures", "cours.cours.nb_heures");
        // endregion
        // region Table Inscription
        TABLE_CORRESPONDANCE.put("inscription.id-etudiant", "etudiants.etudiants.inscriptions.inscription.numet");
        TABLE_CORRESPONDANCE.put("inscription.id-cours", "etudiants.etudiants.inscriptions.inscription.id_cours");
        TABLE_CORRESPONDANCE.put("inscription.annee", "etudiants.etudiants.inscriptions.inscription.annee_universitaire");
        TABLE_CORRESPONDANCE.put("inscription.note", "etudiants.etudiants.inscriptions.inscription.note_cours");
        // endregion
        // region Table Enseigne
        TABLE_CORRESPONDANCE.put("enseigne.id-enseignant", "enseignants.enseignant.enseignements.enseigne.numens");
        TABLE_CORRESPONDANCE.put("enseigne.id-cours", "enseignants.enseignant.enseignements.enseigne.id_cours");
        TABLE_CORRESPONDANCE.put("enseigne.annee", "enseignants.enseignant.enseignements.enseigne.annee_universitaire");
        TABLE_CORRESPONDANCE.put("enseigne.heures", "enseignants.enseignant.enseignements.enseigne.nb_heures");
        // endregion


    }

    //Constructeur par défaut
    private XML() {
    }

    //Accesseurs
    public static XML getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new XML();
        }
        return INSTANCE;
    }

    public String getMedSQL() {
        return medSQL;
    }

    @Override
    public void setMediateurReq(String reqMed) {
        this.medSQL = reqMed.toLowerCase(); //Convertit la chaîne reqMed en minuscules pour permettre la mise en correspondance avec la table de correspondance de la source XML plus tard
    }

    @Override
    public void connexion() {
        try {
            lire_XML("data/Univ_BD_3.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deconnexion() { }

    @Override
    public String reqMedtoReqSrc() {
        if(!this.getMedSQL().isEmpty()) {
            String reqSrc = this.medSQL;

            // Parcourt la table de correspondance afin d'effectuer les correspondances
            for (HashMap.Entry<String, String> entry : TABLE_CORRESPONDANCE.entrySet()) {
                //Récupère clé, valeur (correspondance source)
                String key = entry.getKey();
                String value = entry.getValue();

                //Remplace la valeur clé trouvée par sa valeur correspondante à la source
                reqSrc = reqSrc.replace(key, value);
            }
            return reqSrc;
        }else {
            System.out.println("La requête du médiateur n'est pas définie ! \n. La fonction reqMedtoReqSrc retourne null.");
            return null;
        }
    }


    @Override
    public void executeReq(String reqSrc) {
        //XPath est utilisé pour le requêtage des données
        //XPath est un langage de requête pour localiser une portion d'un document XML
        NodeList L ;
        Element E, E_1;
        String node, element, path="";
        String[] req, nodes;

        /* Découpe la requête source pour la définition de l'expression xPath (identifier le noeud dans lequel on va chercher (node)
         * et l'élement qu'on souhaite afficher (element).*/
        req = reqSrc.split("from"); //Séparation des informations du select et des informations du from
        node = req[0].split("select")[1]; //Séparation de l'instruction de sélection et de l'information pour ne disposer que de la dernière

        //Définit l'expression xPath et l'élement
        node = node.replace('.', '/'); //Définition d'un nouveau délimiteur
        nodes = node.split("/"); //Découpage des sous-noeuds pour construire le chemin du noeud l'expression xPath. Ce dernier ne prend pas en compte l'élement recherché.
        element= nodes[nodes.length-1]; //Définit l'élément recherché

        for(int i=0;i<nodes.length-1;i++){ //Construction du chemin du noeud
            path+="/"+nodes[i];
        }

        //Formatage des informations
        element = element.trim();
        path = (("/"+path).replaceAll("\\s+","")).trim(); //Enlève les espaces

        System.out.println("Requête source : "+ reqSrc);
        System.out.println("Expression xPath : "+path);
        System.out.println("Elément recherché : "+element+"\n");

        try {
            //Connexion à la source
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            Document doc = builderFactory.newDocumentBuilder().parse(new FileInputStream("data/Univ_BD_3.xml"));

            //Définit la requête
            XPath xPath = XPathFactory.newInstance().newXPath();

            //Exemple de requête pour récupérer une liste
            //String expression = "//Etudiants/Etudiant";

            //Exemple de requête ciblée

            String expression = path;

            //Exécute la requête XPATH
            //Récupère le résultat de l'éxécution
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

            //SOP DEBUG ONLY
            System.out.println("Résultat de la requête : ");

            //Affichage du résultat
            if(nodeList.getLength()>0){
                for (int index =0 ; index < nodeList.getLength(); index++)
                {
                    //Noeud
                    E = (Element) nodeList.item(index);

                    //Données du noeud
                    L = E.getElementsByTagName(element);
                    E_1 = (Element) L.item(0);
                    System.out.println(E_1.getTextContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<HashMap<String, Object>> getResFromExecuteReq() {
        return null;
    }

    @Override
    public List<HashMap<String, Object>> tradResToMed(List<HashMap<String, Object>> resSrc) {
        return null;
    }

    //Connection à la source
    public void lire_XML(String path_fichier) throws SAXException, IOException, ParserConfigurationException {

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        Document doc = builderFactory.newDocumentBuilder().parse(new FileInputStream(path_fichier));
        NodeList L;
        Element E, E_1;

        /* Récupération des noeuds de la source XML
         * Cliquez sur l'icône + pour dérouler le code associé au noeud
         */
        //region Liste des étudiants
        System.out.println("---------- ETUDIANTS ----------\n");
        NodeList etudiants = doc.getElementsByTagName("Etudiant");

        if (etudiants.getLength() > 0) {
            //Pour chaque étudiant, on récupère ses données ...
            for (int index = 0; index < etudiants.getLength(); index++) {
                //Noeud étudiant
                E = (Element) etudiants.item(index);

                //Données du noeud étudiant courant
                //Numéro
                L = E.getElementsByTagName("NumEt");
                E_1 = (Element) L.item(0); // un seul noeud NumEt
                System.out.println("Numéro de l'étudiant : " + E_1.getTextContent());

                //Nom
                L = E.getElementsByTagName("nom");
                E_1 = (Element) L.item(0);
                System.out.println("Nom :  " + E_1.getTextContent());

                //Téléphone
                L = E.getElementsByTagName("telephone");
                E_1 = (Element) L.item(0);
                System.out.println("Téléphone :  " + E_1.getTextContent());

                //Provenance
                L = E.getElementsByTagName("Provenance");
                E_1 = (Element) L.item(0);
                System.out.println("Provenance :  " + E_1.getTextContent());

                //Formation précédente
                L = E.getElementsByTagName("FormationPrecedante");
                E_1 = (Element) L.item(0);
                System.out.println("Formation précédente :  " + E_1.getTextContent());

                //Année début
                L = E.getElementsByTagName("AnneeDebut");
                E_1 = (Element) L.item(0);
                System.out.println("Année Début :  " + E_1.getTextContent());

                //Date de naissance
                L = E.getElementsByTagName("dateNaissance");
                E_1 = (Element) L.item(0);
                System.out.println("Date de naissance :  " + E_1.getTextContent());

                //Niveau d'insertion
                L = E.getElementsByTagName("Niveau_insertion");
                E_1 = (Element) L.item(0);
                System.out.println("Niveau d'insertion :  " + E_1.getTextContent());

                //Pays de la formation précédente
                L = E.getElementsByTagName("Pays_formation_precedante");
                E_1 = (Element) L.item(0);
                System.out.println("Pays de la formation précédente :  " + E_1.getTextContent());

                //Inscriptions
                System.out.println("Inscription : ");

                //Pour chaque inscription, on récupère les données
                NodeList inscriptions = E.getElementsByTagName("Inscription");
                if (inscriptions.getLength() > 0) {
                    for (int iIndex = 0; iIndex < inscriptions.getLength(); iIndex++) {
                        //Noeud inscription
                        E = (Element) inscriptions.item(iIndex);

                        //Année universitaire
                        L = E.getElementsByTagName("Annee_universitaire");
                        E_1 = (Element) L.item(0);
                        System.out.println("\tAnnée universitaire : " + E_1.getTextContent());

                        //Numéro de l'étudiant
                        L = E.getElementsByTagName("NumEt");
                        E_1 = (Element) L.item(0);
                        System.out.println("\tNuméro de l'étudiant : " + E_1.getTextContent());

                        //Identifiant du cours
                        L = E.getElementsByTagName("ID_cours");
                        E_1 = (Element) L.item(0);
                        System.out.println("\tIdentifiant du cours : " + E_1.getTextContent());

                        //Note obtenue au cours
                        L = E.getElementsByTagName("Note_cours");
                        E_1 = (Element) L.item(0);
                        System.out.println("\tNote obtenu à ce cours : " + E_1.getTextContent() + "\n");
                    }
                } else {
                    System.out.println("\tAucune inscription \n");
                }
            }
        } else {
            System.out.println("Aucun étudiant \n");
        }
        //endregion

        //region Liste des enseignants
        System.out.println("---------- ENSEIGNANTS ----------\n");

        NodeList enseignants = doc.getElementsByTagName("Enseignant");

        if (enseignants.getLength() > 0) {
            //Pour chaque enseignant, on récupère ses données ...
            for (int index = 0; index < enseignants.getLength(); index++) {
                //Noeud enseignant
                E = (Element) enseignants.item(index);

                //Données du noeud enseignant courant
                //Numéro de l'enseignant
                L = E.getElementsByTagName("NumEns");
                E_1 = (Element) L.item(0);
                System.out.println("Numéro de l'enseignant : " + E_1.getTextContent());

                //Nom
                L = E.getElementsByTagName("Nom");
                E_1 = (Element) L.item(0);
                System.out.println("Nom : " + E_1.getTextContent());

                //Prénom
                L = E.getElementsByTagName("Prenom");
                E_1 = (Element) L.item(0);
                System.out.println("Prénom : " + E_1.getTextContent());

                //Telephone
                L = E.getElementsByTagName("Telephone");
                E_1 = (Element) L.item(0);
                System.out.println("Téléphone : " + E_1.getTextContent());

                //Adresse mail
                L = E.getElementsByTagName("adresseMail");
                E_1 = (Element) L.item(0);
                System.out.println("Adresse mail : " + E_1.getTextContent());

                //Enseignements
                System.out.println("Enseignements : ");
                NodeList enseignements = doc.getElementsByTagName("Enseigne");
                if (enseignements.getLength() > 0) {
                    for (int iIndex = 0; iIndex < enseignements.getLength(); iIndex++) {
                        //Noeud enseigne
                        E = (Element) enseignements.item(index);

                        //Données du noeud enseigne courant
                        //Nombre d'heures
                        L = E.getElementsByTagName("Nb_heures");
                        E_1 = (Element) L.item(0);
                        System.out.println("\tNombre d'heures : " + E_1.getTextContent());

                        //Année universitaire
                        L = E.getElementsByTagName("Annee_universitaire");
                        E_1 = (Element) L.item(0);
                        System.out.println("\tAnnée universitaire : " + E_1.getTextContent());

                        //Identifiant du cours
                        L = E.getElementsByTagName("ID_Cours");
                        E_1 = (Element) L.item(0);
                        System.out.println("\tIdentifiant du cours : " + E_1.getTextContent());

                        //Numéro de l'enseignement
                        L = E.getElementsByTagName("NumEns");
                        E_1 = (Element) L.item(0);
                        System.out.println("\tNuméro de l'enseignement: " + E_1.getTextContent() + "\n");
                    }
                } else {
                    System.out.println("Aucun enseignement \n");
                }
            }

        } else {
            System.out.println("Aucun enseignant \n");
        }
        //endregion

        //region Liste des cours
        System.out.println("---------- COURS ----------\n");

        NodeList cours = doc.getElementsByTagName("Cours");
        if (cours.getLength() > 0) {
            //Pour chaque cours, on récupère ses données ...
            for (int index = 0; index < cours.getLength(); index++) {
                //Noeud enseigne
                E = (Element) cours.item(index);

                //Données du noeud cours courant
                //Nombre d'heures
                L = E.getElementsByTagName("Nb_heures");
                E_1 = (Element) L.item(0);
                System.out.println("Nombre d'heures : " + E_1.getTextContent());

                //Niveau
                L = E.getElementsByTagName("Niveau");
                E_1 = (Element) L.item(0);
                System.out.println("Niveau : " + E_1.getTextContent());

                //Identifiant du cours
                L = E.getElementsByTagName("ID_cours");
                E_1 = (Element) L.item(0);
                System.out.println("Identifiant du cours : " + E_1.getTextContent());

                //Type
                L = E.getElementsByTagName("Type");
                E_1 = (Element) L.item(0);
                System.out.println("Type : " + E_1.getTextContent() + "\n");
            }
        } else {
            System.out.println("Aucun cours \n");
        }

        //endregion
    }
}



