package UI;

import Domain.DatabaseConnector;
import Domain.Product;
import Domain.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MarketPage {

    private DatabaseConnector dbConnector;
    private JTextField txtSearchAProduct;

    public MarketPage() {
        this.dbConnector = new DatabaseConnector();
        String loggedInEmail = SessionManager.getUserEmail();

        JFrame frame = new JFrame("Shopping App - Market");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 700);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        headerPanel.setBackground(new Color(45, 45, 48)); // Dark header background

        JLabel lblTitle = new JLabel("Welcome to the Market", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.CENTER);

        // Filter, Sort, and Basket Buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        controlPanel.setBackground(new Color(45, 45, 48)); // Match header background


        JComboBox<String> brandDropdown = new JComboBox<>();
        List<String> brandNames = dbConnector.getAvailableBrands();
        System.out.println(brandNames);
        brandDropdown.addItem("All");
        for (String brand : brandNames) {
            brandDropdown.addItem(brand);
        }

        String[] sortOptions = {"None","Popularity", "Price: High to Low", "Price: Low to High", "Purchased by Me"};
        JComboBox<String> sortDropdown = new JComboBox<>(sortOptions);


        JComboBox<String> categoryDropdown = new JComboBox<>();
        List<String> categoryNames = dbConnector.getAvailableCategories();
        System.out.println(categoryNames);
        categoryDropdown.addItem("All");
        for (String category : categoryNames) {
            categoryDropdown.addItem(category);
        }


        JButton btnBasket = new JButton("Basket");
        styleButton(btnBasket);
        btnBasket.addActionListener(e -> new BasketPage(SessionManager.getBasket()));

        JLabel label_1 = new JLabel("Brand:");
        label_1.setForeground(Color.WHITE);
        controlPanel.add(label_1);
        controlPanel.add(brandDropdown);
        JLabel label = new JLabel("Sort By:");
        label.setForeground(Color.WHITE);
        controlPanel.add(label);
        controlPanel.add(sortDropdown);
        JLabel label_2 = new JLabel("Category:");


        label_2.setForeground(Color.WHITE);
        controlPanel.add(label_2);
        controlPanel.add(categoryDropdown);
        controlPanel.add(btnBasket);

        headerPanel.add(controlPanel, BorderLayout.SOUTH);

        txtSearchAProduct = new JTextField();
        txtSearchAProduct.setText("Search a product");
        txtSearchAProduct.setColumns(10);

        txtSearchAProduct.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtSearchAProduct.getText().equals("Search a product")) {
                    txtSearchAProduct.setText(""); // Clear the placeholder text
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtSearchAProduct.getText().isEmpty()) {
                    txtSearchAProduct.setText("Search a product"); // Reset placeholder if empty
                }
            }
        });

        controlPanel.add(txtSearchAProduct);


        // Product Panel
        JPanel productPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        productPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        productPanel.setBackground(new Color(240, 240, 240));

        JScrollPane scrollPane = new JScrollPane(productPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Fetch products and display them
        List<Product> products = dbConnector.getProducts();
        for (Product product : products) {
            JPanel productCard = createProductCard(product);
            productPanel.add(productCard);
        }

        categoryDropdown.addActionListener(e -> updateProductTable(productPanel, sortDropdown, categoryDropdown, brandDropdown));
        brandDropdown.addActionListener(e -> updateProductTable(productPanel, sortDropdown, categoryDropdown, brandDropdown));
        sortDropdown.addActionListener(e -> updateProductTable(productPanel, sortDropdown, categoryDropdown, brandDropdown));



        // Back Button
        JButton btnBack = new JButton("Go Back");
        styleButton(btnBack);
        btnBack.setBackground(new Color(255, 51, 51)); // Red for "Go Back"
        btnBack.addActionListener(e -> frame.dispose());

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footerPanel.setBackground(new Color(45, 45, 48)); // Match header background
        footerPanel.add(btnBack);

        // Add components to the frame
        frame.getContentPane().add(headerPanel, BorderLayout.NORTH);

        JButton myOrdersButton = new JButton("My Orders");
        myOrdersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = SessionManager.getUserEmail();
                int customerId = dbConnector.getCustomerID(email);
                new MyOrders(customerId);
            }
        });
        myOrdersButton.setForeground(Color.BLACK);
        headerPanel.add(myOrdersButton, BorderLayout.EAST);
        
        JButton statsButton = new JButton("Explore Brands");
        statsButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                new BrandPage();
        	}
        });
        headerPanel.add(statsButton, BorderLayout.WEST);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(footerPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                productPanel.removeAll();
                String searchText = txtSearchAProduct.getText().trim(); // Trim whitespace for cleaner input
                List<Product> products;

                if (searchText.isEmpty() || searchText.equals("Search a product")) {
                    products = dbConnector.getProducts();
                    System.out.println();
                } else {
                    products = dbConnector.getProductsByName(searchText);
                }

                for (Product product : products) {
                    System.out.println(product.getName());
                    JPanel productCard = createProductCard(product);
                    productPanel.add(productCard);
                }

                productPanel.revalidate(); // Refresh the panel
                productPanel.repaint(); // Ensure UI updates
            }
        });
        controlPanel.add(searchButton);
    }

    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(250, 320));

        // Ürün Görseli
        JLabel lblImage = new JLabel();
        lblImage.setHorizontalAlignment(JLabel.CENTER);
        lblImage.setPreferredSize(new Dimension(250, 200));
        ImageIcon icon = loadProductImage(product.getName());
        lblImage.setIcon(icon);

        // Ürün Adı
        JLabel lblName = new JLabel(product.getName(), JLabel.CENTER);
        lblName.setFont(new Font("Arial", Font.BOLD, 14));
        lblName.setForeground(new Color(51, 51, 102));

        // Fiyat ve Ekle Butonu Paneli
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);

        // Fiyat Etiketi
        JLabel lblPrice = new JLabel(  String.format("%.2f", product.getPrice()) + "TL", JLabel.CENTER);
        lblPrice.setFont(new Font("Arial", Font.BOLD, 16));
        lblPrice.setForeground(new Color(0, 153, 76));
        lblPrice.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Sepete Ekle Butonu
        JButton btnAddToBasket = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Arka planı çizin
                g2d.setColor(new Color(0, 122, 255));
                g2d.fillOval(0, 0, getWidth(), getHeight());

                // + ikonunu çizin
                g2d.setColor(Color.WHITE);
                FontMetrics fm = g2d.getFontMetrics(getFont());
                String text = "+";
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - fm.getDescent();
                g2d.drawString(text, x, y);
                g2d.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(50, 50);
            }
        };
        btnAddToBasket.setContentAreaFilled(false);
        btnAddToBasket.setFocusPainted(false);
        btnAddToBasket.setFont(new Font("Arial", Font.BOLD, 18));
        btnAddToBasket.addActionListener(e -> {
            SessionManager.addBasket(product);
            JOptionPane.showMessageDialog(null, product.getName() + " sepete eklendi!");
        });

        bottomPanel.add(lblPrice, BorderLayout.CENTER);
        bottomPanel.add(btnAddToBasket, BorderLayout.EAST);

        card.add(lblImage, BorderLayout.NORTH);
        card.add(lblName, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }


    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 122, 255)); // Blue background
        button.setForeground(Color.WHITE); // White text
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Larger font for readability
        button.setFocusPainted(false); // Remove focus border
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding
    }

    private ImageIcon loadProductImage(String name) {
        String imagePath = "res/photos/" + name + ".jpg"; // Image path based on product ID
        File file = new File(imagePath);
        String imageePath = "res/photos/" + name + ".jpeg"; // Image path based on product ID
        File filee = new File(imageePath);
        String imageeePath = "res/photos/" + name + ".png"; // Image path based on product ID
        File fileee = new File(imageeePath);

        if (file.exists()) {
            Image img = new ImageIcon(imagePath).getImage().getScaledInstance(250, 200, Image.SCALE_SMOOTH);
            //System.out.println("jpg");
            return new ImageIcon(img);
        } else if (filee.exists()) {
            //System.out.println("jpeg");
            Image imgg = new ImageIcon(imageePath).getImage().getScaledInstance(250, 200, Image.SCALE_SMOOTH);
            return new ImageIcon(imgg);
        } else if (fileee.exists()) {
            //System.out.println("png");
            Image imggg = new ImageIcon(imageeePath).getImage().getScaledInstance(250, 200, Image.SCALE_SMOOTH);
            return new ImageIcon(imggg);
        } else {
            //System.out.println("."+ name +".");
            return null;
        }
    }


    private void updateProductTable(JPanel productPanel, JComboBox<String> sortDropdown, JComboBox<String> categoryDropdown, JComboBox<String> brandDropdown) {
        productPanel.removeAll(); // Clear existing products

        String selectedCategory = (String) categoryDropdown.getSelectedItem();
        String selectedBrand = (String) brandDropdown.getSelectedItem();

        List<Product> filteredProducts;

        // Fetch products based on category and brand
        if ("All".equals(selectedCategory) && "All".equals(selectedBrand)) {
            filteredProducts = dbConnector.getProducts(); // Fetch all products
        } else if ("All".equals(selectedCategory)) {
            filteredProducts = dbConnector.getProductsByBrand(selectedBrand); // Filter by brand only
        } else if ("All".equals(selectedBrand)) {
            filteredProducts = dbConnector.getProductsByCategory(selectedCategory); // Filter by category only
        } else {
            filteredProducts = dbConnector.getProductsByCategoryAndBrand(selectedCategory, selectedBrand); // Filter by both category and brand
        }

        String selectedSort = (String) sortDropdown.getSelectedItem();
        if ("Price: High to Low".equals(selectedSort)) {
            filteredProducts.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice())); // Sort descending
        } else if ("Price: Low to High".equals(selectedSort)) {
            filteredProducts.sort(Comparator.comparingDouble(Product::getPrice)); // Sort ascending
        } else if ("Popularity".equals(selectedSort)) {
            // Fetch product purchase counts and sort by popularity
            //HashMap<Integer, Integer> purchaseCounts = dbConnector.getProductPurchaseCounts();
            filteredProducts = dbConnector.getProductsSortedByPopularity();
        } else if ("Purchased by Me".equals(selectedSort)) {

            filteredProducts = dbConnector.getProductsPurchasedByMe();
        }

        for (Product product : filteredProducts) {
            JPanel productCard = createProductCard(product);
            productPanel.add(productCard);
        }

        productPanel.revalidate(); // Refresh the panel
        productPanel.repaint(); // Ensure UI updates
    }






}
