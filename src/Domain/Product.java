package Domain;

public class Product {
    private final int id;
    private String name;
    private double price;
    private int categoryId;
    public static Boolean isPurchasedByMe;

    public Product(int id, String name, double price, int categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getCategory() {
        return categoryId;
    }

    @Override
    public String toString() {
        return name + " - $" + price;
    }


    public static int getPopularity(Object o) {
        return 0;
    }

    public static boolean isPurchasedByMe(Object o) {
        return isPurchasedByMe;
    }


}
