package Repository;

import Model.Model_CourseMember_Management;
import Model.Model_Statistical_Summary.Transcript_Combobox;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBC_CourseMember_Management {
    private final static Logger log = Logger.getLogger(JDBC_CourseMember_Management.class.getName());
    private final static String URL = "jdbc:sqlserver://MSI:1433;databaseName=EduSys;encrypt=true;trustServerCertificate=true";
    private final static String USER = "sa";
    private final static String PASSWORD = "FuckYouBitch";

    //Fill vao comboBox cua ThematicCb
    public List<String> sp_getCDCourse() {
        List<String> thematicName = new ArrayList<>();
        String query = "{CALL dbo.sp_getCDCourse}";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            log.info("Ket noi thanh cong!");
            try (CallableStatement stmt = conn.prepareCall(query)) {
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    thematicName.add(rs.getString("TenCD"));
                }
            } catch (SQLException e) {
                log.log(Level.SEVERE, "Loi thuc thi truy van!", e);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Ket noi jdbc that bai!", e);
        }
        return thematicName;
    }


    //Fill vao combobox cua CourseCb = TenCD cua thematicCourse
    public List<Transcript_Combobox> getMaCDNgayKG(String TenCD){
        List<Transcript_Combobox> maCDList = new ArrayList<>();
        String query = "{CALL dbo.sp_getMaCDNgayKG(?)}";

        try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD)){
            log.info("Ket noi thanh cong!");
            try(CallableStatement stmt = conn.prepareCall(query)){
                stmt.setString(1, TenCD);
                ResultSet rs = stmt.executeQuery();

                while(rs.next()){
                    Transcript_Combobox maCD = new Transcript_Combobox();

                    maCD.setThematicCode(rs.getString("MaCD"));
                    maCD.setOpeningDay(rs.getDate("NgayKG"));

                    maCDList.add(maCD);
                }
            } catch (SQLException e){
                log.log(Level.SEVERE, "Thuc thi truy van that bai!", e);
            }
        } catch (SQLException e){
            log.log(Level.SEVERE, "Ket noi jdbc that bai!", e);
        }
        return maCDList;
    }

    public int sp_getMaKH(String maCD, Date ngayKG) {
        String query = "{CALL dbo.sp_getMaKH(?, ?)}";
        int maKH = -1; // Giả định -1 là giá trị không hợp lệ

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            log.info("Ket noi thanh cong!");
            try (CallableStatement stmt = conn.prepareCall(query)) {
                stmt.setString(1, maCD);
                stmt.setDate(2, ngayKG);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) { // Chỉ lấy 1 giá trị
                    maKH = rs.getInt("MaKH");
                }

            } catch (SQLException e) {
                log.log(Level.SEVERE, "Thuc thi truy van that bai!", e);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Ket noi JDBC that bai!", e);
        }
        return maKH; // Nếu không có dữ liệu, trả về -1
    }


    public List<Model_CourseMember_Management> getProcCourseMember(int maKH){
        List<Model_CourseMember_Management> courseMemList = new ArrayList<>();
        String query = "{CALL dbo.sp_getProcCourseMember(?)}";

        try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD)){
            log.info("Ket noi jdbc thanh cong");
            try(CallableStatement stmt = conn.prepareCall(query)){
                stmt.setInt(1, maKH);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    Model_CourseMember_Management courseMem = new Model_CourseMember_Management();

                    courseMem.setMaHV(rs.getInt("MaHV"));
                    courseMem.setMaNH(rs.getString("MaNH"));
                    courseMem.setHoTen(rs.getString("HoTen"));
                    courseMem.setDiem(rs.getFloat("Diem"));

                    courseMemList.add(courseMem);
                }
            } catch (SQLException e){
                log.log(Level.SEVERE, "Loi thuc thi truy van!", e);
            }
        } catch (SQLException e){
            log.log(Level.SEVERE, "Ket noi jdbc that bai!", e);
        }
        return courseMemList;
    }















//    public List<Model_CourseMember_Management> getAllCourseMember(){
//        List<Model_CourseMember_Management> courseMemberList = new ArrayList<>();
//        String query = "{CALL dbo.sp_getAllCourseMember}";
//
//        try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD)){
//            log.info("Ket noi jdbc thanh cong!");
//            try(CallableStatement stmt = conn.prepareCall(query)){
//                ResultSet rs = stmt.executeQuery();
//
//                while (rs.next()){
//                    Model_CourseMember_Management cm = new Model_CourseMember_Management();
//
//                    cm.setCMCode(rs.getInt("MaHV"));
//                    cm.setCourseID(rs.getInt("MaKH"));
//                    cm.setStuCode(rs.getString("MaNH"));
//                    cm.setMark(rs.getFloat("Diem"));
//
//                    courseMemberList.add(cm);
//                }
//            } catch (SQLException e){
//                log.log(Level.SEVERE, "Loi thuc thi truy van!");
//            }
//        } catch (SQLException e){
//            log.log(Level.SEVERE, "Loi ket noi jdbc!");
//        }
//        return courseMemberList;
//    }
}
