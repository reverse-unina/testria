/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package testsuitemanager;

import service.DataSet;
import testsuitereducer.*;
import testsuitemanager.TestCase;
import dbmanager.DBmanagement;
import com.mysql.jdbc.ResultSet;
import gui.Start;
import com.mysql.jdbc.Connection;
/**
 *
 * @author angelo
 */

/**
 * La classe Commands, a partire da una traccia passata in ingresso,
 * permette di ricavare la sequenza di comandi da eseguire per
 * riprodurre la stessa.
 */

public class Commands {
    private String[][] command = null;
    private TestCase trace = null;

    /**
     *
     * Costruttore di default.
     */
    public Commands(){

    }
    /**
     *
     * @param tcase --> traccia
     *
     * Costruttore che riceve in ingresso una traccia
     */
    public Commands(Object tcase){
        trace = (TestCase) tcase;
        this.extracttCommands();
    }

    /**
     * Questo metodo ritorna i comandi sottoforma
     * di stringa bidimensionale.
     */
    public String[][] getCommands(){
        return this.command;
    }

    /**
     * Questo metodo estrae dalla traccia
     * le informazioni necessarie per definire
     * i comandi di riesucuzione della stessa.
     */
    private void extracttCommands(){
        String[][] obj = trace.getCleanedTrace();
        command = new String[obj.length+1][3];
        for(int i = 0; i < obj.length; i++){
            command[i][0] = obj[i][4];
            command[i][1] = obj[i][5];
            command[i][2] = obj[i][0];
        }
        command[command.length-1][0] = "end";
        command[command.length-1][1] = obj[obj.length-1][5];
        command[command.length-1][2] = obj[obj.length-1][1];
    }

    /**
     *
     * @param id_interface --> id interfaccia
     *
     * Questo metodo permette di ottenere tutti gli input
     * riguardante una pagina navigata identificata da un
     * particolare id passato come input.
     */
    public static String[][] getValueFields(String id_interface){
        String[][] res = null;
        DBmanagement db = new DBmanagement();

        
        db.db_connection(Start.user, Start.passwo, Start.nomeDB, Start.porto, Start.posiz);
        
        String query = new String("SELECT t.value, t.path FROM tab_input t WHERE t.id_tab_interface = '"+id_interface+"' ");
        ResultSet rs = db.select(query);
        DataSet ds = new DataSet(rs,2);
        //db.close_db_connection(Start.conn);originale

        db.close_db_connection(db.db_connection(Start.user, Start.passwo, Start.nomeDB, Start.porto, Start.posiz));//peppe


        res = new String[ds.getRowCount()][ds.getColCount()];
        res = ds.getMatrixOfRSet();
        return res;
    }

}
