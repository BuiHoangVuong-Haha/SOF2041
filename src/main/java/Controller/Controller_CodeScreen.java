package Controller;

import View.CodeScreen;
import View.LoginScreen;
import View.Register;
import Controller.Controller_Register;
import Controller.Controller_LoginScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class Controller_CodeScreen {
    private CodeScreen view;
    private LoginScreen view1;
    private Register registerView;
    private Controller_LoginScreen view1Controller;

    public Controller_CodeScreen(CodeScreen view){
        this.view = view;

        this.view1Controller = Controller_LoginScreen.getInstance(null);
        this.view1 = this.view1Controller.getView();

        initControl();
        clearText();
    }

    public void initControl(){
        view.getBackButton().addActionListener(e -> setBack());
        view.getNextButton().addActionListener(e -> setNext());
    }

    private void setBack(){
        int confirm = JOptionPane.showConfirmDialog(view.getFrame(), "Ban co muon quay ve lai man hinh dang nhap khong?", "Thong bao", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION){
            view.getFrame().dispose();
            view1.getUsernameField().setText("");
            view1.getPasswordField().setText("");
            view1.getFrame().setVisible(true);
        }
    }

    private void setNext(){
        String code = new String(view.getCodeField().getPassword()).trim();
        if(!code.trim().isEmpty()){
            if(code.equals("abzxyc")){
                JOptionPane.showMessageDialog(view.getFrame(), "Nhap ma thanh cong!", "Thong bao", JOptionPane.INFORMATION_MESSAGE);
                view.getFrame().dispose();
                registerView = new Register();
                new Controller_Register(registerView);
                registerView.getFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(view.getFrame(), "Loi! Ma dang nhap cua ban bi sai, xin vui long kiem tra lai!", "Thong bao", JOptionPane.ERROR_MESSAGE);
                clearText();
            }
        } else {
            JOptionPane.showMessageDialog(view.getFrame(), "Loi! Ban phai dien day du thong tin!", "Thong bao", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearText(){
        view.getCodeField().setText("");
    }

}
