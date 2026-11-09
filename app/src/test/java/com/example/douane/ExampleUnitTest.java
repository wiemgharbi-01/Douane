package com.example.douane;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExampleUnitTest {

    @Test
    public void testGenererCleAvecMatricule() {
        // Test de la contrainte : toutes les clés doivent être préfixées par le matricule via méthode statique
        String cleNom = MainActivity.genererCle("1234", MainActivity.CLE_NOM);
        assertEquals("1234_NOM_VOYAGEUR", cleNom);

        String cleVerdict = MainActivity.genererCle("5678", MainActivity.CLE_VERDICT);
        assertEquals("5678_VERDICT_FINAL", cleVerdict);

        String cleNaissance = MainActivity.genererCle("9999", MainActivity.CLE_ANNEE_NAISSANCE);
        assertEquals("9999_ANNEE_NAISSANCE", cleNaissance);
    }

    @Test
    public void testEstNombrePremier() {
        // Test des nombres premiers
        assertTrue(VerificationActivity.estNombrePremier(2));
        assertTrue(VerificationActivity.estNombrePremier(3));
        assertTrue(VerificationActivity.estNombrePremier(5));
        assertTrue(VerificationActivity.estNombrePremier(7));
        assertTrue(VerificationActivity.estNombrePremier(11));
        assertTrue(VerificationActivity.estNombrePremier(13));
        assertTrue(VerificationActivity.estNombrePremier(17));
        assertTrue(VerificationActivity.estNombrePremier(19));
        assertTrue(VerificationActivity.estNombrePremier(23));
        assertTrue(VerificationActivity.estNombrePremier(53)); // Exemple : 2053 - 2000 = 53

        // Test des nombres non premiers
        assertFalse(VerificationActivity.estNombrePremier(-5));
        assertFalse(VerificationActivity.estNombrePremier(0));
        assertFalse(VerificationActivity.estNombrePremier(1));
        assertFalse(VerificationActivity.estNombrePremier(4));
        assertFalse(VerificationActivity.estNombrePremier(9));
        assertFalse(VerificationActivity.estNombrePremier(15));
        assertFalse(VerificationActivity.estNombrePremier(60)); // Exemple : 2050 - 1990 = 60
    }
}
