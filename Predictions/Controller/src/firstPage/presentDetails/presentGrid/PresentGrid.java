package firstPage.presentDetails.presentGrid;

import DTO.DTOGrid;
import animations.FirstPageGridAnimation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class PresentGrid {

    @FXML
    private GridPane gridPaneGridDetails;

    @FXML
    private TextField rowsTextField;

    @FXML
    private TextField colsTextField;

    @FXML
    private Label colsLabel;

    @FXML
    private Label rowsLabel;

    @FXML
    private Label gridPaneLabel;

    private FirstPageGridAnimation firstPageGridAnimation;

    public void setVisibleGridPage(boolean state) {
        gridPaneGridDetails.visibleProperty().set(state);
    }


    public Node getGridPane() {
        return gridPaneGridDetails;
    }

    public void setGridDetails(DTOGrid gridDetails) {
        rowsTextField.setText(gridDetails.getRows());
        colsTextField.setText(gridDetails.getCols());
        firstPageGridAnimation = new FirstPageGridAnimation(rowsLabel, colsLabel, gridPaneLabel, rowsTextField, colsTextField);
    }

    @FXML
    void startAnimationGridButtonClicked(ActionEvent event) {
        firstPageGridAnimation.playAnimations();
    }

    @FXML
    void stopAnimationGridButtonClicked(ActionEvent event) {
        firstPageGridAnimation.stopAnimations();
    }
}
