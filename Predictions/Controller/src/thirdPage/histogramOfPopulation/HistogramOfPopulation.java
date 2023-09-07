package thirdPage.histogramOfPopulation;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import java.util.Map;

public class HistogramOfPopulation {

    @FXML
    private BarChart<Object, Number> histogramBarChart;

    public BarChart<Object, Number> getHistogramBarChart() {
        return histogramBarChart;
    }

    public void createBarChart(Map<Object, Integer> propertyInfoAboutValues) {
        histogramBarChart.getData().clear();
        XYChart.Series<Object, Number> series = new XYChart.Series<>();

        for (Map.Entry<Object, Integer> entry : propertyInfoAboutValues.entrySet()) {
            Object xValue = entry.getKey();
            Integer yValue = entry.getValue();
            series.getData().add(new XYChart.Data<>(xValue.toString(), yValue));
        }

        histogramBarChart.getData().add(series);

    }
}
