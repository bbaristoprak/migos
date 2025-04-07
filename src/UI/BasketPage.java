package UI;

import Domain.Product;
import Domain.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BasketPage {

    private JPanel basketPanel;
    private JButton btnOrder;
    private List<Product> basket;
    private double totalPrice;

    public BasketPage(List<Product> basket) {
        this.basket = basket;

        JFrame frame = new JFrame("Shopping App - Basket");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLayout(new BorderLayout());

        JLabel lblHeader = new JLabel("Your Basket", JLabel.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(lblHeader, BorderLayout.NORTH);

        basketPanel = new JPanel();
        basketPanel.setLayout(new BoxLayout(basketPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(basketPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        populateBasket();

        btnOrder = new JButton("Order");
        btnOrder.setPreferredSize(new Dimension(100, 30));
        btnOrder.addActionListener(e -> {
            if (basket.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Your basket is empty. Please add items before ordering.", "Empty Basket", JOptionPane.WARNING_MESSAGE);
            } else {
                new OrderPage(frame);
            }
        });

        JButton btnBack = new JButton("Back to Market");
        btnBack.setPreferredSize(new Dimension(150, 30));
        btnBack.addActionListener(e -> frame.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnBack);
        buttonPanel.add(btnOrder);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void populateBasket() {
        basketPanel.removeAll();
        totalPrice = 0;

        if (basket.isEmpty()) {
            JLabel emptyLabel = new JLabel("Your basket is empty.", JLabel.CENTER);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyLabel.setPreferredSize(new Dimension(350, 50));
            basketPanel.add(emptyLabel);
        } else {
            for (Product product : basket) {
                totalPrice += product.getPrice();

                JPanel itemPanel = new JPanel(new BorderLayout());
                itemPanel.setMaximumSize(new Dimension(350, 50));
                itemPanel.setPreferredSize(new Dimension(350, 50));
                itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                JLabel lblProduct = new JLabel(product.getName() + " - $" + product.getPrice());
                JButton btnRemove = new JButton("Remove");
                btnRemove.setPreferredSize(new Dimension(80, 30));


                btnRemove.addActionListener(e -> {
                    basket.remove(product);
                    populateBasket();
                    basketPanel.revalidate();
                    basketPanel.repaint();
                });

                itemPanel.add(lblProduct, BorderLayout.CENTER);
                itemPanel.add(btnRemove, BorderLayout.EAST);
                basketPanel.add(itemPanel);
            }
        }
        SessionManager.setTotal(totalPrice);
        basketPanel.revalidate();
        basketPanel.repaint();
    }
}
