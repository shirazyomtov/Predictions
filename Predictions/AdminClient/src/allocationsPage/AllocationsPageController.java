package allocationsPage;

import DTO.DTOAllRequests;
import DTO.DTORequestsOfSimulations;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.HttpAdminClientUtil;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;

public class AllocationsPageController {

    @FXML
    private GridPane allocationsPageGridPane;

    @FXML
    private ChoiceBox<String> statusChoiceBox;

    @FXML
    private HBox changeStausHbox;

    @FXML
    private Button saveStatusButton;

    @FXML
    private TableView<DTORequestsOfSimulations> requestsTableView;

    @FXML
    private TableColumn<DTORequestsOfSimulations, Integer> requestIdColumn;

    @FXML
    private TableColumn<DTORequestsOfSimulations, String> userNameColumn;

    @FXML
    private TableColumn<DTORequestsOfSimulations, String> worldNameColumn;

    @FXML
    private TableColumn<DTORequestsOfSimulations, Integer> totalAmountOfSimulationToRunColumn;

    @FXML
    private TableColumn<DTORequestsOfSimulations, String> termainationColumn;

    @FXML
    private TableColumn<DTORequestsOfSimulations, String> statusColumn;

    @FXML
    private TableColumn<DTORequestsOfSimulations, String> amountOfRunningSimulations;

    @FXML
    private TableColumn<DTORequestsOfSimulations, String> amountOfFinishedSimulations;

    @FXML
    private ScrollPane scrollPaneWrapper;

    private AllocationsRefresher allocationsRefresher;

    private Timer timer;

    private Integer requestId = null;

    private ObservableList<DTORequestsOfSimulations> data;

    public void setChoiceBoxValues(){
        statusChoiceBox.getItems().addAll("APPROVED", "DECLINED");
    }

    public void allocationsRefresher() {
        allocationsRefresher = new AllocationsRefresher(this::addRequest);
        timer = new Timer();
        timer.schedule(allocationsRefresher, 2000, 2000);
    }

    private void addRequest(DTOAllRequests dtoAllRequests) {
        ObservableList<DTORequestsOfSimulations> newData = FXCollections.observableArrayList();

        for (Map.Entry<Integer, DTORequestsOfSimulations> entry : dtoAllRequests.getRequestsOfSimulationsMap().entrySet()) {
            newData.add(entry.getValue());
        }

        if(requestsTableView.getItems().isEmpty()){
            Platform.runLater(()->{
                requestsTableView.setItems(newData);
                data = newData;
                requestIdColumn.setCellValueFactory(new PropertyValueFactory<>("requestId"));
                userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
                worldNameColumn.setCellValueFactory(new PropertyValueFactory<>("worldName"));
                totalAmountOfSimulationToRunColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
                termainationColumn.setCellValueFactory(new PropertyValueFactory<>("termination"));
                statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
                amountOfRunningSimulations.setCellValueFactory(new PropertyValueFactory<>("amountOfSimulationsCurrentlyRunning"));
                amountOfFinishedSimulations.setCellValueFactory(new PropertyValueFactory<>("amountOfFinishedSimulations"));
            });
        }
        else{
            for (Map.Entry<Integer, DTORequestsOfSimulations> entry : dtoAllRequests.getRequestsOfSimulationsMap().entrySet()) {
                Integer requestId = entry.getKey();
                DTORequestsOfSimulations request = entry.getValue();
                boolean requestExists = data.stream().anyMatch(existingRequest -> existingRequest.getRequestId().equals(requestId));
                if(requestExists){
                    Integer requestIdInData = findIndexByRequestId(requestId);
                    if(!data.get(requestIdInData).getStatus().equals(request.getStatus()) || !data.get(requestIdInData).getAmountOfSimulationsCurrentlyRunning().equals(request.getAmountOfSimulationsCurrentlyRunning()) ||
                            !data.get(requestIdInData).getAmountOfFinishedSimulations().equals(request.getAmountOfFinishedSimulations())){
                        data.set(requestIdInData, request);
                    }
                }
                else{
                    data.add(request);
                }
            }
        }
    }

    private int findIndexByRequestId(int requestIdToFind) {
        for (int i = 0; i < data.size(); i++) {
            DTORequestsOfSimulations request = data.get(i);
            if (request.getRequestId() == requestIdToFind) {
                return i;
            }
        }
        return 0;
    }

    @FXML
    void checkIfPendingStatus(ActionEvent event) {

    }

    public Node getGridAllocationsPage() {
        return allocationsPageGridPane;
    }

    @FXML
    void saveStatusButtonClicked(ActionEvent event) {
        String status = statusChoiceBox.getValue();

        if(status != null){
            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://localhost:8080/Server_Web_exploded/updateStatus").newBuilder();
            urlBuilder.addQueryParameter("status", status);
            urlBuilder.addQueryParameter("requestId", requestId.toString());
            String finalUrl = urlBuilder.build().toString();
            HttpAdminClientUtil.runAsyncGet(finalUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()) {
                        changeStausHbox.setVisible(false);
                        Platform.runLater(()->{
                            statusChoiceBox.setValue(null);
                        });
                    }
                }
            });
        }

    }

    @FXML
    void selectedRequestClicked(MouseEvent event) {
        ObservableList<TablePosition> selectedCells = requestsTableView.getSelectionModel().getSelectedCells();
        if (!selectedCells.isEmpty()) {
            TablePosition selectedCell = selectedCells.get(0);
            int rowIndex = selectedCell.getRow();
            DTORequestsOfSimulations selectedRequest = requestsTableView.getItems().get(rowIndex);

            String status = statusColumn.getCellData(selectedRequest);
            requestId = requestIdColumn.getCellData(selectedRequest);
            if(status.equals("PENDING")){
                changeStausHbox.setVisible(true);
            }
            else{
                changeStausHbox.setVisible(false);
            }
        }

    }
}