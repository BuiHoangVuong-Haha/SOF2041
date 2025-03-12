package Controller;

import Model.Model_Course_Management;
import Model.Model_Statistical_Summary.Transcript_Combobox;
import View.CourseMember_Management;
import Model.Model_CourseMember_Management;
import Repository.JDBC_CourseMember_Management;
import Model.Model_Statistical_Summary.Transcript_Combobox;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller_CourseMember_Management {
    private CourseMember_Management view;
    private List<Model_CourseMember_Management> courseMemList;
    private JDBC_CourseMember_Management jdbc;
    private List<Transcript_Combobox> courseComList;
    private static final Logger log = Logger.getLogger(Controller_CourseMember_Management.class.getName());

    public Controller_CourseMember_Management(CourseMember_Management view) {
        this.view = view;
        courseMemList = new ArrayList<>();
        jdbc = new JDBC_CourseMember_Management();



        loadData();
//        initControl();
    }

//    private void initControl(){
//        view.getAddButton().addActionListener(e -> setAdd());
//        view.getUpdateButton().addActionListener(e -> setUpdate());
//    }

    //ActionListener cho thematicCb -> CourseCb
    private void loadThematicCbToCourseCbData(){
        loadThematicComboBox();
        JComboBox thematicCb = view.getThematicCb();
        thematicCb.setSelectedIndex(0); // Chọn item đầu tiên
        thematicCb.addActionListener(e -> {
            String TenCD = (String) view.getThematicCb().getSelectedItem();

            if (TenCD != null) {
                loadCourseComboBox(TenCD);
            }
        });
    }

    //Load TenCD -> thematicCb
    private void loadThematicComboBox() {
        List<String> thematicName = jdbc.sp_getCDCourse();
        JComboBox<String> thematicCb = view.getThematicCb();
        thematicCb.removeAllItems();
        thematicCb.setSelectedItem(-1);

        for (String code : thematicName) {
            thematicCb.addItem(code);
        }

    }

    //Load MaCD + NgayKG -> courseCb
    private void loadCourseComboBox(String TenCD){

        courseComList = new ArrayList<>();
        courseComList = jdbc.getMaCDNgayKG(TenCD);

        JComboBox<Transcript_Combobox> courseCb = view.getCourseCb();
        courseCb.removeAllItems();
        courseCb.setSelectedIndex(-1);

        for(Transcript_Combobox course : courseComList){
            courseCb.addItem(course); // Hiển thị theo định dạng đã override
        }
    }

    private void loadData() {
        loadThematicCbToCourseCbData();
        JComboBox<Transcript_Combobox> courseCb = view.getCourseCb();
        courseCb.addActionListener(e -> {
            Transcript_Combobox selectedCourse = (Transcript_Combobox) courseCb.getSelectedItem();

            if (selectedCourse != null) {
                String maCD = selectedCourse.getThematicCode();
                Date ngayKG = selectedCourse.getOpeningDay();

                // Chuyển thành java.sql.Date
                java.sql.Date sqlNgayKG = new java.sql.Date(ngayKG.getTime());

                // Gọi stored procedure để lấy MaKH
                int maKH = jdbc.sp_getMaKH(maCD, sqlNgayKG);

                if (maKH != -1) { // Kiểm tra nếu lấy được MaKH hợp lệ
                    courseMemList = jdbc.getProcCourseMember(maKH);
                    view.getCourseMemTblMdl().setRowCount(0);

                    int stt = 1;
                    for (Model_CourseMember_Management courseMem : courseMemList) {
                        view.getCourseMemTblMdl().addRow(new Object[]{
                                stt++, // Tạo STT tự động
                                courseMem.getMaHV(),
                                courseMem.getMaNH(),
                                courseMem.getHoTen(),
                                courseMem.getDiem()
                        });
                    }
                } else {
                    log.log(Level.SEVERE, "MaKH khong hop le!");
                }
            }
        });
    }





}
