package firstPage.presentDetails.presentTermination;

import DTO.DTOTerminationInfo;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

    public void setTerminationDetails(DTOTerminationInfo terminationDetails) {
        if(terminationDetails.getTicks() != null) {
            ticksHbox.setVisible(true);
            ticksTextField.setText(terminationDetails.getTicks().toString());
        }
        if(terminationDetails.getSecond() != null) {
            secondHbox.setVisible(true);
            secondsTextField.setText(terminationDetails.getSecond().toString());
        }
    }

    public void setVisibleTerminationPage(boolean state) {
        gridPaneTerminationDetails.visibleProperty().set(state);
    }

    public Node getTerminationGridPane() {
        return gridPaneTerminationDetails;
    }
}
