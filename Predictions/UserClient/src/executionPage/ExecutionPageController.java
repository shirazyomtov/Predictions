//package executionPage;
//
//import DTO.DTOEntityInfo;
//import DTO.DTOEnvironmentInfo;
//import app.AppController;
//import exceptions.*;
//import javafx.beans.property.BooleanProperty;
//import javafx.beans.property.SimpleBooleanProperty;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.Node;
//import javafx.scene.control.*;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.VBox;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ExecutionPageController {
//    private AppController mainController;
//    @FXML
//    private VBox choseEnvironmentVbox;
//
//    @FXML
//    private VBox choseValueVbox;
//
//    @FXML
//    private ListView<String> entitiesNamesListView;
//
//    @FXML
//    private TextField entityNameTextField;
//
//    @FXML
//    private ListView<String> environmentListView;
//
//    @FXML
//    private TextField environmentNameTextField;
//
//    @FXML
//    private Button saveEnvironmentValueButton;
//
//    @FXML
//    private Button saveValueButton;
//
//    @FXML
//    private GridPane executionPageGridPane;
//
//    @FXML
//    private TextField valueEnvironmentTextField;
//
//    @FXML
//    private TextField valueTextField;
//
//    @FXML
//    private Button startButton;
//
//    @FXML
//    private Button clearButton;
//
//    @FXML
//    private TextField rangeEnvironmentTextField;
//
//    @FXML
//    private TextField typeEnvironmentTextField;
//
//    @FXML
//    private SplitPane executionsPageSplitPane;
//
//    private List<String> entitiesNames = new ArrayList<>();
//
//    private List<String> environmentsNames = new ArrayList<>();
//
//    private BooleanProperty isStartButtonPressed = new SimpleBooleanProperty(false);
//
//    private boolean bonus = false;
//
//    public void setMainController(AppController appController) {
//        this.mainController = appController;
//    }
//
//    public void setExecutionsPageDetails(List<DTOEntityInfo> entitiesDetails, List<DTOEnvironmentInfo> environmentInfos) {
//        bonus = false;
//        resetAllText();
//        createListEntitiesNames(entitiesDetails);
//        entitiesNamesListView.getItems().clear();
//        entitiesNamesListView.getItems().addAll(entitiesNames);
//        createListEnvironmentsNames(environmentInfos);
//        environmentListView.getItems().clear();
//        environmentListView.getItems().addAll(environmentsNames);
//        executionsPageSplitPane.setDividerPositions(0.5);
//        executionsPageSplitPane.getDividers().get(0).positionProperty().addListener((Observable, oldValue, newValue) -> {
//            executionsPageSplitPane.setDividerPositions(0.5);
//        });
//        executionsPageSplitPane.widthProperty().addListener((observable, oldValue, newValue) -> {
//            executionsPageSplitPane.setDividerPositions(0.5);
//        });
//    }
//
//    private void resetAllText() {
//        choseValueVbox.visibleProperty().set(false);
//        choseEnvironmentVbox.visibleProperty().set(false);
//        entityNameTextField.clear();
//        valueTextField.setText("0");
//        valueEnvironmentTextField.clear();
//    }
//
//
//    private void createListEntitiesNames(List<DTOEntityInfo> entitiesDetails) {
//        entitiesNames.clear();
//        for(DTOEntityInfo dtoEntityInfo: entitiesDetails){
//            entitiesNames.add(dtoEntityInfo.getEntityName());
//        }
//    }
//
//    @FXML
//    void entitiesNamesListViewClicked(MouseEvent event) {
//        int selectedIndex = entitiesNamesListView.getSelectionModel().getSelectedIndex();
//        if (selectedIndex >= 0) {
//            choseValueVbox.visibleProperty().set(true);
//            String entityName = entitiesNames.get(selectedIndex);
//            entityNameTextField.setText(entityName);
//            int currentValue = mainController.getEngineManager().getEntityAmount(entityName);
//            valueTextField.setText(String.valueOf(currentValue));
//        }
//    }
//
//    @FXML
//    void saveValueButtonClicked(ActionEvent event){
//        try {
//            if (!valueTextField.getText().isEmpty()) {
//                int amountOfEntityInstance = Integer.parseInt(valueTextField.getText());
//                String entityName = entityNameTextField.getText();
//                mainController.getEngineManager().setAmountOfEntities(entityName, amountOfEntityInstance);
//                mainController.setSuccessMessage("The value was saved successfully");
//            } else {
//                mainController.setErrorMessage("You need to enter a value before pressing save");
//            }
//        }
//        catch (NumberFormatException e)
//        {
//            mainController.setErrorMessage("You need to enter a number as value");
//        }
//        catch (Exception e){
//            mainController.setErrorMessage(e.getMessage());
//        }
//    }
//
//
//    private void createListEnvironmentsNames(List<DTOEnvironmentInfo> environmentInfos) {
//        environmentsNames.clear();
//        for(DTOEnvironmentInfo dtoEnvironmentInfo: environmentInfos){
//            environmentsNames.add(dtoEnvironmentInfo.getName());
//        }
//    }
//
//    @FXML
//    void environmentNamesListViewClicked(MouseEvent event) {
//        int selectedIndex = environmentListView.getSelectionModel().getSelectedIndex();
//
//        if (selectedIndex >= 0) {
//            choseEnvironmentVbox.visibleProperty().set(true);
//            String environmentName = environmentsNames.get(selectedIndex);
//            DTOEnvironmentInfo dtoEnvironmentInfo = mainController.getEngineManager().getEnvironmentNamesList().get(selectedIndex);
//            typeEnvironmentTextField.setText(dtoEnvironmentInfo.getType());
//            if(dtoEnvironmentInfo.getRange() != null) {
//                rangeEnvironmentTextField.setText(dtoEnvironmentInfo.getRange().getFrom() + " - " + dtoEnvironmentInfo.getRange().getTo());
//            }
//            else{
//                rangeEnvironmentTextField.setText("No range");
//            }
//            Object value = mainController.getEngineManager().getValueOfEntity(environmentName);
//            if(value != null){
//                valueEnvironmentTextField.setText(value.toString());
//            }
//            else{
//                valueEnvironmentTextField.setText("");
//            }
//        }
//    }
//
//    @FXML
//    void saveEnvironmentValueButtonClicked(ActionEvent event) {
//        try {
//            if (!valueEnvironmentTextField.getText().isEmpty()) {
//                String valueOfEnvironment = valueEnvironmentTextField.getText();
//                int selectedIndex = environmentListView.getSelectionModel().getSelectedIndex();
//                String environmentName = environmentsNames.get(selectedIndex);
//                mainController.getEngineManager().checkValidValueAndSetValue(environmentName, valueOfEnvironment);
//                mainController.setSuccessMessage("The value was saved successfully");
//            }
//            else {
//                mainController.setErrorMessage("You need to enter a value before pressing save");
//            }
//        }
//        catch (Exception e){
//            mainController.setErrorMessage(e.getMessage());
//        }
//    }
//
//    public void setVisible(boolean state) {
//        executionPageGridPane.visibleProperty().set(state);
//    }
//
//    @FXML
//    void clearButtonClicked(ActionEvent event) {
//        valueTextField.setText("0");
//        valueEnvironmentTextField.clear();
//        mainController.getEngineManager().clearPastValues();
//    }
//
//    @FXML
//    void startButtonClicked(ActionEvent event) throws EntityNotDefine, ObjectNotExist, OperationNotCompatibleTypes, OperationNotSupportedType, FormatException {
//        isStartButtonPressed.set(true);
//        mainController.getEngineManager().setSimulation(bonus);
//        mainController.getEngineManager().addSimulationTask();
//        mainController.setSimulationsDetails();
//        clearButtonClicked(event);
//        mainController.showThirdPage();
//
//    }
//
//    public BooleanProperty getStartButtonPressedProperty() {
//        return isStartButtonPressed;
//    }
//
//    public void resetControllers() {
//        isStartButtonPressed.set(false);
//    }
//
//    public Node getExecutionPageGridPane() {
//        return executionPageGridPane;
//    }
//}