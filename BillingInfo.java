import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BillingInfo {
    private String invoiceNumber;
    private Date invoiceDate;
    private Date dueDate;
    private List<BillingItem> items;
    private double taxRate;
    private double amountPaid;
    private boolean isPaid;

    public BillingInfo(String invoiceNumber, Date invoiceDate, Date dueDate, double taxRate) {
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.dueDate = dueDate;
        this.taxRate = taxRate;
        this.items = new ArrayList<>();
        this.amountPaid = 0;
        this.isPaid = false;
    }

    // Add a billing item
    public void addItem(String description, double amount) {
        items.add(new BillingItem(description, amount));
    }

    // Record a payment
    public void recordPayment(double amount) {
        this.amountPaid += amount;
        if (this.amountPaid >= getTotal()) {
            this.isPaid = true;
        }
    }

    // Calculate subtotal
    public double getSubtotal() {
        return items.stream().mapToDouble(BillingItem::getAmount).sum();
    }

    // Calculate tax
    public double getTax() {
        return getSubtotal() * taxRate;
    }

    // Calculate total
    public double getTotal() {
        return getSubtotal() + getTax();
    }

    // Calculate balance due
    public double getBalanceDue() {
        return getTotal() - amountPaid;
    }

    // Getters and setters
    public String getInvoiceNumber() { return invoiceNumber; }
    public Date getInvoiceDate() { return invoiceDate; }
    public Date getDueDate() { return dueDate; }
    public List<BillingItem> getItems() { return items; }
    public double getTaxRate() { return taxRate; }
    public double getAmountPaid() { return amountPaid; }
    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }

    // Billing item nested class
    public static class BillingItem {
        private String description;
        private double amount;

        public BillingItem(String description, double amount) {
            this.description = description;
            this.amount = amount;
        }

        public String getDescription() { return description; }
        public double getAmount() { return amount; }
    }
}
