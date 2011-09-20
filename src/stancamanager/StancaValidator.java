/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stancamanager;



import com.mysql.jdbc.ResultSet;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;
import dbmanager.DBmanagement;
import gui.Start;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Giuseppe
 */
public class StancaValidator {

    final static String STANCA_REQ1_XPATH = "//html//style";
    final static String STANCA_REQ2_1_XPATH = "//html//frame";
    final static String STANCA_REQ2_2_XPATH = "//html//frame/*[(@title) and (@name) and noframe]";
    final static String STANCA_REQ3_XPATH = "//html//img[not(@alt) and not(@title)]";
    final static String STANCA_REQ7_8_1_XPATH = "//html//*[img/@usemap or object/@usemap]";
    final static String STANCA_REQ7_8_2_XPATH = "//html//map/*[not(@alt) or //a/*[not(@title)]]";
    final static String STANCA_REQ7_8_3_XPATH = "//html//*[img/@ismap or object/@ismap]";
    final static String STANCA_REQ7_8_4_XPATH = "//html//map/*[not(@alt)]";
    final static String STANCA_REQ7_8_5_XPATH = "//html//map";
    final static String STANCA_REQ9_10_1_XPATH = "//html//table";
    final static String STANCA_REQ9_10_2_1_XPATH = "//html//table//tr";
    final static String STANCA_REQ9_10_2_2_XPATH = "//html//table//td";
    final static String STANCA_REQ9_10_2_3_XPATH = "//html//table//th";
    final static String STANCA_REQ9_10_3_XPATH = "//html//table/*/*";
    final static String STANCA_REQ9_10_4_XPATH = "//html//table//tbody or /html//table//thead and /html//table//colgroup/col";
    final static String STANCA_REQ9_10_4_1_XPATH = "//html//table//tbody";
    final static String STANCA_REQ9_10_4_2_XPATH = "//html//table//thead";
    final static String STANCA_REQ9_10_4_3_XPATH = "//html//table//colgroup/col";
    final static String STANCA_REQ9_10_5_XPATH = "//html//table/caption";
    final static String STANCA_REQ9_10_6_XPATH = "//html//table/text() or /html//table[@summary]";
    final static String STANCA_REQ9_10_6_1_XPATH = "//html//table/text()";
    final static String STANCA_REQ9_10_6_2_XPATH = "//html//table[@summary]";
    final static String STANCA_REQ11_XPATH = "//html//link[(@rel='stylesheet')and (@href) or (@rel='alternate stylesheet')]";
    final static String STANCA_REQ13_1_XPATH = "//html//table";
    final static String STANCA_REQ13_2_XPATH = "//html//table/caption";
    //final static String STANCA_REQ13_3_XPATH = "//html//table/tbody//th";
    final static String STANCA_REQ13_3_XPATH = "//html//table//th";
    final static String STANCA_REQ13_4_1_XPATH = "//html//table/text()";
    final static String STANCA_REQ13_4_2_XPATH = "//html//table[(@summary)]";
    final static String STANCA_REQ14_1_XPATH = "//html//form";
    final static String STANCA_REQ14_2_XPATH = "//html//label";
    //final static String STANCA_REQ14_2_XPATH = "//html//form//*[not(label)]";
    final static String STANCA_REQ14_3_XPATH = "//html//input";
    final static String STANCA_REQ14_4_1_XPATH = "/html//form/input[not(@type)]";
    final static String STANCA_REQ14_4_2_XPATH = "/html//form/label[not(@for)]";
    

    final static String STANCA_REQ15_16_17_1_XPATH = "//html//script";
    final static String STANCA_REQ15_16_17_2_XPATH = "//html//noscript";
    final static String STANCA_REQ15_16_17_3_XPATH = "//html//applet";
    final static String STANCA_REQ15_16_17_4_XPATH = "//html//applet[not(@alt)]";
    final static String STANCA_REQ15_16_17_5_XPATH = "//html//applet[not(@longdesc)])";
    final static String STANCA_REQ18_XPATH = "//html//object[not(@alt) or not(@longdesc)]";
    final static String STANCA_REQ19_XPATH = "//html//a[not(text()) and not(@title)]";
    final static String STANCA_REQ19__1_XPATH = "//html//a";
    final static String STANCA_REQ19__2_XPATH = "//html//a[not(@title)]";
    final static String STANCA_REQ20_XPATH = "//html//meta[(@http-equiv='refresh')]";
    final static String STANCA_REQ21_1_XPATH = "//html/body//a[not(@accesskey)]";
    final static String STANCA_REQ21_2_XPATH = "//html/body//a[not(@tabindex)]";
    final static String STANCA_REQ21_3_XPATH = "//html/body//input[not(@tabindex)]";
    final static String STANCA_REQ21_4_XPATH = "//html/body//area[not(@tabindex)]";
    final static String STANCA_REQ21_5_XPATH = "//html/body//button[not(@tabindex)]";
    final static String STANCA_REQ21_6_XPATH = "//html/body//object[not(@tabindex)]";
    final static String STANCA_REQ21_7_XPATH = "//html/body//select[not(@tabindex)]";
    final static String STANCA_REQ21_8_XPATH = "//html/body//textarea[not(@tabindex)]";
    private static String publicIp = null;
    private static String port;
    private static int trueCount = 0;
    private static int falseCount = 0;
    private static int notAvailableTestCount = 0;
    private static String url;

