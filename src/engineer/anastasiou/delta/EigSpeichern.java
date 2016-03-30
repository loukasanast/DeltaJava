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

public class EigSpeichern extends HttpServlet{
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException{
        PrintWriter out = res.getWriter();

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Locale locale = new Locale("de", "DE");
            NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

            File file = new File("c:/Users/Lucas/Documents/Tomcat/webapps/delta/eigenschaften.txt");
            Files.write(file.toPath(), ('\n' + req.getParameter("kontonummer") + ';' + req.getParameter("anzeige") + ';'
                    + req.getParameter("limit") + ';' + req.getParameter("ausland")).getBytes("UTF-8"), StandardOpenOption.APPEND);
        }catch(Exception exc){
            throw exc;
        }
    }
}