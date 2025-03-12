package Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBC_Check {
    private static final Logger log = Logger.getLogger(JDBC_Check.class.getName());
    private static final String URL = "jdbc:sqlserver://MSI:1433;databaseName=EduSys;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "FuckYouBitch";

    public static void main(String[] args) {
        try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD)){
            if(conn!=null){
                log.info("Ket noi thanh cong!");
            }
        } catch(SQLException e){
            log.log(Level.SEVERE, "Ket noi that bai!");
        }
    }
}
