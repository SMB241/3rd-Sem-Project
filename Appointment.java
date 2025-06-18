import java.sql.Date;

public class Appointment {
    private int appointmentId;
    private int patientId;
    private int treatmentId;
    private Date appointmentDate;
    private boolean isPaid;
    private String treatmentName;

    public Appointment(int appointmentId, int patientId, int treatmentId, 
                      Date appointmentDate, boolean isPaid, String treatmentName) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.treatmentId = treatmentId;
        this.appointmentDate = appointmentDate;
        this.isPaid = isPaid;
        this.treatmentName = treatmentName;
    }

    // Getters
    public int getAppointmentId() { return appointmentId; }
    public int getPatientId() { return patientId; }
    public int getTreatmentId() { return treatmentId; }
    public Date getAppointmentDate() { return appointmentDate; }
    public boolean isPaid() { return isPaid; }
    public String getTreatmentName() { return treatmentName; }
}