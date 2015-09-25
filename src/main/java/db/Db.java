package db;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton for DB creation and connection creation
 * @author nilstes
 */
public class Db {

    private static final Logger log = Logger.getLogger(Db.class.getName());
    private static final String DB_NAME = "notsosecurebank";
    private static Db instance = new Db();
    
    private Db() {
        try {
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection("jdbc:h2:~/" + DB_NAME, DB_NAME, "");
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS user ( email VARCHAR(256) PRIMARY KEY, first_name VARCHAR(256), last_name VARCHAR(256), password VARCHAR(256) )");
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS transaction ( from_email VARCHAR(256), to_email VARCHAR(256), text VARCHAR(256), amount DECIMAL(20, 2), transaction_time TIMESTAMP )");
                statement.close();
            } finally {
                connection.close();
            }
            log.info("DB initialized!");
        } catch (Exception exception) {
            log.log(Level.SEVERE, "Failed to start DB", exception);
        }
    }
    
    public static Db instance() {
        return instance;
    }
    
    Connection getConnection() throws SQLException {
        // todo FIXME This is not a good way to get connections
        // Use a pool instead
        return DriverManager.getConnection("jdbc:h2:~/" + DB_NAME, DB_NAME, "");
    }
}