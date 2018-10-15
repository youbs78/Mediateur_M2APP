package model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MySQLTest {
    private MySQL srcMySQL = new MySQL();

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void connexion() {
        Assert.assertEquals(true, this.srcMySQL.connexion() );
        System.out.println("MySQLTest-Connexion s'est bien déroulé !");
    }

    @Test
    public void deconnexion() {
        // Connexion obligatoire avant
        this.srcMySQL.connexion();
        Assert.assertEquals(true, this.srcMySQL.deconnexion());
        System.out.println("MySQLTest-Deconnexion s'est bien déroulé !");
    }
}