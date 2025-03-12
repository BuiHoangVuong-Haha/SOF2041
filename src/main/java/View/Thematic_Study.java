package View;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Thematic_Study {
    private JPanel rootPanel;
    private JTabbedPane tabbedPane1;
    private JTextArea discriptionArea;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton newButton;
    private JButton firstBtn;
    private JButton previousBtn;
    private JButton nextBtn;
    private JButton lastBtn;
    private JTextField codeField;
    private JTextField nameField;
    private JTextField timeField;
    private JTextField tuitionFeeField;
    private JLabel pictureLbl;
    private JTable table1;
    private DefaultTableModel tableModel;
    private JFrame frame;

    public Thematic_Study(){
        try {
            UIManager.setLookAndFeel(new FlatLightLaf()); // Giao diện hiện đại
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        Image resizedImage = originalIcon.getImage().getScaledInstance(100,100, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        frame.setIconImage(resizedIcon.getImage());

        createTable();
    }

    public void createTable(){
        tableModel = new DefaultTableModel( new String[]{"Code", "Name", "Tuition Fee", "Time", "Picture", "Describe"},0);
        table1.setModel(tableModel);

        table1.setDefaultEditor(Object.class, null);
    }

    public JTextField getCodeField() {
        return codeField;
    }

    public JTextField getNameField() {
        return nameField;
    }

    public JTextField getTimeField() {
        return timeField;
    }

    public JTextField getTuitionFeeField() {
        return tuitionFeeField;
    }

    public JTextArea getDiscriptionArea() {
        return discriptionArea;
    }

    public JTable getTable1() {
        return table1;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JButton getNewButton() {
        return newButton;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JLabel getPictureLbl() {
        return pictureLbl;
    }

    public JTabbedPane getTabbedPane1() {
        return tabbedPane1;
    }

    public JFrame getFrame() {
        return frame;
    }
}
