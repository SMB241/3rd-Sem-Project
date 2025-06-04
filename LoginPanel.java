
import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    public LoginPanel(MainFrame mainFrame, PatientManager manager) {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(40, 100, 20, 100));
        form.setBackground(new Color(240, 248, 255));

        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        form.add(new JLabel("Email:"));
        form.add(emailField);
        form.add(new JLabel("Password:"));
        form.add(passwordField);

        JButton loginBtn = new JButton("Login");
        JButton signUpBtn = new JButton("Sign Up");

        loginBtn.addActionListener(e -> {
            if (manager.login(emailField.getText(), new String(passwordField.getPassword()))) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                mainFrame.switchPanel("Dashboard");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        signUpBtn.addActionListener(e -> mainFrame.switchPanel("SignUp"));

        form.add(signUpBtn);
        form.add(loginBtn);
        add(form, BorderLayout.CENTER);
    }
}