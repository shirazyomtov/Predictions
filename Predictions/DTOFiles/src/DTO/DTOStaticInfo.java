package DTO;

import java.util.Map;

public class DTOStaticInfo {
    private Map<Object, Integer> propertyInfoAboutValues;
    private Float averageTickOfProperty;

    private Float averageValue;

    public DTOStaticInfo(Map<Object, Integer> propertyInfoAboutValues, Float averageTickOfProperty, Float averageValue) {
        this.propertyInfoAboutValues = propertyInfoAboutValues;
        this.averageTickOfProperty = averageTickOfProperty;
        this.averageValue = averageValue;
    }

    public Map<Object, Integer> getPropertyInfoAboutValues() {
        return propertyInfoAboutValues;
    }

    public Float getAverageTickOfProperty() {
        return averageTickOfProperty;
    }

    public Float getAverageValue() {
        return averageValue;
    }
}
