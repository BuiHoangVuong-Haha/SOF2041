package Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Model.Model_Students_Management;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JDBC_Students_Management {
    private final static Logger log = Logger.getLogger(JDBC_Students_Management.class.getName());
    private final static String URL = "jdbc:sqlserver://MSI:1433;databaseName=EduSys;encrypt=true;trustServerCertificate=true";
    private final static String USER = "sa";
    private final static String PASSWORD = "FuckYouBitch";

    public List<Model_Students_Management> getAllStudents(){
        List<Model_Students_Management> stuList = new ArrayList<>();
        String query = "{CALL dbo.sp_getAllStudents}";

        try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD)){
            log.info("Ket noi thanh cong!");
            try (CallableStatement stmt = conn.prepareCall(query)){
                ResultSet rs = stmt.executeQuery();

                while(rs.next()){
                    Model_Students_Management stu = new Model_Students_Management();
                    stu.setStuCode(rs.getString("MaNH"));
                    stu.setName(rs.getString("HoTen"));
                    stu.setDate(rs.getDate("NgaySinh"));
                    boolean isGenderBit = rs.getBoolean("GioiTinh");
                    stu.setGender(isGenderBit ? "Nam" : "Nu");
                    stu.setPhoneNum(rs.getString("DienThoai"));
                    stu.setEmail(rs.getString("Email"));
                    stu.setDescription(rs.getString("GhiChu"));
                    stu.setStaffCode(rs.getString("MaNV"));
                    stu.setRegisterDay(rs.getDate("NgayDK"));

                    stuList.add(stu);
                }
            } catch (SQLException e){
                log.log(Level.SEVERE, "Loi thuc thi truy van!");
            }
        } catch (SQLException e){
            log.log(Level.SEVERE, "Loi ket noi JDBC");
        }
        return stuList;
    }

    public void mergeStudents(List<Model_Students_Management> stuList){
        String query = "{CALL dbo.sp_mergeStudents(?)}";
        try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD)){
            log.info("Ket noi thanh cong!");

            //Chuyen doi java.util.Date -> java.sql.Date
            List<Model_Students_Management> convertedList = new ArrayList<>();
            for(Model_Students_Management stu : stuList){
                Model_Students_Management newStu = new Model_Students_Management();
                newStu.setStuCode(stu.getStuCode());
                newStu.setName(stu.getName());

                // Chuyen doi ngay sinh va ngay dang ky tu java.util.Date -> java.sql.Date
                newStu.setDate(stu.getDate() != null ? new java.sql.Date(stu.getDate().getTime()) : null);

                newStu.setGender(stu.getGender());
                newStu.setPhoneNum(stu.getPhoneNum());
                newStu.setEmail(stu.getEmail());
                newStu.setDescription(stu.getDescription());
                newStu.setStaffCode(stu.getStaffCode());

                // Chuyen doi java.util.Date -> java.sql.Date
                newStu.setRegisterDay(stu.getRegisterDay() != null ? new java.sql.Date(stu.getRegisterDay().getTime()) : null);


                convertedList.add(newStu);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            String StudentsJson = objectMapper.writeValueAsString(convertedList);
            try(CallableStatement stmt = conn.prepareCall(query)){
                stmt.setString(1,StudentsJson);
                stmt.execute();
                log.info("Luu du lieu thanh cong!");
            } catch (SQLException e) {
                log.log(Level.SEVERE, "Thuc thi truy van that bai! Chi tiet loi: " + e.getMessage(), e);
            }
        } catch (SQLException e){
            log.log(Level.SEVERE, "Ket noi jdbc that bai!");
        } catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
}