    public static void requisito1(Selenium selenium, Class testClass, AnalyzedInterface ai) {

        try {
            String title = selenium.getTitle();
            if (title == null || title.equals("")) {
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 1 : {0}", "False");
                ai.setRequisito1("False");
                falseCount++;
            } else {                
                String port = getPort();
                String ip = getPublicIp();                
                
                
                String profile2 = "*custom \"C:\\Program Files\\Mozilla Firefox\\firefox.exe\" ";
                Selenium selenium3;
                selenium3 = new DefaultSelenium("localhost", 4444, profile2, "http://validator.w3.org/");
                selenium3.start();
                selenium3.setSpeed("1000");
                selenium3.open("http://validator.w3.org/");
                //pleaseWait(selenium3);
                selenium3.waitForPageToLoad("10000");
                selenium3.type("uri", "http://" + ip + ((port == null || "".equals(port)) ? "" : (":" + port)) + url);
                
                
                
                selenium3.click("//html/body/div[2]/div/fieldset/form/p[3]/a/span");
                selenium3.waitForPageToLoad("10000");
                //pleaseWait(selenium3);
                String htmlVersion = selenium3.getText("//html/body/div[@id='results_container']/form[@id='form']/table/tbody/tr[4]/td[1]/text()");
                if (htmlVersion.equalsIgnoreCase("XHTML 1.0 Strict") || htmlVersion.equalsIgnoreCase("HTML 4.01 Strict")) {
                    Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 1 : {0}", "True");
                    ai.setRequisito1("True");
                    trueCount++;
                } else if (htmlVersion.equalsIgnoreCase("HTML 4.01 Transitional") || htmlVersion.equalsIgnoreCase("XHTML 1.0 Transitional")) {
                    if (selenium.getXpathCount(STANCA_REQ1_XPATH).intValue() > 0) {
                        Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 1 : {0}", "True");
                        ai.setRequisito1("True");
                        trueCount++;
                    } else {
                        Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 1 : {0}", "False");
                        ai.setRequisito1("False");
                        falseCount++;
                    }
                } else {
                    Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 1 : {0}", "False");
                    ai.setRequisito1("False");
                    falseCount++;
                }
                selenium3.stop();
            }
        } catch (SeleniumException e1) {
            Logger.getLogger(testClass.getName()).log(Level.SEVERE, "Impossibile validare Requisito 1 : " + e1.getMessage());
            ai.setRequisito1("Impossibile validare Requisito 1");
            notAvailableTestCount++;
        }

    }

    public static void requisito2(Selenium selenium, Class testClass, AnalyzedInterface ai) {

        //VERIFICO PRESENZA frame
        int count_REQ2_1 = selenium.getXpathCount(STANCA_REQ2_1_XPATH).intValue();
        //SE SONO PRESENTI FRAME
        if (count_REQ2_1 > 0) {
            int count_REQ2_2 = selenium.getXpathCount(STANCA_REQ2_2_XPATH).intValue();
            //VERIFICO PRESENZA @title & @name & noframe
            if (count_REQ2_2 > 0) {
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 2 : {0}", "True");
                ai.setRequisito2("True");
                trueCount++;
            } else {
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 2 : {0}", "False");
                ai.setRequisito2("False");
                falseCount++;
            }
        } //ALTRIMENTI IL REQUISITO E' RISPETTATO
        else {
            Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 2 : {0}", "True");
            ai.setRequisito2("True");
            trueCount++;

        }

    }

    public static void requisito3(Selenium selenium, Class testClass, AnalyzedInterface ai) {
        int count_REQ3 = selenium.getXpathCount(STANCA_REQ3_XPATH).intValue();
        //CONTROLLO CHE NON CI SIANO IMMAGINI CHE NON PRESENTANO ALCUN TESTO ALTERNATIVO
        if (count_REQ3 == 0) {
            Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 3 : {0}", "True");
            ai.setRequisito3("True");
            trueCount++;
        } else {
            Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 3 : {0}", "False");
            ai.setRequisito3("False");
            falseCount++;
        }

    }

