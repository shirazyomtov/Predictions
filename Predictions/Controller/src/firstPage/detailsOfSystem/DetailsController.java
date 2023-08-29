package firstPage.detailsOfSystem;

import DTO.DTOEntityInfo;
import DTO.DTOEnvironmentInfo;
import DTO.DTOPropertyInfo;
import DTO.DTORuleInfo;
import firstPage.FirstPageController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.List;

public class DetailsController {

    @FXML
    private GridPane presentDetailsGridPane;

    @FXML
    private Button gridShowButton;

    @FXML
    private ComboBox<String> entitiesComboBox;

    @FXML
    private Button entitiesShowButton;

    @FXML
    private Button rulesShowButton;

    @FXML
    private ComboBox<String> rulesComboBox;

    @FXML
    private Button environmentShowButton;

    @FXML
    private ComboBox<String> environmentComboBox;

    @FXML
    private Button terminationShowButton;

    private FirstPageController firstPageController;

    public void setFirstPageController(FirstPageController firstPageController) {
        this.firstPageController = firstPageController;
    }

    public void bindShowDetails(SimpleBooleanProperty isFileLoaded){
        presentDetailsGridPane.visibleProperty().bind(isFileLoaded);

    }

    public void setComboBoxes() {
        clearAllComboBoxes();
        setEntitiesComboBox();
        setRulesComboBox();
        setEnvironmentComboBox();
    }

    private void clearAllComboBoxes() {
        entitiesComboBox.getItems().clear();
        rulesComboBox.getItems().clear();
        environmentComboBox.getItems().clear();
    }

    private void setEntitiesComboBox(){
        ObservableList<String> items = entitiesComboBox.getItems();
        for (DTOEntityInfo dtoEntityInfo: firstPageController.getEntityDetails()) {
            items.add(dtoEntityInfo.getEntityName());
        }
    }

    private void setRulesComboBox() {
        ObservableList<String> items = rulesComboBox.getItems();
        for (DTORuleInfo dtoRuleInfo: firstPageController.getRulesDetails()) {
            items.add(dtoRuleInfo.getRuleName());
        }
    }

    private void setEnvironmentComboBox() {
        ObservableList<String> items = environmentComboBox.getItems();
        for (DTOEnvironmentInfo dtoEnvironmentInfo : firstPageController.getEnvironmentDetails()) {
            items.add(dtoEnvironmentInfo.getName());
        }
    }

    @FXML
    void showEntitiesButtonClicked(ActionEvent event) throws IOException {
        String entityName = entitiesComboBox.getValue();
        if(entityName != null) {
            firstPageController.setEntitiesDetails(getAllPropertiesOfEntity(entityName), entityName);
        }
        else{
            firstPageController.setMessageForUser("You need to chose a entity before pressing the show button");
        }
    //todo:
    }

    private List<DTOPropertyInfo> getAllPropertiesOfEntity(String entityName)
    {
        List<DTOPropertyInfo> propertyInfoList = null;
        for(DTOEntityInfo dtoEntityInfo: firstPageController.getEntityDetails()){
            if(dtoEntityInfo.getEntityName().equals(entityName)){
                propertyInfoList = dtoEntityInfo.getProperties();
            }
        }
        return propertyInfoList;
    }

    @FXML
    void rulesShowButtonClicked(ActionEvent event) {
        String ruleName = rulesComboBox.getValue();
        if(ruleName != null) {
            firstPageController.setRulesDetails(getSpecificRule(ruleName));
        }
        else{
            firstPageController.setMessageForUser("You need to chose a rule before pressing the show button");
            //todo: add enum for the error message
        }
    }

    private DTORuleInfo getSpecificRule(String ruleName)
    {
        DTORuleInfo ruleInfo = null;
        for(DTORuleInfo dtoRuleInfo: firstPageController.getRulesDetails()){
            if(dtoRuleInfo.getRuleName().equals(ruleName)){
                ruleInfo = dtoRuleInfo;
            }
        }
        return ruleInfo;
    }

    @FXML
    void terminationShowButtonClicked(ActionEvent event) throws IOException {
        firstPageController.SetTerminationDetails();
    }

    @FXML
    void gridShowButtonClicked(ActionEvent event) {
        firstPageController.setGridDetails();
    }


    @FXML
    void environmentShowButtonClicked(ActionEvent event) {
        String environmentName = environmentComboBox.getValue();
        if(environmentName != null) {
            firstPageController.setEnvironmentDetails(getSpecificEnvironment(environmentName));
        }
        else{
            firstPageController.setMessageForUser("You need to chose a environment before pressing the show button");
        }
    }

    private DTOEnvironmentInfo getSpecificEnvironment(String environmentName)
    {
        DTOEnvironmentInfo environmentInfo = null;
        for(DTOEnvironmentInfo dtoEnvironmentInfo: firstPageController.getEnvironmentDetails()){
            if(dtoEnvironmentInfo.getName().equals(environmentName)){
                environmentInfo = dtoEnvironmentInfo;
            }
        }
        return environmentInfo;
    }
}