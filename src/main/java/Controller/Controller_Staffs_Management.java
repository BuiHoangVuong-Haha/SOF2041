package Controller;

import Model.Model_Staffs_Management;
import Repository.JDBC_Staffs_Management;
import View.Staffs_Management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller_Staffs_Management {
    private Staffs_Management view;
    private final JDBC_Staffs_Management jdbc;
    private List<Model_Staffs_Management> staffsList;
    private static final Logger log = Logger.getLogger(Controller_Staffs_Management.class.getName());
    private String originalCode;
    private int current = -1;

    public Controller_Staffs_Management(Staffs_Management view){
        this.view = view;
        this.jdbc = new JDBC_Staffs_Management();
        this.staffsList = new ArrayList<>();

        loadData();
        initControl();
        clearText();
    }

    private void initControl(){
        view.getAddButton().addActionListener(e -> setAdd());
        view.getNewButton().addActionListener(e -> setNew());
        view.getUpdateButton().addActionListener(e -> setUpdate());
        view.getDeleteButton().addActionListener(e -> setDelete());
        view.getFirstBtn().addActionListener(e -> setFirst());
        view.getPreviousBtn().addActionListener(e -> setPrevious());
        view.getNextBtn().addActionListener(e -> setNext());
        view.getLastBtn().addActionListener(e -> setLast());

        view.getTable1().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    tableRowDoubleClick();
                }
            }
        });
    }

    private void loadData(){
        staffsList = jdbc.getAllStaffs();
        if(staffsList.isEmpty()){
            JOptionPane.showMessageDialog(view.getFrame(), "Loi! Chuong trinh khong the tai danh sach tu DB!", "Thong bao", JOptionPane.ERROR_MESSAGE);
            return;
        }
        loadTable();
    }

    private void loadTable(){
        view.getTableModel().setRowCount(0);
        for(Model_Staffs_Management staff : staffsList){
            view.getTableModel().addRow(new Object[]{
                    staff.getCode(),
                    staff.getPassword(),
                    staff.getName(),
                    staff.getRole()
            });
        }
    }

    private void clearText(){
        view.getCodeTField().setText("");
        view.getPasswordField().setText("");
        view.getCfPasswordField().setText("");
        view.getButtonGroup1().clearSelection();
        view.getNameTField().setText("");
    }

    private void setNew(){
        clearText();
    }

    private void setAdd(){

        if(!setCheckErrors()){
            return;
        }

        String code = view.getCodeTField().getText().trim();
        String password = new String(view.getPasswordField().getPassword()).trim();
        String cfPassword = new String(view.getCfPasswordField().getPassword()).trim();
        String name = view.getNameTField().getText().trim();
        String role = view.getManagerRadioButton().isSelected() ? "Quan ly" : view.getEmployeeRadioButton().isSelected() ? "Nhan vien" : null;

        if(cfPassword.equals(password)){
            Model_Staffs_Management staff = new Model_Staffs_Management();
            staff.setCode(code);
            staff.setPassword(cfPassword);
            staff.setRole(role);
            staff.setName(name);

            staffsList.add(staff);

            jdbc.mergeStaffs(staffsList, null);
            JOptionPane.showMessageDialog(view.getFrame(), "Them moi thanh cong!", "Thong bao", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearText();
        }
    }

    private boolean setCheckErrors(){
        String code = view.getCodeTField().getText().trim();
        String password = new String(view.getPasswordField().getPassword()).trim();
        String cfPassword = new String(view.getCfPasswordField().getPassword()).trim();
        String name = view.getNameTField().getText().trim();

        if(code.isEmpty()){
            JOptionPane.showMessageDialog(view.getFrame(),"Ban chua nhap ma nhan vien!", "Loi!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(password.isEmpty()){
            JOptionPane.showMessageDialog(view.getFrame(), "Ban chua nhap mat khau!", "Loi!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(cfPassword.isEmpty()){
            JOptionPane.showMessageDialog(view.getFrame(), "Ban chua nhap xac nhan mat khau!", "Loi!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(name.isEmpty()){
            JOptionPane.showMessageDialog(view.getFrame(), "Ban chua nhap ten!", "Loi!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(!cfPassword.equals(password)){
            JOptionPane.showMessageDialog(view.getFrame(), "Xac nhan mat khau phai khop voi mat khau", "Thong bao", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(!view.getManagerRadioButton().isSelected() && !view.getEmployeeRadioButton().isSelected()){
            JOptionPane.showMessageDialog(view.getFrame(), "Vui long chon day du thong tin!", "Thong bao", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    //Click 2 times to the table
    private void tableRowDoubleClick(){
        int selectedRow = view.getTable1().getSelectedRow();
        if(selectedRow < 0){
            return;
        }

        //Convert to modelIndex
        int modelRow = view.getTable1().convertRowIndexToModel(selectedRow);

        //Get value from table
        String code = view.getTableModel().getValueAt(modelRow, 0).toString();
        String password = view.getTableModel().getValueAt(modelRow, 1).toString();
        String name = view.getTableModel().getValueAt(modelRow, 2).toString();
        String role = view.getTableModel().getValueAt(modelRow, 3).toString();

        //Gan cho bien toan cuc de kiem tra o phan Update
        originalCode = code;

        //Display value to the field
        view.getCodeTField().setText(code);
        view.getPasswordField().setText(password);
        view.getCfPasswordField().setText(password);
        view.getNameTField().setText(name);
        if(role.equals("Quan ly")){
            view.getManagerRadioButton().setSelected(true);
        } else {
            view.getEmployeeRadioButton().setSelected(true);
        }

        //Move to the FieldText
        view.getTabbedPane1().setSelectedIndex(0);
    }

    private void display(int current){

        if (current < 0 || current >= view.getTable1().getRowCount()) return;

        // Cập nhật current thành giá trị model tương ứng
        this.current = view.getTable1().convertRowIndexToModel(current);

        //Get value from table
        String code = view.getTableModel().getValueAt(current, 0).toString();
        String password = view.getTableModel().getValueAt(current, 1).toString();
        String name = view.getTableModel().getValueAt(current, 2).toString();
        String role = view.getTableModel().getValueAt(current, 3).toString();

        //Display value to the field
        view.getCodeTField().setText(code);
        view.getPasswordField().setText(password);
        view.getCfPasswordField().setText(password);
        view.getNameTField().setText(name);
        if(role.equals("Quan ly")){
            view.getManagerRadioButton().setSelected(true);
        } else {
            view.getEmployeeRadioButton().setSelected(true);
        }
    }

    private void setDelete(){
        if(!setCheckErrors()){
            return;
        }

        String code = view.getCodeTField().getText().trim();
        jdbc.mergeStaffs(staffsList, code);
        JOptionPane.showMessageDialog(view.getFrame(),"Xoa du lieu thanh cong!", "Thong bao", JOptionPane.INFORMATION_MESSAGE);
        loadData();
        clearText();
    }

    private void setUpdate(){

        //Check Errors
        if(!setCheckErrors()){
            return;
        }

        //Warning
        JOptionPane.showMessageDialog(view.getFrame(), "Luu y! Ban se khong the thay doi Ma Nhan Vien!", "Luu y!", JOptionPane.INFORMATION_MESSAGE);

        //Kiem tra xem nguoi dung co thay doi MaNV khong!
        String code = view.getCodeTField().getText().trim();
        if(!code.equals(originalCode)){
            JOptionPane.showMessageDialog(view.getFrame(), "Loi! Ban khong the thay doi Ma Nhan Vien!", "Thong bao", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String password = new String(view.getPasswordField().getPassword()).trim().toString();
        String cfPassword = new String(view.getCfPasswordField().getPassword()).trim().toString();
        String name = view.getNameTField().getText().trim();
        String role = view.getManagerRadioButton().isSelected() ? "Quan ly" : view.getEmployeeRadioButton().isSelected() ? "Nhan vien" : null;

        boolean update = false;

        for(Model_Staffs_Management staff : staffsList){
            if(staff.getCode().equals(code)){
                staff.setPassword(password);
                staff.setName(name);
                staff.setRole(role);
                update = true;
                break;
            }
        }

        if(!update){
            log.log(Level.SEVERE, "Loi khong tim thay Ma Nhan Vien de cap nhat!");
            return;
        }

        jdbc.mergeStaffs(staffsList, null);
        JOptionPane.showMessageDialog(view.getFrame(), "Cap nhat du lieu thanh cong!", "Thong bao", JOptionPane.INFORMATION_MESSAGE);
        loadData();
        clearText();
    }

    private void setFirst(){
        if (view.getTableModel().getRowCount() > 0) { // Kiểm tra nếu bảng có dữ liệu
            current = 0;
            display(current);
        } else {
            JOptionPane.showMessageDialog(view.getFrame(), "Không có dữ liệu để hiển thị!", "Lỗi", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void setPrevious(){
        if (view.getTableModel().getRowCount() > 0) { // Kiểm tra nếu bảng có dữ liệu
            if(current > 0){
                current--;
                display(current);
            }
        } else {
            JOptionPane.showMessageDialog(view.getFrame(), "Không có dữ liệu để hiển thị!", "Lỗi", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void setNext(){
        if (view.getTableModel().getRowCount() > 0) { // Kiểm tra nếu bảng có dữ liệu
            if(current > 0){
                current++;
                display(current);
            }
        } else {
            JOptionPane.showMessageDialog(view.getFrame(), "Không có dữ liệu để hiển thị!", "Lỗi", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void setLast(){
        if (view.getTableModel().getRowCount() > 0) { // Kiểm tra nếu bảng có dữ liệu
            current = staffsList.size() - 1;
            display(current);

        } else {
            JOptionPane.showMessageDialog(view.getFrame(), "Không có dữ liệu để hiển thị!", "Lỗi", JOptionPane.WARNING_MESSAGE);
        }
    }

}
