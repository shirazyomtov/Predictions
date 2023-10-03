package requestsPage;

import DTO.DTOAllWorldsInfo;
import DTO.DTOWorldDefinitionInfo;
import app.AppController;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import okhttp3.*;
import utils.HttpClientUtil;

import java.io.IOException;

import static javafx.collections.FXCollections.observableArrayList;

public class RequestsPageController {

    @FXML
    private Spinner<Integer> amountOfSimulationSpinner;

    @FXML
    private GridPane requestsPageGridPane;

    @FXML
    private TableView<?> requestsTableView;

    @FXML
    private ScrollPane scrollPaneWrapper;

    @FXML
    private HBox secondsAndTicksHbox;

    @FXML
    private Spinner<Integer> secondsSpinner;

    @FXML
    private ChoiceBox<String> simulationNameChoiceBox;

    @FXML
    private Spinner<Integer> ticksSpinner;

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
    }

    public void setControllers(AppController appController){
        mainController = appController;
        requestsSplitPane.setDividerPositions(0.35);
        requestsSplitPane.getDividers().get(0).positionProperty().addListener((Observable, oldValue, newValue) -> {
            requestsSplitPane.setDividerPositions(0.35);
        });
        terminationChoiceBox.setItems(observableArrayList("By user", "By seconds and ticks"));
        terminationChoiceBox.setOnAction(this::setTickAndSecondsHbox);
        ticksSpinner.visibleProperty().bind(ticksCheckBox.selectedProperty());
        secondsSpinner.visibleProperty().bind(secondsCheckBox.selectedProperty());
        configureSpinner(amountOfSimulationSpinner);
        configureSpinner(secondsSpinner);
        configureSpinner(ticksSpinner);
    }

    private void configureSpinner(Spinner<Integer> spinner) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1);
        spinner.setValueFactory(valueFactory);

        spinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                spinner.getEditor().setText(oldValue);
            }
        });

        spinner.getValueFactory().setValue(1);
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
        Integer amountOfSimulation = amountOfSimulationSpinner.getValue();
        String terminationValue = terminationChoiceBox.getValue();
        String ticks = "";
        String seconds = "";
        String byUser = "false";
        boolean valid = true;
        try {
            checkRequestDetails(simulationName, terminationValue);
        }
        catch (Exception e){
            mainController.setErrorMessage(e.getMessage());
            valid = false;
        }

        if(valid) {
            if (terminationValue.equals("By seconds and ticks")) {
                if (ticksCheckBox.isSelected()) {
                    ticks = ticksSpinner.getValue().toString();
                }
                if (secondsCheckBox.isSelected()) {
                    seconds = secondsSpinner.getValue().toString();
                }
            } else {
                byUser = "true";
            }
            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://localhost:8080/Server_Web_exploded/addUserRequest").newBuilder();
            urlBuilder.addQueryParameter("simulationName", simulationName);
            urlBuilder.addQueryParameter("amountOfSimulation", amountOfSimulation.toString());
            urlBuilder.addQueryParameter("ticks", ticks);
            urlBuilder.addQueryParameter("seconds", seconds);
            urlBuilder.addQueryParameter("user", byUser);
            String finalUrl = urlBuilder.build().toString();

            HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                    }
                }
            });
        }
    }

    private void checkRequestDetails(String simulationName, String terminationValue) throws Exception{
        checkIfUserChoseSimulationName(simulationName);
        checkValidTerminationDetails(terminationValue);
    }


    private void checkIfUserChoseSimulationName(String simulationName) throws NullPointerException{
        if(simulationName == null){
            throw new NullPointerException("You need to chose at least one of the worlds name");
        }
    }

    private void checkValidTerminationDetails(String terminationValue) throws Exception {
        if (terminationValue != null) {
            if (terminationValue.equals("By seconds and ticks")) {
                if (!ticksCheckBox.isSelected() && !secondsCheckBox.isSelected()) {
                    throw new Exception("You need to choose at least one of the checkboxes: seconds and ticks");
                }
            }
        }
        else {
            throw new Exception("You need to choose one of the termination checkboxes: By user or By seconds and ticks");
        }
    }

    public Node getGridPaneRequestsPage() {
        return requestsPageGridPane;
    }

    public void setWorldsNames(DTOAllWorldsInfo dtoAllWorldsInfo) {
        for(String worldName: dtoAllWorldsInfo.getDtoWorldDefinitionInfoMap().keySet()){
            if(!checkIfExist(worldName)){
                Platform.runLater(()->{
                    simulationNameChoiceBox.getItems().addAll(worldName);
                });
            }
        }
    }

    private boolean checkIfExist(String worldName) {
        return simulationNameChoiceBox.getItems().contains(worldName);
    }
}