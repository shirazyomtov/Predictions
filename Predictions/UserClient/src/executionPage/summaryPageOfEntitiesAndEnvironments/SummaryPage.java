package executionPage.summaryPageOfEntitiesAndEnvironments;

import DTO.DTOEntitiesAndEnvironmentInfo;
import DTO.DTOEntityInfo;
import DTO.DTOEnvironmentInfo;
import app.AppController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.collections.ObservableList;


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
