package UI;

import Domain.DatabaseConnector;
import Domain.SessionManager;

import javax.swing.*;
import java.awt.*;

public class OrderPage {
    DatabaseConnector db = new DatabaseConnector();

    public OrderPage(JFrame basketFrame) {
        JFrame frame = new JFrame("Shopping App - Order");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());


        JLabel lblHeader = new JLabel("Order Confirmation", JLabel.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(lblHeader, BorderLayout.NORTH);


        JButton btnConfirmOrder = new JButton("Place Order");
        btnConfirmOrder.addActionListener(e -> {
            if (db.insertOrderByEmail(SessionManager.getUserEmail(), SessionManager.getTotal())) {
                JOptionPane.showMessageDialog(frame, "Order placed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                SessionManager.clearBasket();
                basketFrame.dispose();
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Order failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        JButton btnBack = new JButton("Back to Basket");
        btnBack.addActionListener(e -> frame.dispose());


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnConfirmOrder);
        buttonPanel.add(btnBack);


        frame.add(buttonPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}