package app;

import engineManager.EngineManager;
import firstPage.FirstPageController;
import header.HeaderController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
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

//    public static final String HEADER_fXML_RESOURCE = "/header/header.fxml";

    public static final String FIRST_PAGE_FXML_LIGHT_RESOURCE = "/firstPage/firstPage.fxml";

    private Stage primaryStage;

    private SimpleBooleanProperty isFileLoaded;

    private SimpleStringProperty xmlPathProperty;

    private SimpleBooleanProperty isDetailsClicked;

    private EngineManager engineManager = new EngineManager();

    @FXML
    private BorderPane borderPaneComponent;

    @FXML
    private HeaderController headerComponentController;

    @FXML
    private ScrollPane headerComponent;

    @FXML
    private FirstPageController firstPageController;

    public AppController(){
        isFileLoaded = new SimpleBooleanProperty(false);
        xmlPathProperty = new SimpleStringProperty();
        isDetailsClicked = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize() throws Exception {
        loadResources();
        if(headerComponentController != null && firstPageController != null){
            headerComponentController.setMainController(this);
            firstPageController.setControllers(this, engineManager);
            headerComponentController.bindComponents(xmlPathProperty);
        }
    }

    private void loadResources() throws Exception {
        loadResourcesFirstPage();
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

    public SimpleBooleanProperty getIsFileLoadedProperty() {
        return isFileLoaded;
    }

    public String loadXML() throws Exception {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open XML File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
            File f = fileChooser.showOpenDialog(primaryStage);

            if (f == null) // user closed file choosing dialog
                return "";

            engineManager.loadXMLAAndCheckValidation(f.getAbsolutePath());
            firstPageController.setWorldDetailsFromEngine();
            firstPageController.resetAllComponent();
            isFileLoaded.set(true);
            isDetailsClicked.set(false);
            xmlPathProperty.set(f.getAbsolutePath());
            return f.getPath();
        }
        catch (Exception e){
            isDetailsClicked.set(false);
            throw e;
        }
    }

//    private void resetAllProperties(){
//        isFileLoaded.set(false);
//        isDetailsClicked.set(false);
//        xmlPathProperty.set("");
//        //to change to general function
//        //todo:reset all pages
//    }

    public SimpleBooleanProperty getIsDetailsClickedProperty() {
        return isDetailsClicked;
    }

    public void setIsDetailsClickedProperty(boolean state) {
        isDetailsClicked.set(state);
    }
}