package thirdPage.histogramOfPopulation;

import DTO.DTOHistogram;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Map;

public class HistogramOfPopulation {

    @FXML
    private TableColumn<DTOHistogram, Object> valueColumn;

    @FXML
    private TableColumn<DTOHistogram, Integer> amountColumn;

    @FXML
    private TableView<DTOHistogram> histogramOfPopulationTableView;

    @FXML
    private ScrollPane scrollPaneHistogram;

    public Node getHistogramScrollPane() {
        return scrollPaneHistogram;
    }

    public void createTableView(List<DTOHistogram> histogramList) {
        histogramOfPopulationTableView.getItems().clear();

        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value")); // Map.Entry key
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount")); // Map.Entry value

        histogramOfPopulationTableView.getItems().addAll(histogramList);
    }
}
