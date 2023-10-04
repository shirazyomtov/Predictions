package header;

import app.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;

public class HeaderController {
    @FXML
    private ToggleButton executionsToggleButton;

    @FXML
    private Label predictionsLabel;

    @FXML
    private ToggleButton requestsToggleButton;

    @FXML
    private ToggleButton resultsToggleButton;

    @FXML
    private ToggleButton simulationDetailsToggleButton;

    @FXML
    private Label userNameTextField;
    private AppController mainController;

    @FXML
    private GridPane headerGridPane;

    public void setMainController(AppController appController) {
        this.mainController = appController;
    }

    public void bindScreensToButtons() {
        simulationDetailsToggleButton.selectedProperty().addListener(e -> {
            mainController.showSimulationDetailsPage();
        });

        requestsToggleButton.selectedProperty().addListener(e -> {
            mainController.showRequestsPage();
        });

//        executionsToggleButton.selectedProperty().addListener(e -> {
//            mainController.showExecutionsPage(selectedRequest.getRequestId(), selectedRequest.getWorldName());
//        });

        resultsToggleButton.selectedProperty().addListener(e -> {
            mainController.showResultsPage();
        });
    }

    public void bindComponents() {
        bindScreensToButtons();
    }

    @FXML
    void simulationDetailsButtonClicked(ActionEvent event) {

    }

    public void setVisbleRequestPage() {
        requestsToggleButton.setDisable(false);
    }

    public Node getGridPaneHeader() {
        return headerGridPane;
    }

    public void setUserName(String userName) {
        userNameTextField.setText(userName);
    }

    public String getUserName() {
        return userNameTextField.getText();
    }
}
