package de.mathes;

import de.mathes.backend.Speicher;
import de.mathes.backend.SpeicherH2;
import de.mathes.klassen.Benutzer;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpeicherH2Test {

    static SpeicherH2 speicherH2 = null;

    @BeforeAll
    static void init(){
        SpeicherH2.DB_URL = "jdbc:h2:mem:test";
        //SpeicherH2.DB_URL = "jdbc:h2:./kalender_test;AUTO_SERVER=TRUE";
        speicherH2 = new SpeicherH2();
        speicherH2.init();
        assertTrue(speicherH2.istVerbunden());
    }

    @AfterAll
    static void beenden(){
        speicherH2.beenden();
    }

    @Test
    @Order(1)
    void testErstelleBenutzer() {
        long buntzerid = speicherH2.erstelleBenutzer("TestName", Color.BLUE.getRGB());
        assertTrue(buntzerid > 0);
    }

    @Test
    @Order(2)
    void testErstelleProjekt() {
        long projektid = speicherH2.erstelleProjekt("TestProjekt", Color.RED.getRGB());
        assertTrue(projektid > 0);
    }

    @Test
    @Order(3)
    void testErstelleBenutzer_Projekt() {
        Date date = new Date();
        long benutzer_projektid = speicherH2.erstelleBenutzerProjekt(1,1, date ,date);
        ResultSet resultSet = speicherH2.excuteSelect("SELECT BENUTZERNAME ,TEILNAHMEBEGINN , TEILNAHMEENDE , PROJEKTNAME " +
                "FROM BENUTZER " +
                "LEFT JOIN BENUTZER_PROJEKTE BP on BENUTZER.BENUTZERID = BP.BENUTZERID " +
                "LEFT JOIN PROJEKTE P on BP.PROJEKTID = P.PROJEKTID " +
                "Where BENUTZER.BENUTZERID = 1");

        String projektname = null;
        String teilnahmeende = null;
        String teilnahmebeginn = null;

        try {
            if(resultSet.next()){
                teilnahmebeginn = resultSet.getString("Teilnahmebeginn");
                teilnahmeende = resultSet.getString("Teilnahmeende");
                projektname = resultSet.getString("Projektname");

            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals("TestProjekt",projektname);
        assertEquals(String.format("%tF",date),teilnahmebeginn);
        assertEquals(String.format("%tF", date),teilnahmeende);

    }

    //TODO test für Urlaub anlegen

}