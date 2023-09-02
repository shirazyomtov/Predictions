package thirdPage;

import DTO.DTOActions.DTOActionInfo;
import DTO.DTOSimulationInfo;
import app.AppController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
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
    private SplitPane simulationInfoSplitPane;

    @FXML
    private ListView<String> executionListView;

    @FXML
    private ScrollPane endedSimulationInfoScrollPane;

    private AppController mainController;

    private List<DTOSimulationInfo> allDTOSimulationList;

    public void setMainController(AppController appController) {
        this.mainController = appController;
    }

    public void setThirdPageDetails(List<DTOSimulationInfo> simulationInfos) {
        allDTOSimulationList = simulationInfos;
        executionListView.getItems().clear();
        addSimulationsToExecutionListView(simulationInfos);
    }

    private void addSimulationsToExecutionListView(List<DTOSimulationInfo> simulationInfos) {
        String state;
        for(DTOSimulationInfo dtoSimulationInfo: simulationInfos){
            if(dtoSimulationInfo.getFinish()){
                state = "(Ended)";
            }
            else{
                state = "(Active)";
            }
            executionListView.getItems().add(state + " Simulation ID: " + dtoSimulationInfo.getSimulationId() + ", Date: " + dtoSimulationInfo.getSimulationDate());
        }
    }

    @FXML
    void executionListViewClicked(MouseEvent event) {
        int selectedIndex = executionListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            DTOSimulationInfo selectedSimulation = allDTOSimulationList.get(selectedIndex);
            setSpecificSimulationDetails(selectedSimulation);
        }
        else{
            //todo: add message that the user need to chose one of the action it actions name
        }
    }

    private void setSpecificSimulationDetails(DTOSimulationInfo selectedSimulation) {
        simulationInfoSplitPane.setVisible(true);
        if(!selectedSimulation.getFinish()){
            pauseButton.setVisible(false);
            rerunButton.setVisible(false);
            stopButton.setVisible(false);
        }
        else{
            endedSimulationInfoScrollPane.setVisible(false);
        }

        setDetails(selectedSimulation);
    }

    private void setDetails(DTOSimulationInfo selectedSimulation) {
//        currentTickTextField.setText(selectedSimulation.get);
//        secondsCounterTextField.setText(selectedSimulation.get);
//        addEntitiesDetails(selectedSimulation);
    }

    @FXML
    void displayModeComboBoxClicked(ActionEvent event) {

    }

    @FXML
    void entitiesAndPropertiesTreeViewClicked(MouseEvent event) {

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
