package firstPage.presentDetails.presentTermination;

import DTO.DTOTerminationInfo;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class PresentTermination {

    @FXML
    private GridPane gridPaneTerminationDetails;

    @FXML
    private HBox ticksHbox;

    @FXML
    private TextField ticksTextField;

    @FXML
    private HBox secondHbox;

    @FXML
    private TextField secondsTextField;

    @FXML
    private Label terminationLabel;

    public void setTerminationDetailsPage(DTOTerminationInfo terminationDetails) {
        terminationLabel.setVisible(true);
        if(terminationDetails.getTicks() != null) {
            ticksHbox.setVisible(true);
            ticksTextField.setText(terminationDetails.getTicks().toString());
        }
        if(terminationDetails.getSecond() != null) {
            secondHbox.setVisible(true);
            secondsTextField.setText(terminationDetails.getSecond().toString());
        }
        //todo: when we will have in the engine the user termination we will add a text for it
        if(terminationDetails.getTicks() == null && terminationDetails.getSecond() == null && terminationDetails.getTerminationByUser()){
            terminationLabel.setText("The termination is by the user");
        }
    }

    public void setVisibleTerminationPage(boolean state) {
        gridPaneTerminationDetails.visibleProperty().set(state);
    }

    public Node getTerminationGridPane() {
        return gridPaneTerminationDetails;
    }
}
