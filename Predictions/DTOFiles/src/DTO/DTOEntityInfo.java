package DTO;

import java.util.List;

public class DTOEntityInfo {
    private int initialAmount;

    private int finalAmount;

    private String entityName;

    private List<DTOPropertyInfo> properties;


    public DTOEntityInfo(String entityName, List<DTOPropertyInfo> properties) {
        this.entityName = entityName;
        this.properties = properties;
    }

    public DTOEntityInfo(int initialAmount, int finalAmount, String entityName, List<DTOPropertyInfo> properties) {
        this.initialAmount = initialAmount;
        this.finalAmount = finalAmount;
        this.entityName = entityName;
        this.properties = properties;
    }

    public int getInitialAmount() {
        return initialAmount;
    }

    public int getFinalAmount() {
        return finalAmount;
    }

    public String getEntityName() {
        return entityName;
    }

    public List<DTOPropertyInfo> getProperties() {
        return properties;
    }
}
