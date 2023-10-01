package app;

//import executionPage.ExecutionPageController;
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
//import requestsPage.RequestsPageController;
//import resultsPage.ResultsPageController;
import simulationDetailsPage.SimulationDetailsPageController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class AppController {

    public static final String SIMULATION_DETAILS_PAGE_FXML_LIGHT_RESOURCE = "/simulationDetailsPage/simulationDetailsPage.fxml";
    private static final String REQUESTS_PAGE_FXML_LIGHT_RESOURCE = "/requestsPage/requestsPage.fxml";

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

    private SimulationDetailsPageController simulationDetailsPageController;
//    private RequestsPageController requestsPageController;
//
//    private ExecutionPageController executionsPageController;
//
//    private ResultsPageController resultsPageController;


    public AppController(){
        isFileLoaded = new SimpleBooleanProperty(false);
        xmlPathProperty = new SimpleStringProperty();
        isManagementClicked = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize() throws Exception {
        loadResources();
        if(headerComponentController != null){
            headerComponentController.setMainController(this);
            headerComponentController.bindComponents();
            simulationDetailsPageController.setControllers(this);
            showSimulationDetailsPage();
        }
    }

    private void loadResources() throws Exception {
        loadResourcesSimulationDetailsPage();
//        loadResourcesRequestsPage();
    }

    private void loadResourcesSimulationDetailsPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(SIMULATION_DETAILS_PAGE_FXML_LIGHT_RESOURCE);
        fxmlLoader.setLocation(url);
        InputStream inputStream = url.openStream();
        GridPane gridPane = fxmlLoader.load(inputStream);
        simulationDetailsPageController = fxmlLoader.getController();
        simulationDetailsPageController.worldListRefresher();
    }

//    private void loadResourcesRequestsPage() throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        URL url = getClass().getResource(REQUESTS_PAGE_FXML_LIGHT_RESOURCE);
//        fxmlLoader.setLocation(url);
//        InputStream inputStream = url.openStream();
//        GridPane gridPane = fxmlLoader.load(inputStream);
//        requestsPageController = fxmlLoader.getController();
//    }

    public SimpleBooleanProperty getIsFileLoadedProperty() {
        return isFileLoaded;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showSimulationDetailsPage() {
        borderPaneComponent.setCenter(simulationDetailsPageController.getGridPaneSimulationDetailsPage());
    }

    public void showRequestsPage() {
//        borderPaneComponent.setCenter(requestsPageController.getGridPaneRequestsPage());
    }

    public void showExecutionsPage() {
//        borderPaneComponent.setCenter(executionsPageController.getExecutionPageGridPane());
    }

    public void showResultsPage() {
//        borderPaneComponent.setCenter(resultsPageController.getResultsPageGridPane());
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


}
