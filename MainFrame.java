import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private HashMap<String, JPanel> panels;

    public MainFrame() {
        setTitle("Dental Clinic Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        panels = new HashMap<>();

        // Initialize and store panels
        panels.put("SignUp", new SignUpPanel(this));
        panels.put("Dashboard", new DashboardPanel(this));
        panels.put("Appointment", new AppointmentPanel(this));
        panels.put("Treatment", new TreatmentPanel(this));
        panels.put("Billing", new BillingPanel(this));
        panels.put("Login", new JPanel()); // Replace with your actual LoginPanel

        for (String key : panels.keySet()) {
            mainPanel.add(panels.get(key), key);
        }

        add(mainPanel);
        switchPanel("Login"); // Start on Login
        setVisible(true);
    }

    public void switchPanel(String name) {
        cardLayout.show(mainPanel, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
