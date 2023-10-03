package requestsPage;

import DTO.*;
import app.AppController;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import okhttp3.*;
import utils.HttpClientUtil;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;

import static javafx.collections.FXCollections.observableArrayList;

public class RequestsPageController {

    @FXML
    private Spinner<Integer> amountOfSimulationSpinner;

    @FXML
    private GridPane requestsPageGridPane;

    @FXML
    private TableView<DTORequestsOfSimulations> requestsTableView;

    @FXML
    private TableColumn<DTORequestsOfSimulations, Integer> requestIdColumn;

    @FXML
    private TableColumn<DTORequestsOfSimulations, String> worldNameColumn;

    @FXML
    private TableColumn<DTORequestsOfSimulations, Integer> totalAmountOfSimulationToRunColumn;

    @FXML
    private TableColumn<DTORequestsOfSimulations, String> statusColumn;

    @FXML
    private TableColumn<DTORequestsOfSimulations, Integer> amountOfRunningSimulations;

    @FXML
    private TableColumn<DTORequestsOfSimulations, Integer> amountOfFinishedSimulations;

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

    private AllocationsRefresher allocationsRefresher;
    private Timer timer;
    @FXML
    public void initialize() {
    }

    public void allocationsRefresher() {
        String userName = mainController.getUsername();
        allocationsRefresher = new AllocationsRefresher(this::addRequest, userName);
        timer = new Timer();
        timer.schedule(allocationsRefresher, 2000, 2000);
    }

    private void addRequest(DTOAllRequestsByUser dtoAllRequestsByUser) {
        ObservableList<DTORequestsOfSimulations> data = FXCollections.observableArrayList();

        for (DTORequestsOfSimulations request: dtoAllRequestsByUser.getAllRequestsByUser()) {

            // Check if the request ID already exists in the table
//            boolean requestExists = data.stream().anyMatch(existingRequest -> existingRequest.getRequestId().equals(requestId));
//
//            if (requestExists) {
//                int index = data.indexOf(request);
//                if (index >= 0) {
//                    data.set(index, request);
//                }
//            }
//            else {
                data.add(request);
//            }
        }
        if(!dtoAllRequestsByUser.getAllRequestsByUser().isEmpty()) {
            Platform.runLater(() -> {
                DTORequestsOfSimulations selectedItem = requestsTableView.getSelectionModel().getSelectedItem();
                requestsTableView.setItems(data);
                requestIdColumn.setCellValueFactory(new PropertyValueFactory<>("requestId"));
                worldNameColumn.setCellValueFactory(new PropertyValueFactory<>("worldName"));
                totalAmountOfSimulationToRunColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
                statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
                amountOfRunningSimulations.setCellValueFactory(new PropertyValueFactory<>("amountOfSimulationsCurrentlyRunning"));
                amountOfFinishedSimulations.setCellValueFactory(new PropertyValueFactory<>("amountOfFinishedSimulations"));
                if(selectedItem != null) {
                    requestsTableView.getSelectionModel().select(selectedItem);
                }
            });
        }
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
        String userName = mainController.getUsername();
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
            urlBuilder.addQueryParameter("username", userName);
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