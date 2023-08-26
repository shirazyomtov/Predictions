package firstPage;

import app.AppController;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

public class FirstPageController {

    private AppController mainController;

    @FXML
    private SplitPane smallSplitPane;

    @FXML
    private BorderPane codeCalibrationComponent;

    @FXML
    private BorderPane codeConfigComponent;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
}
