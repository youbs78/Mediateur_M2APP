package contract;

public interface MediateurItf {
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