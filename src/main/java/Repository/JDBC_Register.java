package Repository;

import java.sql.*;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import Model.Model_Staffs_Management;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JDBC_Register {
    private static final Logger log = Logger.getLogger(JDBC_Register.class.getName());
    private static final String URL = "jdbc:sqlserver://MSI:1433;databaseName=EduSys;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "FuckYouBitch";

    public void mergeStaffs(List<Model_Staffs_Management> staffsList){
        String query = "{CALL sp_mergeStaffs(?)}";

        try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD)){
            log.info("Ket noi thanh cong!");
            ObjectMapper objectMapper = new ObjectMapper();
            String StaffsJson = objectMapper.writeValueAsString(staffsList);
                try(CallableStatement stmt = conn.prepareCall(query)) {
                    stmt.setString(1, StaffsJson);
                    stmt.execute();
                    log.info("Luu du lieu thanh cong!");
                } catch (SQLException e){
                    log.log(Level.SEVERE, "Thuc thi truy van that bai!");
                }
        } catch(SQLException e){
            log.log(Level.SEVERE, "Ket noi that bai!");
        } catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
}
