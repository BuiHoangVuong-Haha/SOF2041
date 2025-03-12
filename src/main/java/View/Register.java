package View;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class Register {
    private JPanel rootPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton cancelButton;
    private JPasswordField cfPasswordField;
    private JRadioButton managerRadioButton;
    private JRadioButton employeeRadioButton;
    private JTextField nameField;
    private JFrame frame;
    private ButtonGroup buttonGroup1;

    public Register(){

        try {
            UIManager.setLookAndFeel(new FlatLightLaf()); // Giao diện hiện đại
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame();
        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(managerRadioButton);
        buttonGroup1.add(employeeRadioButton);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        frame.setLocation(x,y);

        ImageIcon originalIcon = new ImageIcon("src/Icons/fpt.png");
        Image resizedImage = originalIcon.getImage().getScaledInstance(100,100,Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        frame.setIconImage(resizedIcon.getImage());
    }

    public JFrame getFrame() {
        return frame;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JPasswordField getCfPasswordField() {
        return cfPasswordField;
    }

    public JTextField getNameField() {
        return nameField;
    }

    public JRadioButton getManagerRadioButton() {
        return managerRadioButton;
    }

    public JRadioButton getEmployeeRadioButton() {
        return employeeRadioButton;
    }

    public ButtonGroup getButtonGroup1() {
        return buttonGroup1;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JButton getRegisterButton() {
        return registerButton;
    }
}
