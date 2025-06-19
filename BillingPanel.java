import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

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
        billingInfo.setMargin(new Insets(10, 10, 10, 10));
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
                if ("not_paid".equalsIgnoreCase(appt.isPaid())) { 
                    totalDue += price;
                }
                
                sb.append("ID: ").append(appt.getAppointmentId())
                  .append(" - ").append(sdf.format(appt.getAppointmentDate()))
                  .append(" - ").append(appt.getTreatmentName())
                  .append(" ($").append(String.format("%.2f", price)).append(")")
                  .append(" - Status: ").append(appt.isPaid().equalsIgnoreCase("paid") ? "Paid" : "Due")
                  .append("\n\n");
            }
            
            sb.append("\nTotal Amount Due: $").append(String.format("%.2f", totalDue));
            billingInfo.setText(sb.toString());
        } catch (SQLException e) {
            billingInfo.setText("Error loading appointments: " + e.getMessage());
        }
    }

    private void markAsPaid() {
    try {
        List<Appointment> appointments = patientManager.getPatientAppointments(
            patientManager.getCurrentPatientId());
        
        Appointment unpaidAppt = null;
        for (Appointment a : appointments) {
            if ("not_paid".equalsIgnoreCase(a.isPaid())) {
                unpaidAppt = a;
                break;
            }
        }
        
        if (unpaidAppt == null) {
            JOptionPane.showMessageDialog(this, 
                "No unpaid appointments found",
                "Payment", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Confirm payment
        int confirm = JOptionPane.showConfirmDialog(this,
            "Mark appointment #" + unpaidAppt.getAppointmentId() + 
            " (" + unpaidAppt.getTreatmentName() + ") as paid?",
            "Confirm Payment",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            patientManager.markAppointmentAsPaid(unpaidAppt.getAppointmentId());
            refreshAppointments();
            JOptionPane.showMessageDialog(this, 
                "Appointment marked as paid",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this,
            "Error updating payment: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
}