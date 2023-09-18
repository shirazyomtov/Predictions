package app;

import engineManager.EngineManager;
import enums.SkinsOptions;
import firstPage.FirstPageController;
import header.HeaderController;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import secondPage.SecondPageController;
import thirdPage.ThirdPageController;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class AppController {
    public static final String FIRST_PAGE_FXML_LIGHT_RESOURCE = "/firstPage/firstPage.fxml";
    private static final String SECOND_PAGE_FXML_LIGHT_RESOURCE = "/secondPage/secondPage.fxml";
    private static final String THIRD_PAGE_FXML_LIGHT_RESOURCE = "/thirdPage/thirdPage.fxml";

    private Stage primaryStage;

    private SimpleBooleanProperty isFileLoaded;

    private SimpleStringProperty xmlPathProperty;

    private SimpleBooleanProperty isDetailsClicked;

    private SimpleBooleanProperty isNewExecutionClicked;

    private EngineManager engineManager = new EngineManager();

    @FXML
    private BorderPane borderPaneComponent;

    @FXML
    private HeaderController headerComponentController;

    @FXML
    private GridPane headerComponent;

    @FXML
    private FirstPageController firstPageController;

    @FXML
    private SecondPageController secondPageController;

    @FXML
    private ThirdPageController thirdPageController;
    @FXML
    private ScrollPane scrollPaneComponent;
    private SkinsOptions skinsOption = SkinsOptions.DEFAULT;

    public AppController(){
        isFileLoaded = new SimpleBooleanProperty(false);
        xmlPathProperty = new SimpleStringProperty();
        isDetailsClicked = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize() throws Exception {
        loadResources();
        if(headerComponentController != null && firstPageController != null
            && secondPageController != null && thirdPageController != null){
            headerComponentController.setMainController(this);
            firstPageController.setControllers(this, engineManager);
            headerComponentController.bindComponents(xmlPathProperty, secondPageController.getStartButtonPressedProperty());
            secondPageController.setMainController(this);
            secondPageController.resetControllers();
            thirdPageController.setMainController(this);
            turnOffAnimations();
        }
    }

    private void loadResources() throws Exception {
        loadResourcesFirstPage();
        loadResourcesSecondPage();
        loadResourcesThirdPage();
    }


    private void loadResourcesFirstPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(FIRST_PAGE_FXML_LIGHT_RESOURCE);
        fxmlLoader.setLocation(url);
        InputStream inputStream = url.openStream();
        GridPane gridPane = fxmlLoader.load(inputStream);
        firstPageController = fxmlLoader.getController();
    }


    private void loadResourcesSecondPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(SECOND_PAGE_FXML_LIGHT_RESOURCE);
        fxmlLoader.setLocation(url);
        InputStream inputStream = url.openStream();
        GridPane gridPane = fxmlLoader.load(inputStream);
        secondPageController = fxmlLoader.getController();
    }


    private void loadResourcesThirdPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(THIRD_PAGE_FXML_LIGHT_RESOURCE);
        fxmlLoader.setLocation(url);
        InputStream inputStream = url.openStream();
        ScrollPane scrollPane = fxmlLoader.load(inputStream);
        thirdPageController = fxmlLoader.getController();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public SimpleBooleanProperty getIsFileLoadedProperty() {
        return isFileLoaded;
    }

    public SimpleBooleanProperty getIsDetailsClickedProperty() {
        return isDetailsClicked;
    }

    public void setIsDetailsClickedProperty(boolean state) {
        isDetailsClicked.set(state);
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
            thirdPageController.setAmountOfSimulations(0);
            thirdPageController.setAmountOfSimulationsEnded(0);
            thirdPageController.setSimulationTask(null);
            thirdPageController.setFinishSimulationTask(null);
            thirdPageController.resetPauseAndResume();
            setAllPagesDetails();
            isFileLoaded.set(true);
            isDetailsClicked.set(false);
            xmlPathProperty.set(f.getAbsolutePath());
            return f.getPath();
        }
        catch (Exception e){
            isDetailsClicked.set(false);
            firstPageController.resetAllComponentFirstPage();
            throw e;
        }
    }

    private void setAllPagesDetails() {
        engineManager.clearPastValues();
        secondPageController.getStartButtonPressedProperty().set(false);
        firstPageController.setWorldDetailsFromEngine();
        firstPageController.resetAllComponentFirstPage();
        secondPageController.setVisible(false);
        secondPageController.setSecondPageDetails(engineManager.getEntitiesDetails(), engineManager.getEnvironmentNamesList());
        thirdPageController.setVisible(false);
        thirdPageController.clearAllData();
    }

    public void showFirstPage() {
        borderPaneComponent.setCenter(firstPageController.getGridPaneFirstPage());
    }

    public void showSecondPage() {
        secondPageController.setVisible(true);
        borderPaneComponent.setCenter(secondPageController.getSecondPageGridPane());
    }

    public void showThirdPage() {
        thirdPageController.setVisible(true);
        borderPaneComponent.setCenter(thirdPageController.getThirdPageScrollPane());
    }

    public void setSuccessMessage(String message) {
        pauseTransitionMessage();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setErrorMessage(String errorMessage){
        pauseTransitionMessage();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    private void pauseTransitionMessage(){
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.play();
    }

    public EngineManager getEngineManager() {
        return engineManager;
    }


    public void setSimulationsDetails() {
        thirdPageController.createFinishSimulationTask();
        thirdPageController.setThirdPageDetails(engineManager.getAllPastSimulation());
        //todo : fix this after the second page logic
    }

    public void setSecondPageDetails(Integer simulationId) {
        secondPageController.setSecondPageDetails(engineManager.getInitAmountOfEntitiesAndSetEntitiesByUser(simulationId), engineManager.getEnvironmentValuesOfChosenSimulation(simulationId));
    }


    public void setSkin(SkinsOptions skinsOption) {
        Scene scene = primaryStage.getScene();
        this.skinsOption = skinsOption;
        scene.getStylesheets().clear();

        if(skinsOption != SkinsOptions.DEFAULT)
            scene.getStylesheets().add(skinsOption.getCSS());
    }

    public void setAmountOfCompletedSimulation(String amount)
    {
        headerComponentController.setSimulationsCompletedProperty(amount);
    }

    public void setAmountOfSimulationsInQueue(String amount){
        headerComponentController.setSimulationsInQueueProperty(amount);
    }

    public void setAmountOfSimulationsInProgress(String amount){
        headerComponentController.setSimulationsInProgressProperty(amount);
    }

    public void activeAnimations() {
        firstPageController.startAnimationInFirstPage();
        secondPageController.startAnimationInSecondPage();
    }

    public void turnOffAnimations() {
        firstPageController.stopAnimationInFirstPage();
        secondPageController.stopAnimationInSecondPage();
    }
}