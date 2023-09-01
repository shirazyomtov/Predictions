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
            String filePath = mainController.loadXML(); // status = -1 if user close dialog without choosing file
            if (!filePath.isEmpty())
                setSuccessMessage();
        }
        catch(Exception e){
            mainController.setErrorMessage(e.getMessage());
//            setErrorMessage(e.getMessage());
        }
    }

    @FXML
    void detailsButtonClicked(ActionEvent event) {
        mainController.setIsDetailsClickedProperty(true);
    }

    private void setSuccessMessage() {
        //todo: maybe move to main controller like I did in error message
        pauseTransitionMessage();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("File loaded successfully.");
        alert.showAndWait();
    }

    private void pauseTransitionMessage(){
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.play();
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
//        resultsToggleButton.selectedProperty().bind(startButtonPressedProperty);

        //todo:Add later after we press the start button we move directly to result page
    }
}


