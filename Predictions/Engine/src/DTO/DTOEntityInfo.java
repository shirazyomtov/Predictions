package DTO;

import java.util.List;

public class DTOEntityInfo {
    private int initialAmount;

    private int finalAmount;

    private String entityName;

    private List<DTOPropertyInfo> propertyInfoListDTO;

    public DTOEntityInfo(int initialAmount, int finalAmount, String entityName, List<DTOPropertyInfo> propertyInfoListDTO) {
        this.initialAmount = initialAmount;
        this.finalAmount = finalAmount;
        this.entityName = entityName;
        this.propertyInfoListDTO = propertyInfoListDTO;
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
}
