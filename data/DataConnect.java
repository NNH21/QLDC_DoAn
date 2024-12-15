package QLDC_DoAn.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataConnect {
    private static final String URL = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=QuanLyDanCu1; encrypt=false";
    private static final String USER = "sa";
    private static final String PASSWORD = "12345";

    public static Connection connectToDatabase() {
        Connection connection = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            if (connection != null) {
                return connection;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

