package contract;

import domain.Cours;

import java.util.List;

public interface ExtracteurItf {
	// Générer une table de correspondance entre la source et le médiateur pour 
    //décrire le template des attributs ; (HASHMAP ==>  Cle: Requete Mediateur , Valeur : Requete Source)
    //hashmap<String,String> TableCorespond;
	
	
	//Méthode/fonction de connexion et déconnexion à la source de données crée préalablement qui suit un schéma conceptuel décrit dans un catalogue (dans la BDD/fichier) ;
    boolean connexion();
    boolean deconnexion();
    

    // Méthode/fonction pour recevoir les requêtes du médiateur ;
    void setMediateurReq(String reqMed);
    
    
    // Méthode/fonction pour traduire la requête (vue) du médiateur par le schéma de la source
    // en parcourant la table de correspondance générée par le générateur d’adaptateur
    // et chercher le template qui correspond à la requête du médiateur ;
    String reqMedtoReqSrc(String reqMed);
    
    // Méthode/fonction pour exécuter (interroger la source) les requêtes du médiateur sur la source ;
    void executeReq(String reqSrc);
    
    // Méthode/fonction pour récupérer le résultat de la requête
    List<Object> getResFromExecuteReq(String req);
    
    // Méthode/fonction pour traduire le résultat et l’envoyer au médiateur.
    List<Object> tradResToMed(List<Object> resSrc);
    
   

}
