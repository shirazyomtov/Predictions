package header;

import app.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;

public class HeaderController {

    @FXML
    private ToggleButton allocationsToggleButton;

    @FXML
    private ToggleButton executionsHistoryToggleButton;

    @FXML
    private ToggleButton managementToggleButton;

    @FXML
    private Label predictionsLabel;
    private AppController mainController;

    public void setMainController(AppController appController) {
        this.mainController = appController;
    }

    public void bindScreensToButtons() {
        managementToggleButton.selectedProperty().addListener(e -> {
            mainController.showManagementPage();
        });

        allocationsToggleButton.selectedProperty().addListener(e -> {
            mainController.showAllocationsPage();
        });

        executionsHistoryToggleButton.selectedProperty().addListener(e -> {
            mainController.showResultsPage();
        });
    }

    public void bindComponents() {
        bindScreensToButtons();
    }


    @FXML
    void managementButtonClicked(ActionEvent event) {

    }

    public void setAllcotionButtonVisble() {
        allocationsToggleButton.setDisable(false);
    }

    public void setVisbleResultsPage() {
        executionsHistoryToggleButton.setDisable(false);
    }
}
