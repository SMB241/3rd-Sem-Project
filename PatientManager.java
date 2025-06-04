// PatientManager.java
import java.sql.*;

public class PatientManager {
    private int currentPatientId = -1;

    public boolean login(String email, String password) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT patient_id FROM patients WHERE email = ? AND password = ?")) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                currentPatientId = rs.getInt("patient_id");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getCurrentPatientId() {
        return currentPatientId;
    }

    public void logout() {
        currentPatientId = -1;
    }
}