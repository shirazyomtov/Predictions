package DTO;

import java.util.List;
import java.util.Map;

public class DTOStaticInfo {
    private List<DTOHistogram> histograms;
    private Float averageTickOfProperty;

    private Float averageValue;

    public DTOStaticInfo(List<DTOHistogram> histograms, Float averageTickOfProperty, Float averageValue) {
        this.histograms = histograms;
        this.averageTickOfProperty = averageTickOfProperty;
        this.averageValue = averageValue;
    }

    public List<DTOHistogram> getHistograms() {
        return histograms;
    }

    public Float getAverageTickOfProperty() {
        return averageTickOfProperty;
    }

    public Float getAverageValue() {
        return averageValue;
    }
}
