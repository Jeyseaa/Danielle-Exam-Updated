
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TicketingApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("Application.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Tarlac Express Train Ticketing");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
