package de.mathes.klassen;

import java.time.LocalDate;
import java.util.Date;

public class Urlaub {
    private int urlaubId;
    private int benutzerId;
    private LocalDate urlaubBeginn;
    private LocalDate urlaubEnde;

    public Urlaub(int urlaubId, int benutzerId, LocalDate urlaubBeginn, LocalDate urlaubEnde) {
        this.urlaubId = urlaubId;
        this.benutzerId = benutzerId;
        this.urlaubBeginn = urlaubBeginn;
        this.urlaubEnde = urlaubEnde;
    }

    public int getUrlaubId() {
        return urlaubId;
    }

    public int getBenutzerId() {
        return benutzerId;
    }

    public LocalDate getUrlaubBeginn() {
        return urlaubBeginn;
    }

    public LocalDate getUrlaubEnde() {
        return urlaubEnde;
    }

    @Override
    public String toString() {
        return "Urlaub{" +
                "urlaubId=" + urlaubId +
                ", benutzerId=" + benutzerId +
                ", urlaubBeginn=" + urlaubBeginn +
                ", urlaubEnde=" + urlaubEnde +
                '}';
    }
}
