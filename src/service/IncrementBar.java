/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import gui.Start;
import javax.swing.JFrame;

/**
 *
 * @author angelo
 */

/**
 *
 * Classe utilizzata per controllare il componente
 * JProgressBar presente sull'interfaccia grafica Start.
 * Utilizzare per dare una visione sulla progressione del lavoro.
 */
public class IncrementBar extends Thread{

    private int maxlength = 0;
    private JFrame main = null;

    public IncrementBar(int max, JFrame start){
        this.maxlength = max;
        this.main = start;
    }

    public void run() {
        super.run();
        main.setEnabled(false);
        for (int i = 1; i <= maxlength; i++) {
            try {
                Thread.sleep(25);
            }
            catch (InterruptedException e) { /*Non fare nulla.*/ }
            Start.jPB_progressione.setValue(i);
        }
        main.setEnabled(true);
    }
}
