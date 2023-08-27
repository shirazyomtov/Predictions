package firstPage.detailsOfSystem;

import DTO.DTOEntityInfo;
import DTO.DTOEnvironmentInfo;
import DTO.DTORuleInfo;
import firstPage.FirstPageController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

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
}