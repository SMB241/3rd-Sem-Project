import javax.swing.*;
import java.awt.*;

class TreatmentPanel extends JPanel {
    public TreatmentPanel(MainFrame frame) {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Treatments", SwingConstants.CENTER);
        JButton backBtn = new JButton("Back to Dashboard");
        backBtn.addActionListener(e -> frame.switchPanel("Dashboard"));
        add(label, BorderLayout.NORTH);
        add(backBtn, BorderLayout.SOUTH);
    }
}