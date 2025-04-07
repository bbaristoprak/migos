import UI.LoginPage;
import Domain.DatabaseConnector;


public class Main {
    public static void main(String[] args) {

        DatabaseConnector dbConnector = new DatabaseConnector();
        dbConnector.connectAndShowTables();

        LoginPage loginPage = new LoginPage();
    }
}
