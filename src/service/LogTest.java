/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 *
 * @author Utente
 */
public class LogTest extends IoFileWriting{

    private GregorianCalendar calendar;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");


    public LogTest(String nameTrace, String dateTrace, String idTrace){
        super("log//"+idTrace+"_"+nameTrace+"_"+dateTrace+".txt");
        calendar = new GregorianCalendar();
        super.write(sdf.format(calendar.getTime())+" --- LOG TRACE ---> "+nameTrace);
    }

    public void appendInfo(String info){
        calendar = new GregorianCalendar();
        super.write(sdf.format(calendar.getTime())+" "+info);
    }

}