    public static void requisito4_6(Selenium selenium, Class testClass, AnalyzedInterface ai) {
        try {
            Selenium selenium3;            
            String ip = getPublicIp();
            String port = getPort();                        
            String profile2 = "*custom \"C:\\Program Files\\Mozilla Firefox\\firefox.exe\" ";
            selenium3 = new DefaultSelenium("localhost", 4444, profile2, "http://www.checkmycolours.com/");
            selenium3.start();
            selenium3.setSpeed("1000");
            selenium3.open("http://www.checkmycolours.com/");
            //selenium3.waitForPageToLoad("10000");
            selenium3.type("urlll", "http://" + ip + ((port == null || "".equals(port)) ? "" : (":" + port)) + url);
            
            
            
            
            selenium3.click("//a[@onclick='check();return false;']");
            pleaseWait(selenium3);
            //selenium3.waitForPageToLoad("10000");
            try {
                String res = selenium3.getText("//html/body/div[@id='cmcontainer']/div[@id='aj_results']/div[@id='summary_warn']");
                if (res.contains("Congratulations")) {
                    Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 4 : {0}", "True");
                    Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 6 : {0}", "True");
                    ai.setRequisito4("True");
                    ai.setRequisito6("True");
                    trueCount += 2;
                } else {
                    Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 4 : {0}", "False");
                    Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 6 : {0}", "False");
                    ai.setRequisito4("False");
                    ai.setRequisito6("False");
                    falseCount += 2;
                }
            } catch (SeleniumException e1) {
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 4 : {0}", "False");
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 6 : {0}", "False");
                ai.setRequisito4("False");
                ai.setRequisito6("False");
                falseCount += 2;
            } finally {
                selenium3.stop();
            }
        } catch (SeleniumException e1) {
            Logger.getLogger(testClass.getName()).log(Level.SEVERE, "Impossibile validare Requisito 4 e Requisito 6 : " + e1.getMessage());
            ai.setRequisito4("Impossibile validare Requisito 4");
            ai.setRequisito6("Impossibile validare Requisito 6");
            notAvailableTestCount += 2;
        }
    }

    public static void requisito5(Selenium selenium, Class testClass, AnalyzedInterface ai) {
        //TODO VERIFICARE DISPONIBILITA' SERVIZIO
        try {
            Selenium selenium3;
            String ip = getPublicIp();
            String port = getPort();
            String profile2 = "*custom \"C:\\Program Files\\Mozilla Firefox\\firefox.exe\" ";            
            selenium3 = new DefaultSelenium("localhost", 4444, profile2, "http://tools.webaccessibile.org/");
            selenium3.start();
            selenium3.setSpeed("1000");
            selenium3.open("http://tools.webaccessibile.org/test/check.aspx");
            //pleaseWait(selenium3);
            selenium3.waitForPageToLoad("10000");
            selenium3.type("txtUrlPage", "http://" + ip + ((port == null || "".equals(port)) ? "" : (":" + port)) + url);
            selenium3.click("//*[@id='bttControlla']");
            selenium.isElementPresent("//html/body/form[@id='Form1']/div[@id='pagina']/div[@id='panRisultati']/table[@id='tblRisultati']//img[@src='img/warning.gif']");
            Logger.getLogger(testClass.getName()).log(Level.SEVERE, "Impossibile validare Requisito 5  ");
            ai.setRequisito5("Impossibile validare Requisito 5");
            notAvailableTestCount++;
        } catch (SeleniumException e1) {
            Logger.getLogger(testClass.getName()).log(Level.SEVERE, "Impossibile validare Requisito 5 : " + e1.getMessage());
            ai.setRequisito5("Impossibile validare Requisito 5");
            notAvailableTestCount++;
        }
    }

    public static void requisito7_8(Selenium selenium, Class testClass, AnalyzedInterface ai) {
        int count_REQ7_8_1 = selenium.getXpathCount(STANCA_REQ7_8_1_XPATH).intValue();
        int count_REQ7_8_3 = selenium.getXpathCount(STANCA_REQ7_8_3_XPATH).intValue();
        //SE CI SONO MAPPE CLIENT SIDE
        if (count_REQ7_8_1 > 0) {
            //CONTROLLO PRESENZA ATTRIBUTI alt o title
            int count_REQ7_8_2_1 = selenium.getXpathCount(STANCA_REQ7_8_2_XPATH).intValue();
            if (count_REQ7_8_2_1 == 0) {
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 7 : {0}", "True");
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 8 : {0}", "True");
                ai.setRequisito7("True");
                ai.setRequisito8("True");
                trueCount += 2;
            } else {
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 7 : {0}", "False");
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 8 : {0}", "False");
                ai.setRequisito7("False");
                ai.setRequisito8("False");
                falseCount += 2;
            }
            //ALTRIMENTI CONTROLLO PRESENZA MAPPE LATO CLIENT
        } else if (count_REQ7_8_3 > 0) {
            int count_REQ7_8_4 = selenium.getXpathCount(STANCA_REQ7_8_4_XPATH).intValue();
            if (count_REQ7_8_4 == 0) {
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 7 : {0}", "True");
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 8 : {0}", "True");
                ai.setRequisito7("True");
                ai.setRequisito8("True");
                trueCount += 2;
            } else {
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 7 : {0}", "False");
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 8 : {0}", "False");
                ai.setRequisito7("False");
                ai.setRequisito8("False");
                falseCount += 2;
            }
        } else {
            int count_REQ7_8_5 = selenium.getXpathCount(STANCA_REQ7_8_5_XPATH).intValue();
            if (count_REQ7_8_5 == 0) {
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 7 : {0}", "True");
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 8 : {0}", "True");
                ai.setRequisito7("True");
                ai.setRequisito8("True");
                trueCount += 2;
            } else {
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 7 : {0}", "False");
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 8 : {0}", "False");
                ai.setRequisito7("False");
                ai.setRequisito8("False");
                falseCount += 2;
            }
        }
    }

