package model;

import contract.ExtracteurItf;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.xml.sax.SAXException;

import javax.swing.table.TableCellEditor;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Classe Extracteur/Adaptateur pour la source de donnees XML
 *
 * @author arnold
 * @version 0.1
 */
public class XML implements ExtracteurItf {
    private static XML INSTANCE = null; //Singleton pour bonne pratique
    private Document docXML;
    private String medSQL; //Requête SQL envoyé par le médiateur
    private List<HashMap<String, Object>> resReq;
    private static final HashMap<String, String> TABLE_CORRESPONDANCE = new HashMap<>(); //Table de correspondance en static final pour éviter toutes modifications
    static {
        //region Requête Exercices
        TABLE_CORRESPONDANCE.put(" SELECT Enseignant.ID-Enseignant as id, Enseignant.Nom as nom, Enseignant.Prenom as prenom, SUM(Cours.Heures) as heures " +
                        " FROM   Enseignant, Enseigne, Cours " +
                        " WHERE  Enseignant.ID-Enseignant = Enseigne.ID-Enseignant " +
                        "   AND  Cours.ID-Cours = Enseigne.ID-Cours  " +
                        " GROUP BY Enseignant.ID-Enseignant ;"
                , "reqMed_1");
        TABLE_CORRESPONDANCE.put(" SELECT COUNT(Etudiant.ID-Etudiant) as nb_etudiant_francais " +
                        " FROM   Etudiant " +
                        " WHERE  Etudiant.Provenance = 'France'; "
                , "reqMed_2");
        TABLE_CORRESPONDANCE.put(" SELECT Cours.Type as type, COUNT(Cours.Id-Cours) as nb_cours_par_type " +
                        " FROM Cours " +
                        " GROUP BY Cours.Type; "
                , "reqMed_3");
        //endregion
        // region Table Traduction
        TABLE_CORRESPONDANCE.put("enseignant.id", "NumEns");
        TABLE_CORRESPONDANCE.put("enseignant.nom", "Nom");
        TABLE_CORRESPONDANCE.put("enseignant.prenom", "Prenom");
        TABLE_CORRESPONDANCE.put("heures", "Nb_heures");
        TABLE_CORRESPONDANCE.put("nb_etudiant_francais", "France");
        TABLE_CORRESPONDANCE.put("cours.type", "Type");
        TABLE_CORRESPONDANCE.put("nb_cours_par_type", "Nb_Cours");
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

    public Document getDocXML() {
        return docXML;
    }

    public String getMedSQL() {
        return medSQL;
    }

    @Override
    public void setMediateurReq(String reqMed) {
        this.medSQL = reqMed;
    }

    @Override
    public void connexion() {
        try {
            String pathFile = "data/Univ_BD_3.xml";

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            FileInputStream fileInput = new FileInputStream(pathFile);
            this.docXML = builderFactory.newDocumentBuilder().parse(fileInput);
            fileInput.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deconnexion() {
        this.docXML = null;
    }

    @Override
    public String reqMedtoReqSrc() {
        if (this.medSQL.isEmpty()) return null;
        // Récupère le nom de la méthode à invoquer pour la requête associée
        return TABLE_CORRESPONDANCE.getOrDefault(this.medSQL, null);
    }

    @Override
    public void executeReq(String reqSrc) {
        if (reqSrc == null) return;
        // Excute la requête via son nom
        try {
            this.resReq = (List<HashMap<String, Object>>) this.getClass().getDeclaredMethod(reqSrc).invoke(this);

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<HashMap<String, Object>> getResFromExecuteReq() {
        return this.resReq;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public List<HashMap<String, Object>> tradResToMed(List<HashMap<String, Object>> resSrc) {
        List<HashMap<String, Object>> resMed = new ArrayList<>();
        String keyType = "cours.type";

        // Loop sur la List<HashMap<>>
        for (HashMap<String, Object> row : resSrc) {
            // Parcourt la table de correspondance afin d'effectuer les correspondances
            // On va convertir les noms de colonne source en nom de colonne global
            for (HashMap.Entry<String, String> entry : TABLE_CORRESPONDANCE.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                /*  On retire la ligne avec le nom de colonne correspondant à la source
                    puis on le remet avec le nom de colonne correspondant au mediateur
                    On vérifie que l'entrée correspond avant de la retirer */
                if(row.get(value)!=null){
                    Object obj = row.remove(value);
                    row.put(key, obj);
                }
            } //endloop: TABLE_CORRESPONDANCE.entrySet()

            // Traduction de la valeur des types
            String type = (String) row.getOrDefault(keyType, "");
            switch(type){
                case "Cours Magistral":
                    row.put(keyType, "CM");
                    break;
                case "Travaux diriges":
                    row.put(keyType, "TD");
                    break;
                default:
                    break;
            }
            // Alimente la liste traduite pour le mediateur
            resMed.add(row);
        } //endloop: resSrc

        return resMed;
    }

    /**
     * 1. Afficher pour chaque enseignant, son nombre total d’heures assurées.
     */
    private List<HashMap<String, Object>>  reqMed_1() {
        // Récupère la liste des noeuds avec comme nom "Enseignant"
        NodeList enseignants = this.docXML.getElementsByTagName("Enseignant");
        NodeList enseignes;
        Element noeud;
        String id, nom, prenom;
        Integer compteurHeures;

        List<HashMap<String, Object>> resList = new ArrayList<>();
        Object obj;

        // On boucle sur chaque noeud étudiant trouvé
        for (int index = 0; index < enseignants.getLength(); ++index) {
            HashMap<String, Object> resultat = new HashMap<>();
            // reinit le compteur d'heures
            compteurHeures = 0;
            // On un des noeuds étudiant de la liste
            noeud = (Element) enseignants.item(index);

            /* Dans le noeud courant Enseignant
             * On récupère le premier noeud qui s'appelle "Provenance"
             * Puis sa valeur texte */
            id = noeud.getElementsByTagName("NumEns").item(0).getTextContent();
            nom = noeud.getElementsByTagName("Nom").item(0).getTextContent();
            prenom = noeud.getElementsByTagName("Prenom").item(0).getTextContent();

            //On va parcourir tous les noeuds de type "Enseigne"
            enseignes = noeud.getElementsByTagName("Enseigne");
            for(int index2 = 0; index2 < enseignes.getLength(); ++index2){
                noeud = (Element) enseignes.item(index2);
                compteurHeures += Integer.parseInt(noeud.getElementsByTagName("Nb_heures").item(0).getTextContent());
            }

            // Si le type est déjà renseigne dans les résultats
            // On incrémente sa valeur associé
            resultat.put("NumEns", id);
            resultat.put("Nom", nom);
            resultat.put("Prenom", prenom);
            resultat.put("Nb_heures", compteurHeures);

            resList.add(resultat);
        }

        return resList;
    }

    /**
     * 2. Retourner le nombre d’étudiants dont le pays de Provenance est la ‘France’.
     */
    private List<HashMap<String, Object>> reqMed_2() {
        // Récupère la liste des noeuds avec comme nom "Etudiant"
        NodeList etudiants = this.docXML.getElementsByTagName("Etudiant");
        Element noeud;
        String valeur;

        List<HashMap<String, Object>> resList = new ArrayList<>();
        HashMap<String, Object> resultat = new HashMap<>();
        Object obj;

        // On boucle sur chaque noeud étudiant trouvé
        for (int index = 0; index < etudiants.getLength(); ++index) {
            // On un des noeuds étudiant de la liste
            noeud = (Element) etudiants.item(index);

            /* Dans le noeud courant étudiant
             * On récupère le premier noeud qui s'appelle "Provenance"
             * Puis sa valeur texte */
            valeur = noeud.getElementsByTagName("Provenance").item(0).getTextContent();

            // Si "France" est déjà renseigne dans les résultats
            // On incrémente sa valeur associé
            if(valeur.equals("France")){
                obj = resultat.getOrDefault(valeur,0);
                resultat.put(valeur, ((Integer)obj) + 1);
            }
        }
        resList.add(resultat);
        return resList;
    }

    /**
     * 3. Afficher le nombre de cours par Type (CM, TD ou TP).
     */
    private List<HashMap<String, Object>> reqMed_3() {
        // Récupère la liste des noeuds grâce à son nom
        NodeList cours = this.docXML.getElementsByTagName("Cours");
        Element noeud;
        String valeur;

        List<HashMap<String, Object>> resList = new ArrayList<>();
        HashMap<String, Object> cm = new HashMap<>();
        HashMap<String, Object> td = new HashMap<>();
        Object obj;

        // On boucle sur chaque noeud trouvé
        // On ignore le premier noeud car il s'agit du root
        // Il y a 5 occurence de noeud "Cours" pour actuellement seulement 4 cours réel
        for (int index = 1; index < cours.getLength(); ++index) {
            // On un des noeuds cours de la liste
            noeud = (Element) cours.item(index);

            /* Dans le noeud courant cours
             * On récupère le premier noeud qui s'appelle "Type"
             * Puis sa valeur texte */
            valeur = noeud.getElementsByTagName("Type").item(0).getTextContent();

            if(valeur.equals("Cours Magistral")){
                cm.put("Type",valeur);
                obj = cm.getOrDefault("Nb_Cours",0);
                cm.put("Nb_Cours",(Integer)obj + 1);
            }
            else if(valeur.equals("Travaux diriges")){
                td.put("Type",valeur);
                obj = td.getOrDefault("Nb_Cours",0);
                td.put("Nb_Cours",(Integer)obj + 1);
            }
        }

        resList.add(cm);
        resList.add(td);

        return resList;
    }

    //Connection à la source
    public void lire_XML(){
        this.connexion();

        NodeList L;
        Element E, E_1;

        /* Récupération des noeuds de la source XML
         * Cliquez sur l'icône + pour dérouler le code associé au noeud
         */
        //region Liste des étudiants
        System.out.println("---------- ETUDIANTS ----------\n");
        NodeList etudiants = this.docXML.getElementsByTagName("Etudiant");

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

        NodeList enseignants = this.docXML.getElementsByTagName("Enseignant");

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
                NodeList enseignements = this.docXML.getElementsByTagName("Enseigne");
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

        NodeList cours = this.docXML.getElementsByTagName("Cours");
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



