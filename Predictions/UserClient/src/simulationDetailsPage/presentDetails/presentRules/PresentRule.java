package simulationDetailsPage.presentDetails.presentRules;

import DTO.DTOActions.*;
import DTO.DTOActions.DTOCondition.DTOCondition;
import DTO.DTOActions.DTOCondition.DTOConditionMultiple;
import DTO.DTOActions.DTOCondition.DTOConditionSingle;
import DTO.DTORuleInfo;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PresentRule {
    @FXML
    private GridPane gridPaneRulesDetails;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField ticksTextField;

    @FXML
    private TextField probabilityTextField;

    @FXML
    private TreeView<String> actionDetailsTreeView;

    @FXML
    private ListView<String> actionListView;

    private List<DTOActionInfo> actionInfoList;

    @FXML
    void showActionClicked(MouseEvent event) throws IOException {
        int selectedIndex = actionListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            DTOActionInfo selectedAction = actionInfoList.get(selectedIndex);
            setActionDetailsInTreeView(selectedAction);
        }
        else{
            //todo: add message that the user need to chose one of the action it actions name
        }
    }

    private void setActionDetailsInTreeView(DTOActionInfo selectedAction) {
        TreeItem<String> rootAction = new TreeItem<>(selectedAction.getActionName());
        String valueName;
        if(selectedAction.getActionName().equals("REPLACE")){
            valueName = "Kill entity";
        }
        else if (selectedAction.getActionName().equals("PROXIMITY")) {
            valueName = "Source entity";
        } else{
            valueName = "Prim entity";
        }
        TreeItem<String> branchPrimEntity = addBranch(valueName, selectedAction.getEntityName());
        rootAction.getChildren().add(branchPrimEntity);
        if(!(selectedAction.getSecondEntityName().isEmpty())) {
            TreeItem<String> branchSecondEntity = addBranch("Second entity", selectedAction.getSecondEntityName());
            rootAction.getChildren().add(branchSecondEntity);
        }
        else{
            TreeItem<String> branchSecondEntity = addBranch("Second entity", "There is no secondary entity");
            rootAction.getChildren().add(branchSecondEntity);
        }

        actionDetailsTreeView.setRoot(rootAction);
        setSpecificActionDetails(selectedAction);
    }

    private void setSpecificActionDetails(DTOActionInfo selectedAction) {
        switch(selectedAction.getActionName()){
            case "INCREASE":
            case "DECREASE":
                setIncreaseAndDecreasePage(selectedAction);
                break;
            case "CALCULATION":
                setCalculationPage(selectedAction);
                break;
            case "CONDITION":
                setCondition(selectedAction);
                break;
            case "SET":
                setActionSet(selectedAction);
                break;
            case "KILL":
                break;
            case "REPLACE":
                setActionReplace(selectedAction);
                break;
            case "PROXIMITY":
                setActionProximity(selectedAction);
                break;
        }
    }


    private void setIncreaseAndDecreasePage(DTOActionInfo selectedAction) {
        DTOIncreaseAndDecrease dtoIncreaseAndDecrease = (DTOIncreaseAndDecrease)selectedAction;
        TreeItem<String> branchProperty = addBranch("Property", dtoIncreaseAndDecrease.getPropertyName());
        TreeItem<String> branchBy = addBranch("By", dtoIncreaseAndDecrease.getBy());

        actionDetailsTreeView.getRoot().getChildren().addAll(branchProperty, branchBy);
    }

    private void setCalculationPage(DTOActionInfo selectedAction) {
        DTOCalculation dtoCalculation = (DTOCalculation) selectedAction;
        TreeItem<String> branchCalculationType = addBranch("CalculationType", dtoCalculation.getTypeOfCalculation());
        TreeItem<String> branchResultProp = addBranch("ResultProp", dtoCalculation.getResultProp());
        TreeItem<String> branchArgsProp = new TreeItem<>("Args");
        TreeItem<String> leafFirstArgProp = new TreeItem<>(dtoCalculation.getArg1());
        TreeItem<String> leafSecondArgProp = new TreeItem<>(dtoCalculation.getArg2());

        branchArgsProp.getChildren().addAll(leafFirstArgProp, leafSecondArgProp);
        actionDetailsTreeView.getRoot().getChildren().addAll(branchCalculationType, branchResultProp, branchArgsProp);
    }

    private void setCondition(DTOActionInfo selectedAction) {
        DTOCondition dtoCondition = (DTOCondition)selectedAction;
        setGeneralDetailsCondition(dtoCondition);
        if(dtoCondition.getConditionType().equals("multiple")){
            setMultipleCondition(selectedAction);
        }
        else{
            setSingleCondition(selectedAction);
        }
    }

    private void setGeneralDetailsCondition(DTOCondition dtoCondition) {
        TreeItem<String> branchConditionType = addBranch("ConditionType", dtoCondition.getConditionType());
        TreeItem<String> branchAmountOfThenActions = addBranch("Amount of actions in then", dtoCondition.getAmountOfActionThen());
        actionDetailsTreeView.getRoot().getChildren().addAll(branchConditionType, branchAmountOfThenActions);
        if(!(dtoCondition.getAmountOfActionElse().isEmpty())){
            TreeItem<String> branchAmountOfElseActions = addBranch("Amount of actions in else", dtoCondition.getAmountOfActionElse());
            actionDetailsTreeView.getRoot().getChildren().add(branchAmountOfElseActions);
        }
        else {
            TreeItem<String> branchAmountOfElseActions = addBranch("Amount of actions in else", "0");
            actionDetailsTreeView.getRoot().getChildren().add(branchAmountOfElseActions);
        }
    }

    private void setMultipleCondition(DTOActionInfo selectedAction) {
        DTOConditionMultiple dtoConditionMultiple = (DTOConditionMultiple)selectedAction;
        TreeItem<String> branchLogicalType = addBranch("LogicalType", dtoConditionMultiple.getLogical());
        TreeItem<String> branchAmountOfConditions = addBranch("AmountOfConditions", dtoConditionMultiple.getAmountOfConditions());

        actionDetailsTreeView.getRoot().getChildren().addAll(branchLogicalType, branchAmountOfConditions);
    }

    private void setSingleCondition(DTOActionInfo selectedAction) {
        DTOConditionSingle dtoConditionSingle = (DTOConditionSingle)selectedAction;
        TreeItem<String> branchPropertyName = addBranch("PropertyName", dtoConditionSingle.getPropertyName());
        TreeItem<String> branchOperator = addBranch("Operator", dtoConditionSingle.getOperator());
        TreeItem<String> branchValue = addBranch("Value", dtoConditionSingle.getValue());

        actionDetailsTreeView.getRoot().getChildren().addAll(branchPropertyName, branchOperator, branchValue);
    }

    private void setActionSet(DTOActionInfo selectedAction) {
        DTOSet dtoSet = (DTOSet)selectedAction;
        TreeItem<String> branchProperty = addBranch("Property", dtoSet.getPropertyName());
        TreeItem<String> branchBy = addBranch("Value", dtoSet.getValue());

        actionDetailsTreeView.getRoot().getChildren().addAll(branchProperty, branchBy);
    }


    private void setActionReplace(DTOActionInfo selectedAction) {
        DTOReplace dtoReplace = (DTOReplace)selectedAction;
        TreeItem<String> branchCreateEntity = addBranch("Create entity", dtoReplace.getCreateEntityName());
        TreeItem<String> branchMode = addBranch("Mode", dtoReplace.getMode());

        actionDetailsTreeView.getRoot().getChildren().addAll(branchCreateEntity, branchMode);
    }

    private void setActionProximity(DTOActionInfo selectedAction) {
        DTOProximity dtoProximity = (DTOProximity)selectedAction;
        TreeItem<String> branchTargetEntity = addBranch("Target entity", dtoProximity.getTargetEntity());
        TreeItem<String> branchEnvDepth = addBranch("Env depth", dtoProximity.getOf());
        TreeItem<String> branchAmountOfActions = addBranch("Amount of actions", dtoProximity.getAmountOfActions());

        actionDetailsTreeView.getRoot().getChildren().addAll(branchTargetEntity, branchEnvDepth, branchAmountOfActions);
    }

    private TreeItem<String> addBranch(String valueName, String value){
        TreeItem<String> branch = new TreeItem<>(valueName);
        TreeItem<String> leaf = new TreeItem<>(value);
        branch.getChildren().add(leaf);
        return  branch;
    }

    public void setVisibleEntitiesPage(boolean state) {
        gridPaneRulesDetails.visibleProperty().set(state);
    }

    public Node getRulesGridPane() {
        return gridPaneRulesDetails;
    }

    public void setSpecificRuleDetails(DTORuleInfo specificRule) {
        nameTextField.setText(specificRule.getRuleName());
        ticksTextField.setText(String.valueOf(specificRule.getActivation().getTicks()));
        probabilityTextField.setText(String.valueOf(specificRule.getActivation().getProbability()));
        actionInfoList = specificRule.getAllAction();
        setAllActionNames();
    }

    private void setAllActionNames(){
        List<String> rulesNames = createListActionNames();
        actionListView.getItems().clear();
        actionDetailsTreeView.setRoot(null);
        actionListView.getItems().addAll(rulesNames);
    }

    private List<String> createListActionNames(){
        List<String> rulesNames = new ArrayList<>();
        for(DTOActionInfo dtoActionInfo: actionInfoList){
            rulesNames.add(dtoActionInfo.getActionName());
        }

        return rulesNames;
    }
}
