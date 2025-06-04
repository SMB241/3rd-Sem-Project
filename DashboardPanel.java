import javax.swing.*;
import java.awt.*;

class DashboardPanel extends JPanel {
    public DashboardPanel(MainFrame frame) {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Dashboard", SwingConstants.CENTER);
        add(label, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(1, 4));
        JButton appointmentBtn = new JButton("Appointments");
        JButton treatmentBtn = new JButton("Treatments");
        JButton billingBtn = new JButton("Billing");
        JButton logoutBtn = new JButton("Logout");

        appointmentBtn.addActionListener(e -> frame.switchPanel("Appointment"));
        treatmentBtn.addActionListener(e -> frame.switchPanel("Treatment"));
        billingBtn.addActionListener(e -> frame.switchPanel("Billing"));
        logoutBtn.addActionListener(e -> frame.switchPanel("Login"));

        buttons.add(appointmentBtn);
        buttons.add(treatmentBtn);
        buttons.add(billingBtn);
        buttons.add(logoutBtn);

        add(buttons, BorderLayout.CENTER);
    }
}