package View;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Staffs_Management {
    private JPanel rootPanel;
    private JTabbedPane tabbedPane1;
    private JTextField codeTField;
    private JTextField nameTField;
    private JRadioButton managerRadioButton;
    private JRadioButton employeeRadioButton;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton newButton;
    private JTabbedPane tabbedPane2;
    private JButton firstBtn;
    private JButton previousBtn;
    private JButton nextBtn;
    private JButton lastBtn;
    private JTable table1;
    private JScrollPane scrollPane;
    private JPasswordField passwordField;
    private JPasswordField cfPasswordField;
    private JPanel updatePanel;
    private DefaultTableModel tableModel;
    private final ButtonGroup buttonGroup1;
    private final JFrame frame;
    private final static Logger log = Logger.getLogger(Staffs_Management.class.getName());

    public Staffs_Management(){

        try {
            UIManager.setLookAndFeel(new FlatLightLaf()); // Giao diện hiện đại
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame();
        frame.setTitle("EMPLOYEE MANAGEMENT");
        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        frame.setLocation(x, y);

        buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(managerRadioButton);
        buttonGroup1.add(employeeRadioButton);

        ImageIcon originalIcon = new ImageIcon("src/Icons/fpt.png");
        Image resizedImage = originalIcon.getImage().getScaledInstance(100,100,Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        frame.setIconImage(resizedIcon.getImage());

        createTable();
    }

    private void createTable(){
        tableModel = new DefaultTableModel(
                new String[]{"Code", "Password", "Name", "Role"}, 0
        );
        table1.setModel(tableModel);
        table1.setDefaultEditor(Object.class, null);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JTabbedPane getTabbedPane1() {
        return tabbedPane1;
    }

    public JTextField getCodeTField() {
        return codeTField;
    }

    public JTextField getNameTField() {
        return nameTField;
    }

    public JRadioButton getManagerRadioButton() {
        return managerRadioButton;
    }

    public JRadioButton getEmployeeRadioButton() {
        return employeeRadioButton;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getUpdateButton() {
        return updateButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getNewButton() {
        return newButton;
    }

    public JTabbedPane getTabbedPane2() {
        return tabbedPane2;
    }

    public JButton getFirstBtn() {
        return firstBtn;
    }

    public JButton getPreviousBtn() {
        return previousBtn;
    }

    public JButton getNextBtn() {
        return nextBtn;
    }

    public JButton getLastBtn() {
        return lastBtn;
    }

    public JTable getTable1() {
        return table1;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JPasswordField getCfPasswordField() {
        return cfPasswordField;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public ButtonGroup getButtonGroup1() {
        return buttonGroup1;
    }

    public JFrame getFrame() {
        return frame;
    }
}
