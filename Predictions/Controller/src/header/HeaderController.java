package header;

import app.AppController;
import javafx.animation.PauseTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.util.Duration;

public class HeaderController {

    private AppController mainController;
    @FXML
    private ToggleGroup MenuBarBtn;

    @FXML
    private TextField XMLFileTextField;

    @FXML
    private MenuItem cookieMenuItem;

    @FXML
    private MenuItem defaultMenuItem;

    @FXML
    private ToggleButton detailsToggleButton;

    @FXML
    private MenuItem lionMenuItem;

    @FXML
    private Button loadFileButton;

    @FXML
    private ToggleButton newExecutionToggleButton;

    @FXML
    private MenuItem peachMenuItem;

    @FXML
    private Label predictionsLabel;

    @FXML
    private Button queueManagementButton;

    @FXML
    private ToggleButton resultsToggleButton;

    @FXML
    private Menu skinsMenu;

    @FXML
    private Label messageLabel;

    @FXML
    void cookieMenuItemClicked(ActionEvent event) {

    }

    @FXML
    void defaultMenuItemClicked(ActionEvent event) {

    }

    @FXML
    void lionMenuItemClicked(ActionEvent event) {

    }

    @FXML
    void peachMenuItemClicked(ActionEvent event) {

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
}


