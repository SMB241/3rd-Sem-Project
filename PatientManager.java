import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PatientManager {
    private int currentPatientId = -1;
    private String currentPatientName = "";

    public boolean login(String email, String password) throws SQLException {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "SELECT patient_id, first_name FROM patients WHERE email = ? AND password = ?")) {
            
            stmt.setString(1, email);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                currentPatientId = rs.getInt("patient_id");
                currentPatientName = rs.getString("first_name");
                return true;
            }
            return false;
        }
    }

    public void registerPatient(Patient patient) throws SQLException {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO patients (first_name, last_name, email, password, age, dob, sex, contact, status, address) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            
            stmt.setString(1, patient.getFirstName());
            stmt.setString(2, patient.getLastName());
            stmt.setString(3, patient.getEmail());
            stmt.setString(4, patient.getPassword());
            stmt.setObject(5, patient.getAge(), Types.INTEGER);
            stmt.setDate(6, patient.getDob() != null ? Date.valueOf(patient.getDob()) : null);
            stmt.setString(7, patient.getSex());
            stmt.setString(8, patient.getContact());
            stmt.setString(9, patient.getStatus());
            stmt.setString(10, patient.getAddress());
            
            stmt.executeUpdate();
        }
    }

    public List<Treatment> getAllTreatments() throws SQLException {
        List<Treatment> treatments = new ArrayList<>();
        
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM treatments ORDER BY treatment_id")) {
            
            while (rs.next()) {
                treatments.add(new Treatment(
                    rs.getInt("treatment_id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price")
                ));
            }
        }
        return treatments;
    }

    public List<Treatment> getTreatmentsByIds(List<Integer> treatmentIds) throws SQLException {
        List<Treatment> treatments = new ArrayList<>();
        if (treatmentIds.isEmpty()) return treatments;
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT * FROM treatments WHERE treatment_id IN (" + 
                 treatmentIds.stream().map(String::valueOf)
                     .collect(Collectors.joining(",")) + ")")) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                treatments.add(new Treatment(
                    rs.getInt("treatment_id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price")
                ));
            }
        }
        return treatments;
    }

    public void createAppointment(int patientId, int treatmentId, Date appointmentDate) throws SQLException {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO appointments (patient_id, treatment_id, appointment_date) VALUES (?, ?, ?)")) {
            
            stmt.setInt(1, patientId);
            stmt.setInt(2, treatmentId);
            stmt.setDate(3, appointmentDate);
            
            stmt.executeUpdate();
        }
    }

        public void saveUserTreatments(int patientId, List<Integer> treatmentIds) throws SQLException {
        try (Connection conn = DatabaseConnector.getConnection()) {
            // First clear existing selections
            try (PreparedStatement clearStmt = conn.prepareStatement(
                "DELETE FROM user_treatments WHERE patient_id = ?")) {
                clearStmt.setInt(1, patientId);
                clearStmt.executeUpdate();
            }

            // Insert new selections with name and price
            try (PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO user_treatments (patient_id, treatment_id, treatment_name, treatment_price) " +
                "VALUES (?, ?, (SELECT name FROM treatments WHERE treatment_id = ?), " +
                "(SELECT price FROM treatments WHERE treatment_id = ?))")) {
                
                for (int treatmentId : treatmentIds) {
                    insertStmt.setInt(1, patientId);
                    insertStmt.setInt(2, treatmentId);
                    insertStmt.setInt(3, treatmentId);
                    insertStmt.setInt(4, treatmentId);
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }
        }
    }

        public double getTreatmentPrice(int treatmentId) throws SQLException {
        try (Connection conn = DatabaseConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT price FROM treatments WHERE treatment_id = ?")) {
            stmt.setInt(1, treatmentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price");
            }
        }
        return 0.0; // or throw an exception
    }

    public List<Appointment> getPatientAppointments(int patientId) throws SQLException {
    List<Appointment> appointments = new ArrayList<>();
    
        try (Connection conn = DatabaseConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT a.*, t.name as treatment_name FROM appointments a " +
                "JOIN treatments t ON a.treatment_id = t.treatment_id " +
                "WHERE a.patient_id = ? ORDER BY a.appointment_date")) {
            
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(new Appointment(
                    rs.getInt("appointment_id"),
                    rs.getInt("patient_id"),
                    rs.getInt("treatment_id"),
                    rs.getDate("appointment_date"),
                    rs.getBoolean("is_paid"),
                    rs.getString("treatment_name")
                ));
            }
        }
        return appointments;
    }

        public void markAppointmentAsPaid(int appointmentId) throws SQLException {
        try (Connection conn = DatabaseConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE appointments SET is_paid = TRUE WHERE appointment_id = ?")) {
            stmt.setInt(1, appointmentId);
            stmt.executeUpdate();
        }
    }

    public int getCurrentPatientId() {
        return currentPatientId;
    }

    public String getCurrentPatientName() {
        return currentPatientName;
    }

    public void logout() {
        currentPatientId = -1;
        currentPatientName = "";
    }
}