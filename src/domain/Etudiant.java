package domain;

public class Etudiant {
    private int     id_etudiant;
    private String  nom;
    private String  prenom;
    private String  provenance;
    private String  formationPrecedente;
    private String  paysFormationPrecedente;
    private int     anneeDebut;
    private int     age;
    private String  niveauInsertion;

    public Etudiant() {
    }

    public Etudiant(int id_etudiant, String nom, String prenom, String provenance, String formationPrecedente, String paysFormationPrecedente, int anneeDebut, int age, String niveauInsertion) {
        this.id_etudiant = id_etudiant;
        this.nom = nom;
        this.prenom = prenom;
        this.provenance = provenance;
        this.formationPrecedente = formationPrecedente;
        this.paysFormationPrecedente = paysFormationPrecedente;
        this.anneeDebut = anneeDebut;
        this.age = age;
        this.niveauInsertion = niveauInsertion;
    }

    public int getId_etudiant() {
        return id_etudiant;
    }

    public void setId_etudiant(int id_etudiant) {
        this.id_etudiant = id_etudiant;
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

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public String getFormationPrecedente() {
        return formationPrecedente;
    }

    public void setFormationPrecedente(String formationPrecedente) {
        this.formationPrecedente = formationPrecedente;
    }

    public String getPaysFormationPrecedente() {
        return paysFormationPrecedente;
    }

    public void setPaysFormationPrecedente(String paysFormationPrecedente) {
        this.paysFormationPrecedente = paysFormationPrecedente;
    }

    public int getAnneeDebut() {
        return anneeDebut;
    }

    public void setAnneeDebut(int anneeDebut) {
        this.anneeDebut = anneeDebut;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNiveauInsertion() {
        return niveauInsertion;
    }

    public void setNiveauInsertion(String niveauInsertion) {
        this.niveauInsertion = niveauInsertion;
    }

    @Override
    public String toString() {
        return "Etudiant{" +
                "id_etudiant=" + id_etudiant +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", provenance='" + provenance + '\'' +
                ", formationPrecedente='" + formationPrecedente + '\'' +
                ", paysFormationPrecedente='" + paysFormationPrecedente + '\'' +
                ", anneeDebut=" + anneeDebut +
                ", age=" + age +
                ", niveauInsertion='" + niveauInsertion + '\'' +
                '}';
    }
}
