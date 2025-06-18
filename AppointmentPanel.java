import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import com.toedter.calendar.JCalendar;

public class AppointmentPanel extends JPanel {
    private MainFrame mainFrame;
    private PatientManager patientManager;
    private List<Integer> treatmentIds;
    private JCalendar jCalendar;

    public AppointmentPanel(MainFrame mainFrame, PatientManager patientManager, List<Integer> treatmentIds) {
        this.mainFrame = mainFrame;
        this.patientManager = patientManager;
        this.treatmentIds = treatmentIds;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        add(createHeader("Schedule Appointment"), BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Calendar panel
        JPanel calendarPanel = new JPanel();
        calendarPanel.setBackground(Color.WHITE);
        jCalendar = new JCalendar();
        jCalendar.setWeekOfYearVisible(false);
        jCalendar.setPreferredSize(new Dimension(500, 350));
        calendarPanel.add(jCalendar);
        contentPanel.add(calendarPanel, BorderLayout.CENTER);

        // Treatment summary
        JTextArea summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        summaryArea.setBackground(Color.WHITE);
        
        try {
            List<Treatment> treatments = patientManager.getTreatmentsByIds(treatmentIds);
            StringBuilder summary = new StringBuilder("Selected Treatments:\n");
            for (Treatment t : treatments) {
                summary.append(String.format("- %s ($%.2f)\n", t.getName(), t.getPrice()));
            }
            summaryArea.setText(summary.toString());
        } catch (SQLException e) {
            summaryArea.setText("Could not load treatment details");
        }

        contentPanel.add(new JScrollPane(summaryArea), BorderLayout.SOUTH);
        add(contentPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        JButton backButton = new JButton("Back");
        styleButton(backButton);
        backButton.addActionListener(e -> mainFrame.showPanel("Treatments"));

        JButton confirmButton = new JButton("Confirm Appointment");
        styleButton(confirmButton);
        confirmButton.addActionListener(e -> confirmAppointment());

        buttonPanel.add(backButton);
        buttonPanel.add(confirmButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void confirmAppointment() {
        Date selectedDate = new Date(jCalendar.getDate().getTime());

        try {
            for (Integer treatmentId : treatmentIds) {
                patientManager.createAppointment(
                    patientManager.getCurrentPatientId(),
                    treatmentId,
                    selectedDate
                );
            }

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
            JOptionPane.showMessageDialog(this, 
                String.format("Appointment confirmed for:\n%s\n\n%d treatment(s) scheduled",
                    sdf.format(selectedDate),
                    treatmentIds.size()),
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            mainFrame.showPanel("Dashboard");
        } catch (SQLException e) {
            UIUtils.showError(this, "Failed to create appointments: " + e.getMessage());
        }
    }

    private JLabel createHeader(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        return label;
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
    }
}