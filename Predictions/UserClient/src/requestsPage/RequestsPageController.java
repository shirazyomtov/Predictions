package requestsPage;

import app.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import static javafx.collections.FXCollections.observableArrayList;

public class RequestsPageController {

    @FXML
    private TextField amountOfSimulationTextField;

    @FXML
    private GridPane requestsPageGridPane;

    @FXML
    private TableView<?> requestsTableView;

    @FXML
    private ScrollPane scrollPaneWrapper;

    @FXML
    private HBox secondsAndTicksHbox;

    @FXML
    private TextField secondsTextField;

    @FXML
    private ChoiceBox<String> simulationNameChoiceBox;

    @FXML
    private TextField ticksTextField;

    @FXML
    private ChoiceBox<String> terminationChoiceBox;

    @FXML
    private CheckBox ticksCheckBox;

    @FXML
    private CheckBox secondsCheckBox;

    @FXML
    private SplitPane requestsSplitPane;

    private AppController mainController;

    @FXML
    public void initialize() {
        requestsSplitPane.setDividerPositions(0.35);
        requestsSplitPane.getDividers().get(0).positionProperty().addListener((Observable, oldValue, newValue) -> {
            requestsSplitPane.setDividerPositions(0.35);
        });
        terminationChoiceBox.setItems(observableArrayList("By user", "By seconds and ticks"));
        terminationChoiceBox.setOnAction(this::setTickAndSecondsHbox);
        ticksTextField.visibleProperty().bind(ticksCheckBox.selectedProperty());
        secondsTextField.visibleProperty().bind(secondsCheckBox.selectedProperty());
    }

    private void setTickAndSecondsHbox(ActionEvent actionEvent) {
        String value = terminationChoiceBox.getValue();
        if(value.equals("By seconds and ticks")){
            secondsAndTicksHbox.setVisible(true);
        }
        else{
            secondsAndTicksHbox.setVisible(false);
        }
    }

    @FXML
    void executeButtonClicked(ActionEvent event) {

    }

    @FXML
    void secondsCheckBoxChecked(ActionEvent event) {

    }

    @FXML
    void ticksCheckBoxChecked(ActionEvent event) {

    }

    @FXML
    void submitRequestButtonClicked(ActionEvent event) {
        String simulationName = simulationNameChoiceBox.getValue();
        String amountOfSimulation = amountOfSimulationTextField.getText();
        String terminationValue = terminationChoiceBox.getValue();
        checkRequestDetails(simulationName, amountOfSimulation, terminationValue);
    }

    private void checkRequestDetails(String simulationName, String amountOfSimulation, String terminationValue) {
        checkIfUserChoseSimulationName(simulationName);
        checkValidAmountOfSimulations(amountOfSimulation);
        checkValidTerminationDetails(terminationValue);
    }


    private void checkIfUserChoseSimulationName(String simulationName) {
        if(simulationName == null){
            //error
        }
    }

    private void checkValidAmountOfSimulations(String amountOfSimulation) {
        try{
            Integer amount = Integer.parseInt(amountOfSimulation);
            if (amount > 0) {
                //todo: update the logic
            }
            else{
                mainController.setErrorMessage("You need to enter a number of amount of simulations greater than 0");
            }
        }
        catch (Exception e){
            mainController.setErrorMessage("You need to enter a number of amount of simulations greater than 0");
        }
    }

    private void checkValidTerminationDetails(String terminationValue) {
        try {
            if (terminationValue != null) {
                if (terminationValue.equals("By seconds and ticks")) {
                    validateCheckBoxAndTextField(ticksCheckBox, ticksTextField, "Ticks");
                    validateCheckBoxAndTextField(secondsCheckBox, secondsTextField, "Seconds");
                    //Handle logic for other cases
                }
                else {
                    // Handle logic for other cases
                }
            } else {
                throw new Exception("You need to choose one of the termination checkboxes: By user or By seconds and ticks");
            }
        } catch (Exception e) {
            mainController.setErrorMessage(e.getMessage());
        }
    }

    private void validateCheckBoxAndTextField(CheckBox checkBox, TextField textField, String fieldName) throws Exception {
        if (checkBox.isSelected()) {
            if (textField.getText() == null || textField.getText().isEmpty()) {
                throw new Exception("You need to enter a value in the " + fieldName + " textField");
            }
            else {
                try {
                    int value = Integer.parseInt(textField.getText());
                    if (value < 1) {
                        throw new Exception("You need to enter a number greater than 0 in the " + fieldName + " textField");
                    }
                }
                catch (NumberFormatException e) {
                    throw new Exception("You need to enter a valid number in the " + fieldName + " textField");
                }
            }
        }
    }

    public Node getGridPaneRequestsPage() {
        return requestsPageGridPane;
    }
}