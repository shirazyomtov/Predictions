package secondPage;

import DTO.DTOEntityInfo;
import DTO.DTOEnvironmentInfo;
import app.AppController;
import engineManager.EngineManager;
import exceptions.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import thirdPage.ThirdPageController;

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
    private ScrollPane secondPageScrollPane;

    @FXML
    private TextField valueEnvironmentTextField;

    @FXML
    private TextField valueTextField;

    @FXML
    private Button startButton;

    @FXML
    private Button clearButton;

    @FXML
    private TextField rangeEnvironmentTextField;

    @FXML
    private TextField typeEnvironmentTextField;

    private List<String> entitiesNames = new ArrayList<>();

    private List<String> environmentsNames = new ArrayList<>();

    private BooleanProperty isStartButtonPressed = new SimpleBooleanProperty(false);

    public void setMainController(AppController appController) {
        this.mainController = appController;
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
            int currentValue = mainController.getEngineManager().getEntityAmount(entityName);
            valueTextField.setText(String.valueOf(currentValue));
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
                String entityName = entityNameTextField.getText();
                mainController.getEngineManager().setAmountOfEntities(entityName, amountOfEntityInstance);
                mainController.setSuccessMessage("The value was saved successfully");
            } else {
                mainController.setErrorMessage("You need to enter a value before pressing save");
            }
        }
        catch (NumberFormatException e)
        {
            mainController.setErrorMessage("You need to enter a number as value");
        }
        catch (Exception e){
            mainController.setErrorMessage(e.getMessage());
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
            DTOEnvironmentInfo dtoEnvironmentInfo = mainController.getEngineManager().getEnvironmentNamesList().get(selectedIndex);
            typeEnvironmentTextField.setText(dtoEnvironmentInfo.getType());
            if(dtoEnvironmentInfo.getRange() != null) {
                rangeEnvironmentTextField.setText(dtoEnvironmentInfo.getRange().getFrom() + " - " + dtoEnvironmentInfo.getRange().getTo());
            }
            else{
                rangeEnvironmentTextField.setText("No range");
            }
            Object value = mainController.getEngineManager().getValueOfEntity(environmentName);
            if(value != null){
                valueEnvironmentTextField.setText(value.toString());
            }
            else{
                valueEnvironmentTextField.setText("");
            }
        }
        else{
            //todo: add message that the user need to chose one of the environment it environments name
        }
    }

    @FXML
    void saveEnvironmentValueButtonClicked(ActionEvent event) {
        try {
            if (!valueEnvironmentTextField.getText().isEmpty()) {
                String valueOfEnvironment = valueEnvironmentTextField.getText();
                int selectedIndex = environmentListView.getSelectionModel().getSelectedIndex();
                String environmentName = environmentsNames.get(selectedIndex);
                mainController.getEngineManager().checkValidValueAndSetValue(environmentName, valueOfEnvironment);
                mainController.setSuccessMessage("The value was saved successfully");
            }
            else {
                mainController.setErrorMessage("You need to enter a value before pressing save");
            }
        }
        catch (Exception e){
            mainController.setErrorMessage(e.getMessage());
        }
    }

    public void setVisible(boolean state) {
        secondPageGridPane.visibleProperty().set(state);
    }

    @FXML
    void clearButtonClicked(ActionEvent event) {
        //todo: add the clear function
        valueTextField.setText("0");
        valueEnvironmentTextField.clear();
        mainController.getEngineManager().clearPastValues();
//        mainController.getEngineManager().clearDataOfEntitiesAndEnvironment();
    }

    @FXML
    void startButtonClicked(ActionEvent event) throws EntityNotDefine, ObjectNotExist, OperationNotCompatibleTypes, OperationNotSupportedType, FormatException {
        //todo: after we add the third screen
        isStartButtonPressed.set(true);
        mainController.getEngineManager().setSimulation();
        mainController.getEngineManager().addSimulationTask();
        mainController.setSimulationsDetails();
        clearButtonClicked(event);
        mainController.showThirdPage();

    }

    public BooleanProperty getStartButtonPressedProperty() {
        return isStartButtonPressed;
    }

    public void resetControllers() {
        isStartButtonPressed.set(false);
    }

    public Node getSecondPageScrollPane() {
        return secondPageScrollPane;
    }
}
