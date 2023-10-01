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

    public void createAverageValueOfProperty(Map<Object, Integer> propertyInfoAboutValues) {
        if (propertyInfoAboutValues.isEmpty()) {
            averageValueTextField.setText(String.valueOf(0.0));
        }

        Float sum = 0.0F;
        Integer count = 0;
        Float value;
        for (Integer amount : propertyInfoAboutValues.values()) {
            count += amount ;
        }
        for (Object valueObject : propertyInfoAboutValues.keySet()) {
            value = (Float)valueObject;
            sum += value * propertyInfoAboutValues.get(valueObject);
        }

        averageValueTextField.setText(String.valueOf( sum / count));
    }
}
