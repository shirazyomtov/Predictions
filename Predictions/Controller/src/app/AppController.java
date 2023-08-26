package app;

import engineManager.EngineManager;
import firstPage.FirstPageController;
import header.HeaderController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class AppController {

    public static final String HEADER_fXML_RESOURCE = "/header/header.fxml";

    public static final String FIRST_PAGE_FXML_LIGHT_RESOURCE = "/firstPage/firstPage.fxml";

    private Stage primaryStage;

    private EngineManager engineManager = new EngineManager();

    @FXML
    private BorderPane borderPaneComponent;

    @FXML
    private HeaderController headerController;

    @FXML
    private FirstPageController firstPageController;

    @FXML
    public void initialize() throws Exception {
        loadResources();
        if(headerController != null && firstPageController != null){
            headerController.setMainController(this);
            firstPageController.setMainController(this);
        }
    }

    private void loadResources() throws Exception {
        loadResourcesHeader();
        loadResourcesFirstPage();

    }

    private void loadResourcesHeader() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(HEADER_fXML_RESOURCE);
        fxmlLoader.setLocation(url);
        ScrollPane headerPane = fxmlLoader.load(url.openStream());
        headerController = fxmlLoader.getController();
        borderPaneComponent.setTop(headerPane);
    }

    private void loadResourcesFirstPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(FIRST_PAGE_FXML_LIGHT_RESOURCE);
        fxmlLoader.setLocation(url);
        InputStream inputStream = url.openStream();
        GridPane gridPane = fxmlLoader.load(inputStream);
        firstPageController = fxmlLoader.getController();
        borderPaneComponent.setCenter(gridPane);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public int loadXML() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Machine File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File f = fileChooser.showOpenDialog(primaryStage);

        if (f == null) // user closed file choosing dialog
            return -1;
        engineManager.loadXMLAAndCheckValidation(f.getPath());

        return 0;
    }
}