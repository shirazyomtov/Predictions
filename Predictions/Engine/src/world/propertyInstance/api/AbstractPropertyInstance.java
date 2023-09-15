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

    private List<Integer> valueUpdateList = new ArrayList<>();

    private Map<Integer, List<Integer>> valueUpdateListPerTick = new HashMap<>();

    public AbstractPropertyInstance(String name, Type propertyType, ValueGenerator<T> value, RangeImpl range) {
        this.name = name;
        this.propertyType = propertyType;
        this.valueGenerator = value;
        this.value = this.valueGenerator.generateValue();
        this.range = range;
    }

    @Override
    public void addValueUpdateListPerTick(Integer tick){
        List<Integer> cloneList = getValueUpdateList();
        List<Integer> deepClone = new ArrayList<>();
        for (Integer value : cloneList){
            deepClone.add(value);
        }
        valueUpdateListPerTick.put(tick, deepClone);
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
            valueUpdateList.add(timeTheValueDosentChange);
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
    public List<Integer> getValueUpdateList() {
        if(valueUpdateList.isEmpty()){
            valueUpdateList.add(timeTheValueDosentChange);
        }
        return valueUpdateList;
    }

    @Override
    public RangeImpl getRange() {
        return range;
    }
}
