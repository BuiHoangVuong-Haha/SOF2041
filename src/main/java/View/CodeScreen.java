package View;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class CodeScreen {
    private JPanel rootPanel;
    private JPasswordField codeField;
    private JButton nextButton;
    private JButton backButton;
    private JFrame frame;

    public CodeScreen(){

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
        frame.setLocation(x,y);

        ImageIcon orginalIcon = new ImageIcon("src/Icons/fpt.png");
        Image resizedImage = orginalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        frame.setIconImage(resizedIcon.getImage());
    }

    public JFrame getFrame() {
        return frame;
    }

    public JPasswordField getCodeField() {
        return codeField;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public JButton getNextButton() {
        return nextButton;
    }
}
