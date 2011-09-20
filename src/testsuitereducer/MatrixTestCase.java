/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package testsuitereducer;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import dbmanager.DBmanagement;
import gui.Start;
import testsuitemanager.TestCase;
import java.util.ArrayList;
import java.util.Vector;
import service.DataSet;

/**
 *
 * @author angelo
 */

/**
 *
 * La classe Matrix rappresenta una matrice dove sulle colonne
 * possiamo travare gli stati o le transizioni estratte dal GS, mentre sulle righe
 * le tracce di navigazione. Tra id diversi metodi
 * che la classe mette a disposizione abbiamo un metodo di riduzione basato su
 * un criterio di copertura e un metodo di riempimento.
 *
 */
public class MatrixTestCase {
    
    private String[] states = null;
    private String[] transitions = null;
    private String[] javascript = null;
    private Object[] tCase = null;
    private Object[] tCaseR = null;
    private int[][] matrice = null;
    private int[][] matrice_ridotta = null;
    private int flag = 0;
    private String modalita;


    /**
     *
     * Costruttore di default
     */
    public MatrixTestCase(){
    }

    /**
     *
     * @param st
     * @param tr
     * @param tc
     * @param fla
     * @param mod
     *
     * Questo costruttore setta le varibili locali states, transition, tCase, flag,
     * modalita e matrice. La dimensioni della matrice matrice, sono caratterizzate
     * dal tipologia di copertura scelta ovvero se basata sugli stati (flag == 0) o sulle
     * transizioni (flag == 1).
     */
    public MatrixTestCase(String[] st, String[] tr, Object[] tc, int fla, String mod){
        this.states = st;
        this.transitions = tr;
        this.tCase = tc;
        this.flag = fla;
        modalita = new String(mod);
        if(flag == 0){
            this.matrice = new int[this.tCase.length][this.states.length];
        }
        else if(flag == 2){
            this.javascript = st;
            this.matrice = new int[this.tCase.length][this.javascript.length];
        }
        else{
            this.matrice = new int[this.tCase.length][this.transitions.length];
        }
    }

    /**
     * Questo metodo restituisce i test case ridotti
     * memorizzati in tCaseR.
     */
    public Object[] getTCaseR(){
        return this.tCaseR;
    }

    /**
     *
     * Questo metodo restituisce la matrice non ridotta.
     */
    public int[][] getMatrixNR(){
        return this.matrice;
    }

    /**
     *
     * Questo metodo restituisce la matrice ridotta.
     */
    public int[][] getMatrixR(){
        return this.matrice_ridotta;
    }

