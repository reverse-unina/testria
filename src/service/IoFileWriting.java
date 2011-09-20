/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Utente
 */
/**
 *
 * La classe IOFileWriting permette di creare un file e di potervici accedere in scrittura.
 */
public class IoFileWriting {

    private FileWriter fw;
    private BufferedWriter bw;
    private PrintWriter outFile;
    private File file;
    private String nameFile;
    
    public IoFileWriting(String namFile){
        try {
            nameFile = new String(namFile);
            file = new File(nameFile);
            fw = new FileWriter(file);
            bw = new BufferedWriter (fw);
            outFile = new PrintWriter (bw);
            outFile.flush();
            outFile.close();
            bw.close();
            fw.close();
        } catch (IOException ex) {
            System.out.println("Problema nel creare il file: "+ex.getMessage());
        }
    }

    public void write(String row){
        try {
            file = new File(nameFile);
            fw = new FileWriter(file, true);
            bw = new BufferedWriter (fw);
            outFile = new PrintWriter (bw);
            outFile.println(row);
            outFile.flush();
            outFile.close();
            bw.close();
            fw.close();
        } catch (IOException ex) {
            System.out.println("Problema nella scrittura su file: "+ex.getMessage());
        }
    }
}
