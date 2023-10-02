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
    private TableColumn<DTORequestsOfSimulations, Integer> amountOfRunningSimulations;

    @FXML
    private TableColumn<DTORequestsOfSimulations, Integer> amountOfFinishedSimulations;

    @FXML
    private ScrollPane scrollPaneWrapper;

    private AllocationsRefresher allocationsRefresher;

    private Timer timer;

    private Integer requestId = null;

    public void setChoiceBoxValues(){
        statusChoiceBox.getItems().addAll("APPROVED", "DECLINED");
    }

    public void allocationsRefresher() {
        allocationsRefresher = new AllocationsRefresher(this::addRequest);
        timer = new Timer();
        timer.schedule(allocationsRefresher, 2000, 2000);
    }
//    private void addRequest(DTOAllRequests dtoAllRequests) {
//        ObservableList<DTORequestsOfSimulations> data = requestsTableView.getItems();
//
//        for (Map.Entry<Integer, DTORequestsOfSimulations> entry : dtoAllRequests.getRequestsOfSimulationsMap().entrySet()) {
//            Integer newRequestId = entry.getKey();
//            DTORequestsOfSimulations request = entry.getValue();
//
//            // Check if the request ID already exists in the table
//            boolean requestExists = data.stream().anyMatch(existingRequest -> existingRequest.getRequestId().equals(newRequestId));
//
//            if (requestExists) {
////                int index = data.indexOf(request);
////                if (index >= 0) {
////                    data.set(index, request);
////                }
//            }
//            else {
////                data.add(request);
//                requestsTableView.getItems().add(request);
//
//            }
//        }
    private void addRequest(DTOAllRequests dtoAllRequests) {
        ObservableList<DTORequestsOfSimulations> data = FXCollections.observableArrayList();

        for (Map.Entry<Integer, DTORequestsOfSimulations> entry : dtoAllRequests.getRequestsOfSimulationsMap().entrySet()) {
            Integer requestId = entry.getKey();
            DTORequestsOfSimulations request = entry.getValue();

            // Check if the request ID already exists in the table
            boolean requestExists = data.stream().anyMatch(existingRequest -> existingRequest.getRequestId().equals(requestId));

            if (requestExists) {
                int index = data.indexOf(request);
                if (index >= 0) {
                    data.set(index, request);
                }
            }
            else {
                data.add(request);
            }
        }
        if(!dtoAllRequests.getRequestsOfSimulationsMap().isEmpty()) {
            Platform.runLater(() -> {
                DTORequestsOfSimulations selectedItem = requestsTableView.getSelectionModel().getSelectedItem();
                requestsTableView.setItems(data);
                requestIdColumn.setCellValueFactory(new PropertyValueFactory<>("requestId"));
                worldNameColumn.setCellValueFactory(new PropertyValueFactory<>("worldName"));
                totalAmountOfSimulationToRunColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
                termainationColumn.setCellValueFactory(new PropertyValueFactory<>("termination"));
                statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
                if(selectedItem != null) {
                    requestsTableView.getSelectionModel().select(selectedItem);
                }
            });
        }
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