    /**
     * Questo metodo riempie la matrice
     * servendosi delle informazioni passate nel costruttore.
     */
    public void fillMatrix(){
        String[] values = null;
        String[][] tcase = null;
        TestCase trace = null;
        if(this.flag == 0){//Siamo nel caso degli stati.
            for(int i = 0; i < matrice.length; i++){
                trace = (TestCase)tCase[i];
                tcase = trace.getCleanedTrace();
                if(modalita.equals("label")){
                    values = this.extractLabelST(tcase);

                    //Test - Da eliminare
                    System.out.println("Start --> Modalità label-stati, stampa label per traccia:"+trace.getNameTrace());
                    this.print_2(values);
                    System.out.println("End --> Modalità label-stati, stampa label per traccia:"+trace.getNameTrace()+"\n");
                    //End - Da eliminare

                }
                else{
                    values = this.extractIdST(tcase);

                    //Test - Da eliminare
                    System.out.println("Start --> Modalità id-stati, stampa id per traccia:"+trace.getNameTrace());
                    this.print_2(values);
                    System.out.println("End --> Modalità id-stati, stampa id per traccia:"+trace.getNameTrace()+"\n");
                    //End - Da eliminare

                }
                for(int j = 0; j < matrice[0].length; j++){
                    this.matrice[i][j] = this.compareValue(values,this.states[j]);
                }
            }
        }
        else if(this.flag == 2){
            for(int i = 0; i < matrice.length; i++){
                trace = (TestCase)tCase[i];
                tcase = trace.getCleanedTrace();
                values = this.extractJS(trace.getIdTrace());

                //Test - Da eliminare
                System.out.println("Start --> Modalità javascript, stampa js per traccia:"+trace.getNameTrace());
                this.print_2(values);
                System.out.println("End --> Modalità javascript, stampa js per traccia:"+trace.getNameTrace()+"\n");
                //End - Da eliminare

                for(int j = 0; j < matrice[0].length; j++){
                    if(values.length == 0){
                        this.matrice[i][j] = 0;
                    }
                    else{
                        this.matrice[i][j] = this.compareValue(values,this.javascript[j]);
                    }
                }
            }
        }
        else{//Siamo nel caso delle transizioni.
            for(int i = 0; i < matrice.length; i++){
                trace = (TestCase)tCase[i];
                tcase = trace.getCleanedTrace();
                if(modalita.equals("label")){
                    values = this.extractLabelTR(tcase);

                    //Test - Da eliminare
                    System.out.println("Start --> Modalità label-transizioni, stampa label per traccia:"+trace.getNameTrace());
                    this.print_2(values);
                    System.out.println("End --> Modalità label-transizioni, stampa label per traccia:"+trace.getNameTrace()+"\n");
                    //End - Da eliminare

                }
                else{
                    String[][] temp = this.extractIdTR(tcase);
                    values = new String[temp.length];
                    for(int k = 0; k < values.length; k++){
                        values[k] = temp[k][0];
                    }

                    //Test - Da eliminare
                    System.out.println("Start --> Modalità id-stati, stampa id per traccia:"+trace.getNameTrace());
                    this.print_2(values);
                    System.out.println("End --> Modalità id-stati, stampa id per traccia:"+trace.getNameTrace()+"\n");
                    //End - Da eliminare

                }
                for(int j = 0; j < matrice[0].length; j++){
                    this.matrice[i][j] = this.compareValue(values,this.transitions[j]);
                }
            }
        }

        //Test - Da eliminare
        System.out.println("Start --> Matrice piena.");
        this.print_1(this.matrice, this.tCase);
        System.out.println("End --> Matrice piena.\n");
        //End - Da eliminare

    }

    /**
     * Questo metodo riduce la matrice prodotta
     * in fase di riempimento basandosi su un criterio
     * di copertura tra righe.
     */
    public void ruduceMatrix(){
        ArrayList righeFinal = new ArrayList();
        int[][] copyMatrix = this.copyMatrix(this.matrice);
        Matrice m = new Matrice(copyMatrix);
//            m.printM();
	RigheColonne rc = new RigheColonne();
            rc.reduction(m);
            righeFinal = rc.getTCexential();
//            rc.stampaTC();
        tCaseR = new Object[righeFinal.size()];
        matrice_ridotta = new int[tCaseR.length][matrice[0].length];
        int count = 0;
        for(int k = 0; k < matrice.length; k++){
            if(righeFinal.contains(k)){
                for(int h = 0; h < matrice[0].length; h++){
                    matrice_ridotta[count][h] = matrice[k][h];
                }
                tCaseR[count] = tCase[k];
                count++;
            }
        }

        //Test - Da eliminare
        System.out.println("Start --> Matrice ridotta.");
        this.print_1(this.matrice_ridotta, this.tCaseR);
        System.out.println("End --> Matrice ridotta.\n");
        //End - Da eliminare

    }

    /**
     *
     * @param matrice
     * @return copia
     *
     * Questo metodo restituisce una copia di matrice.
     */
    private int[][] copyMatrix(int[][] matrice){
        int[][] copia = new int[matrice.length][matrice[0].length];
        for(int i = 0; i < copia.length; i++){
            for(int j = 0; j < copia[0].length; j++){
                copia[i][j] = this.matrice[i][j];
            }
        }
        return copia;
    }

    /**
     *
     * @param tcase --> traccia
     *
     * Questo metodo estrae dalla traccia data in ingresso
     * gli id degli stati.
     */
    private String[] extractIdST(String[][] tcase) {
        Vector temp = new Vector();
        for(int i = 0; i < tcase.length; i++){
            if(!temp.contains(tcase[i][8])){
                temp.addElement(tcase[i][8]);
            }
        }
        String[] res = new String[temp.size()];
        for(int j = 0; j < res.length; j++){
            res[j] = (String)temp.elementAt(j);
        }
        return res;
    }

