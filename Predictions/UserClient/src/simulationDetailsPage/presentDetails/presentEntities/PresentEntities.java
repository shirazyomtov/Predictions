package simulationDetailsPage.presentDetails.presentEntities;

import DTO.DTOPropertyInfo;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

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
        fromColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DTOPropertyInfo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DTOPropertyInfo, String> param) {
                String fromValue = null;
                if(param.getValue().getRange() != null) {
                    fromValue = param.getValue().getRange().getFrom();
                }
                return new SimpleStringProperty(fromValue != null ? fromValue : ""); // Return an empty string if fromValue is null
            }
        });

        toColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DTOPropertyInfo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DTOPropertyInfo, String> param) {
                String toValue = null;
                if(param.getValue().getRange() != null) {
                    toValue = param.getValue().getRange().getTo();
                }
                return new SimpleStringProperty(toValue != null ? toValue : ""); // Return an empty string if toValue is null
            }
        });

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