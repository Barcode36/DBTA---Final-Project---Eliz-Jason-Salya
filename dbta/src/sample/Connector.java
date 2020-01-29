package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author elizabethchan
 */
public class Connector {
    public static Connection getConnection()
    {
        Connection conn = null;
        String port = "3306";
        String dbName = "3_otw";
        String username = "CHA7001";
        String password = "pojuxehh";

        try{
            conn = DriverManager.getConnection("jdbc:mysql://dbta.1ez.xyz:" + port + "/" + dbName + "?serverTimezone=GMT", username, password);
        }catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return conn;
    }
}