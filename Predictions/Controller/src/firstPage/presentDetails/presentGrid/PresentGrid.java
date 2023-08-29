package firstPage.presentDetails.presentGrid;

import javafx.fxml.FXML;
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


    public void setVisibleGridPage(boolean state) {
        gridPaneGridDetails.visibleProperty().set(state);
    }
}
