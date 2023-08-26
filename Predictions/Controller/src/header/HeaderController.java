package header;

import app.AppController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
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
            int status = mainController.loadXML(); // status = -1 if user close dialog without choosing file
            if (status != -1)
                setSuccessMessage("File loaded successfully.");
        }
        catch(Exception e){
            setErrorMessage(e.getMessage());
        }
    }

    private void setErrorMessage(String message) {
        messageLabel.setText(message);
    }

    private void setSuccessMessage(String message) {
        messageLabel.setText(message);
    }


    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

}