    public static void requisito9_10(Selenium selenium, Class testClass, AnalyzedInterface ai) {
        int count_REQ9_10_1 = selenium.getXpathCount(STANCA_REQ9_10_1_XPATH).intValue();
        int count_REQ9_10_3 = selenium.getXpathCount(STANCA_REQ9_10_3_XPATH).intValue();
        int count_REQ9_10_5 = selenium.getXpathCount(STANCA_REQ9_10_5_XPATH).intValue();
        //SE SONO PRESENTI TABELLE
        if (count_REQ9_10_1 > 0) {
            int count_REQ9_10_2_1 = selenium.getXpathCount(STANCA_REQ9_10_2_1_XPATH).intValue();
            int count_REQ9_10_2_2 = selenium.getXpathCount(STANCA_REQ9_10_2_2_XPATH).intValue();
            int count_REQ9_10_2_3 = selenium.getXpathCount(STANCA_REQ9_10_2_3_XPATH).intValue();
            int count_REQ9_10_2 = (count_REQ9_10_2_1 * count_REQ9_10_2_2) + count_REQ9_10_2_3;
            int count_REQ9_10_4_1 = selenium.getXpathCount(STANCA_REQ9_10_4_1_XPATH).intValue();
            int count_REQ9_10_4_2 = selenium.getXpathCount(STANCA_REQ9_10_4_2_XPATH).intValue();
            int count_REQ9_10_4_3 = selenium.getXpathCount(STANCA_REQ9_10_4_3_XPATH).intValue();
            int count_REQ9_10_4 = count_REQ9_10_4_1 + (count_REQ9_10_4_2 * count_REQ9_10_4_3);

            //CONTROLLO ASSENZA DI tr E td O th
            if (count_REQ9_10_2 == 0) {
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 9 : {0}", "False");
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 10 : {0}", "False");
                ai.setRequisito9("False");
                ai.setRequisito10("False");
                falseCount += 2;
            }
            //SE PRESENTI SE SONO PRESENTI tr E td O th E TABELLE ANNIDATE
            if (count_REQ9_10_2 > 0 && count_REQ9_10_3 > 0) {
                //CONTROLLO PRESENZA DI tbody o thead e colgroup/col
                if (count_REQ9_10_4 == 0) {
                    Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 9 : {0}", "False");
                    Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 10 : {0}", "False");
                    ai.setRequisito9("False");
                    ai.setRequisito10("False");
                    falseCount += 2;
                }
            }
            if (count_REQ9_10_2 > 0 && count_REQ9_10_4 > 0 && count_REQ9_10_5 == 0) {
                int count_REQ13_4_1 = selenium.getXpathCount(STANCA_REQ9_10_6_1_XPATH).intValue();
                int count_REQ13_4_2 = selenium.getXpathCount(STANCA_REQ9_10_6_2_XPATH).intValue();
                if (count_REQ13_4_1 + count_REQ13_4_2 == 0) {
                    Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 9 : {0}", "False");
                    Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 10 : {0}", "False");
                    ai.setRequisito9("False");
                    ai.setRequisito10("False");
                    falseCount += 2;
                } else {
                    Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 9 : {0}", "True");
                    Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 10 : {0}", "True");
                    ai.setRequisito9("True");
                    ai.setRequisito10("True");
                    trueCount += 2;
                }
            } else if (count_REQ9_10_2 > 0 && count_REQ9_10_3 == 0 && count_REQ9_10_5 > 0) {
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 9 : {0}", "True");
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 10 : {0}", "True");
                ai.setRequisito9("True");
                ai.setRequisito10("True");
                trueCount += 2;
            }
        } //ALTRIMENTI IL REQUISITO è RISPETTATO PERCHE' NON CI SONO TABELLE
        else {
            Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 9 : {0}", "True");
            Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 10 : {0}", "True");
            ai.setRequisito9("True");
            ai.setRequisito10("True");
            trueCount += 2;
        }
    }

