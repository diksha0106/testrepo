package Application;

import java.net.URL;

import dao.UserLoginDao;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Main application class for the TransitUserManager application */
public class Main extends Application {

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            new UserLoginDao(); // Ensure table creation before launching UI
            launch(args);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Make sure you have added the MySQL Connector/J to your project.");
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("An error occurred while initializing the application.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/LoginView.fxml"));
        primaryStage.setTitle("Metro Vibe");
        Scene scene = new Scene(root, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
