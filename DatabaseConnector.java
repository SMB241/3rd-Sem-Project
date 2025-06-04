// DatabaseConnector.java
import java.sql.*;
import javax.swing.*;

public class DatabaseConnector {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/dental_clinic";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS patients (patient_id INT AUTO_INCREMENT PRIMARY KEY, first_name VARCHAR(50), last_name VARCHAR(50), age INT, gender VARCHAR(10), address VARCHAR(255), phone VARCHAR(15), email VARCHAR(100), password VARCHAR(100))");
            stmt.execute("CREATE TABLE IF NOT EXISTS treatments (treatment_id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100), cost DECIMAL(10,2))");
            stmt.execute("CREATE TABLE IF NOT EXISTS appointments (appointment_id INT AUTO_INCREMENT PRIMARY KEY, patient_id INT, treatment_id INT, date DATE, time TIME, FOREIGN KEY (patient_id) REFERENCES patients(patient_id), FOREIGN KEY (treatment_id) REFERENCES treatments(treatment_id))");
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM treatments");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO treatments (name, cost) VALUES ('Cleaning', 500.00), ('Filling', 1500.00), ('Extraction', 1000.00)");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database initialization failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
} 