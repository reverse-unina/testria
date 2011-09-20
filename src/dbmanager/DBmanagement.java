/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dbmanager;

import java.sql.DriverManager;
import java.sql.SQLException;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import gui.Start;

/**
 *
 * @author angelo
 */
/**
 *
 * La classe DBmanagement permette di gestire una connessione a un
 * database MySql e di eseguire query su di esso.
 */
public class DBmanagement {

    private Connection con = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    /**
     *
     * @param user
     * @param password
     * @param db
     * @param port
     * @param location
     *
     * Questo metodo permette di instaurare una connessione con un database MySql.
     * La connessione è ritornata sottoforma di oggetto Connection.
     */
    public Connection db_connection(String user, char[] password, String db, String port, String location) {
            String url = "jdbc:mysql://"+location+":"+port+"/"+db+"";
            try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
            } catch (Exception e) {
                    System.out.println("ERROR opening DB connection " + e+"\n");
                    return con;
            }
            try {
                    String pass = new String(password);
                    con = (Connection) DriverManager.getConnection(url, user, pass);
            } catch (Exception e) {
                    System.out.println("ERROR opening DB connection " + e+"\n");
                    return con;
            }
            //System.out.println("- DB connection open -\n");
            return con;
    }

    /**
     *
     * @param con
     *
     * Questo metodo chiude la connesione con il database. La connessione
     * è rappresentata dal paramentro con.
     */
    public void close_db_connection(java.sql.Connection con) {
            try {
                    con.close();
                    //System.out.println("- DB connection closed -\n");
            } catch (SQLException e) {
                    System.out.println("- ERROR closing DB connection " + e +" -\n");
            }
    }

    /**
     *
     * @param query
     *
     * Questo metodo esegue la query di selezione "query", passata in ingresso, sul db.
     * Ritorna il risultato sottoforma di ResultSet.
     */
    public ResultSet select(String query){
        try {
            stmt = (Statement) con.createStatement();
            rs = (ResultSet) stmt.executeQuery(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("- ERROR impossible to execute the query -");
        }
        return rs;
    }

    /**
     *
     * @param query
     *
     * Questo metodo esegue la query di aggiornamento "query", passata in ingresso, sul db.
     * Ritorna il risultato sottoforma di intero, aggiornamento avvenuto up = 1 , up = 0 altrimenti.
     */
    public int update(String query){
        int up = 0;
        try {
            stmt = (Statement) con.createStatement();
            up = stmt.executeUpdate(query);
            up = 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("- ERROR impossible to execute the query -");
        }
        return up;
    }

    /**
     *
     * @param interf
     *
     * Questo metodo ritorna gli oggetti e gli attributi di una pagina, identificata
     * attraverso un id, interf, passato come input. Gli oggetti e gli attributi
     * ritornati, sottoforma di ResultSet, sono quelli identificato dal criterio di
     * clusterizzazione C1.
     */
    public ResultSet selectObjectAndAttributesC1(int interf) {
        //Da eliminare
        System.out.println("C1 --> "+interf);

        java.sql.CallableStatement storedProcedure = null;
        String dbase = Start.nomeDB;
        try {
            storedProcedure = con.prepareCall("{ call "+dbase+".cluObjAttrInterfaceC1(?) }");
            storedProcedure.setInt(1, interf);
            storedProcedure.execute();
            return (ResultSet) storedProcedure.getResultSet();
        }catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     *
     * @param interf
     *
     * Questo metodo ritorna gli oggetti e gli attributi di una pagina, identificata
     * attraverso un id, interf, passato come input. Gli oggetti e gli attributi
     * ritornati, sottoforma di ResultSet, sono quelli identificato dal criterio di
     * clusterizzazione C2.
     */
    public ResultSet selectObjectAndAttributesC2(int interf) {
        //Da eliminare
        System.out.println("C2 --> "+interf);

        java.sql.CallableStatement storedProcedure = null;
        String dbase = Start.nomeDB;
        try {
            storedProcedure = con.prepareCall("{ call "+dbase+".cluObjAttrInterfaceC2(?) }");
            storedProcedure.setInt(1, interf);
            storedProcedure.execute();
            return (ResultSet) storedProcedure.getResultSet();
        }catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     *
     * @param interf
     *
     * Questo metodo ritorna gli oggetti e gli attributi di una pagina, identificata
     * attraverso un id, interf, passato come input. Gli oggetti e gli attributi
     * ritornati, sottoforma di ResultSet, sono quelli identificato dal criterio di
     * clusterizzazione C3.
     */
    public ResultSet selectObjectAndAttributesC3(int interf) {
        //Da eliminare
        System.out.println("C3 --> "+interf);

        java.sql.CallableStatement storedProcedure = null;
        String dbase = Start.nomeDB;
        try {
            storedProcedure = con.prepareCall("{ call "+dbase+".cluObjAttrInterfaceC3(?) }");
            storedProcedure.setInt(1, interf);
            storedProcedure.execute();
            return (ResultSet) storedProcedure.getResultSet();
        }catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     *
     * @param interf
     *
     * Questo metodo ritorna gli oggetti e gli attributi di una pagina, identificata
     * attraverso un id, interf, passato come input. Gli oggetti e gli attributi
     * ritornati, sottoforma di ResultSet, sono quelli identificato dal criterio di
     * clusterizzazione C4.
     */
    public ResultSet selectObjectAndAttributesC4(int interf) {
        //Da eliminare
        System.out.println("C4 --> "+interf);


        java.sql.CallableStatement storedProcedure = null;
        String dbase = Start.nomeDB;
        try {
            storedProcedure = con.prepareCall("{ call "+dbase+".cluObjAttrInterfaceC4(?) }");
            storedProcedure.setInt(1, interf);
            storedProcedure.execute();
            return (ResultSet) storedProcedure.getResultSet();
        }catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     *
     * Ritorna la connessione instanziata con il db.
     */
    public Connection getConnection(){
        return this.con;
    }

    /**
     *
     * Questo metodo riporta il db ad uno stato noto. Non è un vero e proprio
     * reset, in quanto restano alcune informazioni utili al fine della riesecuzione
     * delle tracce.
     */
    public void resetDB(){
        try {
            String query = new String("DELETE FROM todo");
            stmt = (Statement) con.createStatement();
            stmt.executeUpdate(query);
            query = "DELETE FROM tuser_todo_list";
            stmt.executeUpdate(query);
            query = "DELETE FROM todo_list";
            stmt.executeUpdate(query);
            query = "DELETE FROM tuser_role";
            stmt.executeUpdate(query);
            query = "DELETE FROM tuser";
            stmt.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("- ERROR impossible to reset DB -");
        }
    }

}
