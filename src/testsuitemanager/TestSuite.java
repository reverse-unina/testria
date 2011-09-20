package testsuitemanager;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import org.junit.*;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;
import org.seleniuminspector.SeleniumTestCase;
import dbmanager.DBmanagement;
import gui.Start;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;
import service.DataSet;
import service.LogTest;


/**
 *
 * @author angelo
 */
public class TestSuite extends SeleniumTestCase implements Runnable{

    private Selenium selenium;
    private DBmanagement db = new DBmanagement();
    private LogTest log;
    private String cause = new String("");
    private Connection con = null;
    private Statement stmt = null;

    public TestSuite() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        selenium = new DefaultSelenium("localhost", 4444,"*firefox", "http://127.0.0.1:8080/");
        startupTomcat();
        pleaseWait(20000);
        //ho commentato la riga successiva in quanto non i test non si basano su un dbase
        //resetDB();
        selenium.start();
        
        selenium.setSpeed("1000");
        selenium.open("tudu-dwr/");
//        loginTudu();

    }

    @After
    public void tearDown() {
//        logoutTudu();
        selenium.stop();
        shutdownTomcat();
        pleaseWait(5000);
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //

    //@Test
    @SuppressWarnings("static-access")
    public boolean test(String[][] cmd, String cluster) {
        Commands comm = new Commands();
        String[][] fields = null;
        boolean assertInterface = false;
        for(int i = 0; i < cmd.length; i++){
            log.appendInfo("Verifica interfaccia: "+cmd[i][2]+" cluster selezionato "+cluster);
            log.appendInfo("Eseguo assertPage su interfaccia: "+cmd[i][2]);
            assertInterface = this.assertPage(this.getObj(cluster, Integer.parseInt(cmd[i][2])), cluster);
            if(assertInterface){
                log.appendInfo("AssertPage su interfaccia:"+cmd[i][2]+" eseguito con successo");
                fields = comm.getValueFields(cmd[i][2]);
                if(fields.length != 0){
                    log.appendInfo("Eseguo il typing su interfaccia:"+cmd[i][2]+".");
                    for(int k = 0; k < fields.length; k++){
                        log.appendInfo("Oggetto: "+"//html[1]"+fields[k][1].substring(8)+".");
                        log.appendInfo("Valore: "+fields[k][0]+".");
                        System.out.println("Oggetto: "+"//html[1]"+fields[k][1].substring(8));//Da eliminare
                        System.out.println("Valore: "+fields[k][0]);//Da eliminare
                        try{
                            selenium.type("//html[1]"+fields[k][1].substring(8), fields[k][0]);
                        }
                        catch(SeleniumException e){
                            this.cause = e.getMessage();
                            log.appendInfo("Type --> Fallito: //html[1]"+fields[k][1].substring(8)+" Valore:"+fields[k][0]);
                            System.out.println("Type --> Fallito: //html[1]"+fields[k][1].substring(8)+" Valore:"+fields[k][0]);//Da eliminare
                        }
                    }
                    log.appendInfo("Fine typing su interfaccia:"+cmd[i][2]+".");
                }
                if(cmd[i][0].equals("click")){
                    log.appendInfo("Click su: "+cmd[i][1]);
                    System.out.println("Evento: "+cmd[i][1]);//Da eliminare
                    try{
                        selenium.click(cmd[i][1]);
                    }
                    catch(SeleniumException e){
                        this.cause = e.getMessage();
                        log.appendInfo("Click --> Fallito: "+cmd[i][1]);
                        System.out.println("Click --> Fallito: "+cmd[i][1]);//Da eliminare
                    }
                }
                log.appendInfo("Fine verifica interfaccia: "+cmd[i][2]);
            }
            else{
                System.out.println("AssertPage su interfaccia: "+cmd[i][2]+" fallito. Causa: "+this.cause);//Da eliminare
                log.appendInfo("AssertPage su interfaccia: "+cmd[i][2]+" fallito. Causa: "+this.cause);
                break;
            }
        }
        return assertInterface;
     }

    /**
     * 
     * Esegue l'assert della pagina corrente.
     */
    private boolean assertPage(String[][] obj, String cluster){
        boolean res = false;
        if(cluster.equals("C1") || cluster.equals("C2")){
            for(int i = 0; i < obj.length; i++){
                if(isParticularCase(obj[i][3])){
                    if(selenium.isElementPresent("//html[1]"+obj[i][1].substring(8))){
                        res = true;
                        log.appendInfo("Mod "+cluster+" --> "+obj[i][0]+" "+obj[i][2]+" "+obj[i][3]);
                        System.out.println("Mod "+cluster+" --> "+obj[i][0]+" "+obj[i][2]+" "+obj[i][3]);//Da eliminare
                    }
                }
                else{
                    try{
                        TestSuite.assertEquals(obj[i][3],selenium.getAttribute("//html[1]"+obj[i][1].substring(8)+"@"+obj[i][2]));
                        res = true;
                        log.appendInfo(cluster+" --> "+obj[i][0]+" "+obj[i][2]+" "+obj[i][3]);
                        System.out.println(cluster+" --> "+obj[i][0]+" "+obj[i][2]+" "+obj[i][3]);//Da eliminare
                    }
                    catch(SeleniumException e){
                        res = false;
                        this.cause = e.getMessage();
                        log.appendInfo(cluster+" --> Fallito: "+obj[i][0]+" "+obj[i][2]+" "+obj[i][3]);
                        System.out.println(cluster+" --> Fallito: "+obj[i][0]+" "+obj[i][2]+" "+obj[i][3]);//Da eliminare
                        break;
                    }
                    catch(AssertionError e){
                        res = false;
                        this.cause = e.getMessage();
                        log.appendInfo(cluster+" --> Fallito: "+obj[i][0]+" "+obj[i][2]+" "+obj[i][3]);
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
                    log.appendInfo(cluster+" --> "+obj[i][0]+" "+"//html"+obj[i][1].substring(5));
                    System.out.println(cluster+" --> "+obj[i][0]+" "+"//html"+obj[i][1].substring(5));
                }
                else{
                    log.appendInfo(cluster+" --> Fallito: "+obj[i][0]+" "+"//html"+obj[i][1].substring(5));
                    System.out.println(cluster+" --> Fallito: "+obj[i][0]+" "+"//html"+obj[i][1].substring(5));
                    this.cause = "Elemento non presente.";
                    break;
                }
                
            }
        }
        return res;
    }

    /**
     * 
     * Setta handler log.
     */
    public void setLog(LogTest log){
        this.log = log;
    }

    /**
     * 
     * Resetta il db.
     */
    private void resetDB(){
        Connection connection = null;
        connection = this.db_connection(Start.user, Start.passwo, "tudu", Start.porto, Start.posiz);
        this.resetDBTudu();
        this.close_db_connection(connection);
    }

    /**
     * 
     * Attesa.
     */
    private void pleaseWait(int time){
        synchronized(this){
            try {
                this.wait(time);
            } catch (InterruptedException ex) {
                System.out.println("\nErrore nell'attesa.\n");
            }
        }
    }

    /**
     * 
     * Avvia Tomcat.
     */
    private void startupTomcat(){
        Runtime myRun = Runtime.getRuntime();
        try {
            //myRun.exec("cmd /C start "+"%CATALINA_HOME%\\bin\\startup.bat");

            String s="\"C:\\Users\\Porfirio\\Desktop\\RIA Testing Experiment\\software\\apache-tomcat-6.0.29-windows-x86\\apache-tomcat-6.0.29\\bin\\tomcat6.exe\"";
            System.out.println(s);
            //myRun.exec("cmdAllPermissions.exe /c "+s);
            myRun.exec(s);
            pleaseWait(20000);
            
        }
        catch(Exception e) {e.printStackTrace();System.out.println("Impossible to run Tomcat.");}
        System.out.println("Tomcat avviato");
    }

    /**
     * 
     * Arresta Tomcat.
     */
    private void shutdownTomcat(){
        Runtime myRun = Runtime.getRuntime();
        try {
            //myRun.exec("%CATALINA_HOME%\\bin\\shutdown.bat");
            //myRun.exec("C:\\Programmi\\Apache Software Foundation\\Tomcat 6.0\\bin\\shutdown.bat");
            String s="\"C:\\Users\\Porfirio\\Desktop\\RIA Testing Experiment\\software\\apache-tomcat-6.0.29-windows-x86\\apache-tomcat-6.0.29\\bin\\shutdown.bat\"";
        }
        catch(Exception e) {System.out.println("Impossible to stop Tomcat.");}
    }

    /**
     * 
     * Effettua il login su tudu.
     */
    public void loginTudu(){
        selenium.type("j_username", "angelo");
        selenium.type("j_password", "angelo");
        selenium.click("//input[@value='Log In']");
        selenium.waitForPageToLoad("30000");
    }

    /**
     * 
     * Effettua il logout su tudu.
     */
    public void logoutTudu(){
        selenium.click("link=Log out");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Click here if you want to reconnect");
        selenium.waitForPageToLoad("30000");
    }

    /**
     *
     * Con questo metodo filtriamo gli oggetti che, una volta estratti
     * con la stored procedure "cluObjAttrInterface", possiedono
     * il campo avalue vuoto. La presenza di questi oggetti nel testing
     * porterebbero al fallimento dell'esecuzione.
     */
    private String[][] filterObjs(String[][] objs){
        String[][] res = null;
        Vector v = new Vector();
        for(int i = 0; i < objs.length; i++){
            if(objs[i][3].equals("") || objs[i][3].equals("sortTable('description')") || objs[i][3].equals("sortTable('priority')") || objs[i][3].equals("sortTable('due_date')")){
                v.addElement(i);
            }
        }
        res = new String[objs.length-v.size()][objs[0].length];
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

    /**
     *
     * Preleva gli oggetti dal db, utilizzando le stored procedure
     * a seconda della tipologia di clustering selezionato dall'utente
     * e della particolare pagina oggetto di testing.
     */
    private String[][] getObj(String clu, int face){
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

        /**
     *
     * @param user
     * @param password
     * @param db
     * @param port
     * @param location
     *
     * Questo metodo permette di instaurare una connessione con un database MySql.
     * La connessione è ritornata sottoforma di oggetto Connection.
     */
    public Connection db_connection(String user, char[] password, String db, String port, String location) {
            String url = "jdbc:mysql://"+location+":"+port+"/"+db+"";
            try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
            } catch (Exception e) {
                    System.out.println("ERROR opening DB connection " + e+"\n");
                    return con;
            }
            try {
                    String pass = new String(password);
                    con = (Connection) DriverManager.getConnection(url, user, pass);
            } catch (Exception e) {
                    System.out.println("ERROR opening DB connection " + e+"\n");
                    return con;
            }
            //System.out.println("- DB connection open -\n");
            return con;
    }

    /**
     *
     * @param con
     *
     * Questo metodo chiude la connesione con il database. La connessione
     * è rappresentata dal paramentro con.
     */
    public void close_db_connection(java.sql.Connection con) {
            try {
                    con.close();
                    //System.out.println("- DB connection closed -\n");
            } catch (SQLException e) {
                    System.out.println("- ERROR closing DB connection " + e +" -\n");
            }
    }

     /**
     *
     * Questo metodo riporta il db ad uno stato noto. Non è un vero e proprio
     * reset, in quanto restano alcune informazioni utili al fine della riesecuzione
     * delle tracce.
     */
    private void resetDBTudu(){
        try {
            String query = new String("DELETE FROM todo");
            stmt = (Statement) con.createStatement();
            stmt.executeUpdate(query);
            query = "DELETE FROM tuser_todo_list";
            stmt.executeUpdate(query);
            query = "DELETE FROM todo_list";
            stmt.executeUpdate(query);
            query = "DELETE FROM tuser_role";
            stmt.executeUpdate(query);
            query = "DELETE FROM tuser";
            stmt.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("- ERROR impossible to reset DB -");
        }
    }

    public void run() {
    }
}