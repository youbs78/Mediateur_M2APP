package model;

import domain.Cours;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class MySQLTest {
    private MySQL srcMySQL = MySQL.getInstance();

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void connexion() {
        Assert.assertTrue(this.srcMySQL.connexion() );
        System.out.println("MySQLTest-Connexion s'est bien déroulé !");
    }

    @Test
    public void deconnexion() {
        // Connexion obligatoire avant
        this.srcMySQL.connexion();
        Assert.assertTrue( this.srcMySQL.deconnexion());
        System.out.println("MySQLTest-Deconnexion s'est bien déroulé !");
    }

    @Test
    public void findAllCours() {
        this.srcMySQL.connexion();

        List<Cours> cours = this.srcMySQL.findAllCours();
        for (Cours c : cours) {
            System.out.println(c.toString());
        }

        this.srcMySQL.deconnexion();
    }

    @Test
    public void getInstance() {
        Assert.assertEquals(MySQL.class, MySQL.getInstance().getClass());
    }

    @Test
    public void setMediateurReq() {
    }

    @Test
    public void reqMedtoReqSrc() {
        String reqMed = "SELECT Enseignant.Nom, Enseignant.Prenom, Enseignant.adresseMail " +
                        "FROM Enseignant";
        String reqSrcWanted =   "select enseignant.nom, enseignant.prenom, 'Source 2' as enseignant.adressemail " +
                                "from enseignant";
        Assert.assertEquals(reqSrcWanted, this.srcMySQL.reqMedtoReqSrc(reqMed));
    }

    @Test
    public void executeReq() {
    }

    @Test
    public void getResFromExecuteReq() {
    }

    @Test
    public void tradResToMed() {
    }
}