import java.sql.*;

public class DatabaseConnection {
    private static final String url = "jdbc:mysql://localhost:3306/pin";
    private static final String un = "root";
    private static final String pw = "";

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, un, pw);
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
        return connection;
    }
}