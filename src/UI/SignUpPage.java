package UI;

import Domain.SignUpValidator;
import Domain.DatabaseConnector;

import javax.swing.*;
import java.awt.*;

public class SignUpPage {
    private DatabaseConnector dbConnector;


    public SignUpPage() {
        this.dbConnector = new DatabaseConnector();
        JFrame frame = new JFrame("Sign Up");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);



        JLabel lblTitle = new JLabel("Sign Up");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBounds(240, 20, 220, 30);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(50, 80, 100, 25);
        JTextField txtName = new JTextField();
        txtName.setBounds(170, 80, 250, 25);

        JLabel lblSurname = new JLabel("Surname:");
        lblSurname.setBounds(50, 120, 100, 25);
        JTextField txtSurname = new JTextField();
        txtSurname.setBounds(170, 120, 250, 25);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(50, 160, 100, 25);
        JTextField txtEmail = new JTextField();
        txtEmail.setBounds(170, 160, 250, 25);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 200, 100, 25);
        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBounds(170, 200, 250, 25);

        JLabel lblConfirmPassword = new JLabel("Confirm Password:");
        lblConfirmPassword.setBounds(50, 250, 150, 25);
        JPasswordField txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setBounds(170, 250, 250, 25);

        JButton btnRegister = new JButton("Register");
        btnRegister.setBounds(120, 330, 100, 30);
        JButton btnBack = new JButton("Back");
        btnBack.setBounds(250, 330, 100, 30);

        JLabel lblResult = new JLabel();
        lblResult.setBounds(50, 380, 500, 30);
        lblResult.setForeground(Color.RED);


        btnRegister.addActionListener(e -> {
            String name = txtName.getText();
            String surname = txtSurname.getText();
            String email = txtEmail.getText();
            String password = new String(txtPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());

            boolean isValid = true;
            lblResult.setText("");


            if (!SignUpValidator.isValidEmail(email)) {
                lblResult.setText("Invalid email format.");
                isValid = false;
            }
            if (!SignUpValidator.isValidName(name)) {
                lblResult.setText("Name cannot be empty.");
                isValid = false;
            }
            if (!SignUpValidator.isValidSurname(surname)) {
                lblResult.setText("Surname cannot be empty.");
                isValid = false;
            }
            if (!SignUpValidator.isValidPassword(password)) {
                lblResult.setText("Password must be between 8-45 characters and contain a number.");
                isValid = false;
            }
            if (!SignUpValidator.doPasswordsMatch(password, confirmPassword)) {
                lblResult.setText("Passwords do not match.");
                isValid = false;
            }


            if (isValid) {
                boolean inserted = dbConnector.insertCustomer(name, surname, email, password);

                if (inserted) {
                    JOptionPane.showMessageDialog(frame, "Sign-up successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    txtName.setText("");
                    txtSurname.setText("");
                    txtEmail.setText("");
                    txtPassword.setText("");
                    txtConfirmPassword.setText("");
                    lblResult.setText("All fields are valid.");
                    lblResult.setForeground(Color.GREEN);
                    frame.dispose();
                    new LoginPage();
                } else {
                    lblResult.setText("Error saving user to the database.");
                }
            } else {
                lblResult.setForeground(Color.RED);
            }
        });

        btnBack.addActionListener(e -> {
            frame.dispose();
            new LoginPage();
        });


        frame.add(lblTitle);
        frame.add(lblName);
        frame.add(txtName);
        frame.add(lblSurname);
        frame.add(txtSurname);
        frame.add(lblEmail);
        frame.add(txtEmail);
        frame.add(lblPassword);
        frame.add(txtPassword);
        frame.add(lblConfirmPassword);
        frame.add(txtConfirmPassword);
        frame.add(btnRegister);
        frame.add(btnBack);
        frame.add(lblResult);

        frame.setVisible(true);
    }
}
