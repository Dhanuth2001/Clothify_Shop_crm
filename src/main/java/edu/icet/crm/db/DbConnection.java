package edu.icet.crm.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static DbConnection dbConnection;

    private Connection connection;

    public DbConnection () throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Clothify_crm", "root", "123456");


    }

    public static DbConnection getInstance() throws SQLException, ClassNotFoundException {
        if(dbConnection == null){
            dbConnection=new DbConnection();
        }
        return dbConnection;
    }

    public Connection getConnection() {
        return connection;
    }


}
