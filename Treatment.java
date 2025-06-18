public class Treatment {
    private int treatmentId;
    private String name;
    private String category;
    private double price;

    public Treatment(int treatmentId, String name, String category, double price) {
        this.treatmentId = treatmentId;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public int getTreatmentId() { return treatmentId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
}