    public static void requisito11(Selenium selenium, Class testClass, AnalyzedInterface ai) {
        try {
            //VALUTAZIONE PRESENZA css
            int count_REQ11 = selenium.getXpathCount(STANCA_REQ11_XPATH).intValue();
            if (count_REQ11 == 0) {
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 11 : {0}", "True");
                ai.setRequisito11("True");
                trueCount++;
            } else {
                Selenium selenium3;                
                String ip = getPublicIp();
                String port = getPort();                
                String profile2 = "*custom \"C:\\Program Files\\Mozilla Firefox\\firefox.exe\" ";
                selenium3 = new DefaultSelenium("localhost", 4444, profile2, "http://jigsaw.w3.org/css-validator/");
                selenium3.start();
                selenium3.setSpeed("1000");
                selenium3.open("http://jigsaw.w3.org/css-validator/");
                selenium3.waitForPageToLoad("10000");
                
                
                selenium3.type("uri", "http://" + ip + ((port == null || "".equals(port)) ? "" : (":" + port)) + url);
                

                
                selenium3.click("//fieldset[@id='validate-by-uri']/form/p[3]/label/a/span");
                //selenium3.waitForPageToLoad("10000");
                pleaseWait(selenium3);
                int resultTrue = selenium3.getXpathCount("//html/body/div[@id='results_container']/div[@id='congrats']/h3").intValue();
                int resultFalse = selenium3.getXpathCount("//html/body/div[@id='results_container']/div[@id='errors']/h3").intValue();
                if (resultTrue > 0) {
                    Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 11 : {0}", "True");
                    ai.setRequisito11("True");
                    trueCount++;
                } else if (resultFalse > 0) {
                    Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 11 : {0}", "False");
                    ai.setRequisito11("False");
                    falseCount++;
                } else {
                    Logger.getLogger(testClass.getName()).log(Level.SEVERE, "Impossibile validare Requisito 11 ");
                    ai.setRequisito11("Impossibile validare Requisito 11");
                    notAvailableTestCount++;
                }
                selenium3.stop();
            }
        } catch (SeleniumException e1) {
            Logger.getLogger(testClass.getName()).log(Level.SEVERE, "Impossibile validare Requisito 11 : " + e1.getMessage());
            ai.setRequisito11("Impossibile validare Requisito 11");
            notAvailableTestCount++;
        }
    }

    public static void requisito13(Selenium selenium, Class testClass, AnalyzedInterface ai) {
        int count_REQ13_1 = selenium.getXpathCount(STANCA_REQ13_1_XPATH).intValue();
        int count_REQ13_3 = selenium.getXpathCount(STANCA_REQ13_2_XPATH).intValue();


        //SE SONO PRESENTI TABELLE CONTROLLO LA PRESENZA DI header SULLE CELLE (th) E DI TITOLI (caption)
        if (count_REQ13_1 > 0) {
            //CONTROLLO PRESENZA th NEL tbody DELLA TABELLA SE PRESENTE IL REQUISITO NON è RISPETTATO (HEADER)
            int count_REQ13_6 = selenium.getXpathCount(STANCA_REQ13_3_XPATH).intValue();

            if (count_REQ13_6 > 0) {
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 13 : {0}", "False");
                ai.setRequisito13("False");
                falseCount++;
            }
            //SE caption E' ASSENTE (count_REQ13_6==0) CONTROLLO  CHE NON SIANO STATI USATI HEADER IN MODO SBAGLIATO
            if (count_REQ13_3 + count_REQ13_6 == 0) {
                int count_REQ13_4_1 = selenium.getXpathCount(STANCA_REQ13_4_1_XPATH).byteValue();
                int count_REQ13_4_2 = selenium.getXpathCount(STANCA_REQ13_4_2_XPATH).intValue();
                //CONTROLLO CHE IN ASSENZA DI CAPTION NON SIANO ASSENTI TEXT O SUMMARY
                if (count_REQ13_4_1 + count_REQ13_4_2 == 0) {
                    Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 13 : {0}", "False");
                    ai.setRequisito13("False");
                    falseCount++;
                } else {
                    Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 13 : {0}", "True");
                    ai.setRequisito13("True");
                    trueCount++;
                }
            }
        } //ALTRIMENTI IL REQUISITO è RISPETTATO
        else {
            Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 13 : {0}", "True");
            ai.setRequisito13("True");
            trueCount++;
        }
    }

