package Domain;

public class Order {
    private int id;
    private String date;
    private double total;
    private int customerId;

    public Order(int id, String date, double total, int customerId) {
        this.id = id;
        this.date = date;
        this.total = total;
        this.customerId = customerId;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public double getTotal() {
        return total;
    }

    public int getCustomerId() {
        return customerId;
    }
}
