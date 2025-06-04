import javax.swing.*;
import java.awt.*;

class BillingPanel extends JPanel {
    public BillingPanel(MainFrame frame) {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Billing", SwingConstants.CENTER);
        JButton backBtn = new JButton("Back to Dashboard");
        backBtn.addActionListener(e -> frame.switchPanel("Dashboard"));
        add(label, BorderLayout.NORTH);
        add(backBtn, BorderLayout.SOUTH);
    }
}
