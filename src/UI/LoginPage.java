package UI;

import Domain.DatabaseConnector;
import Domain.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage {

    DatabaseConnector db;

    public LoginPage() {
        this.db = new DatabaseConnector();

        JFrame frame = new JFrame("Shopping App - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        /* // Add and resize logo
        JLabel lblLogo = new JLabel();
        ImageIcon originalIcon = new ImageIcon(getClass().getClassLoader().getResource("migosLogo.png")); // Load logo from res folder
        Image originalImage = originalIcon.getImage(); // Get the original image
        Image resizedImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Resize to 100x100 pixels
        ImageIcon resizedIcon = new ImageIcon(resizedImage); // Create a new ImageIcon with the resized image
        lblLogo.setIcon(resizedIcon);
        lblLogo.setBounds(150, 10, 100, 50); // Adjust position and size

         */

        // Create components for email and password
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(50, 80, 100, 25);

        JTextField txtEmail = new JTextField();
        txtEmail.setBounds(150, 80, 180, 25);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 120, 100, 25);

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 120, 180, 25);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(50, 200, 80, 30);

        JButton btnSignUp = new JButton("Sign Up");
        btnSignUp.setBounds(150, 200, 100, 30);

        JButton btnExit = new JButton("Exit");
        btnExit.setBounds(270, 200, 80, 30);


        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtEmail.getText();
                String password = new String(txtPassword.getPassword());


                if (!email.isEmpty() && !password.isEmpty()) {

                    boolean isValidLogin = db.validateLogin(email, password);

                    if (isValidLogin) {
                        SessionManager.setUserEmail(email);
                        JOptionPane.showMessageDialog(frame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

                        frame.dispose();
                        new MarketPage();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid email or password.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter both email and password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

//        // to test if the market page is visible please use this for main page testing
//        btnLogin.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String email = txtEmail.getText();
//                String password = new String(txtPassword.getPassword());
//                if (true){
//                    JOptionPane.showMessageDialog(frame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
//                    // Open the main page if login is successful
//                    frame.dispose();  // Close the login page
//                    new MarketPage();  // Open the main page (replace with actual class name)
//                } else {
//                    JOptionPane.showMessageDialog(frame, "Invalid email or password.", "Error", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });


        btnSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the login page
                new SignUpPage(); // Open the sign-up page
            }
        });

        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the application
            }
        });


        //frame.add(lblLogo);
        frame.add(lblEmail);  // Changed from lblUsername to lblEmail
        frame.add(txtEmail);  // Changed from txtUsername to txtEmail
        frame.add(lblPassword);
        frame.add(txtPassword);
        frame.add(btnLogin);
        frame.add(btnSignUp);
        frame.add(btnExit);

        // Make the frame visible
        frame.setVisible(true);
    }
}
