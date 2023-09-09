package thirdPage;

import DTO.DTOEntityInfo;
import DTO.DTOPropertyInfo;
import DTO.DTOSimulationInfo;
import app.AppController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import thirdPage.averageValueOfProperty.AverageValueOfProperty;
import thirdPage.histogramOfPopulation.HistogramOfPopulation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ThirdPageController {

    private static final String HISTOGRAM_FXML_LIGHT_RESOURCE = "/thirdPage/histogramOfPopulation/histogramOfPopulation.fxml";
    private static final String AVERAGE_VALUE_FXML_LIGHT_RESOURCE = "/thirdPage/averageValueOfProperty/averageValueOfProperty.fxml";
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
    private LineChart<Integer, Integer> amountOfEntitiesLineChart;

    @FXML
    private ListView<String> entitiesListView;

    @FXML
    private ScrollPane endedSimulationInfoScrollPane;

    @FXML
    private Pane staticInfoPane;

    private AppController mainController;

    private List<DTOSimulationInfo> allDTOSimulationList;

    private DTOSimulationInfo selectedSimulation;

//    private List<DTOEntityInfo> chosenSimulationEntities;

    private HistogramOfPopulation histogramOfPopulation;

    private AverageValueOfProperty averageValueOfProperty;

    private SimpleBooleanProperty isDisplayModePressed;

    private SimpleLongProperty currentTicksProperty;
    private SimpleLongProperty currentSecondsProperty;

    private SimulationTask simulationTask = null;

    private FinishSimulationTask finishSimulationTask = null;

    private SimpleBooleanProperty isFinishProperty;
    private Consumer<List<DTOEntityInfo>> updateTableViewConsumer;

    private Consumer<List<DTOSimulationInfo>> updateFinishSimulationConsumer;

    private List<Integer> finishSimulations = new ArrayList<>();

    public ThirdPageController(){
        this.updateTableViewConsumer = (chosenSimulationEntities) -> {
            Platform.runLater(() -> {
                addEntitiesDetails(chosenSimulationEntities);
            });
        };
        this.updateFinishSimulationConsumer = (allSimulation) -> {
            Platform.runLater(() -> {
                shiraz(allSimulation);
            });
        };
        isDisplayModePressed = new SimpleBooleanProperty(false);
        this.currentTicksProperty = new SimpleLongProperty(0);
        this.currentSecondsProperty = new SimpleLongProperty(0);
        this.isFinishProperty = new SimpleBooleanProperty(false);
    }

    private void shiraz(List<DTOSimulationInfo> allSimulation) {
        boolean flag = false;
        for(DTOSimulationInfo dtoSimulationInfo: allSimulation){
            if(dtoSimulationInfo.getFinish()){
                for (Integer simulation: finishSimulations) {
                    if (simulation.equals(dtoSimulationInfo.getSimulationId())){
                        flag = true;
                    }
                }
                if(!flag) {
                    finishSimulations.add(dtoSimulationInfo.getSimulationId());
                    executionListView.getItems().set(dtoSimulationInfo.getSimulationId() - 1, "(Ended) Simulation ID: " + dtoSimulationInfo.getSimulationId() + ", Date: " + dtoSimulationInfo.getSimulationDate());
                    mainController.setSuccessMessage("The simulation " + dtoSimulationInfo.getSimulationId() + " has ended");
                }
            }
            flag = false;
        }
    }

    @FXML
    public void initialize() throws Exception {
        loadResourcesStaticData();
        if(histogramOfPopulation != null && averageValueOfProperty!= null){
            staticInfoPane.visibleProperty().bind(isDisplayModePressed);
        }
        currentTickTextField.textProperty().bind(Bindings.format("%,d", currentTicksProperty));
        secondsCounterTextField.textProperty().bind(Bindings.format("%,d", currentSecondsProperty));
        isFinishProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                updateFinishSimulation();
            }
        });
    }

    private void loadResourcesStaticData() throws IOException {
        loadResourcesHistogramProperty();
        loadResourcesAverageValueProperty();
    }


    private void loadResourcesHistogramProperty() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(HISTOGRAM_FXML_LIGHT_RESOURCE);
        fxmlLoader.setLocation(url);
        InputStream inputStream = url.openStream();
        BarChart barChart = fxmlLoader.load(inputStream);
        histogramOfPopulation = fxmlLoader.getController();
    }


    private void loadResourcesAverageValueProperty() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(AVERAGE_VALUE_FXML_LIGHT_RESOURCE);
        fxmlLoader.setLocation(url);
        InputStream inputStream = url.openStream();
        VBox vBox = fxmlLoader.load(inputStream);
        averageValueOfProperty = fxmlLoader.getController();
    }

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
            selectedSimulation = mainController.getEngineManager().getAllPastSimulation().get(selectedIndex);
            setSpecificSimulationDetails();
        }
        else{
            //todo: add message that the user need to chose one of the action it actions name
        }
    }

    private void setSpecificSimulationDetails() {
        createTaskOfSimulation();
        simulationInfoGridPane.setVisible(true);
        if(selectedSimulation.getFinish()){
            setFinishSimulationComponentsVisible();
        }
        else{
            pauseButton.setVisible(true);
            resumeButton.setVisible(true);
            stopButton.setVisible(true);
            endedSimulationInfoScrollPane.setVisible(false);
        }

//        setDetails();
    }

    private void setFinishSimulationComponentsVisible(){
        pauseButton.setVisible(false);
        resumeButton.setVisible(false);
        stopButton.setVisible(false);
        endedSimulationInfoScrollPane.setVisible(true);
    }

    private void setFinishSimulationDetails(){
        List<DTOEntityInfo> finalDTOEntities = mainController.getEngineManager().getAllDetailsOfEndedSimulation(selectedSimulation.getSimulationId());
        setEntitiesAndProperties(finalDTOEntities);
        setEntities(finalDTOEntities);
    }

