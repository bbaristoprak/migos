package UI;

import Domain.DatabaseConnector;
import Domain.Order;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class MyOrders {

    private DatabaseConnector dbConnector;

    public MyOrders(int customerId) {
        this.dbConnector = new DatabaseConnector();

        JFrame frame = new JFrame("Shopping App - My Orders");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        headerPanel.setBackground(new Color(45, 45, 48));

        JLabel lblTitle = new JLabel("My Orders", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.CENTER);

        JPanel ordersPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        ordersPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        ordersPanel.setBackground(new Color(240, 240, 240));

        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        List<Order> orders = dbConnector.getOrdersByCustomerId(customerId);
        for (Order order : orders) {
            JPanel orderCard = createOrderCard(order);
            ordersPanel.add(orderCard);
        }


        JButton btnBack = new JButton("Go Back");
        styleButton(btnBack);
        btnBack.setBackground(new Color(255, 51, 51)); // Red for "Go Back"
        btnBack.addActionListener(e -> frame.dispose());

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footerPanel.setBackground(new Color(45, 45, 48)); // Match header background
        footerPanel.add(btnBack);


        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(footerPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private JPanel createOrderCard(Order order) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(800, 100));

        JLabel lblOrderDetails = new JLabel(
                "<html>Date: " + order.getDate() + "<br>" +
                        "Total: $" + String.format("%.2f", order.getTotal()) + "</html>",
                JLabel.LEFT);
        lblOrderDetails.setFont(new Font("Arial", Font.PLAIN, 16));
        lblOrderDetails.setBorder(new EmptyBorder(10, 10, 10, 10));

        StringBuilder productsList = new StringBuilder("<html>Products:<br>");

        // Retrieve products and quantities for this order
        List<String> orderItems = dbConnector.getOrderItems(order.getId());

        for (String item : orderItems) {
            productsList.append(item).append("<br>");
        }

        productsList.append("</html>");

        JLabel lblProducts = new JLabel(productsList.toString(), JLabel.LEFT);
        lblProducts.setFont(new Font("Arial", Font.PLAIN, 14));
        lblProducts.setBorder(new EmptyBorder(10, 10, 10, 10));

        card.add(lblOrderDetails, BorderLayout.WEST);
        card.add(lblProducts, BorderLayout.CENTER);

        return card;
    }


    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 122, 255));
        button.setForeground(Color.WHITE); // White text
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false); // Remove focus border
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding
    }


}
