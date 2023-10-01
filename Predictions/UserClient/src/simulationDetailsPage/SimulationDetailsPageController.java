package simulationDetailsPage;

import DTO.*;
import app.AppController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import simulationDetailsPage.presentDetails.presentEntities.PresentEntities;
import simulationDetailsPage.presentDetails.presentEnvironment.PresentEnvironment;
import simulationDetailsPage.presentDetails.presentGrid.PresentGrid;
import simulationDetailsPage.presentDetails.presentRules.PresentRule;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimulationDetailsPageController {

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


    @FXML
    public void initialize() {
        simulationDetailsPageSplitPane.setDividerPositions(0.5);
        simulationDetailsPageSplitPane.getDividers().get(0).positionProperty().addListener((Observable, oldValue, newValue) -> {
            simulationDetailsPageSplitPane.setDividerPositions(0.5);
        });
    }

    private void addSimulationToTreeView() {
        TreeItem<String> simulationItem = createSimulationTreeItem("Simulation 1");
        TreeItem<String> rootItem = new TreeItem<>("Simulations");
        rootItem.getChildren().addAll(simulationItem);
        simulationTreeView.setRoot(rootItem);
    }

    private TreeItem<String> createSimulationTreeItem(String simulationName) {
        TreeItem<String> simulationItem = new TreeItem<>(simulationName);
        TreeItem<String> entitiesBranch = createEntityBranch();
        TreeItem<String> rulesBranch = createRulesBranch();
        TreeItem<String> environmentBranch = createEnvironmentBranch();
        gridDetails =  engineManager.getDTOGridDetails();
        TreeItem<String> gridBranch = new TreeItem<>("Grid");
        simulationItem.getChildren().addAll(entitiesBranch, rulesBranch, environmentBranch, gridBranch);

        return simulationItem;
    }

    private TreeItem<String> createEntityBranch() {
        entityDetails = engineManager.getEntitiesDetails();
        TreeItem<String> entitiesBranch = new TreeItem<>("Entities");
        for (DTOEntityInfo entityInfo : entityDetails) {
            entitiesBranch.getChildren().add(new TreeItem<>(entityInfo.getEntityName()));
        }

        return entitiesBranch;
    }

    private TreeItem<String> createRulesBranch() {
        rulesDetails = engineManager.getRulesDetails();
        TreeItem<String> rulesBranch = new TreeItem<>("Rules");
        for (DTORuleInfo ruleInfo : rulesDetails) {
            rulesBranch.getChildren().add(new TreeItem<>(ruleInfo.getRuleName()));
        }

        return rulesBranch;
    }

    private TreeItem<String> createEnvironmentBranch() {
        environmentDetails = engineManager.getEnvironmentNamesList();
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
                setGridDetails();
            }
            else if (selectedItem.getParent().getValue().equals("Entities")) {
                simulationName = selectedItem.getParent().getParent().getValue();
                setEntitiesDetails(getAllPropertiesOfEntity(selectedValue), selectedValue);
            }
            else if (selectedItem.getParent().getValue().equals("Rules")) {
                simulationName = selectedItem.getParent().getParent().getValue();
                setRulesDetails(getSpecificRule(selectedValue));
            }
            else if (selectedItem.getParent().getValue().equals("Environments")) {
                simulationName = selectedItem.getParent().getParent().getValue();
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
