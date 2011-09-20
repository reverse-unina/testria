/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package testsuitereducer;

import service.DataSet;
import testsuitemanager.TestCase;
import dbmanager.DBmanagement;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import gui.Start;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author angelo
 */

/**
 * Classe utilizzata per importare il golden standard
 * o dal db o da file con estenzione .dot .
 */
public class ImportGoldenS {

    private File gs = null;
    private String[] states = null;
    private String[] transition = null;
    private String[] javascript = null;
    private Object[] tcases = null;

    /**
     *
     * @param icase --> traccia
     * @param mod --> modalità di estrazione delle info dal db
     *
     * Questo costruttore riceve in ingresso
     * sia la traccia che la modalità di estrazione delle informazioni.
     * Le informazioni posso essere estratte basandosi o sugli id
     * o in base alle label assegnate alle interfacce navigate.
     */
    public ImportGoldenS(Object[] tcase, String mod){
        tcases = tcase;
        if(mod.equals("id")){
            this.importGSDBid();
        }
        else if(mod.equals("js")){
            this.importJavaScript();
        }
        else{
            importGSDBilabel();
        }
    }

    /**
     *
     * @param file --> file da cui importare il GS
     *
     * Utilizzato per importare il GS da file.
     */
    public ImportGoldenS(File f){
        gs = f;
        importGSFile();
    }

    /**
     *
     * Questo metodo ritorna le transizioni estratte dal GS.
     */
    public String[] getTransitions(){
        return this.transition;
    }

    /**
     *
     * Questo metodo ritorna gli stati estratti dal GS.
     */
    public String[] getStates(){
        return this.states;
    }

    /**
     *
     * @param tr --> transizioni
     *
     * Questo metodo permette di settare le transizioni del GS.
     */
    public void setTransition(String[] tr){
        this.transition = tr;
    }

    /**
     *
     * @param st --> stati
     *
     * Qeusto metodo permette di settare gli stati del GS.
     */
    public void setStates(String[] st){
        this.states = st;
    }

