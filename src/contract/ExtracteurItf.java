package contract;

import java.util.HashMap;
import java.util.List;

public interface ExtracteurItf {
     /*  Générer une table de correspondance entre la source et le médiateur pour
         décrire le template des attributs ; (HASHMAP ==>  Cle: Requete Mediateur , Valeur : Requete Source)
         hashmap<String,String> TableCorespond;*/


    /**
     * Méthode/fonction de connexion à la source de données
     * crée préalablement qui suit un schéma conceptuel décrit dans un catalogue
     * dans la BDD/fichier) ;
     */
    void connexion();

    /**
     * Méthode/fonction de déconnexion à la source de données
     * crée préalablement qui suit un schéma conceptuel décrit dans un catalogue
     * dans la BDD/fichier) ;
     */
    void deconnexion();

    /**
     * Méthode/fonction pour recevoir les requêtes du médiateur respectant
     * le schéma conceptuel global
     * La requête doit être mise en minucule !!!
     *
     */
    void setMediateurReq(String reqMed);


    /**
     * Méthode/fonction pour traduire la requête (vue) du médiateur par le schéma de la source
     * en parcourant la table de correspondance générée par le générateur d’adaptateur
     * et chercher le template qui correspond à la requête du médiateur
     *
     * Precondition: Avoir stocké la requête médiateur
     * @return La requête SQL correspondante à celle de la source
     */
    String reqMedtoReqSrc();

    /**
     * Méthode/fonction pour exécuter (interroger la source) les requêtes du médiateur sur la source
     *
     * Precondition: Connexion doit être établie avec la BDD
     * @param reqSrc La requête SQL de la source à exécuter
     */
    void executeReq(String reqSrc);

    /**
     * Méthode/fonction pour récupérer le résultat de la requête
     *
     * Precondition: Requête doit déjà avoir été exécutée
     * @return La hashmap de résultat obtenu par la requête source
     */
    List<HashMap<String, Object>> getResFromExecuteReq();

    /**
     * Méthode/fonction pour traduire le résultat et l’envoyer au médiateur.
     *
     * @param resSrc La liste de résultat obtenu par la requête source
     * @return La hashmap de résultat traduite selon le schéma conceptuel global
     */
    List<HashMap<String, Object>> tradResToMed(List<HashMap<String, Object>>resSrc);


}
