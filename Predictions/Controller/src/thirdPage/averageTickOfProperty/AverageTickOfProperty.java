package thirdPage.averageTickOfProperty;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Map;

public class AverageTickOfProperty {

    @FXML
    private TextField averageTickOfPropertyTextField;

    @FXML
    private VBox averageTickOfPropertyVbox;

    public VBox getAverageTickOfPropertyVbox(){
        return averageTickOfPropertyVbox;
    }

    public void setAverageTickOfProperty(Float averageTickOfProperty) {
        averageTickOfPropertyTextField.setText(String.valueOf(averageTickOfProperty));
    }
}
