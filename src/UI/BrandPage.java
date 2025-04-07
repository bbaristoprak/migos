package UI;

import Domain.DatabaseConnector;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BrandPage {

    DatabaseConnector db = new DatabaseConnector();

    public BrandPage() {
        JFrame frame = new JFrame("Explore Brands");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        List<String[]> brands = db.fetchBrandsFromDatabase();
        if (brands.isEmpty()) {
            JLabel lblNoBrands = new JLabel("No brands available.", JLabel.CENTER);
            lblNoBrands.setFont(new Font("Arial", Font.BOLD, 16));
            mainPanel.add(lblNoBrands);
        } else {
            for (String[] brand : brands) {
                JPanel brandPanel = createBrandCard(brand);
                mainPanel.add(brandPanel);
            }
        }

        frame.add(scrollPane);
        frame.setVisible(true);
    }

    private JPanel createBrandCard(String[] brand) {
        JPanel card = new JPanel(new GridLayout(1, 2));
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(550, 50));

        JLabel lblBrandInfo = new JLabel(
                "<html><b>" + brand[0] + "</b><br>Email: " + brand[1] +
                        "<br>Rating: " + brand[2] + "</html>",
                JLabel.LEFT);
        lblBrandInfo.setFont(new Font("Arial", Font.PLAIN, 14));

        card.add(lblBrandInfo);
        return card;
    }


}
