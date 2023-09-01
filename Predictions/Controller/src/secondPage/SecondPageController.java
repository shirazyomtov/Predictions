package secondPage;

import DTO.DTOEntityInfo;
import DTO.DTOEnvironmentInfo;
import app.AppController;
import engineManager.EngineManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class SecondPageController {
    private AppController mainController;
    @FXML
    private VBox choseEnvironmentVbox;

    @FXML
    private VBox choseValueVbox;



    @FXML
    private ListView<String> entitiesNamesListView;

    @FXML
    private TextField entityNameTextField;

    @FXML
    private ListView<String> environmentListView;

    @FXML
    private TextField environmentNameTextField;

    @FXML
    private Button saveEnvironmentValueButton;

    @FXML
    private Button saveValueButton;

    @FXML
    private GridPane secondPageGridPane;

    @FXML
    private TextField valueEnvironmentTextField;

    @FXML
    private TextField valueTextField;

    @FXML
    private Button startButton;

    @FXML
    private Button clearButton;

    private List<String> entitiesNames = new ArrayList<>();

    private List<String> environmentsNames = new ArrayList<>();

    private BooleanProperty isStartButtonPressed = new SimpleBooleanProperty(false);

    public void setMainController(AppController appController) {
        this.mainController = appController;
    }

    public Node getSecondPageGridPane() {
        return secondPageGridPane;
    }

    public void setSecondPageDetails(List<DTOEntityInfo> entitiesDetails, List<DTOEnvironmentInfo> environmentInfos) {
        resetAllText();
        createListEntitiesNames(entitiesDetails);
        entitiesNamesListView.getItems().clear();
        entitiesNamesListView.getItems().addAll(entitiesNames);
        createListEnvironmentsNames(environmentInfos);
        environmentListView.getItems().clear();
        environmentListView.getItems().addAll(environmentsNames);
    }

    private void resetAllText() {
        choseValueVbox.visibleProperty().set(false);
        choseEnvironmentVbox.visibleProperty().set(false);
        entityNameTextField.clear();
        environmentNameTextField.clear();
        valueTextField.setText("0");
        valueEnvironmentTextField.clear();
    }


    private void createListEntitiesNames(List<DTOEntityInfo> entitiesDetails) {
        entitiesNames.clear();
        for(DTOEntityInfo dtoEntityInfo: entitiesDetails){
            entitiesNames.add(dtoEntityInfo.getEntityName());
        }
    }

    @FXML
    void entitiesNamesListViewClicked(MouseEvent event) {
        choseValueVbox.visibleProperty().set(true);
        int selectedIndex = entitiesNamesListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            String entityName = entitiesNames.get(selectedIndex);
            entityNameTextField.setText(entityName);
        }
        else{
            //todo: add message that the user need to chose one of the entity it entities name
        }
    }

    @FXML
    void saveValueButtonClicked(ActionEvent event){
        try {
            if (!valueTextField.getText().isEmpty()) {
                int amountOfEntityInstance = Integer.parseInt(valueTextField.getText());
                //todo: save the value to the logic
                //todo: check if the amount of entities that the user chose is not bigger than the size of the space
            } else {
                mainController.setErrorMessage("You need to enter a value before pressing save");
            }
        }
        catch (NumberFormatException e)
        {
            mainController.setErrorMessage("You need to enter a number as value");
        }
    }


    private void createListEnvironmentsNames(List<DTOEnvironmentInfo> environmentInfos) {
        environmentsNames.clear();
        for(DTOEnvironmentInfo dtoEnvironmentInfo: environmentInfos){
            environmentsNames.add(dtoEnvironmentInfo.getName());
        }
    }

    @FXML
    void environmentNamesListViewClicked(MouseEvent event) {
        choseEnvironmentVbox.visibleProperty().set(true);
        int selectedIndex = environmentListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            String environmentName = environmentsNames.get(selectedIndex);
            environmentNameTextField.setText(environmentName);
        }
        else{
            //todo: add message that the user need to chose one of the environment it environments name
        }
    }

    @FXML
    void saveEnvironmentValueButtonClicked(ActionEvent event) {

    }

    public void setVisible(boolean state) {
        secondPageGridPane.visibleProperty().set(state);
    }

    @FXML
    void clearButtonClicked(ActionEvent event) {
        //todo: add the clear function
//        mainController.getEngineManager().clearDataOfEntitiesAndEnvironment();
    }

    @FXML
    void startButtonClicked(ActionEvent event) {
        //todo: after we add the third screen
        isStartButtonPressed.set(true);
        mainController.setSimulationsDetails();
    }

    public BooleanProperty getStartButtonPressedProperty() {
        return isStartButtonPressed;
    }

    public void resetControllers() {
        isStartButtonPressed.set(false);
    }
}
