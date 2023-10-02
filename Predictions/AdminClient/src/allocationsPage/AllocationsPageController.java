package allocationsPage;

import DTO.DTOAllRequests;
import DTO.DTORequestsOfSimulations;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.util.Map;
import java.util.Optional;
import java.util.Timer;

public class AllocationsPageController {

    @FXML
    private GridPane allocationsPageGridPane;

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

    public void allocationsRefresher() {
        allocationsRefresher = new AllocationsRefresher(this::addRequest);
        timer = new Timer();
        timer.schedule(allocationsRefresher, 2000, 2000);
    }

    private void addRequest(DTOAllRequests dtoAllRequests) {
        ObservableList<DTORequestsOfSimulations> data = FXCollections.observableArrayList();

        for (Map.Entry<Integer, DTORequestsOfSimulations> entry : dtoAllRequests.getRequestsOfSimulationsMap().entrySet()) {
            Integer requestId = entry.getKey();
            DTORequestsOfSimulations request = entry.getValue();

            // Check if the request ID already exists in the table
            Optional<DTORequestsOfSimulations> existingRequestOpt = data.stream()
                    .filter(existingRequest -> existingRequest.getRequestId().equals(requestId))
                    .findFirst();

            if (existingRequestOpt.isPresent()) {
//                DTORequestsOfSimulations existingRequest = existingRequestOpt.get();
//                existingRequest.setUserName(request.getUserName());
//                existingRequest.setWorldName(request.getWorldName());
                // Update other properties as needed
            } else {
                // If not found, add the item
                data.add(request);
            }
        }
        if(!dtoAllRequests.getRequestsOfSimulationsMap().isEmpty()) {
            Platform.runLater(() -> {
                requestsTableView.setItems(data);
                requestIdColumn.setCellValueFactory(new PropertyValueFactory<>("requestId"));
                worldNameColumn.setCellValueFactory(new PropertyValueFactory<>("worldName"));
                totalAmountOfSimulationToRunColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
                termainationColumn.setCellValueFactory(new PropertyValueFactory<>("termination"));
                statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

            });
        }
    }





    @FXML
    void checkIfPendingStatus(ActionEvent event) {

    }

    public Node getGridAllocationsPage() {
        return allocationsPageGridPane;
    }
}