    /**
     *
     * @param tcase --> traccia
     *
     * Questo metodo estrae dalla traccia data in ingresso
     * le label degli stati.
     */
    private String[] extractLabelST(String[][] tcase){
        String[] id = this.extractIdST(tcase);
        String[] label = new String[id.length];
        for(int i = 0; i < label.length; i++){
            label[i] = ImportGoldenS.getStateLabel(id[i]);
        }
        return label;
    }

    /**
     *
     * @param tcase --> traccia
     *
     * Questo metodo estrae dalla traccia data in ingresso
     * gli id delle transizioni.
     */
    private String[][] extractIdTR(String[][] tcase) {
        Vector temp = new Vector();
        Vector temp_i = new Vector();
        for(int i = 0; i < tcase.length; i++){
            if(!temp.contains(tcase[i][3])){
                temp.addElement(tcase[i][3]);
                temp_i.addElement(i);
            }
        }
        String[][] res = new String[temp.size()][2];
        for(int j = 0; j < res.length; j++){
            res[j][0] = (String)temp.elementAt(j);
            res[j][1] = String.valueOf(temp_i.elementAt(j));
        }
        return res;
    }

    /**
     *
     * @param tcase --> traccia
     *
     * Questo metodo estrae dalla traccia data in ingresso
     * le label delle transizioni.
     */
    private String[] extractLabelTR(String[][] tcase){
        String[][] id = this.extractIdTR(tcase);
        String[] label = new String[id.length];
        for(int i = 0; i < label.length; i++){
            label[i] = ImportGoldenS.getStateLabel(tcase[Integer.parseInt(id[i][1])][0])+";"+ImportGoldenS.getStateLabel(tcase[Integer.parseInt(id[i][1])][1])+";"+ImportGoldenS.getTranzLabel(id[i][0]);
        }
        return label;
    }

    /**
     *
     * @param val --> array di stringhe
     * @param col --> stringa
     *
     * Questo metodo controlla se nell'array val è presenta
     * almeno un elemento pari a col; in caso affermativo restituisce
     * 1 in caso contrario 0.
     */
    private int compareValue(String[] val, String col){
        int res = 0;
        for(int i = 0; i < val.length; i++){
            if(val[i].equalsIgnoreCase(col)){
                res = 1;
            }
            else{
                //vai avanti.
            }
        }
        return res;
    }

    //Metodi test
    private void print_1(int[][] matrix, Object[] tcases){
        String row = new String();
        for(int i = 0; i < matrix.length; i++){
            row = "";
            for(int j = 0; j < matrix[0].length; j++){
                row = row +" "+ matrix[i][j];
            }
            System.out.println(((TestCase)tcases[i]).getIdTrace()+" "+((TestCase)tcases[i]).getNameTrace()+": "+row);
        }
    }

    private void print_2(String[] string){
        for(int i = 0; i < string.length; i++){
            System.out.println(string[i]);
        }
    }
    //End Metodi test


    private String[] extractJS(String idTcase) {
        DBmanagement db = null;
        Connection conndb = null;
        ResultSet rs = null;
        DataSet datas = null;
        String query = null;
        String[][] res_query = null;
        Vector nodupli = new Vector();
        String[] output = null;
        try{
            db = new DBmanagement();
            conndb = db.db_connection(Start.user, Start.passwo, Start.nomeDB, Start.porto, Start.posiz);
            query = new String("SELECT t.value_tab_javascript FROM tab_javascript t WHERE t.id_tab_trace='"+idTcase+"'");
            rs = db.select(query);
            datas = new DataSet(rs, 1);
            res_query = datas.getMatrixOfRSet();
            for(int k = 0; k < res_query.length; k++){
                if(!nodupli.contains(res_query[k][0])){
                    nodupli.addElement(res_query[k][0]);
                }
            }
            db.close_db_connection(conndb);
            output = new String[nodupli.size()];
            for(int j = 0; j < output.length; j++){
                output[j] = (String)nodupli.elementAt(j);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return output;
    }
    

}
