package engineer.anastasiou.delta;

import java.util.Date;

public class Ueberweisung{
    private String kontonummer;
    private Date datum;
    private Geld betrag;

    public Ueberweisung(double betrag){
        this.betrag = new Geld(betrag);
    }

    public String getKontonummer() {
        return kontonummer;
    }

    public void setKontonummer(String kontonummer) {
        this.kontonummer = kontonummer;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public Geld getBetrag() {
        return betrag;
    }
}
