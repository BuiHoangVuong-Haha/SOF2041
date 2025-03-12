package View;
import View.LoginScreen;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class SplashScreen {
    private JPanel rootPanel;
    private JProgressBar progressBar1;
    private Timer timer;
    private int progressValue = 0;
    private JFrame frame;


    public SplashScreen(){

        try {
            UIManager.setLookAndFeel(new FlatLightLaf()); // Giao diện hiện đại
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame();
        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        frame.setLocation(x, y);

        ImageIcon originalIcon = new ImageIcon("src/Icons/fpt.png");
        Image resizedImage = originalIcon.getImage().getScaledInstance(100,100, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        frame.setIconImage(resizedIcon.getImage());
    }

    public JFrame getFrame() {
        return frame;
    }

    public JProgressBar getProgressBar1() {
        return progressBar1;
    }
}
