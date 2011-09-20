/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import testsuitemanager.Commands;
import testsuitemanager.TestCase;
import java.io.FileOutputStream;
import java.io.IOException;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author angelo
 */

/**
 * Classe utilizzata per costuire una suite case. Una suite case
 * é composta da piu test case. La suite case è un file xml
 * che contiene i link ad altri file xml che rapprensetano i test case.
 * Ciascun test case è un insieme di comandi. Una suite case è eseguibile
 * separatamente con Selenium IDE.
 */
public class ParseToXML {
    
    private String path = null;
    private Object[] tCase = null;
    private int dimens = 0;

    public ParseToXML(Object[] tc, int dim, String percorso){
        this.tCase = tc;
        this.dimens = dim;
        this.path = new String(percorso);
        this.createTestSuiteCase();
    }

    /**
     * 
     * @param obj
     * @param name --> nome test case
     * 
     * Crea i test case
     */
    private void createTCase(Object obj, String name){
        try{
            Element rootElement = new Element("html");
            Namespace ns = Namespace.getNamespace("http://www.w3.org/1999/xhtml");
            rootElement.setNamespace(ns);
            Document document = new Document(rootElement);
            DocType docType = new DocType("html","-//W3C//DTD XHTML 1.0 Strict//EN","http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
            document.setDocType(docType);
            Element root_1 = new Element("head");
            rootElement.addContent(root_1);
            root_1.setAttribute("profile","http://selenium-ide.openqa.org/profiles/test-case");
            Element root_2 = new Element("body");
            rootElement.addContent(root_2);
            Element head_1 = new Element("meta");
            Element head_2 = new Element("link");
            Element head_3 = new Element("title");
            root_1.addContent(head_1);
            head_1.setAttribute("http-equiv","Content-Type");
            head_1.setAttribute("content","text/html; charset=UTF-8");
            root_1.addContent(head_2);
            head_2.setAttribute("rel","selenium.base");
            head_2.setAttribute("href","");
            root_1.addContent(head_3);
            head_3.setText("Test case: "+name);
            Element body_1 = new Element("table");
            root_2.addContent(body_1);
            body_1.setAttribute("cellpadding","1");
            body_1.setAttribute("cellspacing","1");
            body_1.setAttribute("border","1");
            Element table_1 = new Element("thead");
            Element table_2 = new Element("tbody");
            body_1.addContent(table_1);
            body_1.addContent(table_2);
            Element thead_1 = new Element("tr");
            Element tr_1 = new Element("td");
            table_1.addContent(thead_1);
            thead_1.addContent(tr_1);
            tr_1.setAttribute("rowspan","1");
            tr_1.setAttribute("colspan","3");
            tr_1.setText(name);

            Element tbody_tr = null;
            Element tr_td_1 = null;
            Element tr_td_2 = null;
            Element tr_td_3 = null;
            Commands cmd = new Commands(obj);
            String[][] comm = cmd.getCommands();
            for(int i = 0; i < comm.length-1; i++){
                tbody_tr = new Element("tr");
                tr_td_1 = new Element("td");
                tr_td_2 = new Element("td");
                tr_td_3 = new Element("td");
                table_2.addContent(tbody_tr);
                tbody_tr.addContent(tr_td_1);
                tr_td_1.setText(comm[i][0]);//command
                tbody_tr.addContent(tr_td_2);
                tr_td_2.setText(comm[i][1]);//type
                if(!comm[i][0].equals("type")){
                    tbody_tr.addContent(tr_td_3);
                    tr_td_3.setText("-");//value
                }
                else{
                    tbody_tr.addContent(tr_td_3);
                    tr_td_3.setText(comm[i][2]);//value
                }
            }

            //Creazione dell'oggetto XMLOutputter
            XMLOutputter outputter = new XMLOutputter();
            //Imposto il formato dell'outputter come "bel formato"
            outputter.setFormat(Format.getPrettyFormat());
            //Produco l'output sul file xml.foo
            FileOutputStream f = new FileOutputStream(this.path+"/"+name);
            //outputter.output(document, f);
            outputter.output(document, f);

            }
            catch (IOException e) {
              System.err.println("Errore durante la creazione del test case "+name);
              e.printStackTrace();
            }
    }

    /**
     * Crea la suite case contenente i test case
     */
    private void createTestSuiteCase(){
        String nomets = null;
        int coda = (int)(100000*Math.random())+1;
        try{
            if(this.dimens == 1){
                nomets = ((TestCase)this.tCase[0]).getNameTrace();
            }
            else{
                nomets = "Test_Suite_"+coda;
            }
            Element rootElement = new Element("html");
            Namespace ns = Namespace.getNamespace("http://www.w3.org/1999/xhtml");
            rootElement.setNamespace(ns);
            Document document = new Document(rootElement);
            DocType docType = new DocType("html","-//W3C//DTD XHTML 1.0 Strict//EN","http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
            document.setDocType(docType);
            Element root_1 = new Element("head");
            rootElement.addContent(root_1);
            Element root_2 = new Element("body");
            rootElement.addContent(root_2);
            Element head_1 = new Element("meta");
            Element head_3 = new Element("title");
            root_1.addContent(head_1);
            head_1.setAttribute("http-equiv","Content-Type");
            head_1.setAttribute("content","text/html; charset=UTF-8");
            root_1.addContent(head_3);
            head_3.setText("Test suite case: "+nomets);
            Element body_1 = new Element("table");
            root_2.addContent(body_1);
            body_1.setAttribute("cellpadding","1");
            body_1.setAttribute("cellspacing","1");
            body_1.setAttribute("border","1");
            body_1.setAttribute("id", "suiteTable");
            body_1.setAttribute("class","selenium");
            
            Element table_2 = new Element("tbody");
            body_1.addContent(table_2);
            Element tbody_1 = new Element("tr");
            table_2.addContent(tbody_1);
            Element tr_1 = new Element("td");
            tbody_1.addContent(tr_1);
            Element td_1 = new Element("b");
            tr_1.addContent(td_1);
            td_1.setText(nomets);

            Element tbody_n = null;
            Element tr_td_1 = null;
            Element td_a_1 = null;
            
            String nome_t_case = null;
            for(int j = 0; j < tCase.length; j++){
                tbody_n = new Element("tr");
                tr_td_1 = new Element("td");
                td_a_1 = new Element("a");
                table_2.addContent(tbody_n);
                tbody_n.addContent(tr_td_1);
                tr_td_1.addContent(td_a_1);
                if(this.dimens == 1){
                    nome_t_case = new String(nomets+"_TC_"+(j+1)+".xml");
                }
                else{
                    nome_t_case = new String(((TestCase)this.tCase[j]).getNameTrace()+"_TC_"+(j+1)+coda+".xml");
                }
                td_a_1.setAttribute("href", nome_t_case);
                td_a_1.setText(nome_t_case);
                createTCase(tCase[j],nome_t_case);
            }

            //Creazione dell'oggetto XMLOutputter
            XMLOutputter outputter = new XMLOutputter();
            //Imposto il formato dell'outputter come "bel formato"
            outputter.setFormat(Format.getPrettyFormat());
            //Produco l'output sul file xml
            FileOutputStream f = new FileOutputStream(this.path+"/"+nomets+".xml");
            //outputter.output(document, f);
            outputter.output(document, f);

            }
            catch (IOException e) {
              System.err.println("Errore durante la creazione della test suite case "+nomets);
              e.printStackTrace();
            }
    }
}
