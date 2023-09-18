package header;

import app.AppController;
import enums.SkinsOptions;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

public class HeaderController {
    private AppController mainController;
    @FXML
    private ToggleGroup MenuBarBtn;

    @FXML
    private TextField XMLFileTextField;

    @FXML
    private MenuItem defaultMenuItem;

    @FXML
    private ToggleButton detailsToggleButton;

    @FXML
    private MenuItem flowersMenuItem;

    @FXML
    private Button loadFileButton;

    @FXML
    private ToggleButton newExecutionToggleButton;

    @FXML
    private Label predictionsLabel;

    @FXML
    private ToggleButton resultsToggleButton;

    @FXML
    private Menu skinsMenu;

    @FXML
    private Label messageLabel;

    @FXML
    private Label simulationsCompletedLabel;

    @FXML
    private Label simulationsInProgressLabel;

    @FXML
    private Label simulationsInQueueLabel;

    @FXML
    private Menu animationsMenu;

    private SimpleStringProperty simulationsCompletedProperty;

    private SimpleStringProperty simulationsInProgressProperty;

    private SimpleStringProperty simulationsInQueueProperty;

    public HeaderController(){
        simulationsCompletedProperty = new SimpleStringProperty("0");
        simulationsInProgressProperty = new SimpleStringProperty("0");
        simulationsInQueueProperty = new SimpleStringProperty("0");
    }

    @FXML
    private void initialize(){
        simulationsCompletedLabel.textProperty().bind(simulationsCompletedProperty);
        simulationsInProgressLabel.textProperty().bind(simulationsInProgressProperty);
        simulationsInQueueLabel.textProperty().bind(simulationsInQueueProperty);
    }

    @FXML
    void dogsMenuItemClicked(ActionEvent event) {
        mainController.setSkin(SkinsOptions.DOGS);
    }

    @FXML
    void defaultMenuItemClicked(ActionEvent event) {
        mainController.setSkin(SkinsOptions.DEFAULT);
    }

    @FXML
    void flowersMenuItemClicked(ActionEvent event) {
        mainController.setSkin(SkinsOptions.FLOWERS);
    }

    @FXML
    void loadFileButtonClicked(ActionEvent event) {
        try {
            String filePath = mainController.loadXML();
            if (!filePath.isEmpty())
                mainController.setSuccessMessage("File loaded successfully.");
        }
        catch(Exception e){
            mainController.setErrorMessage(e.getMessage());
        }
    }

    @FXML
    void detailsButtonClicked(ActionEvent event) {
        mainController.setIsDetailsClickedProperty(true);
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void bindToIsFXMLLoaded(){
        SimpleBooleanProperty isFileLoaded = mainController.getIsFileLoadedProperty();
        detailsToggleButton.disableProperty().bind(isFileLoaded.not());
        newExecutionToggleButton.disableProperty().bind(isFileLoaded.not());
    }

    public void bindXmlPathToTextField(SimpleStringProperty path){
        XMLFileTextField.textProperty().bind(path);
    }

    public void bindComponents(SimpleStringProperty path, BooleanProperty startButtonPressedProperty) {
        bindToIsFXMLLoaded();
        bindXmlPathToTextField(path);
        bindScreensToButtons();
        bindStartButton(startButtonPressedProperty);
    }

    public void bindScreensToButtons() {
        detailsToggleButton.selectedProperty().addListener(e -> {
            mainController.showFirstPage();
        });

        newExecutionToggleButton.selectedProperty().addListener(e -> {
            mainController.showSecondPage();
        });

        resultsToggleButton.selectedProperty().addListener(e -> {
            mainController.showThirdPage();
        });
        //todo: when we have the third screen

    }

    public void bindStartButton(BooleanProperty startButtonPressedProperty) {
        resultsToggleButton.disableProperty().bind(startButtonPressedProperty.not());
//        resultsToggleButton.selectedProperty().bind(startButtonPressedProperty.not());

        //todo:Add later after we press the start button we move directly to result page
    }

    public void setSimulationsCompletedProperty(String simulationsCompletedProperty) {
        this.simulationsCompletedProperty.set(simulationsCompletedProperty);
    }

    public void setSimulationsInProgressProperty(String simulationsInProgressProperty) {
        this.simulationsInProgressProperty.set(simulationsInProgressProperty);
    }

    public void setSimulationsInQueueProperty(String simulationsInQueueProperty) {
        this.simulationsInQueueProperty.set(simulationsInQueueProperty);
    }

    @FXML
    void withAnimationMenuItemClicked(ActionEvent event) {
        mainController.activeAnimations();
    }

    @FXML
    void withoutAnimationMenuItemClicked(ActionEvent event) {
        mainController.turnOffAnimations();
    }
}


