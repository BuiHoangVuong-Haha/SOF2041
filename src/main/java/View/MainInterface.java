package View;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainInterface {
    private JPanel rootPanel;
    private JLabel timeLbl;
    private JButton logOutButton;
    private JButton cancelButton;
    private JButton thematicStudyButton;
    private JButton coursesButton;
    private JButton instructionButton;
    private JMenuItem loginItem;
    private JMenuItem logoutItem;
    private JMenuItem changePasswordItem;
    private JMenuItem cancelItem;
    private JMenuItem studentsItem;
    private JMenuItem thematicStudyItem;
    private JMenuItem coursesItem;
    private JMenuItem stManagementItem;
    private JMenuItem sbyItem;
    private JMenuItem transcriptItem;
    private JMenuItem c2cTranscriptItem;
    private JMenuItem rptItem;
    private JMenuItem instructionItem;
    private JMenuItem aboutProductsItem;
    private JButton studentsButton1;
    private JButton studentsButton;
    private JFrame frame;

    public MainInterface(){

        try {
            UIManager.setLookAndFeel(new FlatLightLaf()); // Giao diện hiện đại
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame();
        frame.setTitle("Management System");
        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        frame.setLocation(x,y);

        ImageIcon originalIcon = new ImageIcon("src/Icons/fpt.png");
        Image resizedImage = originalIcon.getImage().getScaledInstance(100,100,Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        frame.setIconImage(resizedIcon.getImage());

        Timer timer = new Timer(1000, e -> {
            timeLbl.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        });
        timer.start();

        acclerator();
    }

    private void acclerator(){
        loginItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
        logoutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        cancelItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0));

        instructionItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JLabel getTimeLbl() {
        return timeLbl;
    }

    public JButton getLogOutButton() {
        return logOutButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JButton getThematicStudyButton() {
        return thematicStudyButton;
    }

    public JButton getCoursesButton() {
        return coursesButton;
    }

    public JButton getInstructionButton() {
        return instructionButton;
    }

    public JMenuItem getLoginItem() {
        return loginItem;
    }

    public JMenuItem getLogoutItem() {
        return logoutItem;
    }

    public JMenuItem getChangePasswordItem() {
        return changePasswordItem;
    }

    public JMenuItem getCancelItem() {
        return cancelItem;
    }

    public JMenuItem getStudentsItem() {
        return studentsItem;
    }

    public JMenuItem getThematicStudyItem() {
        return thematicStudyItem;
    }

    public JMenuItem getCoursesItem() {
        return coursesItem;
    }

    public JMenuItem getStManagementItem() {
        return stManagementItem;
    }

    public JMenuItem getSbyItem() {
        return sbyItem;
    }

    public JMenuItem getTranscriptItem() {
        return transcriptItem;
    }

    public JMenuItem getC2cTranscriptItem() {
        return c2cTranscriptItem;
    }

    public JMenuItem getRptItem() {
        return rptItem;
    }

    public JMenuItem getInstructionItem() {
        return instructionItem;
    }

    public JMenuItem getAboutProductsItem() {
        return aboutProductsItem;
    }

    public JButton getStudentsButton1() {
        return studentsButton1;
    }

    public JButton getStudentsButton() {
        return studentsButton;
    }

    public JFrame getFrame() {
        return frame;
    }
}
