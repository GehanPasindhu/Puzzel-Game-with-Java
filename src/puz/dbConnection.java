/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package puz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 *
 * @author pasin
 */
public class dbConnection {

    private static Connection c;

    private static void setConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        c = DriverManager.getConnection("jdbc:mysql://localhost:3306/puzel", "root", "");
    }

    public static void iud(String sql) throws Exception {
        if (c == null) {
            setConnection();
        }
        c.createStatement().executeUpdate(sql);
    }

    public static ResultSet search(String sql) throws Exception {
        if (c == null) {
            setConnection();
        }
        return c.createStatement().executeQuery(sql);
    }

    public static Connection getNewConnection() throws Exception {
        if (c == null) {
            setConnection();
        }
        return c;
    }
}

