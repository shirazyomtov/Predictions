package world.propertyInstance.api;

import world.enums.Type;
import world.range.RangeImpl;
import world.value.generator.api.ValueGenerator;

import java.io.Serializable;
import java.util.List;

public interface Property extends Serializable {
    String getName();
    Type getType();
    Object getValue();
    void setValue(Object val);
    Object generateValue();
    Integer getTimeTheValueDosentChange();
    void setTimeTheValueDosentChange(Integer timeTheValueDosentChange);
    boolean isValueUpdate();
    void setValueUpdate(boolean valueUpdate);
    List<Integer> getValueUpdateListBonus(int tick);
    List<Integer> getValueUpdateListWithoutBonus();

    void addValueUpdateListPerTick(int tick);
    RangeImpl getRange();
}