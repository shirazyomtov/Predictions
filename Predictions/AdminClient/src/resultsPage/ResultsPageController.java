package resultsPage;

import DTO.*;
import app.AppController;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import resultsPage.averageTickOfProperty.AverageTickOfProperty;
import resultsPage.averageValueOfProperty.AverageValueOfProperty;
import resultsPage.histogramOfPopulation.HistogramOfPopulation;
import utils.HttpAdminClientUtil;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class ResultsPageController implements Closeable {

    private static final String HISTOGRAM_FXML_LIGHT_RESOURCE = "/resultsPage/histogramOfPopulation/histogramOfPopulation.fxml";
    private static final String AVERAGE_VALUE_FXML_LIGHT_RESOURCE = "/resultsPage/averageValueOfProperty/averageValueOfProperty.fxml";

    private static final String AVERAGE_TICK_PROPERTY_FXML_LIGHT_RESOURCE = "/resultsPage/averageTickOfProperty/averageTickOfProperty.fxml";

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
    private GridPane resultsPageGridPane;

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
    private TableColumn<DTOEntityInfo, String> entityNameColumn;

    @FXML
    private TableColumn<DTOEntityInfo, String> initAmountColumn;

    @FXML
    private TableColumn<DTOEntityInfo, String> finalAmountColumn;

    @FXML
    private TextField secondsCounterTextField;

    @FXML
    private GridPane simulationInfoGridPane;

    @FXML
    private ScrollPane resultsPageScrollPane;

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
    private SplitPane resultsPageSplitPane;

    @FXML
    private SplitPane rightSplitPaneResultsPage;

    @FXML
    private TableView<DTOEnvironmentInfo> environmentTableView;

    @FXML
    private TableColumn<DTOEnvironmentInfo, String> environmentName;

    @FXML
    private TableColumn<DTOEnvironmentInfo, String> environmentValue;

    private AppController mainController;

    private List<DTOSimulationInfo> allDTOSimulationList;

    private DTOSimulationInfo selectedSimulation;

    private DTOWorldInfo worldInfo;

    private HistogramOfPopulation histogramOfPopulation;

    private AverageValueOfProperty averageValueOfProperty;

    private AverageTickOfProperty averageTickOfProperty;

    private SimpleBooleanProperty isDisplayModePressed;

    private SimpleLongProperty currentTicksProperty;
    private SimpleLongProperty currentSecondsProperty;
    private Consumer<List<DTOSimulationInfo>> updateFinishSimulationConsumer;

    private Consumer<DTOWorldInfo> updateDTOWorld;

    private Consumer<Integer> pauseResumeStop;

    private Consumer<Boolean> resetPauseResumeStop;

    private Map<String, Integer> finishSimulations = new HashMap<>();
    private List<Integer> failedSimulations = new ArrayList<>();

    private SimpleStringProperty messageProperty;

    private AllFinishSimulationsRefresher allFinishSimulationsRefresher;
    private Timer timer;

    public ResultsPageController(){
//        this.updateDTOWorld = (currentWorldInfo)->{
//            Platform.runLater(() -> {
//                worldInfo = currentWorldInfo;
//            });
//        };

        isDisplayModePressed = new SimpleBooleanProperty(false);
        this.currentTicksProperty = new SimpleLongProperty(0);
        this.currentSecondsProperty = new SimpleLongProperty(0);
        this.messageProperty = new SimpleStringProperty("");
    }

    @FXML
    public void initialize() throws Exception {
        loadResourcesStaticData();
        if(histogramOfPopulation != null && averageValueOfProperty!= null && averageTickOfProperty != null){
            staticInfoPane.visibleProperty().bind(isDisplayModePressed);
        }
        currentTickTextField.textProperty().bind(Bindings.format("%d", currentTicksProperty));
        secondsCounterTextField.textProperty().bind(Bindings.format("%d", currentSecondsProperty));
        resultsPageSplitPane.setDividerPositions(0.2);
        resultsPageSplitPane.getDividers().get(0).positionProperty().addListener((Observable, oldValue, newValue) -> {
            resultsPageSplitPane.setDividerPositions(0.2);
        });
        resultsPageSplitPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            resultsPageSplitPane.setDividerPositions(0.2);
        });
        rightSplitPaneResultsPage.setDividerPositions(0.4);
        rightSplitPaneResultsPage.getDividers().get(0).positionProperty().addListener((Observable, oldValue, newValue) -> {
            rightSplitPaneResultsPage.setDividerPositions(0.4);
        });
        rightSplitPaneResultsPage.widthProperty().addListener((observable, oldValue, newValue) -> {
            rightSplitPaneResultsPage.setDividerPositions(0.4);
        });
    }


    private void updateAllFinishSimulation(List<DTOSimulationInfo> allSimulation) {
        allDTOSimulationList = allSimulation;
        if(!allSimulation.isEmpty()) {
            if(executionListView.getItems().isEmpty()) {
                mainController.setResultsButtonVisible();
            }
            addSimulationsToExecutionListView();
        }
    }

    private int findSimulationIndex(String worldName, int simulationId) {
        ObservableList<String> items = executionListView.getItems();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).contains("World name: " + worldName + ", Simulation ID: " + simulationId)) {
                return i;
            }
        }
        return -1;
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

    private void addSimulationsToExecutionListView() {
        ObservableList<String> executionList = FXCollections.observableArrayList();
        Platform.runLater(() -> {
            for (DTOSimulationInfo simulation : allDTOSimulationList){
                String state = "";
                if (simulation.getFinish()) {
                    if (simulation.getFailed()) {
                        state = "(Failed)";
                    }
                    else {
                        state = "(Ended)";
                    }
                }
                String simulationId = simulation.getSimulationId().toString();
                String simulationDate = simulation.getSimulationDate();
                String simulationInfoString = state + " User name: " + simulation.getUserName() + ", World name: " + simulation.getWorldName() + ", Simulation ID: " + simulationId + ", Date: " + simulationDate;
                executionList.add(simulationInfoString);
            }
            executionListView.setItems(executionList);
        });
    }

    private String extractSimulationId(String item) {
        String[] parts = item.split("Simulation ID: ");
        if (parts.length > 1) {
            return parts[1].split(",")[0];
        }
        return "0";
    }


    private String extractWorldName(String item) {
        String[] parts = item.split("World name: ");
        if (parts.length > 1) {
            return parts[1].split(",")[0];
        }
        return "0";
    }

    @FXML
    void executionListViewClicked(MouseEvent event) {
        int selectedIndex = executionListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            String simulationId = "0";
            String simulationInfo = executionListView.getItems().get(selectedIndex);
            simulationId = extractSimulationId(simulationInfo).trim();
            String worldName = extractWorldName(simulationInfo).trim();
            selectedSimulation = allDTOSimulationList.get(findDTOSimulationListIndex(worldName, Integer.parseInt(simulationId)));
            try {
                setResultsPageDetails();
                getDetailsAboutCurrentSimulation();
            }
            catch (Exception e){
                System.out.println("c");
            }
        }
    }

    private int findDTOSimulationListIndex(String worldName, int simulationId) {
        for (int i = 0; i < allDTOSimulationList.size(); i++) {
            if(allDTOSimulationList.get(i).getWorldName().equals(worldName)) {
                if (allDTOSimulationList.get(i).getSimulationId().equals(simulationId)) {
                    return i;
                }
            }
        }
        return -1;
    }


    private void getDetailsAboutCurrentSimulation() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://localhost:8080/Server_Web_exploded/getWorldInfo").newBuilder();
        urlBuilder.addQueryParameter("worldName", selectedSimulation.getWorldName());
        urlBuilder.addQueryParameter("simulationId", selectedSimulation.getSimulationId().toString());
        String finalUrl = urlBuilder.build().toString();

        HttpAdminClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    DTOWorldInfo dtoWorldInfo  = gson.fromJson(response.body().charStream(), DTOWorldInfo.class);
                    Platform.runLater(()-> {
                        worldInfo = dtoWorldInfo;
                        currentTicksProperty.set(dtoWorldInfo.getCurrentTick());
                        currentSecondsProperty.set(dtoWorldInfo.getCurrentSecond());
                        updateTableView(dtoWorldInfo);
                        setSpecificSimulationDetails(dtoWorldInfo);
                    });
                }
            }
        });
    }

    private void updateTableView(DTOWorldInfo chosenSimulationWorld){
        addEntitiesDetails(chosenSimulationWorld.getCurrentAmountOfEntities());
        addEnvironmentDetails(chosenSimulationWorld.getEnvironmentInfos());
    }

    private void setSpecificSimulationDetails(DTOWorldInfo dtoWorldInfo) {
        simulationInfoGridPane.setVisible(true);
        if(!selectedSimulation.getFailed()) {
            setTabOfFinishSimulation();
            updateFinishSimulation(dtoWorldInfo);
        }
        else{
            setTabFailedSimulation();
            updateFailedSimulation();
        }
    }

    private void setFinishSimulationComponentsVisible(){
        endedSimulationInfoScrollPane.setVisible(true);
        allEndedSimulationInfoTabPane.getTabs().remove(detailsAboutSimulationTab);
    }

    private void setFinishSimulationDetails(DTOWorldInfo dtoWorldInfo){
        setEntitiesAndProperties(dtoWorldInfo.getCurrentAmountOfEntities());
        setEntities(dtoWorldInfo.getCurrentAmountOfEntities());
    }

    private void addEntitiesDetails(List<DTOEntityInfo> chosenSimulationEntities) {
        entitiesTableView.getItems().clear();
        entitiesTableView.getItems().addAll(chosenSimulationEntities);
        entityNameColumn.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        initAmountColumn.setCellValueFactory(new PropertyValueFactory<>("initialAmount"));
        finalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("finalAmount"));
    }


    private void addEnvironmentDetails(List<DTOEnvironmentInfo> chosenSimulationEnvironments) {
        environmentTableView.getItems().clear();
        environmentTableView.getItems().addAll(chosenSimulationEnvironments);
        environmentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        environmentValue.setCellValueFactory(new PropertyValueFactory<>("value"));
    }

    private void setEntitiesAndProperties(List<DTOEntityInfo> finalDTOEntities) {
        entitiesAndPropertiesTreeView.setRoot(null);
        if(displayModeComboBox.getItems() != null){
            displayModeComboBox.getItems().clear();
        }
        TreeItem<String> root = new TreeItem<>("Entities");
        for (DTOEntityInfo dtoEntityInfo : finalDTOEntities) {
            if(dtoEntityInfo.getFinalAmount() != 0) {
                TreeItem<String> entityBranch = new TreeItem<>(dtoEntityInfo.getEntityName());
                for (DTOPropertyInfo dtoPropertyInfo : dtoEntityInfo.getProperties()) {
                    TreeItem<String> leaf = new TreeItem<>(dtoPropertyInfo.getName());
                    entityBranch.getChildren().add(leaf);
                }
                root.getChildren().add(entityBranch);
            }
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
        String entitiesName = entitiesListView.getSelectionModel().getSelectedItem();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://localhost:8080/Server_Web_exploded/getAmountOfEntities").newBuilder();
        urlBuilder.addQueryParameter("worldName", selectedSimulation.getWorldName());
        urlBuilder.addQueryParameter("simulationID", selectedSimulation.getSimulationId().toString());
        String finalUrl = urlBuilder.build().toString();

        HttpAdminClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Platform.runLater(()->{
                        Gson gson = new Gson();
                        DTOAmountOfEntities amountOfAllEntities = gson.fromJson(response.body().charStream(), DTOAmountOfEntities.class);
                        if(entitiesName != null) {
                            createGraphOfEntityPerTick(amountOfAllEntities.getAmountOfAllEntities(), entitiesName);
                            graphPane.setVisible(true);
                        }
                    });
                }
            }
        });
    }

    private void createGraphOfEntityPerTick(Map<Integer, Map<String, Integer>> amountOfAllEntities, String entitiesName) {
        amountOfEntitiesLineChart.getData().clear();

        XYChart.Series<Integer, Integer> series = new XYChart.Series<>();
        series.setName(entitiesName);

        for (Map.Entry<Integer, Map<String, Integer>> entry : amountOfAllEntities.entrySet()) {
            Integer tick = entry.getKey();
            Map<String, Integer> entityData = entry.getValue();

            Integer amount = entityData.get(entitiesName);

            if (amount != null) {
                    series.getData().add(new XYChart.Data<>(tick, amount));
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
        if(selectedDisplay != null) {
            getPropertyInfoFromServer(entityName, propertyName);
        }
    }


    private void getPropertyInfoFromServer(String entityName, String propertyName) {
        String worldName = selectedSimulation.getWorldName();
        String simulationId = selectedSimulation.getSimulationId().toString();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://localhost:8080/Server_Web_exploded/getStaticInfo").newBuilder();
        urlBuilder.addQueryParameter("worldName", worldName);
        urlBuilder.addQueryParameter("simulationId", simulationId);
        urlBuilder.addQueryParameter("entityName", entityName);
        urlBuilder.addQueryParameter("propertyName", propertyName);
        String finalUrl = urlBuilder.build().toString();

        HttpAdminClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    DTOStaticInfo dtoStaticInfo = gson.fromJson(response.body().charStream(), DTOStaticInfo.class);
                    Platform.runLater(()-> {
                        setDetailsOfDisplayStaticInfo(dtoStaticInfo);
                    });
                }
                else{
                    System.out.println("d");
                }
            }
        });
    }


    private void setDetailsOfDisplayStaticInfo(DTOStaticInfo dtoStaticInfo) {
        Map<Object, Integer> propertyInfoAboutValues;
        String selectedDisplay = displayModeComboBox.getSelectionModel().getSelectedItem();
        switch (selectedDisplay) {
            case "Histogram of population":
                staticInfoPane.getChildren().add(histogramOfPopulation.getHistogramScrollPane());
                propertyInfoAboutValues = dtoStaticInfo.getPropertyInfoAboutValues();
                List<DTOHistogram> histograms= createDTOHistogram(propertyInfoAboutValues);
                histogramOfPopulation.createTableView(histograms);
                break;
            case "Consistency":
                staticInfoPane.getChildren().add(averageTickOfProperty.getAverageTickOfPropertyVbox());
                averageTickOfProperty.setAverageTickOfProperty(dtoStaticInfo.getAverageTickOfProperty());
                break;
            case "Average value":
                staticInfoPane.getChildren().add(averageValueOfProperty.getAverageValueVbox());
                averageValueOfProperty.createAverageValueOfProperty(dtoStaticInfo.getAverageValue());
                break;
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
        List<DTOEntityInfo> finalDTOEntities = worldInfo.getCurrentAmountOfEntities();
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

    public void setVisible(boolean state) {
        resultsPageGridPane.visibleProperty().set(state);
    }

    public Node getResultsPageScrollPane() {
        return resultsPageScrollPane;
    }

    public void clearAllData() {
        simulationInfoGridPane.setVisible(false);
        endedSimulationInfoScrollPane.setVisible(false);
        entitiesListView.getItems().clear();
        entitiesAndPropertiesTreeView.setRoot(null);
        entitiesListView.getItems().clear();
    }

    public void createRefresherOfSimulation() {
        allFinishSimulationsRefresher = new AllFinishSimulationsRefresher(this::updateAllFinishSimulation);
        timer = new Timer();
        timer.schedule(allFinishSimulationsRefresher, 1000, 1000);
    }

    private void updateFinishSimulation(DTOWorldInfo dtoWorldInfo){
        setFinishSimulationComponentsVisible();
        setFinishSimulationDetails(dtoWorldInfo);
    }

    private void updateFailedSimulation() {
        setTabFailedSimulation();
        endedSimulationInfoScrollPane.setVisible(true);
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

    public Node getResultsPageGridPane() {
        return resultsPageGridPane;
    }

    public void setResultsPageDetails() {
        graphPane.setVisible(false);
        displayModeLabel.setVisible(false);
        displayModeComboBox.setVisible(false);
    }

    @Override
    public void close() throws IOException {
        allFinishSimulationsRefresher.cancel();
        timer.cancel();
    }
}

