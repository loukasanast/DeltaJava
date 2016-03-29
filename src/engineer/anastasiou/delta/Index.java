package engineer.anastasiou.delta;

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

        String kontonummer = null;
        String kontoidentifikation = null;

        HttpSession session = req.getSession();

        if(req.getParameter("abmelden") != null){
            session.invalidate();
        }else if(session.getAttribute("kontonummer") != null){
            kontonummer = session.getAttribute("kontonummer").toString();
            kontoidentifikation = session.getAttribute("kontoidentifikation").toString();
        }

        boolean eingeloggt = false;
        Konto konto = new Konto();

        if(req.getParameter("kontonummer") != null && req.getParameter("kontoidentifikation") != null){
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
                byte[] encrypted = new byte[cipher.getOutputSize(input.length)];
                int enc_len = cipher.update(input, 0, input.length, encrypted, 0);
                cipher.doFinal(encrypted, enc_len);

                for(String k : konten){
                    if(k.split(";")[0].equals(req.getParameter("kontonummer"))){
                        for(int i = 0; i < k.split(";")[1].split(",").length; i++){
                            if(k.split(";")[1].split(",")[i].equals(((Byte)encrypted[i]).toString()) && i != k.split(";")[1].split(",").length - 1){
                                continue;
                            }else if(k.split(";")[1].split(",")[i].equals(((Byte)encrypted[i]).toString())){
                                eingeloggt = true;

                                konto.setKontonummer(k.split(";")[0]);
                                konto.setPasswort(k.split(";")[1]);
                                konto.setBlz(k.split(";")[2]);
                                konto.setIban(k.split(";")[3]);
                                konto.setBic(k.split(";")[4]);
                                konto.setInhaber(k.split(";")[5]);

                                if(req.getParameter("remember") != null){
                                    session.setAttribute("kontonummer", konto.getKontonummer());
                                    session.setAttribute("kontoidentifikation", konto.getPasswort());
                                }
                            }else{
                                break;
                            }
                        }
                    }
                }
            }catch(Exception exc) {
                out.println(exc.getMessage());
            }
        }else if(kontonummer != null && kontoidentifikation != null){
            File kontenFile = new File("c:/Users/Lucas/Documents/Tomcat/webapps/delta/konten.txt");
            List<String> konten = Files.readAllLines(kontenFile.toPath());

            for(String k : konten){
                if(k.split(";")[0].equals(kontonummer)) {
                    for(int i = 0; i < k.split(";")[1].split(",").length; i++){
                        if(k.split(";")[1].split(",")[i].equals(kontoidentifikation.split(",")[i]) && i != k.split(";")[1].split(",").length - 1){
                            continue;
                        }else if(k.split(";")[1].split(",")[i].equals(kontoidentifikation.split(",")[i])){
                            eingeloggt = true;

                            konto.setKontonummer(k.split(";")[0]);
                            konto.setPasswort(k.split(";")[1]);
                            konto.setBlz(k.split(";")[2]);
                            konto.setIban(k.split(";")[3]);
                            konto.setBic(k.split(";")[4]);
                            konto.setInhaber(k.split(";")[5]);
                        }else{
                            break;
                        }
                    }
                }
            }
        }

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
            out.println("<form id=\"linform\" method=\"get\" action=\"\">");
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
            out.println("<input type=\"checkbox\" id=\"remember\" name=\"remember\" />");
            out.println("<label for=\"remember\">Eingeloggt bleiben</label>");
            out.println("</div>");
            out.println("</td>");
            out.println("</tr>");
            out.println("</table>");

            if(req.getParameter("kontoidentifikation") != null){
                out.println("<span style=\"color:red;\">Sie haben falsche Kontodaten eingegeben.</span>");
            }

            out.println("</form>");
        }else{
            out.println("<div id=\"theader\">");
            out.println("<div style=\"float:left;color:#FFF;\">Hallo, " + konto.getInhaber() + "</div>");
            out.println("<img src=\"img/lock.png\" alt=\"\" style=\"margin:0 2px;vertical-align:-1px;opacity:.18;\" />");
            out.println("<a href=\"?abmelden=on\">Abmelden</a>");

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

            out.println("<span class=\"dname\">BLZ</span>:" + konto.getBlz());
            out.println("<br />");
            out.println("<span class=\"dname\">Konto-Nr</span>:" + konto.getKontonummer() + "<br />");
            out.println("<br />");
            out.println("<span class=\"dname\">BIC</span>:" + konto.getBic());

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

            out.println(konto.getIban());

            out.println("</div>");
            out.println("<div id=\"mtrcont\">");
            out.println("Saldo &euro;<br />");

            for (String u : umsaetze) {
                saldo += Double.parseDouble(u.split(";")[3]);
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

            for (String u : umsaetze) {
                out.println("<tr>");
                out.println("<td class=\"ltd\">" + u.split(";")[1] + "</td>");
                out.println("<td class=\"mtd\">" + u.split(";")[2] + "</td>");
                out.println("<td class=\"rtd\">" + u.split(";")[3].replace('.', ',') + "</td>");
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