package world.propertyInstance.api;

import world.range.RangeImpl;
import world.value.generator.api.ValueGenerator;
import world.enums.Type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractPropertyInstance<T> implements Property, Serializable {

    private final String name;
    private final Type propertyType;

    private  final RangeImpl range;
    private  ValueGenerator<T> valueGenerator;

    private Object value;

    private Integer timeTheValueDosentChange = 0;
    private boolean valueUpdate = false;

    private List<Integer> valueUpdateListWithoutBonus = new ArrayList<>();
    private Map<Integer, List<Integer>> valueUpdatePerTick = new HashMap<>();

    public AbstractPropertyInstance(String name, Type propertyType, ValueGenerator<T> value, RangeImpl range) {
        this.name = name;
        this.propertyType = propertyType;
        this.valueGenerator = value;
        this.value = this.valueGenerator.generateValue();
        this.range = range;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Type getType() {
        return propertyType;
    }

    @Override
    public T generateValue() {
        return valueGenerator.generateValue();
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        if(range != null){
            if(value instanceof Integer){
                if ((Integer) value < range.getFrom() || (Integer) value > range.getTo()) {
                    return;
                }
            }
            else if(value instanceof Float) {
                if ((Float) value < range.getFrom() || (Float) value > range.getTo()) {
                    return;
                }
            }
        }
        if(value != this.value){
            this.value = value;
            valueUpdateListWithoutBonus.add(timeTheValueDosentChange);
            setTimeTheValueDosentChange(0);
            setValueUpdate(true);
        }
    }

    @Override
    public String toString() {
        return "AbstractPropertyInstance{" +
                "name = '" + name + '\'' +
                ", value = " + value +
                '}';
    }

    @Override
    public Integer getTimeTheValueDosentChange() {
        return timeTheValueDosentChange;
    }

    @Override
    public void setTimeTheValueDosentChange(Integer timeTheValueDosentChange) {
        this.timeTheValueDosentChange = timeTheValueDosentChange;
    }

    @Override
    public boolean isValueUpdate() {
        return valueUpdate;
    }

    @Override
    public void setValueUpdate(boolean valueUpdate) {
        this.valueUpdate = valueUpdate;
    }

    @Override
    public RangeImpl getRange() {
        return range;
    }

    @Override
    public void addValueUpdateListPerTick(int tick){
        List<Integer> valueUpdateListPerTick = new ArrayList<>();
        for(Integer value: getCurrentList()){
            valueUpdateListPerTick.add(value);
        }
        if(timeTheValueDosentChange != 0) {
            valueUpdateListPerTick.add(timeTheValueDosentChange);
        }
        valueUpdatePerTick.put(tick, valueUpdateListPerTick);
    }

    private List<Integer> getCurrentList() {
        return valueUpdateListWithoutBonus;
    }

    @Override
    public List<Integer> getValueUpdateListWithoutBonus() {
        if(timeTheValueDosentChange != 0) {
            valueUpdateListWithoutBonus.add(timeTheValueDosentChange);
        }
        return valueUpdateListWithoutBonus;
    }

    @Override
    public List<Integer> getValueUpdateListBonus(int tick) {
        return valueUpdatePerTick.get(tick);
    }
}
