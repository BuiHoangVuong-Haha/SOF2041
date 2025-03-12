package View;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Statistical_Summary {
    private JPanel rootPanel;
    private JTabbedPane tabbedPane1;
    private JTable studentTbl;
    private DefaultTableModel stuTblModl;
    private JTable transcriptTbl;
    private DefaultTableModel transTblModl;
    private JTable gradeSummaryTbl;
    private DefaultTableModel gradeTblModl;
    private JTable revenueTbl;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private DefaultTableModel revenueTblModl;
    private JFrame frame;

    public Statistical_Summary() {

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
        Image resizedImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        frame.setIconImage(resizedIcon.getImage());

        createStuTbl();
        createTransTbl();
        createGradeTbl();
        createRevenueTbl();
    }

    public void createStuTbl(){
        stuTblModl = new DefaultTableModel(new String[]{"Year", "Number", "Beginning Day", "Last Day"}, 0);
        studentTbl.setModel(stuTblModl);
    }

    public void createTransTbl(){
        transTblModl = new DefaultTableModel(new String[]{"Code", "Name", "Mark", "Grading"},0);
        transcriptTbl.setModel(transTblModl);
    }

    public void createGradeTbl(){
        gradeTblModl = new DefaultTableModel(new String[]{"Thematic Study", "Total Student", "Lowest", "Highest", "Average"},0);
        gradeSummaryTbl.setModel(gradeTblModl);
    }

    public void createRevenueTbl(){
        revenueTblModl = new DefaultTableModel(new String[]{"Thematic Study", "Course Number", "Number", "Revenue", "Highest", "Lowest", "Average"},0);
        revenueTbl.setModel(revenueTblModl);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JTabbedPane getTabbedPane1() {
        return tabbedPane1;
    }

    public JTable getStudentTbl() {
        return studentTbl;
    }

    public DefaultTableModel getStuTblModl() {
        return stuTblModl;
    }

    public JTable getTranscriptTbl() {
        return transcriptTbl;
    }

    public DefaultTableModel getTransTblModl() {
        return transTblModl;
    }

    public JTable getGradeSummaryTbl() {
        return gradeSummaryTbl;
    }

    public DefaultTableModel getGradeTblModl() {
        return gradeTblModl;
    }

    public JTable getRevenueTbl() {
        return revenueTbl;
    }

    public DefaultTableModel getRevenueTblModl() {
        return revenueTblModl;
    }

    public JFrame getFrame() {
        return frame;
    }

    public JComboBox getComboBox1() {
        return comboBox1;
    }

    public JComboBox getComboBox2() {
        return comboBox2;
    }
}
