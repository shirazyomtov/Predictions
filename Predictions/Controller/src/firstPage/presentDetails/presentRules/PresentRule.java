package firstPage.presentDetails.presentRules;

import DTO.DTOActionInfo;
import DTO.DTORuleInfo;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

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
    private TextField actionTypeTextField;

    @FXML
    private TextField primEntityNameTextField;

    @FXML
    private TextField secondEntityNameTextField;

    @FXML
    private ListView<String> actionListView;

    private List<DTOActionInfo> actionInfoList;

    @FXML
    void showActionClicked(MouseEvent event) {
        int selectedIndex = actionListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            DTOActionInfo selectedAction = actionInfoList.get(selectedIndex);
            resetActionDetails();
            setActionDetails(selectedAction);
        }
        else{
            //todo: add message that the user need to chose one of the action it actions name
        }
    }

    private void resetActionDetails() {
        actionTypeTextField.clear();
        primEntityNameTextField.clear();
        secondEntityNameTextField.clear();
        //todo: when I have the screen I will make the screen invisible
    }

    private void setActionDetails(DTOActionInfo selectedAction) {
        actionTypeTextField.setText(selectedAction.getActionName());
        primEntityNameTextField.setText(selectedAction.getEntityName());
        //todo: edit after we have new schema
//        if(selectedAction.getSecondEntity() != null) {
//            secondEntityNameTextField.setText(selectedAction.getSecondEntity());
//        }

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
