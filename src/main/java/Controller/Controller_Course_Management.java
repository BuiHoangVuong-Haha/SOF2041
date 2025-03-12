package Controller;

import View.Course;
import Model.Model_Course_Management;
import Repository.JDBC_Course_Management;
import View.LoginScreen;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller_Course_Management {
    private Course view;
    private JDBC_Course_Management jdbc;
    private List<Model_Course_Management> courseList;
    private LoginScreen view1;
    private Controller_LoginScreen view1Controller;
    private static final Logger log = Logger.getLogger(Controller_Course_Management.class.getName());

    public Controller_Course_Management(Course view) {
        this.view = view;
        jdbc = new JDBC_Course_Management();
        courseList = new ArrayList<>();
        this.view1Controller = Controller_LoginScreen.getInstance(null);
        this.view1 = this.view1Controller.getView();

        loadData();
        initControl();
        display();
    }

    private void initControl(){
        view.getAddButton().addActionListener(e -> setAdd());
        view.getNewButton().addActionListener(e -> setNew());
    }

    private void display() {
        try {
            String staffCode = Controller_LoginScreen.getInstance(view1).getLoggedInUsername();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = dateFormat.format(new Date()); // Format current date

            view.getCreatorTxt().setText(staffCode);
            view.getDayOfCreationTxt().setText(formattedDate);
        } catch (Exception e) {
            log.log(Level.WARNING, "Loi lien quan den parse dayOfCreation", e);
        }
    }


    private void loadData(){
        loadComboBox();
        JComboBox<String> comboBox = view.getSubjectCb();

        comboBox.addActionListener(e -> {
            String selectedCourse = (String) comboBox.getSelectedItem();

            if(selectedCourse != null){
                courseList = jdbc.getAllCourse(selectedCourse);
                view.getTableModel().setRowCount(0);
                for(Model_Course_Management course : courseList){
                    view.getTableModel().addRow(new Object[]{
                            (course.getCode() != null) ? course.getCode() : "N/A",
                            course.getCourseCode(),
                            course.getFee(),
                            course.getDuration(),
                            course.getOpeningDay(),
                            course.getStaffCode(),
                            course.getDayOfCreation()
                    });
                }
            }
        });
    }

    private void loadComboBox(){
        List<String> courseCode = jdbc.sp_getCDCourse();
        JComboBox<String> comboBox = view.getSubjectCb();
        comboBox.removeAllItems();
        comboBox.setSelectedItem(-1);

        for(String code : courseCode){
            comboBox.addItem(code);
        }
    }

    private void setAdd() {
        // Kiểm tra lỗi nhập liệu trước khi tiếp tục
        if (!setCheckErrors()) {
            return;
        }

        // Lấy MaCD add san vao trong List
        JComboBox<String> comboBox = view.getSubjectCb();
        String name = (String) comboBox.getSelectedItem();
        String courseCode = jdbc.getMaCD(name);

        if (courseCode == null) {
            JOptionPane.showMessageDialog(view.getFrame(), "Lỗi! Không tìm thấy Mã chuyên đề!", "Thông báo", JOptionPane.ERROR_MESSAGE);
            return;
        }

        float fee = Float.parseFloat(view.getFeeTxt().getText().trim());
        int duration = Integer.parseInt(view.getDurationTxt().getText().trim());
        String openingDayTxt = view.getOpeningDayTxt().getText().trim();
        String descriptionTxt = view.getDescriptionTxt().getText().trim();

        //Lay username cua form dang nhap
        String staffCode = Controller_LoginScreen.getInstance(view1).getLoggedInUsername();

        // Chuyển đổi từ String sang java.util.Date (đã kiểm tra lỗi trước đó)
        Date openingDate = null;
        Date dayOfCreation = new Date();

        // Lấy lại định dạng ngày đã sử dụng trong `setCheckErrors()`
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            openingDate = dateFormat.parse(openingDayTxt);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view.getFrame(), "Lỗi! Dữ liệu ngày không hợp lệ!", "Thông báo!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tạo đối tượng mới
        Model_Course_Management course = new Model_Course_Management();

        course.setCode(null);
        course.setCourseCode(courseCode);
        course.setFee(fee);
        course.setDuration(duration);
        course.setOpeningDay(openingDate);
        course.setDescription(descriptionTxt);
        course.setStaffCode(staffCode);
        course.setDayOfCreation(dayOfCreation); // Lưu dạng java.sql.Date

        courseList.add(course);

        // Gọi JDBC để lưu vào DB
        try {
            jdbc.mergeCourse(courseList);
            JOptionPane.showMessageDialog(view.getFrame(), "Thêm dữ liệu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view.getFrame(), "Lỗi khi lưu dữ liệu!", "Thông báo", JOptionPane.ERROR_MESSAGE);
            return;
        }
        loadData();
        setNew();
    }



    private boolean setCheckErrors() {
        // Lấy giá trị từ JComboBox
        String code = (String) view.getSubjectCb().getSelectedItem();

        // Lấy giá trị từ các JTextField
        String openingDayTxt = view.getOpeningDayTxt().getText().trim();
        String feeTxt = view.getFeeTxt().getText().trim();
        String durationTxt = view.getDurationTxt().getText().trim();
        String creatorTxt = view.getCreatorTxt().getText().trim();
        String dayOfCreationTxt = view.getDayOfCreationTxt().getText().trim();
        String descriptionTxt = view.getDescriptionTxt().getText().trim();

        // Kiểm tra các trường không được để trống
        if (code == null || code.trim().isEmpty() ||
                openingDayTxt.isEmpty() || feeTxt.isEmpty() || durationTxt.isEmpty() ||
                creatorTxt.isEmpty() || dayOfCreationTxt.isEmpty() || descriptionTxt.isEmpty()) {

            JOptionPane.showMessageDialog(view.getFrame(), "Lỗi! Vui lòng điền đầy đủ thông tin!", "Thông báo!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra và chuyển đổi feeTxt sang float
        float fee;
        try {
            fee = Float.parseFloat(feeTxt);
            if (fee <= 0) {
                JOptionPane.showMessageDialog(view.getFrame(), "Lỗi! Học phí phải là số dương!", "Thông báo!", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view.getFrame(), "Lỗi! Học phí phải là số thực hợp lệ!", "Thông báo!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra và chuyển đổi durationTxt sang int
        int duration;
        try {
            duration = Integer.parseInt(durationTxt);
            if (duration <= 0) {
                JOptionPane.showMessageDialog(view.getFrame(), "Lỗi! Thời gian học phải là số nguyên dương!", "Thông báo!", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view.getFrame(), "Lỗi! Thời gian học phải là số nguyên hợp lệ!", "Thông báo!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Chuyển đổi openingDayTxt và dayOfCreationTxt sang Date (dd/MM/yyyy)
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false); // Bắt lỗi nhập sai định dạng ngày

        Date openingDate, dayOfCreation;
        try {
            openingDate = dateFormat.parse(openingDayTxt);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view.getFrame(), "Lỗi! Ngày khai giảng phải có định dạng dd/MM/yyyy!", "Thông báo!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            dayOfCreation = dateFormat.parse(dayOfCreationTxt);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view.getFrame(), "Lỗi! Ngày tạo phải có định dạng dd/MM/yyyy!", "Thông báo!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra ngày khai giảng không được trước ngày tạo
        if (openingDate.before(dayOfCreation)) {
            JOptionPane.showMessageDialog(view.getFrame(), "Lỗi! Ngày khai giảng không được trước ngày tạo!", "Thông báo!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true; // Nếu tất cả điều kiện hợp lệ
    }


    private void setNew() {
        // Đặt lại JComboBox về item đầu tiên nếu có dữ liệu
        if (view.getSubjectCb().getItemCount() > 0) {
            view.getSubjectCb().setSelectedIndex(0);
        }

        // Xóa nội dung trong các ô nhập liệu
        view.getOpeningDayTxt().setText("");
        view.getFeeTxt().setText("");
        view.getDurationTxt().setText("");
        view.getCreatorTxt().setText("");
        view.getDayOfCreationTxt().setText("");
        view.getDescriptionTxt().setText("");
    }



}
