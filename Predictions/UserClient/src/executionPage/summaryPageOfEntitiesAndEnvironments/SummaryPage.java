package executionPage.summaryPageOfEntitiesAndEnvironments;

import DTO.DTOEntityInfo;
import DTO.DTOEnvironmentInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SummaryPage {

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

    @FXML
    private void initializer(){
        entityNameColumn.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        initialAmountColumn.setCellValueFactory(new PropertyValueFactory<>("initialAmount"));
        environmentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        environmentValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
    }

    @FXML
    void moveToResultsPageClicked(ActionEvent event) {

    }

}
