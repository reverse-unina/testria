package test;
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
import com.thoughtworks.selenium.Selenium;
import org.seleniuminspector.SeleniumTestCase;
import service.Utility;
import testsuitemanager.Commands;
/**
*
* @author angelo
*/
public class tracciaTudu1 extends SeleniumTestCase implements Runnable{
    private Selenium selenium;
    private Utility utility;
 
    public boolean test() {
        selenium = utility.setUp();
        String[][] cmd = new String[8][3];
        boolean assertInterface = false;

        String[][] fields = null;
        Commands comm = new Commands();
        String cluster = new String("C4");
        cmd[0][0] = "click";
        cmd[0][1] = "//html[1]/body[1]/table[1]/tbody[1]/tr[1]/td[2]/ul[1]/li[2]/a[1]";
        cmd[0][2] = "1";
        cmd[1][0] = "click";
        cmd[1][1] = "//html[1]/body[1]/table[2]/tbody[1]/tr[2]/td[1]/div[1]/div[1]/form[1]/input[2]";
        cmd[1][2] = "2";
        cmd[2][0] = "click";
        cmd[2][1] = "//html[1]/body[1]/table[1]/tbody[1]/tr[1]/td[2]/ul[1]/li[1]/a[1]";
        cmd[2][2] = "3";
        cmd[3][0] = "click";
        cmd[3][1] = "//html[1]/body[1]/table[2]/tbody[1]/tr[2]/td[1]/div[1]/table[1]/tbody[1]/tr[1]/td[2]/form[1]/table[1]/tbody[1]/tr[4]/th[1]/input[1]";
        cmd[3][2] = "4";
        cmd[4][0] = "click";
        cmd[4][1] = "//html[1]/body[1]/table[2]/tbody[1]/tr[2]/td[1]/div[1]/table[1]/tbody[1]/tr[1]/td[1]/div[1]/table[1]/thead[1]/tr[3]/td[1]/a[1]";
        cmd[4][2] = "5";
        cmd[5][0] = "click";
        cmd[5][1] = "//html[1]/body[1]/table[2]/tbody[1]/tr[2]/td[1]/div[1]/table[1]/tbody[1]/tr[1]/td[3]/div[3]/div[2]/form[1]/table[1]/tbody[1]/tr[3]/td[1]/a[1]";
        cmd[5][2] = "6";
        cmd[6][0] = "click";
        cmd[6][1] = "//html[1]/body[1]/table[1]/tbody[1]/tr[1]/td[2]/ul[1]/li[3]/a[1]";
        cmd[6][2] = "7";
        cmd[7][0] = "end";
        cmd[7][1] = "//html[1]/body[1]/table[1]/tbody[1]/tr[1]/td[2]/ul[1]/li[3]/a[1]";
        cmd[7][2] = "8";
        for(int i = 0; i < cmd.length; i++){
            assertInterface = utility.assertPage(utility.getObj(cluster, Integer.parseInt(cmd[i][2])), cluster, selenium);
            if(assertInterface){
                fields = comm.getValueFields(cmd[i][2]);
                if(fields.length != 0){
                    for(int k = 0; k < fields.length; k++){
                        selenium.type("//html[1]"+fields[k][1].substring(8), fields[k][0]);
                    }
                }
                if(cmd[i][0].equals("click")){
                    selenium.click(cmd[i][1]);
                }
            }
            else{
                break;
            }
        }
        utility.tearDown(selenium);
        return assertInterface;
     }
 
   public void run() {
   }
}
