package test;
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
import com.thoughtworks.selenium.Selenium;
import org.seleniuminspector.SeleniumTestCase;
import service.Utility;
import testsuitemanager.Commands;
import gui.Start;
import stancamanager.StancaValidator;
import org.xml.sax.SAXException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.betwixt.io.BeanWriter;
import stancamanager.AnalyzedInterface;
import stancamanager.TestTime;
import java.beans.IntrospectionException;
/**
*
* @author Giuseppe
*/
public class Testing_NoUser_2 extends SeleniumTestCase {
    private Selenium selenium;
    private Utility utility = new Utility();
    private long startDateTest =0 ;
    private long endDateTest=0 ;
    private long startDateInterface=0;
    private long endDateInterface=0;
    private List<AnalyzedInterface> analizyedInterfaces = new LinkedList<AnalyzedInterface>();
    private List resultTime = new LinkedList();
 
    public boolean test() {
        startDateTest=System.currentTimeMillis();
        StancaValidator.setPublicIp("143.225.170.126");
        StancaValidator.setPort("8080");
        selenium = utility.setUp("http://localhost:8080/tudu-dwr/welcome.action");
        String[][] cmd = new String[2][3];
        boolean assertInterface = false;

        String[][] fields = null;
        Commands comm = new Commands();
        String cluster = new String("C4");
        cmd[0][0] = "click";
        cmd[0][1] = "//html[1]/body[1]/table[2]/tbody[1]/tr[2]/td[1]/div[1]/table[1]/tbody[1]/tr[1]/td[2]/form[1]/table[1]/tbody[1]/tr[4]/th[1]/input[1]";
        cmd[0][2] = "35";
        cmd[1][0] = "end";
        cmd[1][1] = "//html[1]/body[1]/table[2]/tbody[1]/tr[2]/td[1]/div[1]/table[1]/tbody[1]/tr[1]/td[2]/form[1]/table[1]/tbody[1]/tr[4]/th[1]/input[1]";
        cmd[1][2] = "36";
        for(int i = 0; i < cmd.length; i++){
            startDateInterface=System.currentTimeMillis();
            assertInterface = utility.assertPage(utility.getObj(cluster, Integer.parseInt(cmd[i][2])), cluster, selenium);
            AnalyzedInterface ai = new AnalyzedInterface();
            analizyedInterfaces.add(ai);
            StancaValidator.getIdInterface(cluster, selenium.getLocation(), Testing_NoUser_2.class,Integer.parseInt(cmd[i][2]), ai, assertInterface);
            StancaValidator.requisito1(selenium, Testing_NoUser_2.class, ai);
            StancaValidator.requisito2(selenium, Testing_NoUser_2.class, ai);
            StancaValidator.resultCount(Testing_NoUser_2.class, ai);
            if(assertInterface){
                fields = comm.getValueFields(cmd[i][2]);
                if(fields.length != 0){
                    for(int k = 0; k < fields.length; k++){
                        selenium.type("//html[1]"+fields[k][1].substring(8), fields[k][0]);
                    }
                }
                if(cmd[i][0].equals("click")){
                    selenium.click(cmd[i][1]);
                }
            }
            else{
                endDateInterface=System.currentTimeMillis();
                StancaValidator.printTestTimeInterface(Testing_NoUser_2.class,startDateInterface,endDateInterface,Integer.parseInt(cmd[i][2]),ai);
                break;
            }
            endDateInterface=System.currentTimeMillis();
            StancaValidator.printTestTimeInterface(Testing_NoUser_2.class,startDateInterface,endDateInterface,Integer.parseInt(cmd[i][2]),ai);
        }
        utility.tearDown(selenium);
        endDateTest=System.currentTimeMillis();
        TestTime tt = new TestTime();
        resultTime.add(tt);
        StancaValidator.printTestTime(Testing_NoUser_2.class, startDateTest, endDateTest, tt);
        analizyedInterfaces.addAll(resultTime);
        createLogToFile(analizyedInterfaces);
        return assertInterface;
     }
 
 
    public void bd_parameters() {
        Start.user = "root";
        char[] password = "root".toCharArray();
        Start.passwo = password;
        Start.nomeDB = "creria4";
        Start.porto = "3306";
        Start.posiz = "localhost";
    }
 
    public void createLogToFile(List analizyedInterfaces) {
        try {
            this.analizyedInterfaces = analizyedInterfaces;
            Writer outputWriter = new FileWriter("C:/TestRia/stancaReport/Testing_NoUser_2.xml");
            BeanWriter beanWriter = new BeanWriter(outputWriter);
            beanWriter.enablePrettyPrint();
            beanWriter.writeXmlDeclaration("<?xml version='1.0' encoding='UTF-8'?>");
            beanWriter.write(analizyedInterfaces);
            outputWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(Testing_NoUser_2.class.getName()).log(Level.SEVERE," Errore creazione file di log ", ex);
        } catch (SAXException ex) {
            Logger.getLogger(Testing_NoUser_2.class.getName()).log(Level.SEVERE," Errore creazione file di log ", ex);
        } catch (IntrospectionException ex) {
            Logger.getLogger(Testing_NoUser_2.class.getName()).log(Level.SEVERE," Errore creazione file di log ", ex);
        }
    }
 
    public static void main(String[] args) {
        Testing_NoUser_2 myTest = new Testing_NoUser_2();
        myTest.bd_parameters();
        myTest.test();
    }
 
}