//    private void setDetails() {
//        currentTickTextField.setText(mainController.getEngineManager().getCurrentTick(selectedSimulation.getSimulationId()).toString());
//        secondsCounterTextField.setText(mainController.getEngineManager().getCurrentSecond(selectedSimulation.getSimulationId()).toString());
//        List<DTOEntityInfo> chosenSimulationEntities = mainController.getEngineManager().getAllAmountOfEntities(selectedSimulation.getSimulationId());
//        addEntitiesDetails(chosenSimulationEntities);
//        if (selectedSimulation.getFinish()){
//            List<DTOEntityInfo> finalDTOEntities = mainController.getEngineManager().getAllDetailsOfEndedSimulation(selectedSimulation.getSimulationId());
//            setEntitiesAndProperties(finalDTOEntities);
//            setEntities(finalDTOEntities);
//        }
//    }


    private void addEntitiesDetails(List<DTOEntityInfo> chosenSimulationEntities) {
        entitiesTableView.getItems().clear();
        entitiesTableView.getItems().addAll(chosenSimulationEntities);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("finalAmount"));
        //todo: after we have the simulation that we can see running change the amount to current amount
    }


    private void setEntitiesAndProperties(List<DTOEntityInfo> finalDTOEntities) {
        TreeItem<String> root = new TreeItem<>("Entities");
        for (DTOEntityInfo dtoEntityInfo : finalDTOEntities) {
            TreeItem<String> entityBranch = new TreeItem<>(dtoEntityInfo.getEntityName());
            for(DTOPropertyInfo dtoPropertyInfo: dtoEntityInfo.getProperties()){
                TreeItem<String> leaf = new TreeItem<>(dtoPropertyInfo.getName());
                entityBranch.getChildren().add(leaf);
            }
            root.getChildren().add(entityBranch);
        }
        entitiesAndPropertiesTreeView.setRoot(root);

    }

    private void setEntities(List<DTOEntityInfo> finalDTOEntities) {
        entitiesListView.getItems().clear();
        for(DTOEntityInfo dtoEntityInfo: finalDTOEntities){
            entitiesListView.getItems().add(dtoEntityInfo.getEntityName());
        }
    }


    @FXML
    void entitiesListViewClicked(MouseEvent event) {
        //todo: add a function and data to save the info about amount of each entity in each tick
//        String entitiesName = entitiesListView.getSelectionModel().getSelectedItem();
//        Map<Integer, Map<String, Integer>> amountOfAllEntities = mainController.getEngineManager().getAmountOfEntitiesPerTick(selectedSimulation.getSimulationId());
//        if(entitiesName != null) {
//            createGraphOfEntityPerTick(amountOfAllEntities, entitiesName);
//        }
    }

