package thirdPage;

import DTO.DTOActions.DTOActionInfo;
import DTO.DTOEntityInfo;
import DTO.DTOPropertyInfo;
import DTO.DTOSimulationInfo;
import app.AppController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
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
    private Label displayModeLabel;

    @FXML
    private ComboBox<String> displayModeComboBox;

    @FXML
    private TreeView<String> entitiesAndPropertiesTreeView;



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

    //Graphs population change
    @FXML
    private LineChart<?, ?> amountOfEntitiesLineChart;

    @FXML
    private ListView<String> entitiesListView;

    @FXML
    private ScrollPane endedSimulationInfoScrollPane;

    private AppController mainController;

    private List<DTOSimulationInfo> allDTOSimulationList;

    private DTOSimulationInfo selectedSimulation;

    private List<DTOEntityInfo> chosenSimulationEntities;

    public void setMainController(AppController appController) {
        this.mainController = appController;
    }

    public void setThirdPageDetails(List<DTOSimulationInfo> simulationInfos) {
        displayModeLabel.setVisible(false);
        displayModeComboBox.setVisible(false);
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
            selectedSimulation = allDTOSimulationList.get(selectedIndex);
            setSpecificSimulationDetails();
        }
        else{
            //todo: add message that the user need to chose one of the action it actions name
        }
    }

    private void setSpecificSimulationDetails() {
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

        setDetails();
    }

    private void setDetails() {
        currentTickTextField.setText(mainController.getEngineManager().getCurrentTick(selectedSimulation.getSimulationId()).toString());
        secondsCounterTextField.setText(mainController.getEngineManager().getCurrentSecond(selectedSimulation.getSimulationId()).toString());
        chosenSimulationEntities = mainController.getEngineManager().getAllAmountOfEntities(selectedSimulation.getSimulationId());
        addEntitiesDetails();
        if (selectedSimulation.getFinish()){
            setEntitiesAndProperties();
            setEntities();
        }
    }


    private void addEntitiesDetails() {
        entitiesTableView.getItems().clear();
        entitiesTableView.getItems().addAll(chosenSimulationEntities);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("finalAmount"));
        //todo: after we have the simulation that we can see running change the amount to current amount
    }


    private void setEntitiesAndProperties() {
        TreeItem<String> root = new TreeItem<>("Entities");
        for (DTOEntityInfo dtoEntityInfo : chosenSimulationEntities) {
            TreeItem<String> entityBranch = new TreeItem<>(dtoEntityInfo.getEntityName());
            for(DTOPropertyInfo dtoPropertyInfo: dtoEntityInfo.getProperties()){
                TreeItem<String> leaf = new TreeItem<>(dtoPropertyInfo.getName());
                entityBranch.getChildren().add(leaf);
            }
            root.getChildren().add(entityBranch);
        }
        entitiesAndPropertiesTreeView.setRoot(root);

    }

    private void setEntities() {
        entitiesListView.getItems().clear();
        for(DTOEntityInfo dtoEntityInfo: chosenSimulationEntities){
            entitiesListView.getItems().add(dtoEntityInfo.getEntityName());
        }
    }


    @FXML
    void entitiesListViewClicked(MouseEvent event) {
        //todo: add a function and data to save the info about amount of each entity in each tick
    }

    @FXML
    void displayModeComboBoxClicked(ActionEvent event) {

    }

    @FXML
    void entitiesAndPropertiesTreeViewClicked(MouseEvent event) {
        displayModeComboBox.getItems().clear();
        displayModeComboBox.getItems().add("Histogram of population");
        displayModeComboBox.getItems().add("Consistency");
        TreeItem<String> selectedItem = entitiesAndPropertiesTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (!selectedItem.isLeaf()) {
                return;
            }

            String propertyName = selectedItem.getValue();
            TreeItem<String> entityItem = selectedItem.getParent();
            if (entityItem != null) {
                String entityName = entityItem.getValue();
                setComboBox(entityName, propertyName);
            }
        }
    }

    private void setComboBox(String entityName, String propertyName) {
        chosenSimulationEntities.stream()
                .filter(dtoEntityInfo -> dtoEntityInfo.getEntityName().equals(entityName))
                .findFirst()
                .ifPresent(entity -> {
                    entity.getProperties().stream()
                            .filter(dtoPropertyInfo -> dtoPropertyInfo.getName().equals(propertyName))
                            .filter(dtoPropertyInfo -> dtoPropertyInfo.getType().equals("FLOAT"))
                            .forEach(dtoPropertyInfo -> {
                                String displayMode = "Average value";
                                displayModeComboBox.getItems().add(displayMode);
                            });
                });
        displayModeLabel.setVisible(true);
        displayModeComboBox.setVisible(true);
    }

    @FXML
    void pauseButtonClicked(ActionEvent event) {

    }

    @FXML
    void resumeButtonClicked(ActionEvent event) {

    }

    @FXML
    void stopButtonClicked(ActionEvent event) {

    }

    @FXML
    void rerunButtonClicked(ActionEvent event) {
        if(selectedSimulation.getFinish()) {
            mainController.setSecondPageDetails(selectedSimulation.getSimulationId());
            mainController.showSecondPage();
        }
        else {
            mainController.setErrorMessage("You cannot rerun a simulation that is still running");
        }
    }

    public void setVisible(boolean state) {
        thirdPageGridPane.visibleProperty().set(state);
    }


    public Node getThirdPageGridPane() {
        return thirdPageGridPane;
    }

    public void clearAllData() {
        simulationInfoGridPane.setVisible(false);
        endedSimulationInfoScrollPane.setVisible(false);
        entitiesListView.getItems().clear();
        entitiesAndPropertiesTreeView.setRoot(null);
        entitiesListView.getItems().clear();

    }
}
