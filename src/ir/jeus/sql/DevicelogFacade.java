package ir.jeus.sql;

import ir.jeus.server1.VmsServer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author jeus
 */
public class DevicelogFacade {
    // JDBC driver name and database URL

    static String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    public static void insertToDB(String str) {
        Connection conn = null;
        Statement stmt = null;

        //  Database credentials
        String DB_URL = "jdbc:mysql://" + VmsServer.getHostPort() + "/" + VmsServer.getDbName();

        try {
            System.out.println(str + "----****");
            int dev = Integer.parseInt(str.substring(0, str.indexOf('N')));
            String log = str.substring(str.indexOf('N'));
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            //STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, VmsServer.getUser(), VmsServer.getPass());
            System.out.println("Connected database successfully...");
            //STEP 4: Execute a query
            System.out.println("Inserting records into the table...");
            stmt = conn.createStatement();
            String sql = "INSERT INTO devicelog (device,log) VALUES (" + dev + ",'" + log + "')";
            stmt.executeUpdate(sql);
            System.out.println("Inserted records into the table...");

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try

        System.out.println(
                "Goodbye!");
    }

}
