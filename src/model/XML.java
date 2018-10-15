package model;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class XML {

    //Connection à la source
    public static void lire_XML(String path_fichier) throws SAXException, IOException, ParserConfigurationException{

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        Document doc = builderFactory.newDocumentBuilder().parse(new FileInputStream(path_fichier));
        NodeList L ;
        Element E, E_1;

        /* Récupération des noeuds de la source XML
         * Cliquez sur l'icône + pour dérouler le code associé au noeud
         */
        //region Liste des étudiants
        System.out.println("---------- ETUDIANTS ----------\n");
        NodeList etudiants = doc.getElementsByTagName("Etudiant");

        if(etudiants.getLength()>0){
            //Pour chaque étudiant, on récupère ses données ...
            for (int index =0 ; index < etudiants.getLength(); index++)
            {
                //Noeud étudiant
                E = (Element) etudiants.item(index);

                //Données du noeud étudiant courant
                //Numéro
                L = E.getElementsByTagName("NumEt");
                E_1 = (Element) L.item(0); // un seul noeud NumEt
                System.out.println("Numéro de l'étudiant : "+E_1.getTextContent());

                //Nom
                L = E.getElementsByTagName("nom");
                E_1 = (Element) L.item(0);
                System.out.println("Nom :  "+E_1.getTextContent());

                //Téléphone
                L = E.getElementsByTagName("telephone");
                E_1 = (Element) L.item(0);
                System.out.println("Téléphone :  "+E_1.getTextContent());

                //Provenance
                L = E.getElementsByTagName("Provenance");
                E_1 = (Element) L.item(0);
                System.out.println("Provenance :  "+E_1.getTextContent());

                //Formation précédente
                L = E.getElementsByTagName("FormationPrecedante");
                E_1 = (Element) L.item(0);
                System.out.println("Formation précédente :  "+E_1.getTextContent());

                //Année début
                L = E.getElementsByTagName("AnneeDebut");
                E_1 = (Element) L.item(0);
                System.out.println("Année Début :  "+E_1.getTextContent());

                //Date de naissance
                L = E.getElementsByTagName("dateNaissance");
                E_1 = (Element) L.item(0);
                System.out.println("Date de naissance :  "+E_1.getTextContent());

                //Niveau d'insertion
                L = E.getElementsByTagName("Niveau_insertion");
                E_1 = (Element) L.item(0);
                System.out.println("Niveau d'insertion :  "+E_1.getTextContent());

                //Pays de la formation précédente
                L = E.getElementsByTagName("Pays_formation_precedante");
                E_1 = (Element) L.item(0);
                System.out.println("Pays de la formation précédente :  "+E_1.getTextContent());

                //Inscriptions
                System.out.println("Inscription : ");

                //Pour chaque inscription, on récupère les données
                NodeList inscriptions = E.getElementsByTagName("Inscription");
                if(inscriptions.getLength()>0){
                    for (int iIndex = 0 ; iIndex < inscriptions.getLength(); iIndex++) {
                        //Noeud inscription
                        E = (Element) inscriptions.item(iIndex);

                        //Année universitaire
                        L = E.getElementsByTagName("Annee_universitaire");
                        E_1 = (Element) L.item(0);
                        System.out.println("\tAnnée universitaire : "+E_1.getTextContent());

                        //Numéro de l'étudiant
                        L = E.getElementsByTagName("NumEt");
                        E_1 = (Element) L.item(0);
                        System.out.println("\tNuméro de l'étudiant : "+E_1.getTextContent());

                        //Identifiant du cours
                        L = E.getElementsByTagName("ID_cours");
                        E_1 = (Element) L.item(0);
                        System.out.println("\tIdentifiant du cours : "+E_1.getTextContent());

                        //Note obtenue au cours
                        L = E.getElementsByTagName("Note_cours");
                        E_1 = (Element) L.item(0);
                        System.out.println("\tNote obtenu à ce cours : "+E_1.getTextContent()+"\n");
                    }
                }else{
                    System.out.println("\tAucune inscription \n");
                }
            }
        }else{
            System.out.println("Aucun étudiant \n");
        }
        //endregion

        //region Liste des enseignants
        System.out.println("---------- ENSEIGNANTS ----------\n");

        NodeList enseignants = doc.getElementsByTagName("Enseignant");

        if(enseignants.getLength()>0){
            //Pour chaque enseignant, on récupère ses données ...
            for (int index =0 ; index < enseignants.getLength(); index++) {
                //Noeud enseignant
                E = (Element) enseignants.item(index);

                //Données du noeud enseignant courant
                //Numéro de l'enseignant
                L = E.getElementsByTagName("NumEns");
                E_1 = (Element) L.item(0);
                System.out.println("Numéro de l'enseignant : "+E_1.getTextContent());

                //Nom
                L = E.getElementsByTagName("Nom");
                E_1 = (Element) L.item(0);
                System.out.println("Nom : "+E_1.getTextContent());

                //Prénom
                L = E.getElementsByTagName("Prenom");
                E_1 = (Element) L.item(0);
                System.out.println("Prénom : "+E_1.getTextContent());

                //Telephone
                L = E.getElementsByTagName("Telephone");
                E_1 = (Element) L.item(0);
                System.out.println("Téléphone : "+E_1.getTextContent());

                //Adresse mail
                L = E.getElementsByTagName("adresseMail");
                E_1 = (Element) L.item(0);
                System.out.println("Adresse mail : "+E_1.getTextContent());

                //Enseignements
                System.out.println("Enseignements : ");
                NodeList enseignements = doc.getElementsByTagName("Enseigne");
                if(enseignements.getLength()>0){
                    for (int iIndex = 0 ; iIndex < enseignements.getLength(); iIndex++) {
                        //Noeud enseigne
                        E = (Element) enseignements.item(index);

                        //Données du noeud enseigne courant
                        //Nombre d'heures
                        L = E.getElementsByTagName("Nb_heures");
                        E_1 = (Element) L.item(0);
                        System.out.println("\tNombre d'heures : "+E_1.getTextContent());

                        //Année universitaire
                        L = E.getElementsByTagName("Annee_universitaire");
                        E_1 = (Element) L.item(0);
                        System.out.println("\tAnnée universitaire : "+E_1.getTextContent());

                        //Identifiant du cours
                        L = E.getElementsByTagName("ID_Cours");
                        E_1 = (Element) L.item(0);
                        System.out.println("\tIdentifiant du cours : "+E_1.getTextContent());

                        //Numéro de l'enseignement
                        L = E.getElementsByTagName("NumEns");
                        E_1 = (Element) L.item(0);
                        System.out.println("\tNuméro de l'enseignement: "+E_1.getTextContent()+"\n");
                    }
                } else{
                    System.out.println("Aucun enseignement \n");
                }
            }

        }else{
            System.out.println("Aucun enseignant \n");
        }
        //endregion

        //region Liste des cours
        System.out.println("---------- COURS ----------\n");

        NodeList cours = doc.getElementsByTagName("Cours");
        if(cours.getLength()>0){
            //Pour chaque cours, on récupère ses données ...
            for (int index =0 ; index < cours.getLength(); index++) {
                //Noeud enseigne
                E = (Element) cours.item(index);

                //Données du noeud cours courant
                //Nombre d'heures
                L = E.getElementsByTagName("Nb_heures");
                E_1 = (Element) L.item(0);
                System.out.println("Nombre d'heures : "+E_1.getTextContent());

                //Niveau
                L = E.getElementsByTagName("Niveau");
                E_1 = (Element) L.item(0);
                System.out.println("Niveau : "+E_1.getTextContent());

                //Identifiant du cours
                L = E.getElementsByTagName("ID_cours");
                E_1 = (Element) L.item(0);
                System.out.println("Identifiant du cours : "+E_1.getTextContent());

                //Type
                L = E.getElementsByTagName("Type");
                E_1 = (Element) L.item(0);
                System.out.println("Type : "+E_1.getTextContent()+"\n");
            }
        }else{
            System.out.println("Aucun cours \n");
        }

        //endregion
    }

    public static void main(String[] args){
        try {
            lire_XML("/Users/arnoldsoupramanien/IdeaProjects/Mediateur_M2APP/src/data/Univ_BD_3.xml");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}

//Assurer l'exécution des différentes requêtes

//Permettre l'envoi et la réception de requêtes depuis/vers le médiateur


//Les requis

//Méthode/fonction de connexion et déconnexion à la source de données crée préalablement qui suit un schéma conceptuel décrit dans un catalogue (dans la BDD/fichier) ;

//Méthode/fonction pour recevoir les requêtes du médiateur ;

//Générer une table de correspondance entre la source et le médiateur pour décrire le template des attributs ;

//Méthode/fonction pour traduire la requête (vue) du médiateur par le schéma de la source en parcourant la table de correspondance générée par le générateur d’adaptateur et chercher le template qui correspond à la requête du médiateur ;

//Méthode/fonction pour exécuter (interroger la source) les requêtes du médiateur sur la source ;

//Méthode/fonction pour récupérer le résultat de la requête ;

//Méthode/fonction pour traduire le résultat et l’envoyer au médiateur.

