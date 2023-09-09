package thirdPage.averageValueOfProperty;

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

    public void createAverageValueOfProperty(Map<Object, Integer> propertyInfoAboutValues) {
        if (propertyInfoAboutValues.isEmpty()) {
            averageValueTextField.setText(String.valueOf(0.0));
        }

        int sum = 0;
        for (int value : propertyInfoAboutValues.values()) {
            sum += value;
        }

        averageValueTextField.setText(String.valueOf((double) sum / propertyInfoAboutValues.size()));
    }
}
