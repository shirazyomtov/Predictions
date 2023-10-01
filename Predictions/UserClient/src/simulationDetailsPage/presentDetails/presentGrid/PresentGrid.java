package simulationDetailsPage.presentDetails.presentGrid;

import DTO.DTOGrid;
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


    public void setVisibleGridPage(boolean state) {
        gridPaneGridDetails.visibleProperty().set(state);
    }


    public Node getGridPane() {
        return gridPaneGridDetails;
    }

    public void setGridDetails(DTOGrid gridDetails) {
        rowsTextField.setText(gridDetails.getRows());
        colsTextField.setText(gridDetails.getCols());
    }
}
