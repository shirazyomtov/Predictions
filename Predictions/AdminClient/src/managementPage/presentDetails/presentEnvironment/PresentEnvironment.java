package managementPage.presentDetails.presentEnvironment;

import DTO.DTOEnvironmentInfo;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class PresentEnvironment {

    @FXML
    private GridPane gridPaneEnvironmentDetails;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField typeTextField;

    @FXML
    private HBox rangeHbox;

    @FXML
    private TextField fromTextField;

    @FXML
    private TextField toTextField;

    public void setVisibleEnvironmentPage(boolean state) {
        gridPaneEnvironmentDetails.visibleProperty().set(state);
    }

    public Node getEnvironmentGridPane() {
        return gridPaneEnvironmentDetails;
    }

    public void setEnvironmentDetailsPage(DTOEnvironmentInfo environmentInfo) {
        nameTextField.setText(environmentInfo.getName());
        typeTextField.setText(environmentInfo.getType());
        if(environmentInfo.getRange() != null){
            rangeHbox.setVisible(true);
            fromTextField.setText(environmentInfo.getRange().getFrom());
            toTextField.setText(environmentInfo.getRange().getTo());
        }
        else {
            rangeHbox.setVisible(false);
        }
    }
}
