package thirdPage;

import DTO.DTOEntityInfo;
import DTO.DTOHistogram;
import DTO.DTOPropertyInfo;
import DTO.DTOSimulationInfo;
import app.AppController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import thirdPage.averageTickOfProperty.AverageTickOfProperty;
import thirdPage.averageValueOfProperty.AverageValueOfProperty;
import thirdPage.histogramOfPopulation.HistogramOfPopulation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ThirdPageController {

    private static final String HISTOGRAM_FXML_LIGHT_RESOURCE = "/thirdPage/histogramOfPopulation/histogramOfPopulation.fxml";
    private static final String AVERAGE_VALUE_FXML_LIGHT_RESOURCE = "/thirdPage/averageValueOfProperty/averageValueOfProperty.fxml";

    private static final String AVERAGE_TICK_PROPERTY_FXML_LIGHT_RESOURCE = "/thirdPage/averageTickOfProperty/averageTickOfProperty.fxml";

    @FXML
    private TabPane allEndedSimulationInfoTabPane;

    @FXML
    private Tab detailsAboutSimulationTab;

    @FXML
    private Tab graphsPopulationChangeTab;

    @FXML
    private Tab staticInformationPropertiesTab;

    @FXML
    private Label simulationDetailsLabel;

    @FXML
    private Label failedSimulationCauseLabel;

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
    private ScrollPane thirdPageScrollPane;

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

    @FXML
    private SplitPane thirdPageSplitPane;

    @FXML
    private SplitPane rightSplitPaneThirdPage;

    private AppController mainController;

    private List<DTOSimulationInfo> allDTOSimulationList;

    private DTOSimulationInfo selectedSimulation;

    private HistogramOfPopulation histogramOfPopulation;

    private AverageValueOfProperty averageValueOfProperty;

    private AverageTickOfProperty averageTickOfProperty;

    private SimpleBooleanProperty isDisplayModePressed;

    private SimpleLongProperty currentTicksProperty;
    private SimpleLongProperty currentSecondsProperty;

    private SimpleBooleanProperty isFailedProperty;

    private SimulationTask simulationTask = null;

    private FinishSimulationTask finishSimulationTask = null;

    private SimpleBooleanProperty isFinishProperty;
    private Consumer<List<DTOEntityInfo>> updateTableViewConsumer;

    private Consumer<List<DTOSimulationInfo>> updateFinishSimulationConsumer;

    private List<Integer> finishSimulations = new ArrayList<>();
    private List<Integer> failedSimulations = new ArrayList<>();
    private Integer amountOfSimulations = 0;
    private Integer amountOfSimulationsEnded = 0;
    @FXML
    private Button pastButton;
    @FXML
    private Button futureButton;
    private boolean resumeAfterPastTick = false;

    private Map<Integer, Boolean> pauseButtonPressed = new HashMap<>();

    private Map<Integer, Boolean> resumeButtonPressed = new HashMap<>();

    private SimpleStringProperty messageProperty;

    public ThirdPageController(){
        this.updateTableViewConsumer = (chosenSimulationEntities) -> {
            Platform.runLater(() -> {
                addEntitiesDetails(chosenSimulationEntities);
            });
        };
        this.updateFinishSimulationConsumer = (allSimulation) -> {
            Platform.runLater(() -> {
                updateAllFinishSimulation(allSimulation);
            });
        };
        isDisplayModePressed = new SimpleBooleanProperty(false);
        this.currentTicksProperty = new SimpleLongProperty(0);
        this.currentSecondsProperty = new SimpleLongProperty(0);
        this.isFinishProperty = new SimpleBooleanProperty(false);
        this.messageProperty = new SimpleStringProperty("");
        this.isFailedProperty = new SimpleBooleanProperty(false);
    }


    private void updateAllFinishSimulation(List<DTOSimulationInfo> allSimulation) {
        boolean flag = false;
        for(DTOSimulationInfo dtoSimulationInfo: allSimulation){
            if(dtoSimulationInfo.getFinish()){
                for (Integer simulation: finishSimulations) {
                    if (simulation.equals(dtoSimulationInfo.getSimulationId())){
                        flag = true;
                    }
                }
                if(!flag) {
                    amountOfSimulationsEnded++;
                    setQueueAndProgressSimulation();
                    mainController.setAmountOfCompletedSimulation(amountOfSimulationsEnded.toString());
                    finishSimulations.add(dtoSimulationInfo.getSimulationId());
                    if (!dtoSimulationInfo.getFailed()) {
                        executionListView.getItems().set(dtoSimulationInfo.getSimulationId() - 1, "(Ended) Simulation ID: " + dtoSimulationInfo.getSimulationId() + ", Date: " + dtoSimulationInfo.getSimulationDate());
                        mainController.setSuccessMessage("The simulation " + dtoSimulationInfo.getSimulationId() + " has ended");
                    }
                    else{
                        executionListView.getItems().set(dtoSimulationInfo.getSimulationId() - 1, "(Failed) Simulation ID: " + dtoSimulationInfo.getSimulationId() + ", Date: " + dtoSimulationInfo.getSimulationDate());
                        mainController.setErrorMessage("The simulation " + dtoSimulationInfo.getSimulationId() + " has failed due to: " + dtoSimulationInfo.getMessage());
                    }
                }
            }
            flag = false;
        }
    }

    @FXML
    public void initialize() throws Exception {
        loadResourcesStaticData();
        if(histogramOfPopulation != null && averageValueOfProperty!= null && averageTickOfProperty != null){
            staticInfoPane.visibleProperty().bind(isDisplayModePressed);
        }
        currentTickTextField.textProperty().bind(Bindings.format("%d", currentTicksProperty));
        secondsCounterTextField.textProperty().bind(Bindings.format("%d", currentSecondsProperty));
        isFinishProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                updateFinishSimulation();
            }
        });
        isFailedProperty.addListener((observable, oldValue, newValue) -> {
            if(newValue){
                Platform.runLater(() -> {
                    updateFailedSimulation();
                });
            }
        });
        thirdPageSplitPane.setDividerPositions(0.2);
        thirdPageSplitPane.getDividers().get(0).positionProperty().addListener((Observable, oldValue, newValue) -> {
            thirdPageSplitPane.setDividerPositions(0.2);
        });
        thirdPageSplitPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            thirdPageSplitPane.setDividerPositions(0.2);
        });
        rightSplitPaneThirdPage.setDividerPositions(0.4);
        rightSplitPaneThirdPage.getDividers().get(0).positionProperty().addListener((Observable, oldValue, newValue) -> {
            rightSplitPaneThirdPage.setDividerPositions(0.4);
        });
        rightSplitPaneThirdPage.widthProperty().addListener((observable, oldValue, newValue) -> {
            rightSplitPaneThirdPage.setDividerPositions(0.4);
        });
    }

    private void loadResourcesStaticData() throws IOException {
        loadResourcesHistogramProperty();
        loadResourcesAverageValueProperty();
        loadResourcesAverageTickProperty();
    }


    private void loadResourcesHistogramProperty() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(HISTOGRAM_FXML_LIGHT_RESOURCE);
        fxmlLoader.setLocation(url);
        InputStream inputStream = url.openStream();
        ScrollPane scrollPane = fxmlLoader.load(inputStream);
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


    private void loadResourcesAverageTickProperty() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(AVERAGE_TICK_PROPERTY_FXML_LIGHT_RESOURCE);
        fxmlLoader.setLocation(url);
        InputStream inputStream = url.openStream();
        VBox vBox = fxmlLoader.load(inputStream);
        averageTickOfProperty = fxmlLoader.getController();
    }

    public void setMainController(AppController appController) {
        this.mainController = appController;
    }

    public void setThirdPageDetails(List<DTOSimulationInfo> simulationInfos) {
        addSimulationsToMaps(simulationInfos);
        displayModeLabel.setVisible(false);
        displayModeComboBox.setVisible(false);
        allDTOSimulationList = simulationInfos;
        executionListView.getItems().clear();
        addSimulationsToExecutionListView(simulationInfos);
    }

    private void addSimulationsToMaps(List<DTOSimulationInfo> simulationInfos) {
        for(DTOSimulationInfo dtoSimulationInfo: simulationInfos){
            if(!(pauseButtonPressed.containsKey(dtoSimulationInfo.getSimulationId()))){
                pauseButtonPressed.put(dtoSimulationInfo.getSimulationId(), false);
            }
            if(!(resumeButtonPressed.containsKey(dtoSimulationInfo.getSimulationId()))){
                resumeButtonPressed.put(dtoSimulationInfo.getSimulationId(), false);
            }
        }
    }

    private void addSimulationsToExecutionListView(List<DTOSimulationInfo> simulationInfos) {
        String state;
        for(DTOSimulationInfo dtoSimulationInfo: simulationInfos){
            if(dtoSimulationInfo.getFinish()){
                if(dtoSimulationInfo.getFailed()){
                    state = "(Failed)";
                }
                else {
                    state = "(Ended)";
                }
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
            if(!selectedSimulation.getFailed()) {
                setFinishSimulationComponentsVisible();
                setTabOfFinishSimulation();
            }
            else{
                mainController.getEngineManager().stop(selectedSimulation.getSimulationId());
                setFinishComponentsOfFailedAndEndedSimulation();
                setTabFailedSimulation();
            }
        }
        else{
            pauseButton.setVisible(true);
            resumeButton.setVisible(true);
            stopButton.setVisible(true);
            rerunButton.setVisible(false);
            endedSimulationInfoScrollPane.setVisible(false);

            if(pauseButtonPressed.get(selectedSimulation.getSimulationId())){
                futureButton.setVisible(true);
                pastButton.setVisible(true);
            }
            else{
                futureButton.setVisible(false);
                pastButton.setVisible(false);
            }
        }
    }

    private void setFinishSimulationComponentsVisible(){
        setFinishComponentsOfFailedAndEndedSimulation();
        endedSimulationInfoScrollPane.setVisible(true);
        allEndedSimulationInfoTabPane.getTabs().remove(detailsAboutSimulationTab);
    }

    private void setFinishComponentsOfFailedAndEndedSimulation(){
        pauseButton.setVisible(false);
        resumeButton.setVisible(false);
        stopButton.setVisible(false);
        rerunButton.setVisible(true);
    }

    private void setFinishSimulationDetails(){
        List<DTOEntityInfo> finalDTOEntities = mainController.getEngineManager().getAllDetailsOfEndedSimulation(selectedSimulation.getSimulationId());
        setEntitiesAndProperties(finalDTOEntities);
        setEntities();
    }

    private void addEntitiesDetails(List<DTOEntityInfo> chosenSimulationEntities) {
        entitiesTableView.getItems().clear();
        entitiesTableView.getItems().addAll(chosenSimulationEntities);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("finalAmount"));
        //todo: after we have the simulation that we can see running change the amount to current amount
    }


    private void setEntitiesAndProperties(List<DTOEntityInfo> finalDTOEntities) {
        entitiesAndPropertiesTreeView.setRoot(null);
        if(displayModeComboBox.getItems() != null){
            displayModeComboBox.getItems().clear();
        }
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

    private void setEntities() {
        List<DTOEntityInfo> finalDTOEntities = mainController.getEngineManager().getCurrentEntities(selectedSimulation.getSimulationId());
        entitiesListView.getItems().clear();
        for(DTOEntityInfo dtoEntityInfo: finalDTOEntities){
            entitiesListView.getItems().add(dtoEntityInfo.getEntityName());
        }
    }


    @FXML
    void entitiesListViewClicked(MouseEvent event) {
        //todo: add a function and data to save the info about amount of each entity in each tick
        String entitiesName = entitiesListView.getSelectionModel().getSelectedItem();
        Map<Integer, Map<String, Integer>> amountOfAllEntities = mainController.getEngineManager().getAmountOfEntitiesPerTick(selectedSimulation.getSimulationId());
        if(entitiesName != null) {
            createGraphOfEntityPerTick(amountOfAllEntities, entitiesName);
            graphPane.setVisible(true);
        }
    }

    private void createGraphOfEntityPerTick(Map<Integer, Map<String, Integer>> amountOfAllEntities, String entitiesName) {
        amountOfEntitiesLineChart.getData().clear();

        XYChart.Series<Integer, Integer> series = new XYChart.Series<>();
        series.setName(entitiesName);

        int jumpInterval = 1000;
        int currentTick = 0;

        for (Map.Entry<Integer, Map<String, Integer>> entry : amountOfAllEntities.entrySet()) {
            Integer tick = entry.getKey();
            Map<String, Integer> entityData = entry.getValue();

            Integer amount = entityData.get(entitiesName);

            if (amount != null) {
                if(Integer.parseInt(currentTickTextField.getText()) > 3000) {
                    if (tick >= currentTick) {
                        series.getData().add(new XYChart.Data<>(tick, amount));
                        currentTick += jumpInterval;
                    }
                }
                else{
                    series.getData().add(new XYChart.Data<>(tick, amount));
                }
            }

            if(tick.equals(Integer.parseInt(currentTickTextField.getText()))){
                break;
            }
        }

        amountOfEntitiesLineChart.getData().add(series);
    }

    @FXML
    void displayModeComboBoxClicked(ActionEvent event) {
        isDisplayModePressed.set(true);
        staticInfoPane.getChildren().clear();
        String selectedDisplay = displayModeComboBox.getSelectionModel().getSelectedItem();
        TreeItem<String> selectedItem = entitiesAndPropertiesTreeView.getSelectionModel().getSelectedItem();
        if(selectedItem == null){
            return;
        }
        String propertyName = selectedItem.getValue();
        String entityName = selectedItem.getParent().getValue();
        Integer currentTick = Integer.parseInt(currentTickTextField.getText());
        if(selectedDisplay != null) {
            Map<Object, Integer> propertyInfoAboutValues = mainController.getEngineManager().createPropertyValuesMap(selectedSimulation.getSimulationId(), entityName, propertyName, currentTick);
            switch (selectedDisplay) {
                case "Histogram of population":
                    staticInfoPane.getChildren().add(histogramOfPopulation.getHistogramScrollPane());
                    List<DTOHistogram> histograms= createDTOHistogram(propertyInfoAboutValues);
                    histogramOfPopulation.createTableView(histograms);
                    break;
                case "Consistency":
                    staticInfoPane.getChildren().add(averageTickOfProperty.getAverageTickOfPropertyVbox());
                    averageTickOfProperty.setAverageTickOfProperty(mainController.getEngineManager().getAverageTickOfSpecificProperty(selectedSimulation.getSimulationId(), entityName, propertyName, currentTick));
                    break;
                case "Average value":
                    staticInfoPane.getChildren().add(averageValueOfProperty.getAverageValueVbox());
                    averageValueOfProperty.createAverageValueOfProperty(propertyInfoAboutValues);
                    break;
            }
        }
    }

    private List<DTOHistogram> createDTOHistogram(Map<Object, Integer> propertyInfoAboutValues) {
        List<DTOHistogram> histogramsList = new ArrayList<>();
        for(Object object: propertyInfoAboutValues.keySet()){
            histogramsList.add(new DTOHistogram(object, propertyInfoAboutValues.get(object)));
        }

        return histogramsList;
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
        mainController.getEngineManager().pause(selectedSimulation.getSimulationId());
        resumeButtonPressed.put(selectedSimulation.getSimulationId(), false);
        pauseButtonPressed.put(selectedSimulation.getSimulationId(), true);
        futureButton.setVisible(true);
        if(selectedSimulation.getBonusActive()) {
            pastButton.setVisible(true);
        }
        setAllDetailsForPastAndFutureTick();
    }

    @FXML
    void resumeButtonClicked(ActionEvent event) {
        if(resumeAfterPastTick){
            synchronized(simulationTask){
                simulationTask.setPast(false);
                resumeAfterPastTick = false;
                simulationTask.notifyAll();
            }
        }
        mainController.getEngineManager().resume(selectedSimulation.getSimulationId());
        pauseButtonPressed.put(selectedSimulation.getSimulationId(), false);
        resumeButtonPressed.put(selectedSimulation.getSimulationId(), true);
        futureButton.setVisible(false);
        pastButton.setVisible(false);
        endedSimulationInfoScrollPane.setVisible(false);
        graphPane.setVisible(false);
        isDisplayModePressed.set(false);
        displayModeLabel.setVisible(false);
        displayModeComboBox.setVisible(false);
    }

    @FXML
    void stopButtonClicked(ActionEvent event) {
        if(resumeAfterPastTick){
            synchronized(simulationTask){
                simulationTask.setPast(false);
                resumeAfterPastTick = false;
                simulationTask.notifyAll();
            }
        }
        mainController.getEngineManager().stop(selectedSimulation.getSimulationId());
        futureButton.setVisible(false);
        pastButton.setVisible(false);
        //todo: move to functions
        pauseButton.setVisible(false);
        resumeButton.setVisible(false);
        stopButton.setVisible(false);
        rerunButton.setVisible(true);
        currentTicksProperty.set(mainController.getEngineManager().getCurrentTick(selectedSimulation.getSimulationId()));
        currentSecondsProperty.set(mainController.getEngineManager().getCurrentSecond(selectedSimulation.getSimulationId()));
        List<DTOEntityInfo> currentTickAmountOfEntities = mainController.getEngineManager().getCurrentTickAmountOfEntities(selectedSimulation.getSimulationId(), Integer.parseInt(currentTickTextField.getText()));
        addEntitiesDetails(currentTickAmountOfEntities);
        graphPane.setVisible(false);
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



    public Node getThirdPageScrollPane() {
        return thirdPageScrollPane;
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
            simulationTask = new SimulationTask(selectedSimulation.getSimulationId(), mainController.getEngineManager(), currentTicksProperty, currentSecondsProperty, isFinishProperty, updateTableViewConsumer, isFailedProperty);
            new Thread(simulationTask).start();
        }
        else {
            simulationTask.setSimulationId(selectedSimulation.getSimulationId());
        }
    }

    private void updateFinishSimulation(){
        Platform.runLater(() -> {
            futureButton.setVisible(false);
            pastButton.setVisible(false);
            selectedSimulation = mainController.getEngineManager().getAllPastSimulation().get(selectedSimulation.getSimulationId() - 1);
            if(!selectedSimulation.getFailed()) {
                setFinishSimulationComponentsVisible();
                setFinishSimulationDetails();
            }
        });
    }

    public void setSimulationTask(SimulationTask simulationTask) {
        this.simulationTask = simulationTask;
    }

    public void createFinishSimulationTask() {
        amountOfSimulations++;
        setQueueAndProgressSimulation();
        if(finishSimulationTask == null) {
            finishSimulations.clear();
            finishSimulationTask = new FinishSimulationTask(updateFinishSimulationConsumer, mainController.getEngineManager());
            new Thread(finishSimulationTask).start();
        }
    }

    public void setFinishSimulationTask(FinishSimulationTask finishSimulationTask) {
        this.finishSimulationTask = finishSimulationTask;
    }

    public void setAmountOfSimulations(Integer amountOfSimulations) {
        this.amountOfSimulations = amountOfSimulations;
    }

    public void setAmountOfSimulationsEnded(Integer amountOfSimulationsEnded) {
        this.amountOfSimulationsEnded = amountOfSimulationsEnded;
    }

    public void setQueueAndProgressSimulation(){
        Integer amountOfSimulationInProgress = amountOfSimulations - amountOfSimulationsEnded;
        Integer amountOfSimulationsInQueue = amountOfSimulationInProgress - mainController.getEngineManager().getAmountOfThreads();;
        if(amountOfSimulationsInQueue > 0 ) {
            mainController.setAmountOfSimulationsInQueue(amountOfSimulationsInQueue.toString());
            mainController.setAmountOfSimulationsInProgress(mainController.getEngineManager().getAmountOfThreads().toString());
        }
        else{
            mainController.setAmountOfSimulationsInQueue("0");
            mainController.setAmountOfSimulationsInProgress(amountOfSimulationInProgress.toString());
        }
    }

    @FXML
    void futureButtonClicked(ActionEvent event) {
        Integer tick = mainController.getEngineManager().getCurrentTick(selectedSimulation.getSimulationId());
        Integer currentTick = Integer.parseInt(currentTickTextField.getText()) + 1;
        if(currentTick >= tick) {
            if(resumeAfterPastTick){
                synchronized(simulationTask){
                    simulationTask.setPast(false);
                    resumeAfterPastTick = false;
                    simulationTask.notifyAll();
                }
            }

            mainController.getEngineManager().futureTick(selectedSimulation.getSimulationId());
        }
        else{
            setPastDetails(currentTick);
        }
        endedSimulationInfoScrollPane.setVisible(true);
        graphPane.setVisible(false);
        isDisplayModePressed.set(false);
        displayModeComboBox.setVisible(false);
        displayModeLabel.setVisible(false);
    }

    private void setPastDetails(Integer value) {
        mainController.getEngineManager().setFutuerBonus(selectedSimulation.getSimulationId());
        simulationTask.setPast(true);
        resumeAfterPastTick = true;
        currentTicksProperty.set(value);
        List<DTOEntityInfo> currentTickAmountOfEntities = mainController.getEngineManager().getCurrentTickAmountOfEntities(selectedSimulation.getSimulationId(), value);
        addEntitiesDetails(currentTickAmountOfEntities);
        currentSecondsProperty.set(mainController.getEngineManager().getAllSecondsPerTick(selectedSimulation.getSimulationId()).get(value));
    }

    @FXML
    void pastButtonClicked(ActionEvent event) {
        try {
            Integer currentTick = Integer.parseInt(currentTickTextField.getText());
            Integer enteredValue = currentTick - 1;
            if (enteredValue < 1) {
                throw new Exception();
            }
            else {
                setPastDetails(enteredValue);
                endedSimulationInfoScrollPane.setVisible(true);
                graphPane.setVisible(false);
                isDisplayModePressed.set(false);
                displayModeComboBox.setVisible(false);
                displayModeLabel.setVisible(false);
            }
        }
        catch(Exception e){
            mainController.setErrorMessage("You can go back up till tick number 1");
        }
    }

    public void resetPauseAndResume() {
        pauseButtonPressed.clear();
        resumeButtonPressed.clear();
    }

    private void setAllDetailsForPastAndFutureTick(){
        setTabOfFinishSimulation();
        setFinishSimulationDetails();
    }


    private void updateFailedSimulation() {
        setTabFailedSimulation();
        endedSimulationInfoScrollPane.setVisible(true);
        pauseButton.setVisible(false);
        resumeButton.setVisible(false);
        stopButton.setVisible(false);
        rerunButton.setVisible(true);
    }

    private void setTabOfFinishSimulation(){
        allEndedSimulationInfoTabPane.getTabs().clear();
        allEndedSimulationInfoTabPane.getTabs().add(graphsPopulationChangeTab);
        allEndedSimulationInfoTabPane.getTabs().add(staticInformationPropertiesTab);
    }

    private void setTabFailedSimulation(){
        endedSimulationInfoScrollPane.setVisible(true);
        allEndedSimulationInfoTabPane.getTabs().clear();
        allEndedSimulationInfoTabPane.getTabs().add(detailsAboutSimulationTab);
        String simulationInfo = "Simulation: id - " + selectedSimulation.getSimulationId() + " date - "+ selectedSimulation.getSimulationDate() + " has failed";
        simulationDetailsLabel.setText(simulationInfo);
        failedSimulationCauseLabel.setText("The simulation has failed due to :\n" + selectedSimulation.getMessage());
    }

    public SimulationTask getSimulationTask() {
        return simulationTask;
    }
}

