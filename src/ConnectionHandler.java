import java.sql.*;

public class ConnectionHandler {

    java.sql.Connection con;

    public void startConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/book_store","root","password");
    }

    public void closeConnection() throws SQLException {
        con.close();
    }

    public ResultSet excuteQuery (String query) throws SQLException {
        Statement stmt = con.createStatement();
        return stmt.executeQuery(query);
    }

}
