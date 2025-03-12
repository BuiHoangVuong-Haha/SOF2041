package Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import Model.Model_Statistical_Summary.*;

public class JDBC_Statistical_Summary {
    private static final Logger log = Logger.getLogger(JDBC_Staffs_Management.class.getName());
    private static final String URL = "jdbc:sqlserver://MSI:1433;databaseName=EduSys;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "FuckYouBitch";

    //Student----------------------------------------------------------------------------------------------------------------------------
    public List<Student> getProcStudent(){
        List<Student> stuList = new ArrayList<>();
        String query = "{CALL dbo.sp_LuongNguoiHoc}";

        try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD)){
            log.info("Ket noi jdbc thanh cong!");
            try(CallableStatement stmt = conn.prepareCall(query)){
                ResultSet rs = stmt.executeQuery();

                while(rs.next()){
                    Student stu = new Student();
                    stu.setYear(rs.getInt("Nam"));
                    stu.setNumber(rs.getInt("SoLuong"));
                    stu.setFirst(rs.getDate("DauTien"));
                    stu.setLast(rs.getDate("CuoiCung"));

                    stuList.add(stu);
                }
            } catch (SQLException e){
                log.log(Level.SEVERE, "Thuc thi truy van that bai!");
            }
        } catch (SQLException e){
            log.log(Level.SEVERE, "Ket noi jdbc that bai!");
        }
        return stuList;
    }
    //Student----------------------------------------------------------------------------------------------------------------------------



    //Transcript----------------------------------------------------------------------------------------------------------------------------
    //Fill combobox
    public List<Transcript_Combobox> getCDTranscript(){
        List<Transcript_Combobox> maCDList = new ArrayList<>();
        String query = "{CALL dbo.sp_getCDTranscript}";

        try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD)){
            log.info("Ket noi thanh cong!");
            try(CallableStatement stmt = conn.prepareCall(query)){
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

    //insert maCD -> DB de tim maKH --> lay ra cai List
    public List<Transcript> getBangDiem(String maCD){
        String insertQueryGetMaKH = "{CALL dbo.sp_getMaKH(?)}"; //tu Java -> DB
        String insertQueryGetBangDiem = "{CALL sp_BangDiem(?)}"; //tu DB -> Java
        List<Transcript> transList = new ArrayList<>();

        try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD)){
            int maKH = -1;
            log.info("Ket noi jdbc thanh cong!");
            try(CallableStatement stmt = conn.prepareCall(insertQueryGetMaKH)){
                stmt.setString(1,maCD);
                ResultSet rs =  stmt.executeQuery();
                if(rs.next()){
                    maKH = rs.getInt("MaKH");
                }
                log.info("Thuc thi truy van thanh cong");
            } catch (SQLException e){
                log.log(Level.SEVERE, "Thuc thi truy van that bai!(insert maCD)");
            }

            if(maKH != -1){
                try(CallableStatement stmt = conn.prepareCall(insertQueryGetBangDiem)){
                    stmt.setInt(1,maKH);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()){
                        Transcript trans = new Transcript();
                        trans.setStuCode(rs.getString("MaNH"));
                        trans.setName(rs.getString("HoTen"));
                        trans.setMark(rs.getFloat("Diem"));

                        // Xác định xếp loại
                        float mark = trans.getMark();
                        String grading;
                        if (mark >= 9.0) {
                            grading = "Xuat sac";
                        } else if (mark >= 8.0) {
                            grading = "Gioi";
                        } else if (mark >= 6.5) {
                            grading = "Kha";
                        } else if (mark >= 5.0) {
                            grading = "Trung binh";
                        } else if (mark < 5.0){
                            grading = "Yeu";
                        }
                        else {
                            grading = "Chua nhap";
                        }
                        trans.setGrading(grading); // Gán giá trị xếp loại vào đối tượng

                        transList.add(trans);
                    }
                } catch (SQLException e){
                    log.log(Level.SEVERE, "Thuc thi truy van that bai! (Fill table voi maKH)");
                }
            }

        } catch (SQLException e){
            log.log(Level.SEVERE, "Ket noi jdbc that bai!");
        }
        return transList;
    }
    //Transcript----------------------------------------------------------------------------------------------------------------------------



    //Grade_Summary----------------------------------------------------------------------------------------------------------------------------
    public List<Grade_Summary> getProcGradeSummary(){
        List<Grade_Summary> gradeList = new ArrayList<>();
        String query = "{CALL dbo.sp_DiemChuyenDe}";

        try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD)){
            log.info("Ket noi jdbc thanh cong!");
            try(CallableStatement stmt = conn.prepareCall(query)){
                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    Grade_Summary grade = new Grade_Summary();

                    grade.setThematic(rs.getString("ChuyenDe"));
                    grade.setTotal(rs.getInt("SoHV"));
                    grade.setLowest(rs.getInt("ThapNhat"));
                    grade.setHighest(rs.getInt("CaoNhat"));
                    grade.setAvg(rs.getFloat("TrungBinh"));

                    gradeList.add(grade);
                }
            } catch (SQLException e){
                log.log(Level.SEVERE,"Loi thuc thi truy van!");
            }
        } catch (Exception e) {
            log.log(Level.SEVERE,"Loi ket noi jdbc!");
        }
        return gradeList;
    }
    //Grade_Summary----------------------------------------------------------------------------------------------------------------------------



    //Revenue----------------------------------------------------------------------------------------------------------------------------
    //Fill combobox
    public List<Integer> getYearRevenue() {
        List<Integer> years = new ArrayList<>();
        String query = "{CALL dbo.sp_getYearRevenue}";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            log.info("Ket noi JDBC thanh cong!");

            try (CallableStatement stmt = conn.prepareCall(query)) {
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    years.add(rs.getInt("year"));
                }
            } catch (SQLException e) {
                log.log(Level.SEVERE, "Loi thuc thi truy van!", e);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Loi ket noi JDBC!", e);
        }
        return years;
    }

    //Fill lai Table
    public List<Revenue> getProcRevenue(int year){
        List<Revenue> revenueList = new ArrayList<>();
        String query = "{CALL dbo.sp_DoanhThu(?)}"; //DB -> Java (co truyen tham so tu Java)

        try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD)){
            log.info("Ket noi jdbc thanh cong!");

            try(CallableStatement stmt = conn.prepareCall(query)){
                stmt.setInt(1, year);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    Revenue re = new Revenue();

                    re.setThematic(rs.getString("ChuyenDe"));
                    re.setCourseNumber(rs.getInt("SoKH"));
                    re.setStuNumber(rs.getInt("SoHV"));
                    re.setFeeAvg(rs.getFloat("DoanhThu"));
                    re.setFeeLowest(rs.getFloat("ThapNhat"));
                    re.setFeeHighest(rs.getFloat("CaoNhat"));
                    re.setFeeTotal(rs.getFloat("TrungBinh"));

                    revenueList.add(re);
                }
            } catch (SQLException e){
                log.log(Level.SEVERE, "Loi thuc thi truy van!", e);
            }
        } catch (SQLException e){
            log.log(Level.SEVERE, "Loi ket noi jdbc!", e);
        }
        return revenueList;
    }
}
//Revenue----------------------------------------------------------------------------------------------------------------------------
