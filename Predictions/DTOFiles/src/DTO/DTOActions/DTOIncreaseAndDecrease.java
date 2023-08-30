package DTO.DTOActions;

public class DTOIncreaseAndDecrease extends DTOActionInfo{
    private final String propertyName;
    private final String by;

    public DTOIncreaseAndDecrease(String actionName, String entityName, String secondEntityName, String propertyName, String by) {
        super(actionName, entityName, secondEntityName);
        this.propertyName = propertyName;
        this.by = by;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getBy() {
        return by;
    }
}