    public static void requisito14(Selenium selenium, Class testClass, AnalyzedInterface ai) {
        int count_REQ14_1 = selenium.getXpathCount(STANCA_REQ14_1_XPATH).intValue();
        //CONTROLLO PRESENZA forms
        if (count_REQ14_1 > 0) {
            //CONTROLLO PRESENZA label
            int count_REQ14_2 = selenium.getXpathCount(STANCA_REQ14_2_XPATH).intValue();
            //boolean res = selenium.isElementPresent(STANCA_REQ14_2_XPATH);
            //String label = selenium.getSelectedLabel(STANCA_REQ14_2_XPATH);
            //System.out.println(res);
            if (count_REQ14_2 > 0) {
                //CONTROLLO PRESENZA input
                int count_REQ14_3 = selenium.getXpathCount(STANCA_REQ14_3_XPATH).intValue();
                if (count_REQ14_3 > 0) {
                    //CONTROLLO ATTRIBUTI type PER INPUT E for PER LABEL
                    int count_REQ14_4_1 = selenium.getXpathCount(STANCA_REQ14_4_1_XPATH).intValue();
                    int count_REQ14_4_2 = selenium.getXpathCount(STANCA_REQ14_4_2_XPATH).intValue();
                    //int count_prova = selenium.getXpathCount(STANCA_prova_XPATH).intValue();

                    if (count_REQ14_4_1 * count_REQ14_4_2 == 0) {
                        Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 14 : {0}", "True");
                        ai.setRequisito14("True");
                        trueCount++;
                    } else {
                        Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 14 : {0}", "True");
                        ai.setRequisito14("False");
                        falseCount++;
                    }
                } else {
                    Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 14 : {0}", "False");
                    ai.setRequisito14("False");
                    falseCount++;
                }
            } else {
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 14 : {0}", "False");
                ai.setRequisito14("False");
                falseCount++;
            }
        } //SE NON CI SONO FORMS IL REQUISITO E' RISPETTATO
        else {
            Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 14 : {0}", "True");
            ai.setRequisito14("True");
            trueCount++;
        }
    }

    public static void requisito15_16_17(Selenium selenium, Class testClass, AnalyzedInterface ai) {

        //CONTROLLO PRESENZA script E applet
        int count_REQ15_16_17_1 = selenium.getXpathCount(STANCA_REQ15_16_17_1_XPATH).intValue();
        int count_REQ15_16_17_3 = selenium.getXpathCount(STANCA_REQ15_16_17_3_XPATH).intValue();
        //SE SONO PRESENTI SCRIPT
        if (count_REQ15_16_17_1 > 0) {
            //CONTROLLO LA PRESENZA DI UNA DESCRIZIONE ALTERNATIVA  DELLO script NEL TAG noscript
            String text = selenium.getText("//html//body//script");
            int count_REQ15_16_17_2 = selenium.getXpathCount(STANCA_REQ15_16_17_2_XPATH).intValue();
            if (count_REQ15_16_17_2 == 0) {
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 15 : {0}", "Warning");
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 16 : {0}", "Warning");
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 17 : {0}", "Warning");
                ai.setRequisito15("Warning");
                ai.setRequisito16("Warning");
                ai.setRequisito17("Warning");
                notAvailableTestCount += 3;
            } else {
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 15 : {0}", "True");
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 16 : {0}", "True");
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 17 : {0}", "True");
                ai.setRequisito15("True");
                ai.setRequisito16("True");
                ai.setRequisito17("True");
                trueCount += 3;
            }
            //ALTRIMENTI PASSO A CONTROLARE LA PRESENZA DEGLI APPLET
        } else if (count_REQ15_16_17_3 > 0) {
            //CONTROLLO CHE NON CI SIANO APPLET NON AVENTI alt O longdesc
            int count_REQ15_16_17_4 = selenium.getXpathCount(STANCA_REQ15_16_17_4_XPATH).intValue();
            int count_REQ15_16_17_5 = selenium.getXpathCount(STANCA_REQ15_16_17_5_XPATH).intValue();
            if (count_REQ15_16_17_4 + count_REQ15_16_17_5 == 0) {
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 15 : {0}", "False");
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 16 : {0}", "False");
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 17 : {0}", "False");
                ai.setRequisito15("False");
                ai.setRequisito16("False");
                ai.setRequisito17("False");
                falseCount += 3;
            } else {
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 15 : {0}", "True");
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 16 : {0}", "True");
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 17 : {0}", "True");
                ai.setRequisito15("True");
                ai.setRequisito16("True");
                ai.setRequisito17("True");
                trueCount += 3;
            }
        } //ALTRIMENTI I REQUISITI SONO RISPETTATI
        else {
            Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 15 : {0}", "True");
            Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 16 : {0}", "True");
            Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 17 : {0}", "True");
            ai.setRequisito15("True");
            ai.setRequisito16("True");
            ai.setRequisito17("True");
            trueCount += 3;
        }

    }

