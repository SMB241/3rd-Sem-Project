import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private PatientManager patientManager;

    public MainFrame() {
        initializeUI();
        patientManager = new PatientManager();
        setupPanels();
    }

    private void initializeUI() {
        setTitle("Dental Clinic Management");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);
    }

    private void setupPanels() {
        mainPanel.add(new LoginPanel(this, patientManager), "Login");
        mainPanel.add(new SignUpPanel(this, patientManager), "SignUp");
        mainPanel.add(new DashboardPanel(this), "Dashboard");
        mainPanel.add(new TreatmentPanel(this, patientManager), "Treatments");
        mainPanel.add(new BillingPanel(this, patientManager), "Billing");
        
        showPanel("Login");
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    public void addPanel(JPanel panel, String name) {
        mainPanel.add(panel, name);
        cardLayout.show(mainPanel, name);
    }

    public PatientManager getPatientManager() {
        return patientManager;
    }
}