//    private void createGraphOfEntityPerTick(Map<Integer, Map<String, Integer>> amountOfAllEntities, String entitiesName) {
//        amountOfEntitiesLineChart.getData().clear();
//        NumberAxis xAxis = new NumberAxis();
//        NumberAxis yAxis = new NumberAxis();
//        xAxis.setLabel("Tick"); // Label for X-axis
//        yAxis.setLabel("Amount"); // Label for Y-axis
//
//        XYChart.Series<Integer, Integer> series = new XYChart.Series<>();
//        series.setName(entitiesName);
//
//        for (Map.Entry<Integer, Map<String, Integer>> entry : amountOfAllEntities.entrySet()) {
//            Integer tick = entry.getKey();
//            Map<String, Integer> entityData = entry.getValue();
//
//            Integer amount = entityData.get(entitiesName);
//
//            if (amount != null) {
//                series.getData().add(new XYChart.Data<>(tick, amount));
//            }
//        }
//
//        amountOfEntitiesLineChart.getData().add(series);
//    }

    @FXML
    void displayModeComboBoxClicked(ActionEvent event) {
        isDisplayModePressed.set(true);
        staticInfoPane.getChildren().clear();
        String selectedDisplay = displayModeComboBox.getSelectionModel().getSelectedItem();
        TreeItem<String> selectedItem = entitiesAndPropertiesTreeView.getSelectionModel().getSelectedItem();
        String propertyName = selectedItem.getValue();
        String entityName = selectedItem.getParent().getValue();
        Map<Object, Integer> propertyInfoAboutValues = mainController.getEngineManager().createPropertyValuesMap(selectedSimulation.getSimulationId(), entityName, propertyName);
        if(selectedDisplay != null) {
            switch (selectedDisplay) {
                case "Histogram of population":
                    staticInfoPane.getChildren().add(histogramOfPopulation.getHistogramBarChart());
                    histogramOfPopulation.createBarChart(propertyInfoAboutValues);
                    break;
                case "Consistency":
                    break;
                case "Average value":
                    staticInfoPane.getChildren().add(averageValueOfProperty.getAverageValueVbox());
                    averageValueOfProperty.createAverageValueOfProperty(propertyInfoAboutValues);
                    break;
            }
        }
    }

    @FXML
    void entitiesAndPropertiesTreeViewClicked(MouseEvent event) {
        displayModeComboBox.getItems().clear();
        displayModeComboBox.getItems().add("Histogram of population");
        displayModeComboBox.getItems().add("Consistency");
        TreeItem<String> selectedItem = entitiesAndPropertiesTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (!selectedItem.isLeaf()) {
                displayModeComboBox.setVisible(false);
                displayModeLabel.setVisible(false);
                return;
            }

            String propertyName = selectedItem.getValue();
            TreeItem<String> entityItem = selectedItem.getParent();
            if (entityItem != null) {
                String entityName = entityItem.getValue();
                setComboBox(entityName, propertyName);
            }
        }
        else{
            displayModeComboBox.setVisible(false);
            displayModeLabel.setVisible(false);
        }
    }

    private void setComboBox(String entityName, String propertyName) {
        List<DTOEntityInfo> finalDTOEntities = mainController.getEngineManager().getAllDetailsOfEndedSimulation(selectedSimulation.getSimulationId());
        finalDTOEntities.stream()
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
//        selectedSimulation = mainController.getEngineManager().getAllPastSimulation().get(selectedSimulation.getSimulationId() - 1);
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

    public void createTaskOfSimulation() {
        if(simulationTask == null) {
            simulationTask = new SimulationTask(selectedSimulation.getSimulationId(), mainController.getEngineManager(), currentTicksProperty, currentSecondsProperty, isFinishProperty, updateTableViewConsumer);
            new Thread(simulationTask).start();
        }
        else {
            simulationTask.setSimulationId(selectedSimulation.getSimulationId());
        }
    }

    private void updateFinishSimulation(){
        Platform.runLater(() -> {
            selectedSimulation = mainController.getEngineManager().getAllPastSimulation().get(selectedSimulation.getSimulationId() - 1);
            setFinishSimulationComponentsVisible();
            setFinishSimulationDetails();
        });
    }

    public void setSimulationTask(SimulationTask simulationTask) {
        this.simulationTask = simulationTask;
    }

    public void createFinishSimulationTask() {
        if(finishSimulationTask == null) {
            finishSimulations.clear();
            finishSimulationTask = new FinishSimulationTask(updateFinishSimulationConsumer, mainController.getEngineManager());
            new Thread(finishSimulationTask).start();
        }
    }

    public void setFinishSimulationTask(FinishSimulationTask finishSimulationTask) {
        this.finishSimulationTask = finishSimulationTask;
    }
}

