package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Learner {
    private JPanel rootPanel;
    private JTabbedPane tabbedPane1;
    private JTextField codeField;
    private JTextField birthField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextArea descriptionArea;
    private JTextField nameField;
    private JRadioButton maleRbt;
    private JRadioButton femaleRbt;
    private JTable table1;
    private JTextField searchField;
    private JButton searchButton;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton newButton;
    private JButton button5;
    private JButton button6;
    private JButton button7;
    private JButton button8;
    private DefaultTableModel tableModel;
    private JFrame frame;
    private ButtonGroup buttonGroup1;

    public Learner(){
        frame = new JFrame();
        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

        buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(maleRbt);
        buttonGroup1.add(femaleRbt);

        createTable();
    }

    public void createTable(){
        tableModel = new DefaultTableModel(
                new String[]{"Code", "Name", "Birthday", "Gender", "Phone Number", "Email", "StaffCode", "Register Day"}, 0
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

    public JTextField getCodeField() {
        return codeField;
    }

    public JTextField getBirthField() {
        return birthField;
    }

    public JTextField getEmailField() {
        return emailField;
    }

    public JTextField getPhoneField() {
        return phoneField;
    }

    public JTextArea getDescriptionArea() {
        return descriptionArea;
    }

    public JTextField getNameField() {
        return nameField;
    }

    public JRadioButton getMaleRbt() {
        return maleRbt;
    }

    public JRadioButton getFemaleRbt() {
        return femaleRbt;
    }

    public JTable getTable1() {
        return table1;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JButton getSearchButton() {
        return searchButton;
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

    public JButton getButton5() {
        return button5;
    }

    public JButton getButton6() {
        return button6;
    }

    public JButton getButton7() {
        return button7;
    }

    public JButton getButton8() {
        return button8;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JFrame getFrame() {
        return frame;
    }

    public ButtonGroup getButtonGroup1() {
        return buttonGroup1;
    }
}
