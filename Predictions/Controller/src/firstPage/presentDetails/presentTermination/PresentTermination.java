package firstPage.presentDetails.presentTermination;

import DTO.DTOTerminationInfo;
import animations.FirstPageTerminationAnimation;
import javafx.event.ActionEvent;
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

    @FXML
    private Label secondsLabel;

    @FXML
    private Label ticksLabel;

    private FirstPageTerminationAnimation firstPageTerminationAnimation;

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
        if(terminationDetails.getTicks() == null && terminationDetails.getSecond() == null && terminationDetails.getTerminationByUser()){
            terminationLabel.setText("The termination is by the user");
        }
        firstPageTerminationAnimation = new FirstPageTerminationAnimation(terminationLabel, secondsLabel, ticksLabel, ticksTextField, secondsTextField);
    }

    public void setVisibleTerminationPage(boolean state) {
        gridPaneTerminationDetails.visibleProperty().set(state);
    }

    public Node getTerminationGridPane() {
        return gridPaneTerminationDetails;
    }

    @FXML
    void startAnimationGridButtonClicked(ActionEvent event) {
        firstPageTerminationAnimation.playAnimations();
    }

    @FXML
    void stopAnimationGridButtonClicked(ActionEvent event) {
        firstPageTerminationAnimation.stopAnimations();
    }
}
