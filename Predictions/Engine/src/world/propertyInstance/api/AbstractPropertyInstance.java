package world.propertyInstance.api;

import world.value.generator.api.ValueGenerator;
import world.enums.Type;

public abstract class AbstractPropertyInstance<T> implements Property {

    private final String name;
    private final Type propertyType;
    private  ValueGenerator<T> valueGenerator;

    private Object value;

    public AbstractPropertyInstance(String name, Type propertyType, ValueGenerator<T> value) {
        this.name = name;
        this.propertyType = propertyType;
        this.valueGenerator = value;
        this.value = value;
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

//    @Override
//    public void setValueGenerator(Object valueGenerator) {
//        this.valueGenerator = (ValueGenerator<T>)valueGenerator;
//    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "AbstractPropertyInstance{" +
                "name = '" + name + '\'' +
                ", value = " + value +
                '}';
    }

}
