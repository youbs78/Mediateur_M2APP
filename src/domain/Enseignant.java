package domain;

public class Enseignant {
    private int     id_enseignant;
    private String  nom;
    private String  prenom;
    private String  adresseMail;

    public Enseignant() {
    }

    public Enseignant(int id_enseignant, String nom, String prenom, String adresseMail) {
        this.id_enseignant = id_enseignant;
        this.nom = nom;
        this.prenom = prenom;
        this.adresseMail = adresseMail;
    }

    public int getId_enseignant() {
        return id_enseignant;
    }

    public void setId_enseignant(int id_enseignant) {
        this.id_enseignant = id_enseignant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresseMail() {
        return adresseMail;
    }

    public void setAdresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
    }

    @Override
    public String toString() {
        return "Enseignant{" +
                "id_enseignant=" + id_enseignant +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", adresseMail='" + adresseMail + '\'' +
                '}';
    }
}
