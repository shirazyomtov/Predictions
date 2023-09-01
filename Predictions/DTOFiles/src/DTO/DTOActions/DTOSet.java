package DTO.DTOActions;

public class DTOSet extends DTOActionInfo{
    private final String propertyName;
    private final String value;

    public DTOSet(String actionName, String entityName, String secondEntityName, String propertyName, String value) {
        super(actionName, entityName, secondEntityName);
        this.propertyName = propertyName;
        this.value = value;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getValue() {
        return value;
    }
}
