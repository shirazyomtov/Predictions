package header;

import app.AppController;
import javafx.animation.PauseTransition;
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
            setErrorMessage(e.getMessage());
        }
    }

    @FXML
    void detailsButtonClicked(ActionEvent event) {
        mainController.setIsDetailsClickedProperty(true);
    }

    public void setErrorMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-background-color: #fc6060; -fx-font-size: 14px; -fx-text-fill: white");
        pauseTransitionMessage();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(messageLabel.getText());

        // Show the alert and wait for user interaction
        alert.showAndWait();
    }

    private void setSuccessMessage() {
        messageLabel.setText("File loaded successfully.");
        messageLabel.setStyle("-fx-background-color: #D3EBCD; -fx-font-size: 14px; -fx-text-fill: black");
        pauseTransitionMessage();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(messageLabel.getText());

        // Show the alert and wait for user interaction
        alert.showAndWait();
    }

    private void pauseTransitionMessage(){
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {messageLabel.setText("");});
        pause.play();
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void bindToIsFXMLLoaded(){
        SimpleBooleanProperty isFileLoaded = mainController.getIsFileLoadedProperty();
        detailsToggleButton.disableProperty().bind(isFileLoaded.not());
    }

    public void bindXmlPathToTextField(SimpleStringProperty path){
        XMLFileTextField.textProperty().bind(path);
    }

    public void bindComponents(SimpleStringProperty path) {
        bindToIsFXMLLoaded();
        bindXmlPathToTextField(path);
    }
}