    public static void requisito18(Selenium selenium, Class testClass, AnalyzedInterface ai) {
        int count_REQ18_XPATH = selenium.getXpathCount(STANCA_REQ18_XPATH).intValue();
        //SE NON SONO PRESENTI object SENZA alt OPPURE SENZA longdesc IL REQUISITO E' RISPETTATO
        if (count_REQ18_XPATH == 0) {
            Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 18 : {0}", "True");
            ai.setRequisito18("True");
            trueCount++;
        } else {
            Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 18 : {0}", "False");
            ai.setRequisito18("False");
            falseCount++;
        }
    }

    public static void requisito19(Selenium selenium, Class testClass, AnalyzedInterface ai) {
        //int count_REQ19_XPATH = selenium.getXpathCount(STANCA_REQ19_XPATH).intValue();
        try{
        if(selenium.isTextPresent(STANCA_REQ19__1_XPATH)&&selenium.isElementPresent(STANCA_REQ19__2_XPATH)){
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 19 : {0}", "False");
                ai.setRequisito19("False");
                falseCount++;
        }else{
                Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 19 : {0}", "True");
                ai.setRequisito19("True");
                trueCount++;
        }
        } catch (SeleniumException e1) {
            Logger.getLogger(testClass.getName()).log(Level.SEVERE, "Impossibile validare Requisito 19 : " + e1.getMessage());
            ai.setRequisito19("Impossibile validare Requisito 19");
            notAvailableTestCount++;
        }
    }

    public static void requisito20(Selenium selenium, Class testClass, AnalyzedInterface ai) {
        int count_REQ20_XPATH = selenium.getXpathCount(STANCA_REQ20_XPATH).intValue();
        //SE NEL META NON E' PRESENTE http-equiv='refresh'  IL REQUISITO E RISPETTATO
        if (count_REQ20_XPATH == 0) {
            Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 20 : {0}", "True");
            ai.setRequisito20("True");
            trueCount++;
        } else {
            Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 20 : {0}", "False");
            ai.setRequisito20("False");
            falseCount++;
        }
    }

    public static void requisito21(Selenium selenium, Class testClass, AnalyzedInterface ai) {
        int count_REQ21_1_XPATH = selenium.getXpathCount(STANCA_REQ21_1_XPATH).intValue();
        int count_REQ21_2_XPATH = selenium.getXpathCount(STANCA_REQ21_2_XPATH).intValue();
        int count_REQ21_3_XPATH = selenium.getXpathCount(STANCA_REQ21_3_XPATH).intValue();
        int count_REQ21_4_XPATH = selenium.getXpathCount(STANCA_REQ21_4_XPATH).intValue();
        int count_REQ21_5_XPATH = selenium.getXpathCount(STANCA_REQ21_5_XPATH).intValue();
        int count_REQ21_6_XPATH = selenium.getXpathCount(STANCA_REQ21_6_XPATH).intValue();
        int count_REQ21_7_XPATH = selenium.getXpathCount(STANCA_REQ21_7_XPATH).intValue();
        int count_REQ21_8_XPATH = selenium.getXpathCount(STANCA_REQ21_8_XPATH).intValue();
        //SE NON CI SONO a CHE NON HANNO @accesskey E GLI ELEMENTI a,button, area, input, object, select, textarea SENZA @tabindex IL REQUISITO E' RISPETTATO
        if (count_REQ21_1_XPATH * (count_REQ21_2_XPATH + count_REQ21_3_XPATH + count_REQ21_4_XPATH + count_REQ21_5_XPATH + count_REQ21_6_XPATH + count_REQ21_7_XPATH + count_REQ21_8_XPATH) == 0) {
            Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 21 : {0}", "True");
            ai.setRequisito21("Warning");
            trueCount++;
        } else {
            Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisito 21 : {0}", "False");
            ai.setRequisito21("False");
            falseCount++;
        }
    }

    //RESTITUISCE L'INDIRIZZO PUBLICO. SE IL VALORE E' null SI ASSEGNA IL NUOVO VALORE RECUPERATO ATTRAVERSO IL SERVIZIO WHATISMYIP
    private static String getPublicIp() {
        if (StancaValidator.publicIp == null || StancaValidator.publicIp.equals("")) {
            Selenium selenium2;
            String profile2 = "*custom \"C:\\Program Files\\Mozilla Firefox\\firefox.exe\" ";
            selenium2 = new DefaultSelenium("localhost", 4444, profile2, "http://www.whatismyip.com/");
            selenium2.start();
            selenium2.setSpeed("1000");
            selenium2.open("http://www.whatismyip.com/");
            publicIp = parseIp(selenium2.getText("//*[@id='ip']"));
            selenium2.stop();
        }
        return publicIp;

    }

