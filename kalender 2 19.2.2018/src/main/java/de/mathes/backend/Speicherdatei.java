package de.mathes.backend;

import de.mathes.klassen.Benutzer;
import de.mathes.klassen.Benutzer_Projekt;
import de.mathes.klassen.Projekte;
import de.mathes.klassen.Urlaub;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

public class Speicherdatei implements Speicher {


    @Override
    public void init() {

    }

    @Override
    public long erstelleBenutzer(String benutzerName, int benutzerFarbe) {
        return 0;
    }

    @Override
    public long erstelleProjekt(String projektName, int projektFarbe) {
        return 0;
    }

    @Override
    public long erstelleBenutzerProjekt(int projektId, int benutzerId, LocalDate teilnahmeBeginn, LocalDate teilnahmeEnde) {
        return 0;
    }

    @Override
    public long erstelleUrlaub(int benutzerId, LocalDate urlaubBeginn, LocalDate urlaubEnde) {
        return 0;
    }

    @Override
    public long loescheBenutzer(int benutzerId) {
        return 0;
    }

    @Override
    public long loescheProjekt(int projektId) {
        return 0;
    }

    @Override
    public long loescheBenutzerProjekt(int benutzerId, int projektId) {
        return 0;
    }

    @Override
    public long loescheUrlaub(int urlaubId) {
        return 0;
    }

    @Override
    public long aendereBenutzer(int benutzerId, String benutzerName, int benutzerFarbe, boolean benutzerAktiv) {
        return 0;
    }

    @Override
    public long aendereProjekt(int projektId, String projektName, int projektFarbe) {
        return 0;
    }

    @Override
    public long aendereBenutzer_Projekt(int benutzerId, int projektId, LocalDate teilnahmeBeginn, LocalDate teilnahmeEnde) {
        return 0;
    }

    @Override
    public long aendereUrlaub(int urlaubId, LocalDate urlaubBeginn, LocalDate urlaubEnde) {
        return 0;
    }

    @Override
    public List<Benutzer> gebeAlleBenutzer() {
        return null;
    }

    @Override
    public List<Projekte> gebeAlleProjekte() {
        return null;
    }

    @Override
    public int gebeZeilenAnzahl(LocalDate anfangsDatum, LocalDate endDatum) {
        return 0;
    }

    @Override
    public List<Benutzer_Projekt> gebeBenutzerProjekteInZeitraum(LocalDate anfangsDatum, LocalDate endDatum) {
        return null;
    }

    @Override
    public Benutzer gebeBenutzer(int benutzerId) {
        return null;
    }

    @Override
    public Projekte gebeProjekt(int projektId) {
        return null;
    }

    @Override
    public Benutzer_Projekt gebeBenutzer_Projekt(int benutzerId, int projektId) {
        return null;
    }

    @Override
    public Urlaub gebeUrlaub(int urlaubId) {
        return null;
    }

    @Override
    public void beenden() {

    }
}
