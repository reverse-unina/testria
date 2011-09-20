/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package testsuitemanager;

import service.DataSet;
import com.mysql.jdbc.ResultSet;
import java.util.Vector;

/**
 *
 * @author angelo
 */

/**
 *
 * La classe Trace rappresenta la nostra traccia. Essa
 * estende la classe DataSet particolarizzandola con
 * l'aggiunta di ulteriori informazioni.
 */
public class TestCase extends DataSet{
    
    private String[][] cleanedTrace = null;
    private String nameTrace;
    private String idTrace;
    private String userNameTrace;
    private String dateTrace;

    /**
     *
     * @param tr --> traccia
     * @param name --> nome traccia
     *
     * Questo costruttore inizialiazza la classe Trace
     * prendendo in ingresso una traccia sottoforma
     * di array bidimensionale e il nome della traccia.
     */
    public TestCase(String[][] tr, String name){
        super(tr);
        cleanedTrace = super.getMatrixOfRSet();
        nameTrace = new String(name);
    }

    /**
     *
     * @param rs --> traccia sotto forma di ResultSet
     * @param numCols --> numero di colonne
     *
     * Questo costruttore inizializza la classe Trace
     * prendendo in ingresso una traccia rs e il numero di colonne
     * che deve avere.
     */
    public TestCase(ResultSet rs, int numCols){
        super(rs, numCols);
    }

    /**
     *
     * @param name --> nome traccia
     *
     * Questo metodo setta il nome della traccia.
     */
    public void setNameTrace(String name){
        this.nameTrace = new String(name);
    }

    /**
     * 
     * Questo metodo restituisce il nome della traccia.
     */
    public String getNameTrace(){
        return this.nameTrace;
    }

    /**
     *
     * @param id --> id traccia
     *
     * Questo metodo setta l'id della traccia.
     */
    public void setIdTrace(String id){
        this.idTrace = new String(id);
    }

    /**
     *
     * Questo metodo restituisce l'id della traccia.
     */
    public String getIdTrace(){
        return this.idTrace;
    }

    /**
     *
     * @param username --> username
     *
     * Questo metodo setta lo username della traccia.
     */
    public void setUserNameTrace(String userName){
        this.userNameTrace = new String(userName);
    }

    /**
     *
     * Questo metodo restituisce l'username della traccia.
     */
    public String getUserNameTrace(){
        return this.userNameTrace;
    }

    /**
     *
     * @param date --> data di creazione
     *
     * Questo metodo setta la data di creazione della traccia.
     */
    public void setDateTrace(String date){
        this.dateTrace = new String(date);
    }

    /**
     *
     * Questo metodo restituisce la data di creazione della traccia.
     */
    public String getDateTrace(){
        return this.dateTrace;
    }

    /**
     *
     * @param trace --> traccia
     *
     * Questo metodo setta la la traccia pulita pari a quella passata in ingresso.
     */
    private void setCleanedTrace(String[][] trace){
        this.cleanedTrace = trace;
    }

    /**
     *
     * Questo metodo restituisce la traccia pulita.
     */
    public String[][] getCleanedTrace(){
        return cleanedTrace;
    }

    /**
     *
     * @param filter
     *
     * Questo metodo pulisce la traccia eliminando eventi che coincidono
     * con il filtro passato in ingresso.
     */
    public void cleanTrace(String filter){
        String str[][] = null;
        String res[][] = null;
        try{
            str = cleanedTrace;
            Vector id = new Vector();
            int temp = 0;
            int colType = 4;
            if(filter.equals("keydown")){
                for(int i = 0; i < str.length; i++){
                    if(str[i][colType].equals(filter)){
                        temp = i;
                        str[i][colType] = "type";
                    }
                    else if(!str[i][colType].equals(filter) && temp != 0){
                        id.addElement(temp);
                        temp = 0;
                        id.addElement(i);
                    }
                    else if(!str[i][colType].equals(filter)){
                        id.addElement(i);
                    }
                }
            }
            else{
                for(int i = 0; i < str.length; i++){
                    if(!str[i][colType].equals(filter)){
                        id.addElement(i);
                    }
                }
            }
            res = new String[id.size()][super.getColCount()];
            for(int i = 0; i < id.size(); i++){
                for(int j = 0; j < super.getColCount(); j++){
                    res[i][j] = str[Integer.parseInt(id.get(i).toString())][j];
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        //Test
        System.out.println("Operazione di filtraggio, filtro: "+filter+"\n");
        String st = new String();
        for(int j = 0; j < res.length; j++){
            for(int k = 0; k < res[0].length; k++){
                st = st + " " + res[j][k];
            }
            System.out.println(st+"\n");
            st = "";
        }
        System.out.println("Fine operazione di filtraggio.\n");
        //End test

        setCleanedTrace(res);
    }

    /**
     * Questo metodo effettua una conversione degli xpath
     * in modo da renderli compatibili in selenium.
     */
    public void convertPathObj(){
        String str[][] = null;
        try{
            str = super.getMatrixOfRSet();
            int colPath = 5;
            for(int i = 0; i < str.length; i++){
                str[i][colPath] = "//html[1]"+str[i][colPath].substring(8);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        //Test
        System.out.println("Operazione di conversione path\n");
        String st = new String();
        for(int j = 0; j < str.length; j++){
            for(int k = 0; k < str[0].length; k++){
                st = st + " " + str[j][k];
            }
            System.out.println(st+"\n");
            st = "";
        }
        System.out.println("Fine operazione di conversione path.\n");
        //End test

        setCleanedTrace(str);
    }

}
