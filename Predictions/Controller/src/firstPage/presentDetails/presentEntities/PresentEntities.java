package firstPage.presentDetails.presentEntities;

import DTO.DTOPropertyInfo;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.util.List;

public class PresentEntities {
    @FXML
    private GridPane gridPaneEntitiesDetails;
    @FXML
    private TextField entityNameTextField;

    @FXML
    private TableView<DTOPropertyInfo> entitiesTableView;

    @FXML
    private TableColumn<DTOPropertyInfo, String> nameColumn;

    @FXML
    private TableColumn<DTOPropertyInfo, String> typeColumn;

    @FXML
    private TableColumn<DTOPropertyInfo, String> rangeColumn;

    @FXML
    private TableColumn<DTOPropertyInfo, String> fromColumn;

    @FXML
    private TableColumn<DTOPropertyInfo, String> toColumn;

    @FXML
    private TableColumn<DTOPropertyInfo, Boolean> isRandomInitColumn;

    public void setAllColumns(List<DTOPropertyInfo> properties){
        entitiesTableView.getItems().clear();
        entitiesTableView.getItems().addAll(properties);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        rangeColumn.setCellValueFactory(new PropertyValueFactory<>("range"));
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("from"));
        toColumn.setCellValueFactory(new PropertyValueFactory<>("to"));
        isRandomInitColumn.setCellValueFactory(new PropertyValueFactory<>("isRandom"));
    }

    public void setEntityNameTextField(String entityName) {
        entityNameTextField.setText(entityName);
    }

    public void setVisibleEntitiesPage(boolean state) {
        gridPaneEntitiesDetails.visibleProperty().set(state);
    }

    public Node getEntitiesGridPane() {
        return gridPaneEntitiesDetails;
    }
}