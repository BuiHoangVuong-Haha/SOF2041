package Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import Model.Model_Thematic_Study;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JDBC_Thematic_Study {
    private final static Logger log = Logger.getLogger(JDBC_Thematic_Study.class.getName());
    private final static String URL = "jdbc:sqlserver://MSI:1433;databaseName=EduSys;encrypt=true;trustServerCertificate=true";
    private final static String USER = "sa";
    private final static String PASSWORD = "FuckYouBitch";

    public List<Model_Thematic_Study> getAllChuyenDe(){
        List<Model_Thematic_Study> thematicList = new ArrayList<>();
        String query = "{CALL dbo.sp_getAllChuyenDe}";

        try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD)){
            log.info("Ket noi thanh cong!");
            try(CallableStatement stmt = conn.prepareCall(query)){
                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    Model_Thematic_Study thematic = new Model_Thematic_Study();
                    thematic.setCode(rs.getString("MaCD"));
                    thematic.setName(rs.getString("TenCD"));
                    thematic.setTuitionFee(rs.getFloat("HocPhi"));
                    thematic.setTime(rs.getInt("ThoiLuong"));
                    thematic.setPicture(rs.getString("Hinh"));
                    thematic.setDescribe(rs.getString("MoTa"));

                    thematicList.add(thematic);
                }
            } catch (SQLException e){
                log.log(Level.SEVERE, "Loi thuc thi truy van!");
            }
        } catch (SQLException e){
            log.log(Level.SEVERE, "Loi ket noi JDBC");
        }
        return thematicList;
    }

    public void mergeThematicStudy(List<Model_Thematic_Study> thematicList){
        String insertQuery = "{CALL dbo.sp_mergeThematicStudy(?)}";
        try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD)){
            log.info("Ket noi jdbc thanh cong!");
            ObjectMapper objectMapper = new ObjectMapper();
            String ThematicJson = objectMapper.writeValueAsString(thematicList);

            try(CallableStatement stmt = conn.prepareCall(insertQuery)){
                stmt.setString(1, ThematicJson);
                stmt.execute();
                log.info("Luu du lieu thanh cong!");
            } catch (SQLException e){
                log.log(Level.SEVERE, "Thuc thi truy van that bai!", e);
            }
        } catch (SQLException e){
            log.log(Level.SEVERE, "Ket noi jdbc that bai!", e);
        } catch (JsonProcessingException e){
            log.log(Level.SEVERE, "Loi insert Json!", e);
        }
    }
}
