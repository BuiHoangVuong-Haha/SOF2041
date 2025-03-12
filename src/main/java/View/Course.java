package View;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Course {
    private JPanel rootPanel;
    private JTabbedPane tabbedPane1;
    private JTextField openingDayTxt;
    private JTextField feeTxt;
    private JTextField durationTxt;
    private JTextField creatorTxt;
    private JTextField dayOfCreationTxt;
    private JComboBox subjectCb;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnFirst;
    private JButton btnPrevious;
    private JButton btnNext;
    private JButton btnLast;
    private JTextArea descriptionTxt;
    private JButton addButton;
    private JButton newButton;
    private JTable table1;
    private JFrame frame;
    private DefaultTableModel tableModel;

    public Course(){
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
        Image resizedImage = originalIcon.getImage();
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        frame.setIconImage(resizedIcon.getImage());

        createTable();
    }

    public void createTable(){
        tableModel = new DefaultTableModel(
                null,
                new String[]{"Code", "Course ID", "Fees", "Duration", "Opening Day", "Staff Code", "Day Of Creation"}
        );
        table1.setModel(tableModel);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JTabbedPane getTabbedPane1() {
        return tabbedPane1;
    }

    public JTextField getOpeningDayTxt() {
        return openingDayTxt;
    }

    public JTextField getFeeTxt() {
        return feeTxt;
    }

    public JTextField getDurationTxt() {
        return durationTxt;
    }

    public JTextField getCreatorTxt() {
        return creatorTxt;
    }

    public JTextField getDayOfCreationTxt() {
        return dayOfCreationTxt;
    }

    public JComboBox getSubjectCb() {
        return subjectCb;
    }

    public JButton getBtnUpdate() {
        return btnUpdate;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public JButton getBtnFirst() {
        return btnFirst;
    }

    public JButton getBtnPrevious() {
        return btnPrevious;
    }

    public JButton getBtnNext() {
        return btnNext;
    }

    public JButton getBtnLast() {
        return btnLast;
    }

    public JTextArea getDescriptionTxt() {
        return descriptionTxt;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getNewButton() {
        return newButton;
    }

    public JTable getTable1() {
        return table1;
    }

    public JFrame getFrame() {
        return frame;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}
