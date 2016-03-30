package engineer.anastasiou.delta;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.text.NumberFormat;
import javax.servlet.*;
import javax.servlet.http.*;

public class Ueberweiser extends HttpServlet{
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException{
        PrintWriter out = res.getWriter();

        try{
            Eigenschaften eigenschaften = new Eigenschaften(req.getParameter("kontonummer"));

            if(Double.parseDouble(req.getParameter("betrag").replace(',', '.')) > eigenschaften.getLimit() || Double.parseDouble(req.getParameter("betrag").replace(',', '.')) > Double.parseDouble(req.getParameter("saldo").replace(',', '.'))){
                throw new IllegalArgumentException();
            }

            Ueberweisung ueberweisung = new Ueberweisung(Double.parseDouble(req.getParameter("betrag").replace(',', '.')));
            ueberweisung.setKontonummer(req.getParameter("kontonummer"));
            ueberweisung.setDatum(new Date());

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Locale locale = new Locale("de", "DE");
            NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

            File file = new File("c:/Users/Lucas/Documents/Tomcat/webapps/delta/data.txt");
            Files.write(file.toPath(), ('\n' + ueberweisung.getKontonummer() + ';' + "Überweisung" + ';' + sdf.format(ueberweisung.getDatum()) + ';'
                    + (-ueberweisung.getBetrag().getBetrag())).getBytes("UTF-8"), StandardOpenOption.APPEND);
        }catch(Exception exc){
            throw exc;
        }
    }
}