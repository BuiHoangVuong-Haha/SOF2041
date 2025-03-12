package Repository;

import Model.Model_Course_Management;
import Model.Model_Staffs_Management;
import Model.Model_Students_Management;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBC_Course_Management {
    private final static Logger log = Logger.getLogger(JDBC_Course_Management.class.getName());
    private final static String URL = "jdbc:sqlserver://MSI:1433;databaseName=EduSys;encrypt=true;trustServerCertificate=true";
    private final static String USER = "sa";
    private final static String PASSWORD = "FuckYouBitch";

    //Fill vao comboBox
    public List<String> sp_getCDCourse() {
        List<String> courseCode = new ArrayList<>();
        String query = "{CALL dbo.sp_getCDCourse}";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            log.info("Ket noi thanh cong!");
            try (CallableStatement stmt = conn.prepareCall(query)) {
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    courseCode.add(rs.getString("TenCD"));
                }
            } catch (SQLException e) {
                log.log(Level.SEVERE, "Loi thuc thi truy van!", e);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Ket noi jdbc that bai!", e);
        }
        return courseCode;
    }

    //Fill vao table
    public List<Model_Course_Management> getAllCourse(String courseCode) {
        List<Model_Course_Management> courseList = new ArrayList<>();
        String query = "{CALL dbo.sp_getCourseByTenCD(?)}";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            log.info("Ket noi jdbc thanh cong");
            try (CallableStatement stmt = conn.prepareCall(query)) {
                stmt.setString(1, courseCode);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Model_Course_Management course = new Model_Course_Management();

                    // ✅ Xử lý NULL khi lấy MaKH từ database
                    Integer maKH = (rs.getObject("MaKH") != null) ? rs.getInt("MaKH") : null;
                    course.setCode(maKH);

                    course.setCourseCode(rs.getString("MaCD"));
                    course.setFee(rs.getFloat("HocPhi"));
                    course.setDuration(rs.getInt("ThoiLuong"));
                    course.setOpeningDay(rs.getDate("NgayKG"));
                    course.setStaffCode(rs.getString("MaNV"));
                    course.setDayOfCreation(rs.getDate("NgayTao"));
                    course.setDescription(rs.getString("GhiChu"));

                    courseList.add(course);
                }
            } catch (SQLException e) {
                log.log(Level.SEVERE, "Thuc thi truy van that bai!", e);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Loi ket noi JDBC!", e);
        }
        return courseList;
    }


    //getMaCD
    public String getMaCD(String tenCD) {
        String spGetMaCDQuery = "{CALL dbo.sp_getMaCD(?)}"; // Gọi stored procedure
        String maCD = null;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            log.info("Kết nối thành công!");

            // 1️⃣ Lấy MaCD từ TenCD
            try (CallableStatement stmt = conn.prepareCall(spGetMaCDQuery)) {
                stmt.setString(1, tenCD);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        maCD = rs.getString("MaCD");
                    }
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Lỗi kết nối JDBC hoặc thực thi truy vấn!", e);
        }

        return maCD; // Đảm bảo luôn trả về giá trị (có thể là null nếu không tìm thấy)
    }

    //Merge vao DB va loc = MaCD (Da tich hop luon viec loc ra MaCD)
        public void mergeCourse (List < Model_Course_Management > courseList) {
            String spMergeQuery = "{CALL dbo.sp_mergeCourse(?, NULL, ?, ?, ?, ?, ?, ?, ?)}"; // Gọi stored procedure

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                log.info("Kết nối thành công!");

                for (Model_Course_Management course : courseList) {
                    try (CallableStatement stmt = conn.prepareCall(spMergeQuery)) {
                        stmt.setString(1, "ADD");
                        stmt.setString(2, course.getCourseCode()); // MaCD
                        stmt.setDouble(3, course.getFee()); // HocPhi
                        stmt.setInt(4, course.getDuration()); // ThoiLuong
                        stmt.setString(5, course.getStaffCode()); // MaNV
                        stmt.setString(6, course.getDescription()); // GhiChu
                        stmt.setDate(7, course.getOpeningDay() != null ? new java.sql.Date(course.getOpeningDay().getTime()) : null); // NgayKG
                        stmt.setDate(8, course.getDayOfCreation() != null ? new java.sql.Date(course.getDayOfCreation().getTime()) : null); // NgayTao

                        stmt.execute();
                        log.info("Thuc thi thanh cong!");
                    } catch (SQLException e) {
                        log.log(Level.SEVERE, "Thực thi truy vấn thất bại!", e);
                    }
                }
            } catch (SQLException e) {
                log.log(Level.SEVERE, "Kết nối JDBC thất bại!", e);
            }
        }
    }


