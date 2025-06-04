import javax.swing.*;
import java.awt.*;

// SignUpPanel.java
public class SignUpPanel extends JPanel {
    public SignUpPanel(MainFrame frame) {
        setLayout(new GridBagLayout());
        JLabel label = new JLabel("Sign Up");
        JButton backBtn = new JButton("Back to Login");
        backBtn.addActionListener(e -> frame.switchPanel("Login"));
        add(label);
        add(backBtn);
    }
}
