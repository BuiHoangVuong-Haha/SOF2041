package Controller;

import View.MainInterface;
import View.LoginScreen;
import View.Staffs_Management;
import View.Learner;
import View.Thematic_Study;
import View.Course;
import View.CourseMember_Management;
import View.Statistical_Summary;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Controller_MainInterface {
    private static Controller_MainInterface instance;
    private final MainInterface view;
    private LoginScreen view1;
    private Controller_LoginScreen view1Controller;
    private Staffs_Management staffsView;
    private Controller_Staffs_Management staffsController;
    private Learner learnerView;
    private Controller_Learner learnerController;
    private Thematic_Study thematicView;
    private Controller_Thematic_Study thematicController;
    private Course courseView;
    private Controller_Course_Management courseController;
    private CourseMember_Management cmView;
    private Statistical_Summary stView;


    public Controller_MainInterface(MainInterface view){
        this.view = view;

        //Goi doi tuong cu cho loginScreen
        this.view1Controller = Controller_LoginScreen.getInstance(null);
        this.view1 = this.view1Controller.getView();

        initControl();
    }

    public static Controller_MainInterface getInstance(MainInterface view){
        if (instance == null){
            instance = new Controller_MainInterface(view);
        }
        return instance;
    }

    public MainInterface getView() {
        return view;
    }

    private void initControl(){
        view.getLogOutButton().addActionListener(e -> setLogOut());
        view.getCancelButton().addActionListener(e -> setCancel());
        view.getThematicStudyButton().addActionListener(e -> setThematicStudy());
        view.getStudentsButton1().addActionListener(e -> setStudents1());
        view.getCoursesButton().addActionListener(e -> setCourses());
        view.getStudentsButton().addActionListener(e -> setStudents());
//        view.getInstructionButton().addActionListener(e -> setInstruction());
//
//        view.getLoginItem().addActionListener(e -> setLogin());
        view.getLogoutItem().addActionListener(e -> setLogOut());
        view.getChangePasswordItem().addActionListener(e -> setChangePassword());
        view.getCancelItem().addActionListener(e -> setCancel());

        view.getStudentsItem().addActionListener(e -> setStudents1());
        view.getThematicStudyItem().addActionListener(e -> setThematicStudy());
        view.getCoursesItem().addActionListener(e -> setCourses());
        view.getStManagementItem().addActionListener(e -> setStManagement());


        view.getSbyItem().addActionListener(e -> setSby());
        view.getTranscriptItem().addActionListener(e -> setTranscript());
        view.getC2cTranscriptItem().addActionListener(e -> setC2cTranscript());
        view.getRptItem().addActionListener(e -> setRpt());
//
//        view.getInstructionItem().addActionListener(e -> setInstruction());
//        view.getAboutProductsItem().addActionListener(e -> setAboutProducts());
    }

    private void setCancel(){
        int confirm = JOptionPane.showConfirmDialog(view.getFrame(), "Ban co chac chan muon thoat khong?", "Thong bao", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(confirm == JOptionPane.YES_OPTION){
            view.getFrame().dispose();
        }
    }

    private void setLogOut(){
        int confirm = JOptionPane.showConfirmDialog(view.getFrame(), "Ban co chac chan muon dang xuat ra khong?", "Thong bao", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(confirm == JOptionPane.YES_OPTION){
            view.getFrame().dispose();
            view1.getUsernameField().setText("");
            view1.getPasswordField().setText("");
            view1.getFrame().setVisible(true);
        }
    }

    private void setChangePassword(){
        setStManagement();
    }

    private void setStManagement(){
        view.getFrame().setVisible(false);
        staffsView = new Staffs_Management();
        staffsController = new Controller_Staffs_Management(staffsView);
        staffsView.getFrame().setVisible(true);
        SwingUtilities.invokeLater(() -> {
            staffsView.getFrame().getContentPane().requestFocusInWindow();
        });

        staffsView.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                view.getFrame().setVisible(true);
            }
        });
    }

    private void setStudents1(){
        view.getFrame().setVisible(false);
        learnerView = new Learner();
        learnerController = new Controller_Learner(learnerView);

        SwingUtilities.invokeLater(() -> {
            learnerView.getFrame().setVisible(true);
            learnerController.addCodePlaceHolder();
            learnerController.addDatePlaceHolder();
            learnerController.addEmailPlaceHolder();
            learnerController.addPhoneNumPlaceHolder();
            learnerController.addSearchPlaceHolder();
            learnerView.getFrame().getContentPane().requestFocusInWindow();
        });

        learnerView.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                view.getFrame().setVisible(true);
            }
        });
    }

    private void setThematicStudy(){
        view.getFrame().setVisible(false);
        thematicView = new Thematic_Study();
        thematicController = new Controller_Thematic_Study(thematicView);
        thematicView.getFrame().setVisible(true);
        SwingUtilities.invokeLater(() -> {

            thematicController.addCodePlaceHolder();
            thematicController.addNamePlaceHolder();
            thematicController.addTimePlaceHolder();
            thematicController.addTuitionFeePlaceHolder();
            thematicView.getFrame().getContentPane().requestFocusInWindow();
        });

        thematicView.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                view.getFrame().setVisible(true);
            }
        });
    }

    private void setCourses(){
        view.getFrame().setVisible(false);
        courseView = new Course();
        courseController = new Controller_Course_Management(courseView);
        courseView.getFrame().setVisible(true);

        SwingUtilities.invokeLater(() -> {});

        courseView.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                view.getFrame().setVisible(true);
            }
        });
    }

    private void setStudents(){
        view.getFrame().setVisible(false);
        cmView = new CourseMember_Management();
        new Controller_CourseMember_Management(cmView);
        cmView.getFrame().setVisible(true);

        cmView.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                view.getFrame().setVisible(true);
            }
        });
    }

    private void setStatisticalSummary(int tabIndex){
        view.getFrame().setVisible(false);
        stView = new Statistical_Summary();
        new Controller_Statistical_Summary(stView);
        stView.getFrame().setVisible(true);
        stView.getTabbedPane1().setSelectedIndex(tabIndex);

        stView.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                view.getFrame().setVisible(true);
            }
        });
    }

    private void setSby(){
        setStatisticalSummary(0);
    }

    private void setC2cTranscript(){
        setStatisticalSummary(2);
    }

    private void setTranscript(){
        setStatisticalSummary(1);
    }

    private void setRpt(){
        setStatisticalSummary(3);
    }
}
