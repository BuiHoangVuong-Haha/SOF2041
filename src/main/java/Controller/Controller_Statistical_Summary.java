package Controller;

import View.Statistical_Summary;
import Model.Model_Statistical_Summary.*;
import Repository.JDBC_Statistical_Summary;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class Controller_Statistical_Summary {
    private Statistical_Summary view;
    private JDBC_Statistical_Summary jdbc;
    private List<Student> stuList;
    private List<Transcript> transList;
    private List<Grade_Summary> gradeList;
    private List<Revenue> revenueList;
    private List<Transcript_Combobox> transComList;

    public Controller_Statistical_Summary(Statistical_Summary view) {
        this.view = view;
        jdbc = new JDBC_Statistical_Summary();
        stuList = new ArrayList<>();
        transList = new ArrayList<>();
        gradeList = new ArrayList<>();
        revenueList = new ArrayList<>();

        loadData();
    }

    private void loadData(){
        loadStuData();
        loadGSData();
        loadTransData();
        loadReData();
    }

    //Fill table student
    private void loadStuData(){
        stuList = jdbc.getProcStudent();
        view.getStuTblModl().setRowCount(0);
        for(Student stu : stuList){
            view.getStuTblModl().addRow(new Object[]{
                    stu.getYear(),
                    stu.getNumber(),
                    stu.getFirst(),
                    stu.getLast()
            });
        }
    }


    //Fill void loadGradeSummaryData
    private void loadGSData(){
        gradeList = jdbc.getProcGradeSummary();
        view.getGradeTblModl().setRowCount(0);
        for(Grade_Summary grade : gradeList){
            view.getGradeTblModl().addRow(new Object[]{
                    grade.getThematic(),
                    grade.getTotal(),
                    grade.getLowest(),
                    grade.getHighest(),
                    grade.getAvg()
            });
        }
    }


    //Fill table Transcript
    //Fill ComboBox transcript
    private void loadTransComboBox(){
        transComList = jdbc.getCDTranscript();

        JComboBox<Transcript_Combobox> comboBox = view.getComboBox1();
        comboBox.removeAllItems();
        comboBox.setSelectedIndex(-1);

        for(Transcript_Combobox trans : transComList){
            comboBox.addItem(trans); // Hiển thị theo định dạng đã override
        }
    }

    //Fill Table transcript
    private void loadTransData() {
        loadTransComboBox(); // Load dữ liệu vào comboBox

        JComboBox<Transcript_Combobox> comboBox = view.getComboBox1();

        // Thêm ActionListener để cập nhật dữ liệu khi chọn item
        comboBox.addActionListener(e -> {
            Transcript_Combobox selectedTrans = (Transcript_Combobox) comboBox.getSelectedItem();

            if (selectedTrans != null) {
                String maCD = selectedTrans.getThematicCode();
                transList = jdbc.getBangDiem(maCD);

                // Xóa dữ liệu cũ và thêm dữ liệu mới vào bảng
                view.getTransTblModl().setRowCount(0);
                for (Transcript trans : transList) {
                    view.getTransTblModl().addRow(new Object[]{
                            trans.getStuCode(),
                            trans.getName(),
                            trans.getMark(),
                            trans.getGrading()
                    });
                }
            }
        });

        // Đặt sau khi đã load dữ liệu vào comboBox
        if (comboBox.getItemCount() > 0) {
            comboBox.setSelectedIndex(0);  // Chọn mục đầu tiên
            comboBox.dispatchEvent(new ActionEvent(comboBox, ActionEvent.ACTION_PERFORMED, "")); // Gọi sự kiện
        }
    }


    //Revenue
    //get year -> DB
    private void loadReCombobox(){
        List<Integer> reComList = jdbc.getYearRevenue();
        JComboBox<Integer> comboBox = view.getComboBox2();
        comboBox.removeAllItems();
        comboBox.setSelectedIndex(-1);

        for(int reCom : reComList){
            comboBox.addItem(reCom);
        }
    }

    //Fill table revenue
    private void loadReData() {
        loadReCombobox();
        JComboBox<Integer> comboBox = view.getComboBox2();

        comboBox.addActionListener(e -> {

            Integer selectedTrans = (Integer) comboBox.getSelectedItem(); // Dùng Integer thay vì int

            if (selectedTrans != null) { // Kiểm tra null trước khi sử dụng
                revenueList = jdbc.getProcRevenue(selectedTrans);
                view.getRevenueTblModl().setRowCount(0);

                for (Revenue re : revenueList) {
                    view.getRevenueTblModl().addRow(new Object[]{
                            re.getThematic(),
                            re.getCourseNumber(),
                            re.getStuNumber(),
                            re.getFeeTotal(),
                            re.getFeeHighest(),
                            re.getFeeLowest(),
                            re.getFeeAvg()
                    });
                }
            }
        });

        if(comboBox.getItemCount() > 0){
            comboBox.setSelectedIndex(0);
            comboBox.dispatchEvent(new ActionEvent(comboBox, ActionEvent.ACTION_PERFORMED,""));
        }
    }

}
