package Controller;
import View.LoginScreen;
import View.SplashScreen;
import Controller.Controller_LoginScreen;

import javax.swing.*;

public class Controller_PlashScreen {
    private SplashScreen view_plashScreen;
    private LoginScreen view_loginScreen;
    private Controller_LoginScreen controller_loginScreen;

    private int progressValue = 0;

    public Controller_PlashScreen(SplashScreen view_plashScreen){
        this.view_plashScreen = view_plashScreen;
        view_loginScreen = new LoginScreen();
        controller_loginScreen = Controller_LoginScreen.getInstance(view_loginScreen);

        initControl();
    }

    private void initControl(){
        view_plashScreen.getFrame().setVisible(true);

        Timer timer = new Timer(50, e -> {
            progressValue++;
            view_plashScreen.getProgressBar1().setValue(progressValue);
            if(progressValue >= 100){
                ((Timer) e.getSource()).stop();
                view_plashScreen.getFrame().dispose();
                view_loginScreen.getFrame().setVisible(true);

                SwingUtilities.invokeLater(() -> {
                    controller_loginScreen.addPlaceholderToUsername();
                    view_loginScreen.getFrame().getContentPane().requestFocusInWindow();
                });
            }
        });
        timer.start();
    }
}
