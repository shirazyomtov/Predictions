package thirdPage;

import DTO.DTOSimulationInfo;
import app.AppController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;

import java.util.List;

public class ThirdPageController {

    @FXML
    private GridPane thirdPageGridPane;

    @FXML
    private TableColumn<?, ?> amountColumn;

    @FXML
    private TextField currentTickTextField;

    @FXML
    private ComboBox<?> displayModeComboBox;

    @FXML
    private TreeView<?> entitiesAndPropertiesTreeView;

    @FXML
    private ListView<?> entitiesListView;

    @FXML
    private TableView<?> entitiesTableView;

    @FXML
    private Pane graphPane;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private Button pauseButton;

    @FXML
    private Button rerunButton;

    @FXML
    private Button resumeButton;

    @FXML
    private TextField secondsCounterTextField;

    @FXML
    private Button stopButton;


    @FXML
    private ListView<DTOSimulationInfo> executionListView;

    private AppController mainController;

//    private List<DTOSimulationInfo> simulationInfoList = new ArrayList<>();

    public void setMainController(AppController appController) {
        this.mainController = appController;
    }

    public void setThirdPageDetails(List<DTOSimulationInfo> simulationInfos) {
        executionListView.getItems().clear();
        executionListView.getItems().addAll(simulationInfos);
    }

    @FXML
    void displayModeComboBoxClicked(ActionEvent event) {

    }

    @FXML
    void entitiesAndPropertiesTreeViewClicked(MouseEvent event) {

    }

    @FXML
    void executionListViewClicked(MouseEvent event) {

    }

    @FXML
    void pauseButtonClicked(ActionEvent event) {

    }

    @FXML
    void rerunButtonClicked(ActionEvent event) {

    }

    @FXML
    void resumeButtonClicked(ActionEvent event) {

    }

    @FXML
    void stopButtonClicked(ActionEvent event) {

    }


    public void setVisible(boolean state) {
        thirdPageGridPane.visibleProperty().set(state);
    }


    public Node getThirdPageGridPane() {
        return thirdPageGridPane;
    }
}