    /**
     * Metodo utilizzato per estarre dal file selezionato le
     * sole informazioni di interesse per il GS, stati e transizioni.
     */
    private void importGSFile(){
        Vector res = new Vector();
        try{
            URL url = null;
            url = gs.toURL();
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            CharSequence cs = "->";
            line = br.readLine();
            while(line != null){
                if(line.contains(cs)){
                    res.addElement(line);
                }
                else{
                    //vai avanti.
                }
                line = br.readLine();
            }
            is.close();
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        extractTranState(res);

        //Test - Da eliminare
        System.out.println("Start --> Stampa stati importati da file.");
        this.print_1(this.states);
        System.out.println("End --> Stampa stati importati da file.\n");
        System.out.println("Start --> Stampa transizioni importati da file.");
        this.print_1(this.transition);
        System.out.println("End --> Stampa transizioni importati da file.\n");
        //End - Da eliminare

    }

    /**
     * A partire dalle informazioni restituite da importGSFile
     * questo metodo restituisce due array states e transactions
     * (definiti come attributi della classe) che rappresentano
     * rispettivamente gli stati e le transizioni del GS.
     */
    private void extractTranState(Vector gs){
        String app_1 = null;
        String app_2 = null;
        String tra = new String();
        Vector tr = new Vector();
        Vector st = new Vector();
        StringTokenizer stk = null;
        for(int i = 0; i < gs.size(); i++){
            app_1 = (String)gs.elementAt(i);
            stk = new StringTokenizer(app_1,"\"->\"");
            app_2 = stk.nextToken();
            app_2 = app_2.substring(0, app_2.length());
            if(!st.contains(app_2)){
                st.addElement(app_2);
            }
            tra = app_2;
            app_2 = stk.nextToken();
            if(!st.contains(app_2)){
                st.addElement(app_2);
            }
            tra = tra+";"+app_2;
            app_2 = stk.nextToken();
            app_2 = stk.nextToken();
            tra = tra+";"+app_2;
            if(!tr.contains(tra)){
                tr.addElement(tra);
            }
        }
        //Crea array stati
        String[] stati = new String[st.size()];
        for(int i = 0; i < stati.length; i++){
            stati[i] = (String) st.elementAt(i);
        }
        this.setStates(stati);
        //Crea array transizioni
        String[] transizioni = new String[tr.size()];
        for(int i = 0; i < tr.size(); i++){
            transizioni[i] = (String) tr.elementAt(i);
        }
        this.setTransition(transizioni);
    }

    /**
     * Importa il GS dall'db sfruttando le tracce selezionate.
     * Si prendono come stati e come transizioni tutti gli stati
     * e le transizioni esplorate (senza ripetizioni) nelle tracce
     * selezionate, definiti attraverso le label.
     */
    private void importGSDBilabel(){
        TestCase trace = null;
        String[][] cltr = null;
        String[] cols = null;
        Vector tran = new Vector();
        Vector state = new Vector();
        String temp = new String();
        for(int i = 0; i < tcases.length; i++){
            trace = (TestCase)tcases[i];
            cltr = trace.getCleanedTrace();
            for(int j = 0; j < cltr.length; j++){
                temp = ImportGoldenS.getStateLabel(cltr[j][0])+";"+ImportGoldenS.getStateLabel(cltr[j][1])+";"+cltr[j][7];
                if(!tran.contains(temp)){
                    tran.addElement(temp);
                }
            }
            temp = ImportGoldenS.getStateLabel(cltr[0][0]);
            if(!state.contains(temp)){
                state.addElement(temp);
            }
            for(int k = 0; k < cltr.length; k++){
                if(!state.contains(cltr[k][6])){
                    state.addElement(cltr[k][6]);
                }
            }
        }
        cols = new String[tran.size()];
        for(int i = 0; i < cols.length; i++){
            cols[i] = (String)tran.elementAt(i);
        }
        setTransition(cols);
        cols = new String[state.size()];
        for(int i = 0; i < cols.length; i++){
            cols[i] = (String)state.elementAt(i);
        }
        setStates(cols);

        //Test - Da eliminare
        System.out.println("Start --> Stampa label stati importati dal DB.");
        this.print_1(this.states);
        System.out.println("End --> Stampa label stati importati da file.\n");
        System.out.println("Start --> Stampa label transizioni importati da DB.");
        this.print_1(this.transition);
        System.out.println("End --> Stampa label transizioni importati da DB.\n");
        //End - Da eliminare

    }

    /**
     * Importa il GS dall'db sfruttando le tracce selezionate.
     * Si prendono come stati e come transizioni tutti gli stati
     * e le transizioni esplorate (senza ripetizioni) nelle tracce
     * selezionate, definiti attraverso gli id.
     */
    private void importGSDBid(){
        TestCase trace = null;
        String[][] cltr = null;
        String[] cols = null;
        Vector tran = new Vector();
        Vector state = new Vector();
        for(int i = 0; i < tcases.length; i++){
            trace = (TestCase)tcases[i];
            cltr = trace.getCleanedTrace();
            for(int j = 0; j < cltr.length; j++){
                if(!tran.contains(cltr[j][3])){
                    tran.addElement(cltr[j][3]);
                }
            }
            for(int k = 0; k < cltr.length; k++){
                if(!state.contains(cltr[k][8])){
                    state.addElement(cltr[k][8]);
                }
            }
        }
        cols = new String[tran.size()];
        for(int i = 0; i < cols.length; i++){
            cols[i] = (String)tran.elementAt(i);
        }
        setTransition(cols);
        cols = new String[state.size()];
        for(int i = 0; i < cols.length; i++){
            cols[i] = (String)state.elementAt(i);
        }
        setStates(cols);

        //Test - Da eliminare
        System.out.println("Start --> Stampa id stati importati dal DB.");
        this.print_1(this.states);
        System.out.println("End --> Stampa id stati importati da file.\n");
        System.out.println("Start --> Stampa id transizioni importati da DB.");
        this.print_1(this.transition);
        System.out.println("End --> Stampa id transizioni importati da DB.\n");
        //End - Da eliminare

    }

    /**
     *
     * @param id --> id interfaccia
     *
     * Restituisce, a partire dall'id dell'interfaccia,
     * la corrispondente label assegnata in fase di tracing.
     */
    public static String getStateLabel(String id){
        DBmanagement db = null;
        Connection conndb = null;
        ResultSet rs = null;
        DataSet datas = null;
        String query = null;
        String res = new String();
        try{
            db = new DBmanagement();
            conndb = db.db_connection(Start.user, Start.passwo, Start.nomeDB, Start.porto, Start.posiz);
            query = new String("SELECT t.ilabel FROM tab_interface t WHERE t.id_tab_interface='"+id+"'");
            rs = db.select(query);
            datas = new DataSet(rs, 1);
            res = datas.getMatrixOfRSet()[0][0];
            db.close_db_connection(conndb);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }

    /**
     *
     * @param id --> id transizione
     *
     * Restituisce, a partire dall'id della transizione,
     * la corrispondente label assegnata in fase di tracing.
     */
    public static String getTranzLabel(String id){
        DBmanagement db = null;
        Connection conndb = null;
        ResultSet rs = null;
        DataSet datas = null;
        String query = null;
        String res = new String();
        try{
            db = new DBmanagement();
            conndb = db.db_connection(Start.user, Start.passwo, Start.nomeDB, Start.porto, Start.posiz);
            query = new String("SELECT t.tlabel FROM tab_transition t WHERE t.id_tab_transition='"+id+"'");
            rs = db.select(query);
            datas = new DataSet(rs, 1);
            res = datas.getMatrixOfRSet()[0][0];
            db.close_db_connection(conndb);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }

    //Metodi test
    private void print_1(String[] string){
        for(int i = 0; i < string.length; i++){
            System.out.println(string[i]);
        }
    }
    //End Metodi test

    private void importJavaScript() {
        DBmanagement db = null;
        Connection conndb = null;
        ResultSet rs = null;
        DataSet datas = null;
        String query = null;
        String[][] res = null;
        TestCase tr;
        Vector nodupli = new Vector();
        try{
            db = new DBmanagement();
            conndb = db.db_connection(Start.user, Start.passwo, Start.nomeDB, Start.porto, Start.posiz);
            for(int i = 0; i < tcases.length; i++){
                tr = (TestCase)tcases[i];
                query = new String("SELECT t.value_tab_javascript FROM tab_javascript t WHERE t.id_tab_trace='"+tr.getIdTrace()+"'");
                rs = db.select(query);
                datas = new DataSet(rs, 1);
                res = datas.getMatrixOfRSet();
                for(int k = 0; k < res.length; k++){
                    if(!nodupli.contains(res[k][0])){
                        nodupli.addElement(res[k][0]);
                    }
                }
            }
            db.close_db_connection(conndb);
            this.javascript = new String[nodupli.size()];
            for(int j = 0; j < javascript.length; j++){
                javascript[j] = (String)nodupli.elementAt(j);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public String[] getJavascript(){
        return this.javascript;
    }

}
