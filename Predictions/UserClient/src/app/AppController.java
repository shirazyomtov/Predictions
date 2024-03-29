package app;

//import executionPage.ExecutionPageController;
import DTO.DTOAllWorldsInfo;
import DTO.DTOEntitiesAndEnvironmentInfo;
import executionPage.ExecutionPageController;
import executionPage.summaryPageOfEntitiesAndEnvironments.SummaryPage;
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
import resultsPage.ResultsPageController;
import login.HeaderLoginController;
import requestsPage.RequestsPageController;
import simulationDetailsPage.SimulationDetailsPageController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class AppController {

    public static final String SIMULATION_DETAILS_PAGE_FXML_LIGHT_RESOURCE = "/simulationDetailsPage/simulationDetailsPage.fxml";
    private static final String REQUESTS_PAGE_FXML_LIGHT_RESOURCE = "/requestsPage/requestsPage.fxml";
    private static final String HEADER_FXML_LIGHT_RESOURCE = "/header/header.fxml";

    private static final String HEADER_LOGIN_FXML_LIGHT_RESOURCE = "/login/headerLogin.fxml";
    private static final String EXECUTION_PAGE_FXML_LIGHT_RESOURCE = "/executionPage/executionPage.fxml";
    private static final String RESULTS_PAGE_FXML_LIGHT_RESOURCE = "/resultsPage/resultsPage.fxml";

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

    private HeaderLoginController headerLoginComponentController;
    private SimulationDetailsPageController simulationDetailsPageController;
    private RequestsPageController requestsPageController;

    private ExecutionPageController executionsPageController;

    private ResultsPageController resultsPageController;


    public AppController(){
        isFileLoaded = new SimpleBooleanProperty(false);
        xmlPathProperty = new SimpleStringProperty();
        isManagementClicked = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize() throws Exception {
        loadResources();
        if(headerComponentController != null && requestsPageController != null
            && executionsPageController != null && resultsPageController != null){
            borderPaneComponent.setTop(headerLoginComponentController.getGridPaneLoginHeader());
            headerLoginComponentController.setMainController(this);
            executionsPageController.setMainControllerAndLoadResources(this);
            resultsPageController.setMainController(this);
        }
    }

    private void loadResources() throws Exception {
        loadHeader();
        loadLoginHeader();
        loadResourcesSimulationDetailsPage();
        loadResourcesRequestsPage();
        loadLoginHeaderExecutionPage();
        loadResourcesResultsPage();
    }

    private void loadLoginHeader()  throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(HEADER_LOGIN_FXML_LIGHT_RESOURCE);
        fxmlLoader.setLocation(url);
        InputStream inputStream = url.openStream();
        GridPane gridPane = fxmlLoader.load(inputStream);
        headerLoginComponentController = fxmlLoader.getController();
    }

    private void loadHeader()  throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(HEADER_FXML_LIGHT_RESOURCE);
        fxmlLoader.setLocation(url);
        InputStream inputStream = url.openStream();
        GridPane gridPane = fxmlLoader.load(inputStream);
        headerComponentController = fxmlLoader.getController();
    }

    private void loadResourcesSimulationDetailsPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(SIMULATION_DETAILS_PAGE_FXML_LIGHT_RESOURCE);
        fxmlLoader.setLocation(url);
        InputStream inputStream = url.openStream();
        GridPane gridPane = fxmlLoader.load(inputStream);
        simulationDetailsPageController = fxmlLoader.getController();
    }

    private void loadResourcesRequestsPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(REQUESTS_PAGE_FXML_LIGHT_RESOURCE);
        fxmlLoader.setLocation(url);
        InputStream inputStream = url.openStream();
        GridPane gridPane = fxmlLoader.load(inputStream);
        requestsPageController = fxmlLoader.getController();
    }

    private void loadLoginHeaderExecutionPage() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(EXECUTION_PAGE_FXML_LIGHT_RESOURCE);
        fxmlLoader.setLocation(url);
        InputStream inputStream = url.openStream();
        GridPane gridPane = fxmlLoader.load(inputStream);
        executionsPageController = fxmlLoader.getController();
    }

    private void loadResourcesResultsPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(RESULTS_PAGE_FXML_LIGHT_RESOURCE);
        fxmlLoader.setLocation(url);
        InputStream inputStream = url.openStream();
        ScrollPane scrollPane = fxmlLoader.load(inputStream);
        resultsPageController = fxmlLoader.getController();
    }

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
        borderPaneComponent.setCenter(requestsPageController.getGridPaneRequestsPage());
        requestsPageController.allocationsRefresher();
    }

    public void showExecutionsPage(Integer requestId, String worldName) {
        borderPaneComponent.setCenter(executionsPageController.getExecutionPageGridPane());
        executionsPageController.setRequestIdAndWorldName(requestId, worldName);
        executionsPageController.setExecutionsPageDetails();
    }

    public void showSummaryPage(SummaryPage summaryPageController, Integer requestId, String worldName, Integer executeID, DTOEntitiesAndEnvironmentInfo dtoEntitiesAndEnvironmentInfo) {
        borderPaneComponent.setCenter(summaryPageController.getSummaryPageGridPane());
        summaryPageController.setDetails(this,requestId, worldName, executeID, dtoEntitiesAndEnvironmentInfo);
    }

    public void showResultsPage() {
        headerComponentController.setVisbleResultsPage();
        borderPaneComponent.setCenter(resultsPageController.getResultsPageGridPane());
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


    public void setRequestPage(DTOAllWorldsInfo dtoAllWorldsInfo) {
        headerComponentController.setVisbleRequestPage();
        requestsPageController.setWorldsNames(dtoAllWorldsInfo);
    }

    public void switchToApplication() throws IOException {
        borderPaneComponent.setTop(headerComponentController.getGridPaneHeader());
        headerComponentController.setMainController(this);
        headerComponentController.bindComponents();
        simulationDetailsPageController.setControllers(this);
        simulationDetailsPageController.worldListRefresher();
        showSimulationDetailsPage();
        requestsPageController.setControllers(this);
    }

    public void updateUserName(String userName) {
        headerComponentController.setUserName(userName);
    }

    public String getUsername() {
        return headerComponentController.getUserName();
    }


    public void setExecuteIdInExecutionPage(Integer executeID) {
        executionsPageController.setExecuteId(executeID);
    }

    public void setSimulationsDetails() {
        resultsPageController.createFinishSimulationRefresher();
        resultsPageController.setResultsPageDetails();
    }
}
