package Domain;

import java.util.ArrayList;
import java.util.List;

public class SessionManager {
    private static String userEmail;
    private static List<Product> basket = new ArrayList<>();
    private static double total = 0.0;

    public static String getUserEmail() {
        return userEmail;
    }

    public static void setUserEmail(String email) {
        userEmail = email;
    }

    public static List<Product> getBasket() {
        return basket;
    }

    public static void setBasket(List<Product> newBasket) {
        basket = newBasket;
    }

    public static void addBasket(Product product) {
        basket.add(product);
    }

    public static double getTotal() {
        return total;
    }

    public static void setTotal(double newTotal) {
        total = newTotal;
    }

    public static void clearBasket() {
        basket.clear();
        total = 0.0;
    }
}
