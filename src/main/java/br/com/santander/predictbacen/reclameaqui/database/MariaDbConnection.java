package br.com.santander.predictbacen.reclameaqui.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MariaDbConnection {
    String userName,password,url,driver;
    Connection con;
    Statement st;

    private static MariaDbConnection ourInstance = new MariaDbConnection();

    public static MariaDbConnection getInstance() {
        return ourInstance;
    }

    private MariaDbConnection() {
        userName="root";
        password="my-root";
        url="jdbc:mariadb://localhost:3306/predictreclameaqui";
        driver="org.mariadb.jdbc.Driver";
        try {
            Class.forName(driver);
            this.con=DriverManager.getConnection(url, userName, password);
            this.st=con.createStatement();
            System.out.println("Connection is successful");
        } catch (Exception e) {
            System.out.println("Connection error");
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Statement getStatement() {
        try {
            return this.con.createStatement();
        } catch (Exception ex) {
            return null;
        }
    }

    public Connection getConnection() {
        return this.con;
    }
}
