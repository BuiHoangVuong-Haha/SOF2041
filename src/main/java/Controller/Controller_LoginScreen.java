package Controller;

import View.LoginScreen;
import View.MainInterface;
import Controller.Controller_MainInterface;
import Repository.JDBC_Staffs_Management;
import View.CodeScreen;
import Controller.Controller_CodeScreen;
import Model.Model_Staffs_Management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Controller_LoginScreen {
    private static Controller_LoginScreen instance;

    private LoginScreen view;
    private MainInterface view1;
    private CodeScreen codeView;
    private JDBC_Staffs_Management jdbc;
    private List<Model_Staffs_Management> staffsList;
    private String loggedInUsername;

    public Controller_LoginScreen(LoginScreen view) {
        this.view = view;
        this.jdbc = new JDBC_Staffs_Management();
        this.staffsList = new ArrayList<>();

        initControl();
        addPlaceholderToUsername();
        clearText();
    }

    public static Controller_LoginScreen getInstance(LoginScreen view) {
        if (instance == null) {
            instance = new Controller_LoginScreen(view);
        }
        return instance;
    }

    public LoginScreen getView() {
        return view;
    }

    public void initControl() {
        view.getLoginButton().addActionListener(e -> setLogin());
        view.getCancelButton().addActionListener(e -> setCancel());
        view.getRegisterButton().addActionListener(e -> setRegister());
    }

    private void setLogin() {
        String username = view.getUsernameField().getText().trim();
        String password = new String(view.getPasswordField().getPassword()).trim();
        staffsList = jdbc.getAllStaffs();

        boolean found = false;

        if(username.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(view.getFrame(), "Loi! Vui long dien day du thong tin!", "Thong bao", JOptionPane.ERROR_MESSAGE);
        } else {
            for (Model_Staffs_Management staff : staffsList) {
                if (username.equals(staff.getCode())) {
                    found = true;
                    if (password.equals(staff.getPassword())) {
                        //Luu username sau khi dang nhap
                        loggedInUsername = username;
                        JOptionPane.showMessageDialog(view.getFrame(), "Dang nhap thanh cong", "Thong bao", JOptionPane.INFORMATION_MESSAGE);
                        view.getFrame().setVisible(false);
                        view1 = new MainInterface();
                        new Controller_MainInterface(view1);
                        view1.getFrame().setVisible(true);
                        return;
                    } else {
                        JOptionPane.showMessageDialog(view.getFrame(), "Mat khau khong dung, xin vui long kiem tra !", "Loi!", JOptionPane.ERROR_MESSAGE);
                        view.getPasswordField().setText("");
                        return;
                    }
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(view.getFrame(), "Ten dang nhap khong dung, vui long kiem tra lai!", "Loi!", JOptionPane.ERROR_MESSAGE);
                clearText();
            }
        }
    }


    private void setCancel() {
        int confirm = JOptionPane.showConfirmDialog(view.getFrame(), "Ban co chac chan muon thoat khong?", "Thong bao", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            view.getFrame().dispose();
        }
    }

    private void clearText() {
        view.getUsernameField().setText("");
        view.getPasswordField().setText("");
    }

    private void setRegister() {
        int confirm = JOptionPane.showConfirmDialog(view.getFrame(), "Ban co chac chan muon dang ky khong?", "Thong bao", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            view.getFrame().setVisible(false);
            codeView = new CodeScreen();
            new Controller_CodeScreen(codeView);
            codeView.getFrame().setVisible(true);
        } else {
            clearText();
        }
    }

    public void addPlaceholderToUsername() {
        JTextField username = view.getUsernameField();
        String placeholder = "Nhap ten dang nhap...";

        // Set initial placeholder style
        username.setText(placeholder);
        username.setForeground(Color.GRAY);
        username.setFont(username.getFont().deriveFont(Font.ITALIC));

        // Add Mouse Listenner
//        username.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (username.getText().equals(placeholder)){
//                    username.setText("");
//                    username.setForeground(Color.BLACK);
//                    username.setFont(username.getFont().deriveFont(Font.PLAIN));
//                }
//            }
//        });

        // Xu ly o mat Focus
        username.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e){
                if(username.getText().equals(placeholder)){
                    username.setText("");
                    username.setForeground(Color.BLACK);
                    username.setFont(username.getFont().deriveFont(Font.PLAIN));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(username.getText().isEmpty()){
                    username.setText(placeholder);
                    username.setForeground(Color.GRAY);
                    username.setFont(username.getFont().deriveFont(Font.ITALIC));
                }
            }
        });
    }

    //Tao phuong thuc getter cho username de co the duoc truy cap
    public String getLoggedInUsername() {
        return loggedInUsername;
    }
}
