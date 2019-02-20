package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleDriver;

public class Database {

    private static final String DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
    private String connectionString;
    private OracleConnection Conn;

    public Database(String connection) {
        setConnectionString(connection);
    }

    public Database() {
    }

    public OracleConnection getConn()
            throws SQLException, ClassNotFoundException {
        DriverManager.registerDriver(new OracleDriver());
        Class.forName("oracle.jdbc.driver.OracleDriver");

        this.Conn = ((OracleConnection) DriverManager.getConnection(this.connectionString));

        return this.Conn;
    }

    public boolean dbIsAlive(Connection c) {
        try {
            return !c.isClosed();
        } catch (SQLException ex) {
        }
        return false;
    }

    public String getDbUsername() throws SQLException {
        return this.Conn.getUserName().toString();
    }

    public String getConnectionString() {
        return this.connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }
}
