package de.mathes.klassen;

public class Benutzer {

    private int benutzerId;
    private String benutzerName;
    private int benutzerFarbe;
    private boolean benutzerAktiv;
    private boolean benutzerAnzeigen;
    private boolean benutzerUrlaubAnzeigen;

    public Benutzer(int benutzerId, String benutzerName, int benutzerFarbe, boolean benutzerAktiv) {
        this.benutzerId = benutzerId;
        this.benutzerName = benutzerName;
        this.benutzerFarbe = benutzerFarbe;
        this.benutzerAktiv = benutzerAktiv;
        this.benutzerAnzeigen = true;
        this.benutzerUrlaubAnzeigen = true;
    }

    public void setBenutzerAktiv(boolean benutzerAktiv) {
        this.benutzerAktiv = benutzerAktiv;
    }

    public void setBenutzerAnzeigen(boolean benutzerAnzeigen) {
        this.benutzerAnzeigen = benutzerAnzeigen;
    }

    public void setBenutzerUrlaubAnzeigen(boolean benutzerUrlaubAnzeigen) {
        this.benutzerUrlaubAnzeigen = benutzerUrlaubAnzeigen;
    }

    public int getBenutzerId() {
        return benutzerId;
    }

    public String getBenutzerName() {
        return benutzerName;
    }

    public int getBenutzerFarbe() {
        return benutzerFarbe;
    }

    public boolean isBenutzerAktiv() {
        return benutzerAktiv;
    }

    public boolean isBenutzerAnzeigen() {
        return benutzerAnzeigen;
    }

    public boolean isBenutzerUrlaubAnzeigen() {
        return benutzerUrlaubAnzeigen;
    }

    @Override
    public String toString() {
        return "Benutzer{" +
                "benutzerId=" + benutzerId +
                ", benutzerName='" + benutzerName + '\'' +
                ", benutzerFarbe=" + benutzerFarbe +
                ", benutzerAktiv=" + benutzerAktiv +
                '}';
    }
}
