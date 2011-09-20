/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import com.mysql.jdbc.ResultSet;

/**
 *
 * @author angelo
 */

/**
 * La classe DataSet permette di ricevere in
 * ingresso un ResultSet e restituire
 * i dati sottoforma di matrice bidimensionale.
 * La classe DataSet permette anche di ricevere
 * in ingresso direttamente la matrice bidimensionale
 * rappresentativa dei dati.
 */
public class DataSet {
    private ResultSet rs = null;
    private String[][] matrixRs = null;
    private int nrow = 0;
    private int ncol = 0;

    /**
     *
     * @param rset --> ResultSet di una query
     * @param ncolon --> Numero di colonne che dovrà avere la matrice
     *
     * Questo cotruttore, a partire da rset passato
     * in ingresso, permette di creare un oggetto DataSet
     * contenente una matrice bidimensionale (matrixRs) caratterizzata da
     * un numero di colonne pari a ncolon.
     *
     */
    public DataSet(ResultSet rset, int ncolon){
        this.rs = rset;
        this.countRowsRs();
        this.ncol = ncolon;
        this.convertRStoMatrix(ncol);
    }

    /**
     *
     * @param ds --> Matrice bidimensionale
     *
     * Questo costruttore permette di creare un oggetto DataSet
     * contenente una matrice bidimensionale pari a ds passato in ingresso.
     *
     */
    public DataSet(String[][] ds){
        matrixRs = ds;
        nrow = ds.length;
        ncol = ds[0].length;
    }

    /**
     *
     * @param rset --> ResultSet di una query
     *
     * Questo cotruttore, a partire da rset passato
     * in ingresso, permette di creare un oggetto DataSet
     * contenente una matrice bidimensionale (matrixRs).
     *
     */
    public DataSet(ResultSet rset){
        this.rs = rset;
        this.countRowsRs();
        this.countColoumnRs();
        this.convertRStoMatrix();
    }

    /**
     * Questo metodo conta il numero di
     * righe della matrice matrixRs.
     */
    private void countRowsRs(){
        int nrows = 0;
        try{
            rs.beforeFirst();
            while(rs.next()){
                nrows = nrows + 1;
            }
            rs.beforeFirst();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        nrow = nrows;
    }

    /**
     * Questo metodo conta il
     * numero di colonne della
     * matrice matrixRs.
     */
    private void countColoumnRs(){
        int nclos = 1;
        try{
            rs.beforeFirst();
            boolean flag = true;
            while(flag){
                try{
                    rs.next();
                    rs.getString(nclos);
                    nclos = nclos + 1;
                }
                catch(Exception ex){
                    flag = false;
                }
            }
            rs.beforeFirst();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        ncol = nclos-1;
    }

    /**
     * Questo metodo converte il ResultSet passato
     * al costruttore in una matrice bidimensionale.
     */
    private void convertRStoMatrix(){
        String matrixRset[][] = new String[nrow][ncol];
        try{
            rs.beforeFirst();
            for(int i = 0; i < nrow; i++){
                rs.next();
                for(int j = 0; j < ncol; j++){
                    matrixRset[i][j] = rs.getString(j+1);
                }
            }
            rs.beforeFirst();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        matrixRs = matrixRset;
    }

    /**
     *
     * @param ncols
     *
     * Questo metodo converte il ResultSet passato al
     * costrutto in una matrice bidimensionale avente
     * numero di colonne pari a ncols.
     *
     */
    private void convertRStoMatrix(int ncols){
        String matrixRset[][] = new String[nrow][ncols];
        try{
            rs.beforeFirst();
            for(int i = 0; i < nrow; i++){
                rs.next();
                for(int j = 0; j < ncol; j++){
                    matrixRset[i][j] = rs.getString(j+1);
                }
            }
            rs.beforeFirst();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        matrixRs = matrixRset;
    }

    /**
     * Questo metodo ritorna
     * il numero di righe di matrixRs.
     */
    public int getRowCount(){
        return nrow;
    }

    /**
     * Questo metodo ritorna
     * il numero di colonne di matrixRs.
     */
    public int getColCount(){
        return ncol;
    }


    /**
     * Questo metodo ritorna
     * la matrice matrixRs.
     */
    public String[][] getMatrixOfRSet(){
        return matrixRs;
    }
}
