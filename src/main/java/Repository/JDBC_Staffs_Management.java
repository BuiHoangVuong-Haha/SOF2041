package Repository;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;
import Model.Model_Staffs_Management;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JDBC_Staffs_Management {
    private static final Logger log = Logger.getLogger(JDBC_Staffs_Management.class.getName());
    private static final String URL = "jdbc:sqlserver://MSI:1433;databaseName=EduSys;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "FuckYouBitch";

    public List<Model_Staffs_Management> getAllStaffs(){
        List<Model_Staffs_Management> staffsList = new ArrayList<>();
        String query = "{CALL dbo.sp_getAllStaffs}";

        try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD)){
            log.info("Ket noi thanh cong!");
            try (CallableStatement stmt = conn.prepareCall(query)){
                ResultSet rs = stmt.executeQuery();

                while(rs.next()){
                    Model_Staffs_Management staff = new Model_Staffs_Management();
                    staff.setCode(rs.getString("MaNV"));
                    staff.setPassword(rs.getString("MatKhau"));
                    staff.setName(rs.getString("HoTen"));
                    boolean isRoleBit = rs.getBoolean("VaiTro");
                    staff.setRole(isRoleBit ? "Quan ly" : "Nhan vien");

                    staffsList.add(staff);
                }
            } catch (SQLException e){
                log.log(Level.WARNING, "Thuc thi truy van du lieu that bai!");
            }
        } catch(SQLException e){
            log.log(Level.SEVERE, "Ket noi that bai!");
        }
        return staffsList;
    }

    public void mergeStaffs (List<Model_Staffs_Management> staffsList, String deleteMaNV){
        String insertQuery = "{CALL sp_mergeStaffs(?, ?)}";
        try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD)){
            log.info("Ket noi thanh cong!");
            ObjectMapper objectMapper = new ObjectMapper();
            String StaffsJson = objectMapper.writeValueAsString(staffsList);

            // Thêm log để kiểm tra dữ liệu trước khi gửi vào Stored Procedure
            log.info("JSON gửi vào stored procedure: " + StaffsJson);
            log.info("Mã nhân viên cần xóa: " + (deleteMaNV == null ? "NULL" : deleteMaNV));

            try(CallableStatement stmt = conn.prepareCall(insertQuery)){
                stmt.setString(1, StaffsJson);

                if(deleteMaNV != null){
                    stmt.setString(2, deleteMaNV);
                } else {
                    stmt.setObject(2, null, Types.NVARCHAR);
                }

                stmt.execute();
                log.info("Luu du lieu thanh cong!");
            } catch (SQLException e){
                log.log(Level.WARNING, "Thuc thi truy van that bai!", e);
            }
        } catch (SQLException e){
            log.log(Level.SEVERE, "Ket noi that bai!", e);
        } catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
}
