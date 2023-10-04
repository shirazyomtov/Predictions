package executionPage;

import DTO.DTOActions.DTOActionDeserialize;
import DTO.DTOActions.DTOActionInfo;
import DTO.DTOAllWorldsInfo;
import DTO.DTOEntitiesAndEnvironmentInfo;
import DTO.DTOEntityInfo;
import DTO.DTOEnvironmentInfo;
import app.AppController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.HttpClientUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExecutionPageController {
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
    private GridPane executionPageGridPane;

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

    @FXML
    private SplitPane executionsPageSplitPane;

    private List<String> entitiesNames = new ArrayList<>();

    private List<String> environmentsNames = new ArrayList<>();

    private List<DTOEnvironmentInfo> dtoEnvironmentsInfo;

    private BooleanProperty isStartButtonPressed = new SimpleBooleanProperty(false);
    private Integer requestId;
    private String worldName;

    public void setMainController(AppController appController) {
        this.mainController = appController;
    }

    public void setExecutionsPageDetails() {
        executionsPageSplitPane.setDividerPositions(0.5);
        executionsPageSplitPane.getDividers().get(0).positionProperty().addListener((Observable, oldValue, newValue) -> {
            executionsPageSplitPane.setDividerPositions(0.5);
        });
        executionsPageSplitPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            executionsPageSplitPane.setDividerPositions(0.5);
        });
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://localhost:8080/Server_Web_exploded/showEntitiesAndEnvironment").newBuilder();
        urlBuilder.addQueryParameter("worldName", worldName);
        String finalUrl = urlBuilder.build().toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    DTOEntitiesAndEnvironmentInfo dtoEntitiesAndEnvironmentInfo = gson.fromJson(response.body().charStream(), DTOEntitiesAndEnvironmentInfo.class);

                    Platform.runLater(()->{
                        resetAllText();
                        createListEntitiesNames(dtoEntitiesAndEnvironmentInfo.getDtoEntityInfoList());
                        entitiesNamesListView.getItems().clear();
                        entitiesNamesListView.getItems().addAll(entitiesNames);
                        createListEnvironmentsNames(dtoEntitiesAndEnvironmentInfo.getDtoEnvironmentInfoList());
                        dtoEnvironmentsInfo = dtoEntitiesAndEnvironmentInfo.getDtoEnvironmentInfoList();
                        environmentListView.getItems().clear();
                        environmentListView.getItems().addAll(environmentsNames);
                    });
                }
            }
        });
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
        int selectedIndex = entitiesNamesListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            choseValueVbox.visibleProperty().set(true);
            String entityName = entitiesNames.get(selectedIndex);
            entityNameTextField.setText(entityName);
            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://localhost:8080/Server_Web_exploded/getCurrentAmountOfEntity").newBuilder();
            urlBuilder.addQueryParameter("worldName", worldName);
            urlBuilder.addQueryParameter("entityName", entityName);
            String finalUrl = urlBuilder.build().toString();

            HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        Gson gson = new Gson();
                        Integer currentValue= gson.fromJson(response.body().charStream(), Integer.class);

                        Platform.runLater(()->{
                            valueTextField.setText(String.valueOf(currentValue));
                        });
                    }
                }
            });
        }
    }

    @FXML
    void saveValueButtonClicked(ActionEvent event) {
        try {
            if (!valueTextField.getText().isEmpty()) {
                Integer amountOfEntityInstance = Integer.parseInt(valueTextField.getText());
                String entityName = entityNameTextField.getText();
                HttpUrl.Builder urlBuilder = HttpUrl.parse("http://localhost:8080/Server_Web_exploded/setCurrentAmountOfEntity").newBuilder();
                urlBuilder.addQueryParameter("worldName", worldName);
                urlBuilder.addQueryParameter("entityName", entityName);
                urlBuilder.addQueryParameter("amount", amountOfEntityInstance.toString());
                String finalUrl = urlBuilder.build().toString();

                HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            Platform.runLater(() -> {
                                mainController.setSuccessMessage("The value was saved successfully");
                            });
                        }
                        else {
                            handleErrorResponse(response);
                        }
                    }
                });
            } else {
                mainController.setErrorMessage("You need to enter a value before pressing save");
            }
        }
        catch (NumberFormatException e) {
            mainController.setErrorMessage("You need to enter a number as value");
        }
        catch (Exception e) {
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
        int selectedIndex = environmentListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            choseEnvironmentVbox.visibleProperty().set(true);
            String environmentName = environmentsNames.get(selectedIndex);
            DTOEnvironmentInfo dtoEnvironmentInfo = dtoEnvironmentsInfo.get(selectedIndex);
            typeEnvironmentTextField.setText(dtoEnvironmentInfo.getType());
            if(dtoEnvironmentInfo.getRange() != null) {
                rangeEnvironmentTextField.setText(dtoEnvironmentInfo.getRange().getFrom() + " - " + dtoEnvironmentInfo.getRange().getTo());
            }
            else{
                rangeEnvironmentTextField.setText("No range");
            }
            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://localhost:8080/Server_Web_exploded/getCurrentValueOfEnvironment").newBuilder();
            urlBuilder.addQueryParameter("worldName", worldName);
            urlBuilder.addQueryParameter("environmentName", environmentName);
            String finalUrl = urlBuilder.build().toString();

            HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        Gson gson = new Gson();
                        Object currentValue= gson.fromJson(response.body().charStream(), Object.class);

                        Platform.runLater(()->{
                            if(currentValue != null){
                                valueEnvironmentTextField.setText(currentValue.toString());
                            }
                            else{
                                valueEnvironmentTextField.setText("");
                            }
                        });
                    }
                }
            });
        }
    }

    @FXML
    void saveEnvironmentValueButtonClicked(ActionEvent event) {
        try {
            if (!valueEnvironmentTextField.getText().isEmpty()) {
                String valueOfEnvironment = valueEnvironmentTextField.getText();
                int selectedIndex = environmentListView.getSelectionModel().getSelectedIndex();
                String environmentName = environmentsNames.get(selectedIndex);
                HttpUrl.Builder urlBuilder = HttpUrl.parse("http://localhost:8080/Server_Web_exploded/setCurrentValueOfEnvironment").newBuilder();
                urlBuilder.addQueryParameter("worldName", worldName);
                urlBuilder.addQueryParameter("environmentName", environmentName);
                urlBuilder.addQueryParameter("value", valueOfEnvironment);
                String finalUrl = urlBuilder.build().toString();

                HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            Platform.runLater(() -> {
                                mainController.setSuccessMessage("The value was saved successfully");
                            });
                        }
                        else {
                            handleErrorResponse(response);
                        }
                    }
                });
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
        executionPageGridPane.visibleProperty().set(state);
    }

    @FXML
    void clearButtonClicked(ActionEvent event) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://localhost:8080/Server_Web_exploded/clearPastValues").newBuilder();
        urlBuilder.addQueryParameter("worldName", worldName);
        String finalUrl = urlBuilder.build().toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Platform.runLater(() -> {
                        valueTextField.setText("0");
                        valueEnvironmentTextField.clear();
                    });
                }
                else {
                    handleErrorResponse(response);
                }
            }
        });
    }

    @FXML
    void startButtonClicked(ActionEvent event){
//            throws EntityNotDefine, ObjectNotExist, OperationNotCompatibleTypes, OperationNotSupportedType, FormatException {
//        isStartButtonPressed.set(true);
//        mainController.getEngineManager().setSimulation();
//        mainController.getEngineManager().addSimulationTask();
//        mainController.setSimulationsDetails();
//        clearButtonClicked(event);
//        mainController.showResultsPage();

    }

    public BooleanProperty getStartButtonPressedProperty() {
        return isStartButtonPressed;
    }

    public void resetControllers() {
        isStartButtonPressed.set(false);
    }

    public Node getExecutionPageGridPane() {
        return executionPageGridPane;
    }

    public void setRequestIdAndWorldName(Integer requestId, String worldName) {
        this.requestId = requestId;
        this.worldName = worldName;
    }

    private void handleErrorResponse(Response response) throws IOException {
        String responseBody = response.body().string();
        Pattern pattern = Pattern.compile("<b>Message</b>\\s*(.*?)</p>");
        Matcher matcher = pattern.matcher(responseBody);

        if (matcher.find()) {
            String errorMessage = matcher.group(1).trim();
            Platform.runLater(() -> {
                mainController.setErrorMessage(errorMessage);
            });
        }
    }
}