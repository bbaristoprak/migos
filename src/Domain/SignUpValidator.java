package Domain;

public class SignUpValidator {

    /**
     * Validates the format of an email.
     * The format must be:
     * - Any number of characters before '@'
     * - '@' followed by any number of characters
     * - '.' followed by exactly 3 characters
     * - Entire email must not exceed 45 characters
     *
     * @param email The email string to validate
     * @return true if the email format is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        // Check length requirement
        if (email.length() > 45) {
            return false;
        }


        String emailRegex = "^[^@]+@[^@]+\\.[a-zA-Z]{3}$";


        return email.matches(emailRegex);
    }

    /**
     * Validates the format of a password.
     * The password must:
     * - Be between 8 and 45 characters
     * - Contain at least one number
     *
     * @param password The password string to validate
     * @return true if the password format is valid, false otherwise
     */
    public static boolean isValidPassword(String password) {

        if (password.length() < 8 || password.length() > 45) {
            return false;
        }


        String passwordRegex = ".*\\d.*";


        return password.matches(passwordRegex);
    }

    /**
     * Validates that the password and confirm password match.
     *
     * @param password The original password
     * @param confirmPassword The confirm password
     * @return true if the passwords match, false otherwise
     */
    public static boolean doPasswordsMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    /**
     * Validates the name.
     * The name must not be empty.
     *
     * @param name The name string to validate
     * @return true if the name is valid, false otherwise
     */
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    /**
     * Validates the surname.
     * The surname must not be empty.
     *
     * @param surname The surname string to validate
     * @return true if the surname is valid, false otherwise
     */
    public static boolean isValidSurname(String surname) {
        return surname != null && !surname.trim().isEmpty();
    }
}
