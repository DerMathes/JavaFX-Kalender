package de.mathes.klassen;

public class Projekte {

    public int projektId;
    public String projektName;
    public int projektFarbe;
    public boolean projektAnzeigen;
 //TODO private

    public Projekte(int projektId, String projektName, int projektFarbe) {
        this.projektId = projektId;
        this.projektName = projektName;
        this.projektFarbe = projektFarbe;
        this.projektAnzeigen = true;
    }

    public Projekte(int projektId, String projektName, int projektFarbe, boolean projektAnzeigen) {
        this.projektId = projektId;
        this.projektName = projektName;
        this.projektFarbe = projektFarbe;
        this.projektAnzeigen = projektAnzeigen;
    }

    public int getProjektId() {
        return projektId;
    }

    public String getProjektName() {
        return projektName;
    }

    public int getProjektFarbe() {
        return projektFarbe;
    }

    public void setProjektAnzeigem(boolean projektAnzeigem) {
        this.projektAnzeigen = projektAnzeigem;
    }

    public boolean isProjektAnzeigem() {
        return projektAnzeigen;
    }

    @Override
    public String toString() {
        return "Projekte{" +
                "projektId=" + projektId +
                ", projektName='" + projektName + '\'' +
                ", projektFarbe=" + projektFarbe +
                '}';
    }
}
