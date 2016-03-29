package delta;

import java.io.*;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.*;
import javax.servlet.http.*;

public class Index extends HttpServlet {

    public void requestHandler(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        boolean eingeloggt = false;

        byte[] keyBytes = {1, 23, 54, 120, 44, 64, 0, 8};
        byte[] ivBytes = {33, 44, 65, 66, 47, 38, 8, 99};
        byte[] input = req.getParameter("kontoidentifikation").getBytes();

        File kontenFile = new File("c:/Users/Lucas/Documents/Tomcat/webapps/delta/konten.txt");
        List<String> konten = Files.readAllLines(kontenFile.toPath());

        try{
            SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            byte[] encrypted= new byte[cipher.getOutputSize(input.length)];
            int enc_len = cipher.update(input, 0, input.length, encrypted, 0);
            cipher.doFinal(encrypted, enc_len);

            for(String konto : konten){
                if(konto.split(";")[0].equals(req.getParameter("kontonummer")) && konto.split(";")[1].equals(new String(encrypted))){
                    eingeloggt = true;
                }
            }

            //out.println(new String(encrypted));

            /*cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            byte[] decrypted = new byte[cipher.getOutputSize(enc_len)];
            int dec_len = cipher.update(encrypted, 0, enc_len, decrypted, 0);
            dec_len += cipher.doFinal(decrypted, dec_len);*/
        }catch(Exception exc){
            out.println(exc.getMessage());
        }

        Konto konto = new Konto();

        File dataFile = new File("c:/Users/Lucas/Documents/Tomcat/webapps/delta/data.txt");
        List<String> umsaetze = Files.readAllLines(dataFile.toPath());
        double saldo = 0;

        Locale locale = new Locale("de", "DE");
        NumberFormat nf = NumberFormat.getInstance(locale);

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset=\"utf-8\" />");
        out.println("<title>Delta Bank</title>");
        out.println("<link href=\"./css/site.css\" rel=\"stylesheet\" type=\"text/css\" />");
        out.println("</head>");
        out.println("<body>");
        out.println("<section id=\"wrapper\">");
        out.println("<header>");

