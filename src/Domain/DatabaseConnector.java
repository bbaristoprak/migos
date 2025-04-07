package Domain;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseConnector {
    private final String url = "jdbc:mysql://localhost:3306/supermarket_db";
    private final String user = "root";
    private final String password = "";

    public void connectAndShowTables() {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection successful!");

            String query = "SHOW TABLES";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            System.out.println("Tables:");
            while (resultSet.next()) {
                System.out.println("- " + resultSet.getString(1));
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean insertCustomer(String name, String surname, String email, String password) {
        try {

            Connection connection = DriverManager.getConnection(url, user, this.password);
            String query =
            """
            INSERT INTO Customer (Name, Surname, Email, Password)
            VALUES (?, ?, ?, ?)
            """;

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password);

            int rowsInserted = preparedStatement.executeUpdate();

            connection.close();

            if (rowsInserted > 0) {
                System.out.println("Customer inserted successfully!");
                return true;
            } else {
                System.out.println("Failed to insert customer.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean validateLogin(String email, String password) {
        try (Connection connection = DriverManager.getConnection(url, user, this.password)) {
            String query =

            """
            SELECT *
            FROM Customer
            WHERE Email = ? AND Password = ?
           """;

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, email);
                statement.setString(2, password);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }



    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT ProductID, Name, Price, CategoryID FROM Product WHERE Stock_Quantity > 0";


        try (Connection connection = DriverManager.getConnection(url, user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("ProductID");
                String name = resultSet.getString("Name");
                double price = resultSet.getDouble("Price");
                int categoryId = resultSet.getInt("CategoryID");

                products.add(new Product(id, name, price, categoryId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT P.ProductID, P.Name, P.Price, P.CategoryID FROM Product P, Category C WHERE P.CategoryID = C.CategoryID AND Category_Name = ?";

        try (Connection connection = DriverManager.getConnection(url, user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, category);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("ProductID");
                    String name = resultSet.getString("Name");
                    double price = resultSet.getDouble("Price");
                    int categoryId = resultSet.getInt("CategoryID");

                    products.add(new Product(id, name, price, categoryId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> getProductsByName(String name) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT P.ProductID, P.Name, P.Price, P.CategoryID FROM Product P WHERE P.Name LIKE ?";

        try (Connection connection = DriverManager.getConnection(url, user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, "%" + name + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("ProductID");
                    String productName = resultSet.getString("Name");
                    double price = resultSet.getDouble("Price");
                    int categoryId = resultSet.getInt("CategoryID");

                    products.add(new Product(id, productName, price, categoryId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    public int getCustomerID(String email) {
        int customerId = -1;
        String query = "SELECT CustomerID FROM Customer WHERE Email = ?";

        try (Connection connection = DriverManager.getConnection(url, user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    customerId = resultSet.getInt("CustomerID");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customerId;
    }


    public List<Order> getOrdersByCustomerId(int customerId) {
        List<Order> orders = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection(url, user, this.password);
            String query =
                    """
                    SELECT OrderID, Order_Date, Total_Price
                    FROM Orders
                    WHERE CustomerID = ?
                    """;

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, customerId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int orderId = resultSet.getInt("OrderID");
                String  orderDate = resultSet.getString("Order_Date");
                double totalPrice = resultSet.getDouble("Total_Price");


                Order order = new Order(orderId, orderDate, totalPrice, customerId);
                orders.add(order);
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }
    public List<String> getAvailableCategories() {
        List<String> categories = new ArrayList<>();
        String query = "SELECT Category_Name FROM Category";

        try (Connection connection = DriverManager.getConnection(url, user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                categories.add(resultSet.getString("Category_Name"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }

    public List<String> getAvailableBrands() {
        List<String> brands = new ArrayList<>();
        String query = "SELECT Name FROM Brand";

        try (Connection connection = DriverManager.getConnection(url, user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                brands.add(resultSet.getString("Name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return brands;
    }

    public List<Product> getProductsByBrand(String brand) {
        List<Product> products = new ArrayList<>();
        String query = """
                   SELECT P.ProductID, P.Name, P.Price, P.CategoryID 
                   FROM Product P
                   JOIN Brand B ON P.BrandID = B.BrandID
                   WHERE B.Name = ?
                   """;

        try (Connection connection = DriverManager.getConnection(url, user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, brand);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("ProductID");
                    String name = resultSet.getString("Name");
                    double price = resultSet.getDouble("Price");
                    int categoryId = resultSet.getInt("CategoryID");

                    products.add(new Product(id, name, price, categoryId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        List<Product> products = new ArrayList<>();
        String query = """
                   SELECT P.ProductID, P.Name, P.Price, P.CategoryID 
                   FROM Product P
                   JOIN Brand B ON P.BrandID = B.BrandID
                   JOIN Category C ON P.CategoryID = C.CategoryID
                   WHERE C.Category_Name = ? AND B.Name = ?
                   """;

        try (Connection connection = DriverManager.getConnection(url, user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, category);
            preparedStatement.setString(2, brand);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("ProductID");
                    String name = resultSet.getString("Name");
                    double price = resultSet.getDouble("Price");
                    int categoryId = resultSet.getInt("CategoryID");

                    products.add(new Product(id, name, price, categoryId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }



    public HashMap<Integer, Integer> getProductPurchaseCounts() {
        HashMap<Integer, Integer> purchaseCounts = new HashMap<>();
        String query = """
                   SELECT ProductID, SUM(Quantity) AS TotalQuantity
                   FROM Order_Item
                   GROUP BY ProductID
                   """;

        try (Connection connection = DriverManager.getConnection(url, user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int productId = resultSet.getInt("ProductID");
                int totalQuantity = resultSet.getInt("TotalQuantity");
                purchaseCounts.put(productId, totalQuantity); // Store product ID and total purchase count
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return purchaseCounts;
    }


    public List<Product> getProductsSortedByPopularity() {
        List<Product> products = new ArrayList<>();
        String query = """
                   SELECT P.ProductID, P.Name, P.Price, P.CategoryID, 
                          COALESCE(SUM(OI.Quantity), 0) AS TotalQuantity
                   FROM Product P
                   LEFT JOIN Order_Item OI ON P.ProductID = OI.ProductID
                   GROUP BY P.ProductID, P.Name, P.Price, P.CategoryID
                   ORDER BY TotalQuantity DESC
                   """;

        try (Connection connection = DriverManager.getConnection(url, user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("ProductID");
                String name = resultSet.getString("Name");
                double price = resultSet.getDouble("Price");
                int categoryId = resultSet.getInt("CategoryID");

                products.add(new Product(id, name, price, categoryId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }
    public boolean insertOrderByEmail(String email, double totalPrice) {
        String getCustomerIdQuery = "SELECT CustomerID FROM Customer WHERE Email = ?";
        String insertOrderQuery =
                """
                INSERT INTO Orders (Order_Date, Total_Price, CustomerID)
                VALUES (CURDATE(), ?, ?)
                """;
        String getLastOrderIdQuery = "SELECT LAST_INSERT_ID()";

        try (Connection connection = DriverManager.getConnection(url, user, this.password);
             PreparedStatement getCustomerStmt = connection.prepareStatement(getCustomerIdQuery);
             PreparedStatement insertOrderStmt = connection.prepareStatement(insertOrderQuery);
             PreparedStatement lastOrderIdStmt = connection.prepareStatement(getLastOrderIdQuery)) {


            getCustomerStmt.setString(1, email);
            ResultSet resultSet = getCustomerStmt.executeQuery();

            if (resultSet.next()) {
                int customerId = resultSet.getInt("CustomerID");


                insertOrderStmt.setDouble(1, totalPrice);
                insertOrderStmt.setInt(2, customerId);
                int rowsInserted = insertOrderStmt.executeUpdate();

                if (rowsInserted > 0) {

                    ResultSet orderIdResult = lastOrderIdStmt.executeQuery();
                    if (orderIdResult.next()) {
                        int orderId = orderIdResult.getInt(1);


                        if (insertOrderItems(orderId)) {
                            System.out.println("Order placed successfully for customer: " + email);
                            return true;
                        } else {
                            System.out.println("Failed to insert order items.");
                        }
                    }
                }
            } else {
                System.out.println("Customer not found for email: " + email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean insertOrderItems(int orderId) {
        String insertOrderItemQuery =
                """
                INSERT INTO Order_Item (Quantity, OrderID, ProductID)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DriverManager.getConnection(url, user, this.password);
             PreparedStatement insertOrderItemStmt = connection.prepareStatement(insertOrderItemQuery)) {

            Map<Integer, Integer> productQuantities = new HashMap<>();
            for (Product product : SessionManager.getBasket()) {
                productQuantities.put(product.getId(), productQuantities.getOrDefault(product.getId(), 0) + 1);
            }

            for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
                int productId = entry.getKey();
                int quantity = entry.getValue();

                insertOrderItemStmt.setInt(1, quantity);
                insertOrderItemStmt.setInt(2, orderId);
                insertOrderItemStmt.setInt(3, productId);

                insertOrderItemStmt.addBatch();
            }

            int[] rowsInserted = insertOrderItemStmt.executeBatch();

            return rowsInserted.length > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<String> getOrderItems(int orderId) {
        List<String> items = new ArrayList<>();
        String query = """
            SELECT p.Name, oi.Quantity 
            FROM Order_Item oi
            JOIN Product p ON oi.ProductID = p.ProductID
            WHERE oi.OrderID = ?
            """;

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String productName = rs.getString("Name");
                int quantity = rs.getInt("Quantity");
                items.add(productName + " (x" + quantity + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }


    public List<Product> getProductsPurchasedByMe(){
        List<Product> purchasedProducts = new ArrayList<>();
        String query = """
            SELECT DISTINCT p.ProductID, p.Name, p.Price, p.CategoryID
            FROM Product p
            JOIN Order_Item oi ON p.ProductID = oi.ProductID
            JOIN Orders o ON oi.OrderID = o.OrderID
            JOIN Customer c ON o.CustomerID = c.CustomerID
            WHERE c.Email = ?
            """;

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, SessionManager.getUserEmail());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("ProductID");
                String productName = rs.getString("Name");
                double productPrice = rs.getDouble("Price");
                int categoryId = rs.getInt("CategoryID");

                Product product = new Product(productId, productName, productPrice, categoryId);
                purchasedProducts.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return purchasedProducts;
    }
    public List<String[]> fetchBrandsFromDatabase() {
        List<String[]> brandList = new ArrayList<>();
        String query = "SELECT Name, Email, Rating FROM Brand ORDER BY Rating DESC";

        try (Connection connection = DriverManager.getConnection(url, user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                String email = resultSet.getString("Email");
                String rating = String.format("%.2f", resultSet.getDouble("Rating"));
                brandList.add(new String[]{name, email, rating});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return brandList;
    }
}

