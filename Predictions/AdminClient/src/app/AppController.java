package app;

import allocationsPage.AllocationsPageController;
import header.HeaderController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import managementPage.ManagementPageController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class AppController {

    public static final String MANAGEMENT_PAGE_FXML_LIGHT_RESOURCE = "/managementPage/ManagementPage.fxml";
    private static final String ALLOCATIONS_PAGE_FXML_LIGHT_RESOURCE = "/allocationsPage/allocations.fxml";

    @FXML
    private BorderPane borderPaneComponent;

    @FXML
    private HeaderController headerComponentController;

    @FXML
    private GridPane headerComponent;
    @FXML
    private ScrollPane scrollPaneComponent;

    private SimpleBooleanProperty isFileLoaded;

    private SimpleStringProperty xmlPathProperty;

    private SimpleBooleanProperty isManagementClicked;

    private Stage primaryStage;

    @FXML
    private ManagementPageController managementPageController;

    @FXML
    private AllocationsPageController allocationsPageController;

    public AppController(){
        isFileLoaded = new SimpleBooleanProperty(false);
        xmlPathProperty = new SimpleStringProperty();
        isManagementClicked = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize() throws Exception {
        loadResources();
        if(headerComponentController != null && managementPageController != null &&
            allocationsPageController != null){
            headerComponentController.setMainController(this);
            managementPageController.setControllers(this);
            headerComponentController.bindComponents();
            showManagementPage();
            allocationsPageController.setChoiceBoxValues();
        }
    }

    private void loadResources() throws Exception {
        loadResourcesManagementPage();
        loadResourcesAllocationsPage();
    }


    private void loadResourcesManagementPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(MANAGEMENT_PAGE_FXML_LIGHT_RESOURCE);
        fxmlLoader.setLocation(url);
        InputStream inputStream = url.openStream();
        GridPane gridPane = fxmlLoader.load(inputStream);
        managementPageController = fxmlLoader.getController();
    }


    private void loadResourcesAllocationsPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(ALLOCATIONS_PAGE_FXML_LIGHT_RESOURCE);
        fxmlLoader.setLocation(url);
        InputStream inputStream = url.openStream();
        GridPane gridPane = fxmlLoader.load(inputStream);
        allocationsPageController = fxmlLoader.getController();
        allocationsPageController.allocationsRefresher();
    }

    public SimpleBooleanProperty getIsFileLoadedProperty() {
        return isFileLoaded;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showManagementPage() {
        borderPaneComponent.setCenter(managementPageController.getGridPaneManagementPage());
    }


    public void showAllocationsPage() {
        borderPaneComponent.setCenter(allocationsPageController.getGridAllocationsPage());
    }

    public Window getPrimaryStage() {
        return primaryStage;
    }

    public void setSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setErrorMessage(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    public void setVisbleAlloctionPage() {
        headerComponentController.setAllcotionButtonVisble();
    }

}
