package executionPage.summaryPageOfEntitiesAndEnvironments;

import DTO.DTOEntitiesAndEnvironmentInfo;
import DTO.DTOEntityInfo;
import DTO.DTOEnvironmentInfo;
import app.AppController;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.collections.ObservableList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.HttpClientUtil;

import java.io.IOException;


public class SummaryPage {
    @FXML
    private GridPane summaryPageGridPane;

    @FXML
    private TableView<DTOEntityInfo> entitiesTableView;

    @FXML
    private TableColumn<DTOEntityInfo, String> entityNameColumn;

    @FXML
    private TableColumn<DTOEntityInfo, String> initialAmountColumn;

    @FXML
    private TableView<DTOEnvironmentInfo> environmentTableView;
    @FXML
    private TableColumn<DTOEntityInfo, String> environmentNameColumn;

    @FXML
    private TableColumn<DTOEnvironmentInfo, String> environmentValueColumn;

    private AppController mainController;

    private Integer requestId;
    private String worldName;
    private Integer executeID;

    @FXML
    void moveToResultsPageClicked(ActionEvent event) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://localhost:8080/Server_Web_exploded/startSimulation").newBuilder();
        urlBuilder.addQueryParameter("worldName", worldName);
        urlBuilder.addQueryParameter("userName", mainController.getUsername());
        urlBuilder.addQueryParameter("requestId",requestId.toString());
        urlBuilder.addQueryParameter("executeID", executeID.toString());
        String finalUrl = urlBuilder.build().toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Platform.runLater(()->{
                        mainController.setExecuteIdInExecutionPage(executeID + 1);
                        mainController.setSimulationsDetails();
                        mainController.showResultsPage();
                    });
                }
            }
        });
    }

    public Node getSummaryPageGridPane() {
        return summaryPageGridPane;
    }

    public void setDetails(AppController mainController, Integer requestId, String worldName, Integer executeID, DTOEntitiesAndEnvironmentInfo dtoEntitiesAndEnvironmentInfo) {
        this.mainController = mainController;
        this.requestId = requestId;
        this.worldName = worldName;
        this.executeID = executeID;
        entityNameColumn.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        initialAmountColumn.setCellValueFactory(new PropertyValueFactory<>("initialAmount"));
        environmentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        environmentValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        ObservableList<DTOEntityInfo> observableListEntities = FXCollections.observableList(dtoEntitiesAndEnvironmentInfo.getDtoEntityInfoList());
        ObservableList<DTOEnvironmentInfo> observableListEnvironment = FXCollections.observableList(dtoEntitiesAndEnvironmentInfo.getDtoEnvironmentInfoList());
        entitiesTableView.setItems(observableListEntities);
        environmentTableView.setItems(observableListEnvironment);
    }

    @FXML
    void cancelButtonClicked(ActionEvent event) {
        mainController.showExecutionsPage(requestId, worldName);
        mainController.setExecuteIdInExecutionPage(executeID);
    }
}
