

package tikape.kysymyksetjavastaukset.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {


    public Database() throws ClassNotFoundException {
    }

    public static Connection getConnection() throws SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        if (dbUrl != null && dbUrl.length() > 0) {
            try {
                return DriverManager.getConnection(dbUrl);
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return DriverManager.getConnection("jdbc:sqlite:questions.db");
    }
}