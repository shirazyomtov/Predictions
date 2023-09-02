import app.AppController;
import firstPage.FirstPageController;
import header.HeaderController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.InputStream;
import java.net.URL;

public class Main extends Application {
    public static final String APP_FXML_LIGHT_RESOURCE = "/app/app.fxml";

    public static void mainO(String[] args) {
        Thread.currentThread().setName("main");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(APP_FXML_LIGHT_RESOURCE);
        fxmlLoader.setLocation(url);
        BorderPane root = fxmlLoader.load(url.openStream());
        AppController appController  = fxmlLoader.getController();
        appController.setPrimaryStage(primaryStage);

        primaryStage.setTitle("Predictions");
        Scene scene = new Scene(root, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}