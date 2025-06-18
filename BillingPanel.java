import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class BillingPanel extends JPanel {
    private PatientManager patientManager;
    private JTextArea billingInfo;

    public BillingPanel(MainFrame mainFrame, PatientManager patientManager) {
        this.patientManager = patientManager;
        initializeUI(mainFrame);
    }

    private void initializeUI(MainFrame mainFrame) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        add(UIUtils.createHeader("Billing & Payment"), BorderLayout.NORTH);

        billingInfo = new JTextArea();
        billingInfo.setEditable(false);
        billingInfo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        refreshAppointments();

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        content.add(new JScrollPane(billingInfo), BorderLayout.CENTER);

        JButton refreshButton = UIUtils.createStyledButton("Refresh", new Color(0, 102, 204));
        refreshButton.addActionListener(e -> refreshAppointments());

        JButton payButton = UIUtils.createStyledButton("Mark as Paid", new Color(0, 150, 0));
        payButton.addActionListener(e -> markAsPaid());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(refreshButton);
        buttonPanel.add(payButton);
        content.add(buttonPanel, BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);

        JButton backButton = UIUtils.createStyledButton("Back to Dashboard", new Color(0, 102, 204));
        backButton.addActionListener(e -> mainFrame.showPanel("Dashboard"));

        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);
        bottom.add(backButton);
        add(bottom, BorderLayout.SOUTH);
    }

    private void refreshAppointments() {
        try {
            List<Appointment> appointments = patientManager.getPatientAppointments(
                patientManager.getCurrentPatientId());
            
            StringBuilder sb = new StringBuilder();
            sb.append("Your Appointments and Charges:\n\n");
            
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
            double totalDue = 0.0;
            
            for (Appointment appt : appointments) {
                double price = patientManager.getTreatmentPrice(appt.getTreatmentId());
                if (!appt.isPaid()) {
                    totalDue += price;
                }
                
                sb.append("ID: ").append(appt.getAppointmentId())
                  .append(" - ").append(sdf.format(appt.getAppointmentDate()))
                  .append(" - ").append(appt.getTreatmentName())
                  .append(" ($").append(String.format("%.2f", price)).append(")")
                  .append(appt.isPaid() ? " (Paid)" : " (Due)")
                  .append("\n");
            }
            
            sb.append("\nTotal Amount Due: $").append(String.format("%.2f", totalDue));
            billingInfo.setText(sb.toString());
        } catch (SQLException e) {
            billingInfo.setText("Error loading appointments: " + e.getMessage());
        }
    }

    private void markAsPaid() {
        // Implement payment logic here
        JOptionPane.showMessageDialog(this, 
            "Payment functionality will be implemented here",
            "Payment", JOptionPane.INFORMATION_MESSAGE);
    }
}