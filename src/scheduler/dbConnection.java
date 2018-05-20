//may need to remove this package
package scheduler;

import java.sql.*;

public class dbConnection {

    private Connection connection;        // The database connection object.
    private Statement statement;          // the database statement object, used to execute SQL commands

    //connects to the database with a username and password
    public void connectDB(String url, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
        System.out.println("Database connected!");
    }

    //gets employee name from login table
    public void getEmployeeName() throws SQLException {
        ResultSet rs;

        statement = connection.createStatement();

        rs = statement.executeQuery("select * from login");

        while (rs.next()) {
            System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3) + " " + rs.getString(4));
        }
    }

    //gets schedule from the schedule table
    public ResultSet getSchedule(String username) throws SQLException {
        ResultSet rs;

        statement = connection.createStatement();

        rs = statement.executeQuery("select * from schedule where username = '" + username + "'");

        return rs;

    }

    //returns the connection object
    public Connection getConnection() {
        return connection;
    }

    //closes the connection
    public void closeConnections() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //closes the statement
    public void closeStatement() {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
