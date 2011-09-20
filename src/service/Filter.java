/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author angelo
 */

/**
 * 
 * Classe utilizzate per filtrare i file con estensione diversa da .dot.
 */
public class Filter extends FileFilter{
    
    private String ext = null;

    public Filter(String est){
        this.ext = new String(est);
    }

    public boolean accept(File f) {
        String extension = null;
        if(f.isDirectory()){
            return true;
        }else{
            extension = new String(this.getExtension(f));
            if (extension != null) {
                if (extension.equals(ext)){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
    }

    public String getDescription() {
        return this.ext;
    }

    private String getExtension(File f) {
        String exte = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            exte = s.substring(i+1).toLowerCase();
        }
        return exte;
    }

}
