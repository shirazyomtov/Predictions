package resultsPage.averageValueOfProperty;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Map;

public class AverageValueOfProperty {

    @FXML
    private TextField averageValueTextField;

    @FXML
    private VBox averageValueVbox;

    public VBox getAverageValueVbox(){
        return averageValueVbox;
    }

    public void createAverageValueOfProperty(Float averageValue) {
        averageValueTextField.setText(String.valueOf(averageValue));
    }
}
