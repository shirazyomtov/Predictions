package world.entity.instance;

import world.entity.instance.location.Location;
import world.propertyInstance.api.Property;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class EntityInstance implements Serializable {

    private final String name;
    private final Map<String, Property> allProperty;

    private Location location;

    private boolean isKill = false;

    public EntityInstance(String name, Map<String, Property> allProperty, Location location) {
        this.name = name;
        this.allProperty = allProperty;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Map<String, Property> getAllProperty() {
        return allProperty;
    }

    public Object getPropertyValue(String propertyName, boolean propertyValue){
        if(allProperty.containsKey(propertyName)){
                if (propertyValue) {
                    return allProperty.get(propertyName).getValue();
                }
                else{
                    return allProperty.get(propertyName).getTimeTheValueDosentChange();
                }
        }

        return null;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Float getAvgAmountOfTickTheValueDosentChange(String propertyName, int tick, boolean bonus, boolean futureTickWithBonus4) {
        float sum = 0;
        Property property = allProperty.get(propertyName);
        List<Integer> propertyValueList;
        if (bonus && !futureTickWithBonus4){
            propertyValueList = property.getValueUpdateListBonus(tick);
        }
        else{
            propertyValueList = property.getValueUpdateListWithoutBonus();
        }
        for (Integer amount : propertyValueList){
            sum = sum + amount;
        }
        return sum/propertyValueList.size();
    }

    public void setKill(boolean kill) {
        isKill = kill;
    }

    public boolean isKill() {
        return isKill;
    }
}
