/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Utente
 */
public class JProgress extends JProgressBar implements TableCellRenderer {

    private int max;

    public JProgress(){

    }

    public JProgress(Vector dims) {
        super();
        max = this.getMaximum(dims);
    }

    private int getMaximum(Vector dims){
        int maximum = Integer.parseInt((String)dims.elementAt(0));
        for (int i = 1; i < dims.size(); i++) {
            if (Integer.parseInt((String)dims.elementAt(i)) > maximum) {
                maximum = Integer.parseInt((String)dims.elementAt(i));
            }
        }
        return maximum;
    }

    private int getMax(){ return this.max; }

    public Component getTableCellRendererComponent(JTable table, Object arg1, boolean isSelected, boolean hasFocus, int row, int column) {
        if(arg1 instanceof Integer){
            NumberFormat formatter = new DecimalFormat("#0.00");
            JProgressBar bar = new JProgressBar();
            String val = formatter.format((double)((Integer)arg1)/this.getMax());
            bar.setMinimum(0);
            bar.setMaximum(100);
            bar.setStringPainted(true);
            bar.setValue((int) ((Double.parseDouble(val.substring(0, val.indexOf(","))+"."+val.substring(val.indexOf(",")+1, val.length())) * 100)));
            bar.setString(Integer.toString((Integer)arg1));
            return bar;
        }
//        else if(arg1 instanceof Color){
//            JProgressBar bar = new JProgressBar();
//            bar.setMinimum(0);
//            bar.setMaximum(100);
//            bar.setValue(100);
//            bar.setStringPainted(true);
//            bar.setString("");
//            bar.setForeground((Color)arg1);
//            return bar;
//        }
        else if(arg1 instanceof String){
            JProgressBar bar = new JProgressBar();
            bar.setMinimum(0);
            bar.setMaximum(100);
            bar.setValue(100);
            bar.setStringPainted(true);
            if(((String)arg1).startsWith("G")){
                bar.setString(((String)arg1).substring(1, ((String)arg1).length()));
                bar.setForeground(Color.GREEN);
            }
            else{
                bar.setString(((String)arg1).substring(1, ((String)arg1).length()));
                bar.setForeground(Color.RED);
            }
            return bar;
        }
        return null;
    }
}