        if(!eingeloggt) {
            out.println("<div id=\"theader\">");
            out.println("<a href=\"#\">Zurück zu deltabank.com</a>");
            out.println("</div>");
            out.println("<div id=\"mheader\">");
            out.println("<img id=\"logo\" src=\"img/logo.png\" alt=\"\" />");
            out.println("</div>");
            out.println("</header>");
            out.println("<main>");
            out.println("<div id=\"tmain\">");
            out.println("<span id=\"tlmain\">Online Banking</span>");
            out.println("<div id=\"trmain\">");
            out.println("<img id=\"flag\" src=\"img/gb.png\" />");
            out.println("&nbsp;");
            out.println("<a href=\"#\">English Version</a>");
            out.println("</div>");
            out.println("</div>");
            out.println("<section id=\"cont\">");
            out.println("<div id=\"lcont\">");
            out.println("</div>");
            out.println("<section id=\"mcont\">");
            out.println("<form id=\"linform\" method=\"post\" action=\"\">");
            out.println("<table style=\"margin-top:27px;\">");
            out.println("<tr>");
            out.println("<td>Konto-Nr.:</td>");
            out.println("<td><input type=\"text\" name=\"kontonummer\" required /></td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td>Passwort:</td>");
            out.println("<td><input type=\"password\" name=\"kontoidentifikation\" required /></td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td></td>");
            out.println("<td>");
            out.println("<a href=\"#\" id=\"lanmelden\">Anmelden</a>");
            out.println("<div style=\"float:right;font-size:9px;\">");
            out.println("<input type=\"checkbox\" id=\"remember\" />");
            out.println("<label for=\"remember\">Eingeloggt bleiben</label>");
            out.println("</div>");
            out.println("</td>");
            out.println("</tr>");
            out.println("</table>");
            out.println("</form>");
        }else {
            out.println("<div id=\"theader\">");
            out.println("<img src=\"img/lock.png\" alt=\"\" style=\"margin:0 2px;vertical-align:-1px;opacity:.18;\" />");
            out.println("<a href=\"#\">Abmelden</a>");
            out.println("</div>");
            out.println("<div id=\"mheader\">");
            out.println("<img id=\"logo\" src=\"img/logo.png\" alt=\"\" />");
            out.println("</div>");
            out.println("</header>");
            out.println("<main>");
            out.println("<div id=\"tmain\">");
            out.println("<span id=\"tlmain\">Online Banking</span>");
            out.println("<div id=\"trmain\">");
            out.println("<img id=\"flag\" src=\"img/gb.png\" />");
            out.println("&nbsp;");
            out.println("<a href=\"#\">English Version</a>");
            out.println("</div>");
            out.println("</div>");
            out.println("<section id=\"cont\">");
            out.println("<div id=\"lcont\">");
            out.println("<span id=\"ltcont\">Girokonto</span>");
            out.println("<br />");
            out.println("<span class=\"dname\">BLZ</span>:76020096");
            out.println("<br />");
            out.println("<span class=\"dname\">Konto-Nr</span>:570997745<br />");
            out.println("<br />");
            out.println("<span class=\"dname\">BIC</span>:PBNKDEFF");
            out.println("</div>");
            out.println("<section id=\"mcont\">");
            out.println("<nav id=\"mnav\">");
            out.println("<ul>");
            out.println("<li id=\"umslink\" class=\"btab btabact\">");
            out.println("<img src=\"img/graph.png\" alt=\"\" />");
            out.println("Umsätze");
            out.println("</li>");
            out.println("<li id=\"uberlink\" class=\"btab\">");
            out.println("<img src=\"img/hand.png\" alt=\"\" />");
            out.println("Überweisung");
            out.println("</li>");
            out.println("<li id=\"eiglink\" class=\"btab\">");
            out.println("<img src=\"img/tool.png\" alt=\"\" />");
            out.println("Eigenschaften");
            out.println("</li>");
            out.println("</ul>");
            out.println("<div class=\"fix\"></div>");
            out.println("</nav>");
            out.println("<div id=\"mtcont\">");
            out.println("<div id=\"mtlcont\">");
            out.println("IBAN<br />");
            out.println("DE3120010020999999999");
            out.println("</div>");
            out.println("<div id=\"mtrcont\">");
            out.println("Saldo &euro;<br />");

            for (String umsatz : umsaetze) {
                saldo += Double.parseDouble(umsatz.split(";")[3]);
            }

            out.println("<span id=\"saldo\">" + nf.format(saldo) + "</span>");

            out.println("</div>");
            out.println("<div class=\"fix\"></div>");
            out.println("<table id=\"tums\" class=\"mtable\" cellspacing=\"0\">");
            out.println("<tr>");
            out.println("<th class=\"lth\">Umsätze &euro;</th>");
            out.println("<th class=\"mth\">Datum</th>");
            out.println("<th class=\"rth\"></th>");
            out.println("</tr>");

            for (String umsatz : umsaetze) {
                out.println("<tr>");
                out.println("<td class=\"ltd\">" + umsatz.split(";")[1] + "</td>");
                out.println("<td class=\"mtd\">" + umsatz.split(";")[2] + "</td>");
                out.println("<td class=\"rtd\">" + umsatz.split(";")[3].replace('.', ',') + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("<table id=\"tuber\" class=\"mtable\" cellspacing=\"0\">");
            out.println("<tr>");
            out.println("<td class=\"ltd\" style=\"font-weight:700;\">Betrag</td>");
            out.println("<td class=\"intd\">");
            out.println("<input type=\"text\" class=\"inuber\" />");
            out.println("</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td class=\"ltd\">IBAN</td>");
            out.println("<td class=\"intd\">");
            out.println("<input type=\"text\" class=\"inuber\" />");
            out.println("</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td class=\"ltd\">BIC</td>");
            out.println("<td class=\"intd\">");
            out.println("<input type=\"text\" class=\"inuber\" />");
            out.println("</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td class=\"ltd\">Verwendungszweck</td>");
            out.println("<td class=\"intd\">");
            out.println("<input type=\"text\" class=\"inuber\" />");
            out.println("</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td colspan=\"2\">");
            out.println("<input id=\"btnsenden\" type=\"submit\" value=\"Senden\" />");
            out.println("</td>");
            out.println("</tr>");
            out.println("</table>");
            out.println("<table id=\"teig\" class=\"mtable\" cellspacing=\"0\">");
            out.println("<tr>");
            out.println("<td class=\"ltd\" style=\"font-weight:700;\">Anzeige der</td>");
            out.println("<td class=\"mtd\"></td>");
            out.println("<td class=\"rtd\">");
            out.println("<select class=\"einsel\">");
            out.println("<option>letzten 2 Jahre</option>");
            out.println("<option>letzten 3 Monate</option>");
            out.println("<option>letzten Woche</option>");
            out.println("</select>");
            out.println("</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td class=\"ltd\" style=\"font-weight:700;\">Überweisungslimit</td>");
            out.println("<td class=\"mtd\"></td>");
            out.println("<td class=\"rtd\">");
            out.println("<select class=\"einsel\">");
            out.println("<option>&euro; 500</option>");
            out.println("<option>&euro; 5.000</option>");
            out.println("<option>&euro; 20.000</option>");
            out.println("</select>");
            out.println("</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td class=\"ltd\" style=\"font-weight:700;\">Auslandsüberweisungen</td>");
            out.println("<td class=\"mtd\"></td>");
            out.println("<td class=\"rtd\">");
            out.println("<select class=\"einsel\">");
            out.println("<option>Ein</option>");
            out.println("<option>Aus</option>");
            out.println("</select>");
            out.println("</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td colspan=\"3\">");
            out.println("<input id=\"btnspeichern\" type=\"submit\" value=\"Speichern\" />");
            out.println("</td>");
            out.println("</tr>");
            out.println("</table>");
            out.println("</div>");
        }

        out.println("</section>");
        out.println("<div class=\"fix\"></div>");
        out.println("</section>");
        out.println("<div id=\"cnote\">Copyright &copy; Delta Bank 2016</div>");
        out.println("</main>");
        out.println("</section>");
        out.println("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.2/jquery.min.js\"></script>");
        out.println("<script>");
        out.println("(function(){");
        out.println("'use strict';");
        out.println("$('.btab').click(function(){");
        out.println("$('.btab').removeClass('btabact');");
        out.println("$(this).addClass('btabact');");
        out.println("$('.mtable').css('display', 'none');");
        out.println("switch($(this).attr('id')){");
        out.println("case 'umslink':");
        out.println("$('#tums').css('display', 'table');");
        out.println("break;");
        out.println("case 'uberlink':");
        out.println("$('#tuber').css('display', 'table');");
        out.println("break;");
        out.println("case 'eiglink':");
        out.println("$('#teig').css('display', 'table');");
        out.println("break;");
        out.println("}");
        out.println("});");
        out.println("$('#lanmelden').click(function(){ $('#linform').submit(); });");
        out.println("}());");
        out.println("</script>");
        out.println("</body>");
        out.println("</html>");
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException{
        requestHandler(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException{
        requestHandler(req, res);
    }
}