package Controller;
import View.Register;
import View.LoginScreen;
import Repository.JDBC_Register;
import Model.Model_Staffs_Management;
import Controller.Controller_LoginScreen;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.text.View;

public class Controller_Register {
    private Register view;
    private JDBC_Register jdbc;
    private LoginScreen view1;
    private Controller_LoginScreen view1Controller;
    private List<Model_Staffs_Management> staffsList;

    public Controller_Register(Register view){
        this.view = view;

        this.view1Controller = Controller_LoginScreen.getInstance(null);
        this.view1 = this.view1Controller.getView();

        jdbc = new JDBC_Register();
        staffsList = new ArrayList<>();

        initControl();
        clearText();
    }

    private void initControl(){
        view.getRegisterButton().addActionListener(e -> setRegister());
        view.getCancelButton().addActionListener(e -> setCancel());
    }

    private void clearText(){
        view.getNameField().setText("");
        view.getButtonGroup1().clearSelection();
        view.getUsernameField().setText("");
        view.getPasswordField().setText("");
        view.getCfPasswordField().setText("");
    }

    private void setCancel(){
        int confirm = JOptionPane.showConfirmDialog(view.getFrame(), "Ban co muon thoat khong?", "Thong bao", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(confirm == JOptionPane.YES_OPTION){
            view.getFrame().dispose();
            view1.getUsernameField().setText("");
            view1.getPasswordField().setText("");
            view1.getFrame().setVisible(true);
        }
    }

    private void setRegister(){

        String name = view.getNameField().getText().trim();
        String role = view.getManagerRadioButton().isSelected() ? "Quan ly" : view.getEmployeeRadioButton().isSelected() ? "Nhan vien" : null;
        String username = view.getUsernameField().getText().trim();
        String password = new String(view.getPasswordField().getPassword()).trim();
        String cfPassword = new String(view.getCfPasswordField().getPassword()).trim();

        if (!name.trim().isEmpty() && !role.trim().isEmpty() && !username.trim().isEmpty() && !password.trim().isEmpty() && !cfPassword.trim().isEmpty()){
            if(cfPassword.trim().equals(password)){
                Model_Staffs_Management staff = new Model_Staffs_Management();
                staff.setName(name);
                staff.setRole(role);
                staff.setCode(username);
                staff.setPassword(cfPassword);

                staffsList.add(staff);

                jdbc.mergeStaffs(staffsList);

                JOptionPane.showMessageDialog(view.getFrame(), "Dang ky thanh cong!", "Thong bao", JOptionPane.INFORMATION_MESSAGE);
                view.getFrame().dispose();
                view1.getFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(view.getFrame(), "Loi! Mat khau phai trung voi o mat khau xac nhan!", "Thong bao", JOptionPane.ERROR_MESSAGE);
                view.getCfPasswordField().setText("");
            }
        }
        else {
            JOptionPane.showMessageDialog(view.getFrame(), "Loi! Cac o khong duoc de trong!", "Thong bao", JOptionPane.ERROR_MESSAGE);
            clearText();
        }
    }
}
