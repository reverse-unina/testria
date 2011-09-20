/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import dbmanager.DBmanagement;
import com.thoughtworks.selenium.*;
import org.seleniuminspector.SeleniumTestCase;
import gui.Start;
import java.util.Vector;
/**
 *
 * @author Utente
 */
public class Utility {
    private DBmanagement db = new DBmanagement();

    public Selenium setUp() {
        Selenium selenium;
        selenium = new DefaultSelenium("localhost", 4444,"*firefox", "http://localhost:8080/");
        startupTomcat();
        pleaseWait(12000);
        resetDB();
        selenium.start();
        selenium.setSpeed("1000");
        selenium.open("tudu/");
        return selenium;
    }

    public void tearDown(Selenium selenium) {
        selenium.stop();
        shutdownTomcat();
        pleaseWait(5000);
    }
   
    private void resetDB(){
       Connection connection = null;
       connection = db.db_connection(Start.user, Start.passwo, "tudu", Start.porto, Start.posiz);
       db.resetDB();
       db.close_db_connection(connection);
    }

    private void startupTomcat(){
       Runtime myRun = Runtime.getRuntime();
       try {
           myRun.exec("cmd /C start "+"%CATALINA_HOME%\\bin\\startup.bat");
       }
       catch(Exception e) {System.out.println("Impossible to run Tomcat.");}
    }

    private void shutdownTomcat(){
       Runtime myRun = Runtime.getRuntime();
       try {
           myRun.exec("cmd /C start "+"%CATALINA_HOME%\\bin\\shutdown.bat");
       }
       catch(Exception e) {System.out.println("Impossible to stop Tomcat.");}
    }

    private void pleaseWait(int time){
        synchronized(this){
            try {
                this.wait(time);
            }catch (InterruptedException ex) {
                System.out.println("Errore nell'attesa.");
            }
        }
    }

    public boolean assertPage(String[][] obj, String cluster, Selenium selenium){
        boolean res = false;
        if(cluster.equals("C1") || cluster.equals("C2")){
            for(int i = 0; i < obj.length; i++){
                if(isParticularCase(obj[i][3])){
                    if(selenium.isElementPresent("//html[1]"+obj[i][1].substring(8))){
                        res = true;
                        System.out.println("Mod "+cluster+" --> "+obj[i][0]+" "+obj[i][2]+" "+obj[i][3]);//Da eliminare
                    }
                }
                else{
                    try{
                        SeleniumTestCase.assertEquals(obj[i][3],selenium.getAttribute("//html[1]"+obj[i][1].substring(8)+"@"+obj[i][2]));
                        res = true;
                        System.out.println(cluster+" --> "+obj[i][0]+" "+obj[i][2]+" "+obj[i][3]);//Da eliminare
                    }
                    catch(SeleniumException e){
                        res = false;
                        System.out.println(cluster+" --> Fallito: "+obj[i][0]+" "+obj[i][2]+" "+obj[i][3]);//Da eliminare
                        break;
                    }
                    catch(AssertionError e){
                        res = false;
                        System.out.println(cluster+" --> Fallito: "+obj[i][0]+" "+obj[i][2]+" "+obj[i][3]);//Da eliminare
                        break;
                    }
                }
            }
        }
        else{
            for(int i = 0; i < obj.length; i++){
                res = selenium.isElementPresent("//html"+obj[i][1].substring(5));
                if(res){
                    System.out.println(cluster+" --> "+obj[i][0]+" "+"//html"+obj[i][1].substring(5));
                }
                else{
                    System.out.println(cluster+" --> Fallito: "+obj[i][0]+" "+"//html"+obj[i][1].substring(5));
                    break;
                }

            }
        }
        return res;
    }

    public String[][] getObj(String clu, int face){
       ResultSet rs = null;
       DataSet ds = null;
       String[][] res = null;
       db.db_connection(Start.user, Start.passwo, Start.nomeDB, Start.porto, Start.posiz);
       if(clu.equals("C1")){
           rs = db.selectObjectAndAttributesC1(face);
           ds = new DataSet(rs,4);
           res = filterObjs(ds.getMatrixOfRSet());
           db.close_db_connection(db.getConnection());
       }
       else if(clu.equals("C2")){
           rs = db.selectObjectAndAttributesC2(face);
           ds = new DataSet(rs,4);
           res = filterObjs(ds.getMatrixOfRSet());
           db.close_db_connection(db.getConnection());
       }
       else if(clu.equals("C3")){
           rs = db.selectObjectAndAttributesC3(face);
           ds = new DataSet(rs,2);
           res = ds.getMatrixOfRSet();
           db.close_db_connection(db.getConnection());
       }
       else{
           rs = db.selectObjectAndAttributesC4(face);
           ds = new DataSet(rs,2);
           res = ds.getMatrixOfRSet();
           db.close_db_connection(db.getConnection());
       }
       return res;
   }

    public String[][] filterObjs(String[][] objs){
       String[][] res = null;
       Vector v = new Vector();
       for(int i = 0; i < objs.length; i++){
           if(objs[i][3].equals("")){
               v.addElement(i);
           }
       }
       res = new String[objs.length-v.size()][objs[0].length];
       System.out.println(objs.length+" "+res.length+" "+v.size());
       int row = 0;
       for(int j = 0; j < objs.length; j++){
           if(!v.contains(j)){
               for(int k = 0; k < objs[0].length; k++){
                   res[row][k] = objs[j][k];
               }
               row++;
           }
       }
       return res;
   }

    private boolean isParticularCase(String caso){
        boolean res = false;
        String[] casiParticolari = {"showQuickEditTodo", "reopenTodo", "/tudu/j_acegi_security_check;jsessionid=", "/tudu/register.action;jsessionid=", "/tudu/welcome.action;jsessionid=", "javascript:renderTableListId", "/tudu/secure/backupTodoList.action?listId=", "/tudu/secure/restoreTodoList.action?listId=", "showQuickEditTodo", "completeTodo", "quickEditTodo", "javascript:showEditTodo", "javascript:deleteTodo", "/tudu/welcome.action", "/tudu/register.action", "/tudu/j_acegi_security_check", "/tudu/rss/showRssFeed.action?listId="};
        for(int i = 0; i < casiParticolari.length; i++){
            if(caso.startsWith(casiParticolari[i]) || caso.contains(casiParticolari[i])){
                res = true;
            }
        }
        return res;
    }

}
