package simulationDetailsPage;

import DTO.*;
import app.AppController;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import simulationDetailsPage.presentDetails.WorldInfoRefresher;
import simulationDetailsPage.presentDetails.presentEntities.PresentEntities;
import simulationDetailsPage.presentDetails.presentEnvironment.PresentEnvironment;
import simulationDetailsPage.presentDetails.presentGrid.PresentGrid;
import simulationDetailsPage.presentDetails.presentRules.PresentRule;
import sun.reflect.generics.tree.Tree;
import utils.HttpClientUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class SimulationDetailsPageController {
    private static final String ENTITIES_DETAILS_PAGE = "/simulationDetailsPage/presentDetails/presentEntities/presentEntity.fxml";

    private static final String RULES_DETAILS_PAGE = "/simulationDetailsPage/presentDetails/presentRules/presentRule.fxml";

    private static final String ENVIRONMENT_DETAILS_PAGE = "/simulationDetailsPage/presentDetails/presentEnvironment/presentEnvironment.fxml";

    private static final String GRID_DETAILS_PAGE = "/simulationDetailsPage/presentDetails/presentGrid/presentGrid.fxml";


    @FXML
    private ScrollPane scrollPaneWrapper;

    @FXML
    private GridPane simulationDetailsPageGridPane;

    @FXML
    private SplitPane simulationDetailsPageSplitPane;

    @FXML
    private TreeView<String> simulationTreeView;

    @FXML
    private VBox vboxDetails;

    private AppController mainController;

    private PresentEntities presentEntities;

    private PresentRule presentRule;

    private PresentEnvironment presentEnvironment;

    private PresentGrid presentGrid;

    private List<DTOEntityInfo> entityDetails;

    private List<DTORuleInfo> rulesDetails;

    private List<DTOEnvironmentInfo> environmentDetails;

    private DTOGrid gridDetails;

    private Map<String, DTOWorldDefinitionInfo> allSimulation = new HashMap<>();

    private WorldInfoRefresher worldInfoRefresher;

    private Timer timer;

    @FXML
    public void initialize() {
        simulationDetailsPageSplitPane.setDividerPositions(0.5);
        simulationDetailsPageSplitPane.getDividers().get(0).positionProperty().addListener((Observable, oldValue, newValue) -> {
            simulationDetailsPageSplitPane.setDividerPositions(0.5);
        });
    }

    public void setControllers(AppController appController) throws IOException {
        setMainController(appController);
        setPresentDetailsInvisibleAndSetDetails();
        setRootTreeView();
    }

    private void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    private void setPresentDetailsInvisibleAndSetDetails() throws IOException {
        loadEntitiesDetailsResourced();
        loadRulesDetailsResourced();
        loadEnvironmentDetailsResourced();
        loadGridDetailsResourced();
    }

    private void loadEntitiesDetailsResourced() throws IOException {
        FXMLLoader fxmlLoader = loadResourced(ENTITIES_DETAILS_PAGE);
        presentEntities = fxmlLoader.getController();
    }

    private void loadRulesDetailsResourced() throws IOException {
        FXMLLoader fxmlLoader = loadResourced(RULES_DETAILS_PAGE);
        presentRule = fxmlLoader.getController();
    }

    private void loadEnvironmentDetailsResourced() throws IOException {
        FXMLLoader fxmlLoader = loadResourced(ENVIRONMENT_DETAILS_PAGE);
        presentEnvironment = fxmlLoader.getController();
    }

    public void loadGridDetailsResourced() throws IOException {
        FXMLLoader fxmlLoader = loadResourced(GRID_DETAILS_PAGE);
        presentGrid = fxmlLoader.getController();
    }

    public FXMLLoader loadResourced(String path) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(path);
        fxmlLoader.setLocation(url);
        InputStream inputStream = url.openStream();
        GridPane gridPane = fxmlLoader.load(inputStream);
        return fxmlLoader;
    }

    private void setRootTreeView(){
        TreeItem<String> rootItem = new TreeItem<>("Worlds");
        simulationTreeView.setRoot(rootItem);
    }

    public void worldListRefresher(){
        worldInfoRefresher = new WorldInfoRefresher(this::addWorldsNamesToTreeView);
        timer = new Timer();
        timer.schedule(worldInfoRefresher, 2000, 2000);
    }

    public void addWorldsNamesToTreeView(DTOAllWorldsInfo dtoAllWorldsInfo){
        for(String worldName: dtoAllWorldsInfo.getDtoWorldDefinitionInfoMap().keySet()){
            boolean isExist = checkIfExist(worldName);
            if(!isExist){
                Platform.runLater(() -> addSimulationDetails(dtoAllWorldsInfo.getDtoWorldDefinitionInfoMap().get(worldName)));
            }
        }
    }

    private boolean checkIfExist(String worldName) {
        for(TreeItem<String> treeItemName: simulationTreeView.getRoot().getChildren()){
            if(treeItemName.getValue().equals(worldName)){
                return true;
            }
        }
        return false;
    }

    private void addSimulationDetails(DTOWorldDefinitionInfo dtoWorldDefinitionInfo) {
        TreeItem<String> simulationItem = createSimulationTreeItem(dtoWorldDefinitionInfo.getWorldName(), dtoWorldDefinitionInfo);

        Platform.runLater(() -> {
            simulationTreeView.getRoot().getChildren().addAll(simulationItem);
        });
    }

    private TreeItem<String> createSimulationTreeItem(String simulationName, DTOWorldDefinitionInfo dtoWorldDefinitionInfo) {
        TreeItem<String> simulationItem = new TreeItem<>(simulationName);
        allSimulation.put(simulationName, dtoWorldDefinitionInfo);
        entityDetails = dtoWorldDefinitionInfo.getEntitiesList();
        rulesDetails = dtoWorldDefinitionInfo.getRulesList();
        environmentDetails = dtoWorldDefinitionInfo.getEnvironmentsList();
        gridDetails =  dtoWorldDefinitionInfo.getGrid();
        TreeItem<String> entitiesBranch = createEntityBranch();
        TreeItem<String> rulesBranch = createRulesBranch();
        TreeItem<String> environmentBranch = createEnvironmentBranch();
        TreeItem<String> gridBranch = new TreeItem<>("Grid");
        simulationItem.getChildren().addAll(entitiesBranch, rulesBranch, environmentBranch, gridBranch);

        return simulationItem;
    }

    private TreeItem<String> createEntityBranch() {
        TreeItem<String> entitiesBranch = new TreeItem<>("Entities");
        for (DTOEntityInfo entityInfo : entityDetails) {
            entitiesBranch.getChildren().add(new TreeItem<>(entityInfo.getEntityName()));
        }

        return entitiesBranch;
    }

    private TreeItem<String> createRulesBranch() {
        TreeItem<String> rulesBranch = new TreeItem<>("Rules");
        for (DTORuleInfo ruleInfo : rulesDetails) {
            rulesBranch.getChildren().add(new TreeItem<>(ruleInfo.getRuleName()));
        }

        return rulesBranch;
    }

    private TreeItem<String> createEnvironmentBranch() {
        TreeItem<String> environmentBranch = new TreeItem<>("Environments");
        for (DTOEnvironmentInfo environmentInfo : environmentDetails) {
            environmentBranch.getChildren().add(new TreeItem<>(environmentInfo.getName()));
        }

        return environmentBranch;
    }

    @FXML
    void simulationTreeViewClicked(MouseEvent event) throws IOException {
        TreeItem<String> selectedItem = simulationTreeView.getSelectionModel().getSelectedItem();
        String simulationName;
        if (selectedItem != null && selectedItem.getParent() != null && selectedItem.getParent().getParent() != null) {
            String selectedValue = selectedItem.getValue();
            if (selectedValue.equals("Grid")) {
                simulationName = selectedItem.getParent().getValue();
                gridDetails =  allSimulation.get(simulationName).getGrid();
                setGridDetails();
            }
            else if (selectedItem.getParent().getValue().equals("Entities")) {
                simulationName = selectedItem.getParent().getParent().getValue();
                entityDetails = allSimulation.get(simulationName).getEntitiesList();
                setEntitiesDetails(getAllPropertiesOfEntity(selectedValue), selectedValue);
            }
            else if (selectedItem.getParent().getValue().equals("Rules")) {
                simulationName = selectedItem.getParent().getParent().getValue();
                rulesDetails = allSimulation.get(simulationName).getRulesList();
                setRulesDetails(getSpecificRule(selectedValue));
            }
            else if (selectedItem.getParent().getValue().equals("Environments")) {
                simulationName = selectedItem.getParent().getParent().getValue();
                environmentDetails = allSimulation.get(simulationName).getEnvironmentsList();
                setEnvironmentDetails(getSpecificEnvironment(selectedValue));
            }
        }
    }

    private List<DTOPropertyInfo> getAllPropertiesOfEntity(String entityName)
    {
        List<DTOPropertyInfo> propertyInfoList = null;
        for(DTOEntityInfo dtoEntityInfo: entityDetails){
            if(dtoEntityInfo.getEntityName().equals(entityName)){
                propertyInfoList = dtoEntityInfo.getProperties();
            }
        }
        return propertyInfoList;
    }

    private void setEntitiesDetails(List<DTOPropertyInfo> allPropertiesOfEntity, String entityName) throws IOException {
        presentEntities.setVisibleEntitiesPage(true);
        vboxDetails.getChildren().clear();
        vboxDetails.getChildren().add(presentEntities.getEntitiesGridPane());
        presentEntities.setAllColumns(allPropertiesOfEntity);
        presentEntities.setEntityNameTextField(entityName);
    }

    private DTORuleInfo getSpecificRule(String ruleName)
    {
        DTORuleInfo ruleInfo = null;
        for(DTORuleInfo dtoRuleInfo: rulesDetails){
            if(dtoRuleInfo.getRuleName().equals(ruleName)){
                ruleInfo = dtoRuleInfo;
            }
        }
        return ruleInfo;
    }

    private void setRulesDetails(DTORuleInfo specificRule) {
        presentRule.setVisibleEntitiesPage(true);
        vboxDetails.getChildren().clear();
        vboxDetails.getChildren().add(presentRule.getRulesGridPane());
        presentRule.setSpecificRuleDetails(specificRule);
    }

    private void setGridDetails() {
        presentGrid.setVisibleGridPage(true);
        vboxDetails.getChildren().clear();
        vboxDetails.getChildren().add(presentGrid.getGridPane());
        presentGrid.setGridDetails(gridDetails);
    }

    private DTOEnvironmentInfo getSpecificEnvironment(String environmentName)
    {
        DTOEnvironmentInfo environmentInfo = null;
        for(DTOEnvironmentInfo dtoEnvironmentInfo: environmentDetails){
            if(dtoEnvironmentInfo.getName().equals(environmentName)){
                environmentInfo = dtoEnvironmentInfo;
            }
        }
        return environmentInfo;
    }

    private void setEnvironmentDetails(DTOEnvironmentInfo dtoEnvironmentInfo) {
        presentEnvironment.setVisibleEnvironmentPage(true);
        vboxDetails.getChildren().clear();
        vboxDetails.getChildren().add(presentEnvironment.getEnvironmentGridPane());
        presentEnvironment.setEnvironmentDetailsPage(dtoEnvironmentInfo);
    }

    public Node getGridPaneSimulationDetailsPage() {
        return simulationDetailsPageGridPane;
    }
}
