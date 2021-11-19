package de.mathes.klassen;

import java.time.LocalDate;
import java.util.Date;

public class Benutzer_Projekt {
    private int projektId;
    private int benutzerId;
    private LocalDate teilnahmeBeginn;
    private LocalDate teilnahmeEnde;


    public Benutzer_Projekt(int projektId, int benutzerId, LocalDate teilnahmeBeginn, LocalDate teilnahmeEnde) {
        this.projektId = projektId;
        this.benutzerId = benutzerId;
        this.teilnahmeBeginn = teilnahmeBeginn;
        this.teilnahmeEnde = teilnahmeEnde;
    }

    public int getProjektId() {
        return projektId;
    }

    public int getBenutzerId() {
        return benutzerId;
    }

    public LocalDate getTeilnahmeBeginn() {
        return teilnahmeBeginn;
    }

    public LocalDate getTeilnahmeEnde() {
        return teilnahmeEnde;
    }

    @Override
    public String toString() {
        return "Benutzer_Projekt{" +
                "projektId=" + projektId +
                ", benutzerId=" + benutzerId +
                ", teilnahmeBeginn=" + teilnahmeBeginn +
                ", teilnahmeEnde=" + teilnahmeEnde +
                '}';
    }
}
