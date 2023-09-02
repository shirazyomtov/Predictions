package thirdPage;

import DTO.DTOActions.DTOActionInfo;
import DTO.DTOEntityInfo;
import DTO.DTOPropertyInfo;
import DTO.DTOSimulationInfo;
import app.AppController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private TextField currentTickTextField;

    @FXML
    private ComboBox<?> displayModeComboBox;

    @FXML
    private TreeView<String> entitiesAndPropertiesTreeView;

    @FXML
    private ListView<String> entitiesListView;

    @FXML
    private Pane graphPane;

    @FXML
    private TableView<DTOEntityInfo> entitiesTableView;

    @FXML
    private TableColumn<DTOEntityInfo, String> nameColumn;

    @FXML
    private TableColumn<DTOEntityInfo, String> amountColumn;

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
    private GridPane simulationInfoGridPane;

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
                state = "(Running)";
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
        simulationInfoGridPane.setVisible(true);
        if(selectedSimulation.getFinish()){
            pauseButton.setVisible(false);
            resumeButton.setVisible(false);
            stopButton.setVisible(false);
            endedSimulationInfoScrollPane.setVisible(true);
        }
        else{
            pauseButton.setVisible(true);
            resumeButton.setVisible(true);
            stopButton.setVisible(true);
            endedSimulationInfoScrollPane.setVisible(false);
        }

        setDetails(selectedSimulation);
    }

    private void setDetails(DTOSimulationInfo selectedSimulation) {
        currentTickTextField.setText(mainController.getEngineManager().getCurrentTick(selectedSimulation.getSimulationId()).toString());
        secondsCounterTextField.setText(mainController.getEngineManager().getCurrentSecond(selectedSimulation.getSimulationId()).toString());
        List<DTOEntityInfo> entityInfos = mainController.getEngineManager().getAllAmountOfEntities(selectedSimulation.getSimulationId());
        addEntitiesDetails(entityInfos);
        if (selectedSimulation.getFinish()){
            setEntitiesAndProperties(entityInfos);
            setEntities(entityInfos);
        }
    }


    private void addEntitiesDetails(List<DTOEntityInfo> allAmountOfEntities) {
        entitiesTableView.getItems().clear();
        entitiesTableView.getItems().addAll(allAmountOfEntities);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("finalAmount"));
        //todo: after we have the simulation that we can see running change the amount to current amount
    }


    private void setEntitiesAndProperties(List<DTOEntityInfo> allAmountOfEntities) {
        TreeItem<String> root = new TreeItem<>("Entities");
        for (DTOEntityInfo dtoEntityInfo : allAmountOfEntities) {
            TreeItem<String> entityBranch = new TreeItem<>(dtoEntityInfo.getEntityName());
            for(DTOPropertyInfo dtoPropertyInfo: dtoEntityInfo.getProperties()){
                TreeItem<String> leaf = new TreeItem<>(dtoPropertyInfo.getName());
                entityBranch.getChildren().add(leaf);
            }
            root.getChildren().add(entityBranch);
        }
        entitiesAndPropertiesTreeView.setRoot(root);

    }

    private void setEntities(List<DTOEntityInfo> allAmountOfEntities) {
        for(DTOEntityInfo dtoEntityInfo: allAmountOfEntities){
            entitiesListView.getItems().add(dtoEntityInfo.getEntityName());
        }
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