    private static String parseIp(String text) {
        String token = "";
        char[] toParse = text.toCharArray();
        int startIP = 0;
        char c = 0;

        //Consuma tutto quello che si trova prima dell'indirizzo IP
        for (startIP = 0; startIP < toParse.length; startIP++) {
            c = toParse[startIP];
            if ("0123456789.".indexOf(c) > -1) {
                break;
            }
        }
        for (char digit : text.substring(startIP).toCharArray()) {
            if ("0123456789.".indexOf(digit) > -1) {
                token += digit;
            } else {
                break;
            }
        }
        return token;
    }
//RESTITUISCE IL NUMERO DI REQUISITI TRUE FALSE E IMPOSSIBILI DA VALIDARE

    public static void resultCount(Class testClass, AnalyzedInterface ai) {
        Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisiti true : {0}", trueCount);
        Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisiti false : {0}", falseCount);
        Logger.getLogger(testClass.getName()).log(Level.INFO, " Requisiti impossibili da validare : {0}", notAvailableTestCount);
        ai.setRequisitiFalse(falseCount);
        ai.setRequisitiTrue(trueCount);
        ai.setRequisitiNonValidati(notAvailableTestCount);
        trueCount = 0;
        falseCount = 0;
        notAvailableTestCount = 0;

    }
//RESTITUISCE INFORMAZIONI RELATIVE ALL'INTERFACCIA ANALIZZATA

    public static void getIdInterface(String cluster, String urlInterface, Class testClass, int idInterface, AnalyzedInterface ai, boolean assertInterfaccia) {
        Logger.getLogger(testClass.getName()).log(Level.INFO, " Url interfaccia : {0}", urlInterface);
        Logger.getLogger(testClass.getName()).log(Level.INFO, " Id interfaccia : {0}", idInterface);
        Logger.getLogger(testClass.getName()).log(Level.INFO, " Cluster : {0}", cluster);
        ai.setClusterInterfaccia(cluster);
        ai.setIdInterfaccia(idInterface);
        //sb.setUrlInterfaccia(urlInterface);
        ai.setAssertInterfaccia(assertInterfaccia);
        ai.setTraccia(testClass.getName().substring(18));        
        StancaValidator.url="/pages/"+addressPagesSaved(idInterface);
        
        
            

    }

    public static void printTestTimeInterface(Class testClass, long startTime, long endTime, int idInterface, AnalyzedInterface ai) {

        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        Timestamp resultdate = new Timestamp(endTime - startTime);
        Logger.getLogger(testClass.getName()).log(Level.INFO, " Tempo durata test su interfaccia " + idInterface + " = {0}", sdf.format(resultdate));
        ai.setDurataTestInterfaccia(sdf.format(resultdate));

    }

    public static void printTestTime(Class testClass, long startTime, long endTime, TestTime tt) {

        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        Timestamp resultdate = new Timestamp(endTime - startTime);
        Logger.getLogger(testClass.getName()).log(Level.INFO, " Tempo durata totale test = {0}", sdf.format(resultdate));
        tt.setDurataTotaleTest(sdf.format(resultdate));
    }

    public static void setPublicIp(String ip) {
        StancaValidator.publicIp = ip;
    }

    public static String getPort() {
        return StancaValidator.port;
    }

    public static void setPort(String port) {
        StancaValidator.port = port;
    }

    private static void pleaseWait(Selenium selenium) {
        synchronized (selenium) {
            try {
                selenium.wait(15000);
            } catch (InterruptedException ex) {
                System.out.println("Errore nell'attesa.");
            }
        }
    }

    public static String addressPagesSaved(int id_interface) {

        String query = new String("SELECT t.urilocal FROM tab_interface t where t.id_tab_interface= '" + id_interface + "' ");
        String addressFile="";
        DBmanagement db = new DBmanagement();
        db.db_connection(Start.user, Start.passwo, Start.nomeDB, Start.porto, Start.posiz);
        ResultSet rs = db.select(query);
        try {
            if (rs.next()) {
                addressFile = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StancaValidator.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.close_db_connection(db.db_connection(Start.user, Start.passwo, Start.nomeDB, Start.porto, Start.posiz));
        return addressFile;
    }


    //Crea un file temporaneo data una stringa;

    private static void createNewInterface(String htmlSource, int idInterface) throws IOException{
        //FIXME da portare in una classe utility di TestRia
        //File tempfile = File.createTempFile(new Long(System.currentTimeMillis()).toString(), ".html");
        File newInterface=new File("C:\\Users\\Peppe\\Desktop\\apache-tomcat-6.0.29\\webapps\\pages/"+idInterface+".html");
        // Delete temp file when program exits.
        //newInterface.deleteOnExit();

        // Write to temp file
        BufferedWriter out = new BufferedWriter(new FileWriter(newInterface));
        out.write(htmlSource);
        out.close();
        //return newInterface;
    }

}
