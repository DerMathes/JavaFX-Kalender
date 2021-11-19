package de.mathes.backend;

import de.mathes.klassen.Benutzer;
import de.mathes.klassen.Benutzer_Projekt;
import de.mathes.klassen.Projekte;
import de.mathes.klassen.Urlaub;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

public interface Speicher {

    public void init();
    long erstelleBenutzer(String benutzerName, int benutzerFarbe);
    long erstelleProjekt(String projektName, int projektFarbe);
    long erstelleBenutzerProjekt(int projektId, int benutzerId, LocalDate teilnahmeBeginn, LocalDate teilnahmeEnde);
    long erstelleUrlaub(int benutzerId, LocalDate urlaubBeginn, LocalDate urlaubEnde);

    long loescheBenutzer(int benutzerId);
    long loescheProjekt(int projektId);
    long loescheBenutzerProjekt(int benutzerId, int projektId);
    long loescheUrlaub(int urlaubId);

    long aendereBenutzer(int benutzerId,String benutzerName, int benutzerFarbe , boolean benutzerAktiv);
    long aendereProjekt(int projektId,String projektName, int projektFarbe);
    long aendereBenutzer_Projekt(int benutzerId, int projektId,LocalDate teilnahmeBeginn, LocalDate teilnahmeEnde);
    long aendereUrlaub(int urlaubId, LocalDate urlaubBeginn, LocalDate urlaubEnde);


    public List<Benutzer> gebeAlleBenutzer();
    public List<Projekte> gebeAlleProjekte();

    int gebeZeilenAnzahl(LocalDate anfangsDatum , LocalDate endDatum);
    List<Benutzer_Projekt> gebeBenutzerProjekteInZeitraum(LocalDate anfangsDatum , LocalDate endDatum);

    Benutzer gebeBenutzer(int benutzerId);
    Projekte gebeProjekt(int projektId);
    Benutzer_Projekt gebeBenutzer_Projekt(int benutzerId, int projektId);
    Urlaub gebeUrlaub(int urlaubId);

    public void beenden();
}
