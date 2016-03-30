package engineer.anastasiou.delta;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

public class Eigenschaften{
    private String kontonummer;
    private int anzeige;
    private double limit;
    private String ausland;

    public Eigenschaften(String kontonummer)
            throws IOException{
        this.kontonummer = kontonummer;

        File file = new File("c:/Users/Lucas/Documents/Tomcat/webapps/delta/eigenschaften.txt");

        for(String e : Files.readAllLines(file.toPath())){
            if(e.split(";")[0].equals(this.kontonummer)){
                anzeige = Integer.parseInt(e.split(";")[1]);
                limit = Double.parseDouble(e.split(";")[2]);
                ausland = e.split(";")[3];
            }
        }
    }

    public String getKontonummer() {
        return kontonummer;
    }

    public int getAnzeige() {
        return anzeige;
    }

    public double getLimit() {
        return limit;
    }

    public String getAusland() {
        return ausland;
    }
}
