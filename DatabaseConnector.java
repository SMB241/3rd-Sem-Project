import java.sql.*;

public class DatabaseConnector {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/dental_clinic";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void initializeDatabase() throws SQLException {
        try (Connection conn = getConnection()) {
            Statement stmt = conn.createStatement();
            
            // Create patients table
            stmt.execute("CREATE TABLE IF NOT EXISTS patients (" +
                "patient_id INT AUTO_INCREMENT PRIMARY KEY," +
                "first_name VARCHAR(50) NOT NULL," +
                "last_name VARCHAR(50) NOT NULL," +
                "email VARCHAR(100) UNIQUE NOT NULL," +
                "password VARCHAR(255) NOT NULL," +
                "age INT," +
                "dob DATE," +
                "sex VARCHAR(10)," +
                "contact VARCHAR(20)," +
                "status VARCHAR(20)," +
                "address TEXT)");

            // Create treatments table
            stmt.execute("CREATE TABLE IF NOT EXISTS treatments (" +
                "treatment_id INT PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL," +
                "category VARCHAR(50) NOT NULL," +
                "price DECIMAL(10,2) NOT NULL)");

            // Create appointments table
            stmt.execute("CREATE TABLE IF NOT EXISTS appointments (" +
                "appointment_id INT AUTO_INCREMENT PRIMARY KEY," +
                "patient_id INT NOT NULL," +
                "treatment_id INT NOT NULL," +
                "appointment_date DATE NOT NULL," +
                "is_paid BOOLEAN DEFAULT FALSE," +
                "FOREIGN KEY (patient_id) REFERENCES patients(patient_id)," +
                "FOREIGN KEY (treatment_id) REFERENCES treatments(treatment_id))");

            stmt.execute("CREATE TABLE IF NOT EXISTS user_treatments (" +
            "patient_id INT NOT NULL," +
            "treatment_id INT NOT NULL," +
            "treatment_name VARCHAR(100) NOT NULL," +  // New column
            "treatment_price DECIMAL(10,2) NOT NULL," + // New column
            "PRIMARY KEY (patient_id, treatment_id)," +
            "FOREIGN KEY (patient_id) REFERENCES patients(patient_id)," +
            "FOREIGN KEY (treatment_id) REFERENCES treatments(treatment_id))");

            // Insert default treatments
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM treatments");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO treatments (treatment_id, name, category, price) VALUES " +
                    "(1, 'Dental Consultation', 'Preventive', 50.00)," +
                    "(2, 'Teeth Cleaning', 'Preventive', 80.00)," +
                    "(3, 'Dental Fillings', 'Restorative', 120.00)," +
                    "(4, 'Root Canal Treatment', 'Restorative', 200.00)," +
                    "(5, 'Teeth Whitening', 'Cosmetic', 150.00)");
            }
        }
    }
}