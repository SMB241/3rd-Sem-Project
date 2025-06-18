import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreatmentPanel extends JPanel {
    private MainFrame mainFrame;
    private PatientManager patientManager;
    private Map<Integer, Treatment> treatmentMap = new HashMap<>();
    private List<Integer> selectedTreatmentIds = new ArrayList<>();

    public TreatmentPanel(MainFrame mainFrame, PatientManager patientManager) {
        this.mainFrame = mainFrame;
        this.patientManager = patientManager;
        initializeUI();
        loadTreatments();
    }

    private void loadTreatments() {
        try {
            List<Treatment> treatments = patientManager.getAllTreatments();
            for (Treatment treatment : treatments) {
                treatmentMap.put(treatment.getTreatmentId(), treatment);
            }
        } catch (SQLException e) {
            UIUtils.showError(this, "Failed to load treatments: " + e.getMessage());
        }
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        add(UIUtils.createHeader("Select Treatments"), BorderLayout.NORTH);

        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        checkboxPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        checkboxPanel.setBackground(Color.WHITE);

        try {
            List<Treatment> treatments = patientManager.getAllTreatments();
            for (Treatment treatment : treatments) {
                JCheckBox cb = new JCheckBox(
                    String.format("%s ($%.2f)", treatment.getName(), treatment.getPrice())
                );
                cb.setFont(new Font("SansSerif", Font.PLAIN, 14));
                cb.setBackground(Color.WHITE);
                cb.addActionListener(e -> {
                    if (cb.isSelected()) {
                        selectedTreatmentIds.add(treatment.getTreatmentId());
                    } else {
                        selectedTreatmentIds.remove(Integer.valueOf(treatment.getTreatmentId()));
                    }
                });
                checkboxPanel.add(cb);
                checkboxPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        } catch (SQLException e) {
            checkboxPanel.add(new JLabel("Failed to load treatments"));
        }

        add(new JScrollPane(checkboxPanel), BorderLayout.CENTER);

        JButton backButton = UIUtils.createStyledButton("Back to Dashboard", new Color(0, 102, 204));
        backButton.addActionListener(e -> mainFrame.showPanel("Dashboard"));

        JButton nextButton = UIUtils.createStyledButton("Next", new Color(0, 102, 204));
        nextButton.addActionListener(e -> {
            if (selectedTreatmentIds.isEmpty()) {
                UIUtils.showError(this, "Please select at least one treatment");
                return;
            }

            try {
                patientManager.saveUserTreatments(patientManager.getCurrentPatientId(), selectedTreatmentIds);
                AppointmentPanel appointmentPanel = new AppointmentPanel(mainFrame, patientManager, selectedTreatmentIds);
                mainFrame.addPanel(appointmentPanel, "Appointment");
                mainFrame.showPanel("Appointment");
            } catch (SQLException ex) {
                UIUtils.showError(this, "Failed to save treatments: " + ex.getMessage());
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(backButton);
        bottomPanel.add(nextButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}