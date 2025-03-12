package View;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CourseMember_Management {
    private JComboBox thematicCb;
    private JTextField searchTxt;
    private JTable courseMemTbl;
    private JButton updateBtn;
    private JPanel rootPanel;
    private JComboBox courseCb;
    private JTabbedPane tabbedPane1;
    private JTable studentTbl;
    private JButton deleteBtn;
    private DefaultTableModel courseMemTblMdl;
    private DefaultTableModel stuTblMdl;
    private JFrame frame;

    public CourseMember_Management() {

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

        createCourseMemTable();
        createStuTable();
    }

    public void createCourseMemTable(){
        String[] columnName = {"Ordinal Number", "Course Member Code", "Student Code", "Name", "Mark"};
        courseMemTblMdl = new DefaultTableModel(null, columnName){
            @Override
            public Class<?> getColumnClass(int columnIndex){
                return columnIndex == 5 ? Boolean.class : super.getColumnClass(columnIndex);
            }
        };
        courseMemTbl.setModel(courseMemTblMdl);
    }

    public void createStuTable(){
        stuTblMdl = new DefaultTableModel(
                new String[]{"Student Code", "Name", "Gender", "Birth", "Phone Number", "Email"}, 0
        );
        studentTbl.setModel(stuTblMdl);
    }

    public JComboBox getThematicCb() {
        return thematicCb;
    }

    public JTextField getSearchTxt() {
        return searchTxt;
    }

    public JTable getCourseMemTbl() {
        return courseMemTbl;
    }

    public JButton getUpdateBtn() {
        return updateBtn;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JComboBox getCourseCb() {
        return courseCb;
    }

    public JTabbedPane getTabbedPane1() {
        return tabbedPane1;
    }

    public JTable getStudentTbl() {
        return studentTbl;
    }

    public JButton getDeleteBtn() {
        return deleteBtn;
    }

    public DefaultTableModel getCourseMemTblMdl() {
        return courseMemTblMdl;
    }

    public DefaultTableModel getStuTblMdl() {
        return stuTblMdl;
    }

    public JFrame getFrame() {
        return frame;
    }
}
