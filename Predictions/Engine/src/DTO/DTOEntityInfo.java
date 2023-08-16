package DTO;

public class DTOEntityInfo {
    private int initialAmount;

    private int finalAmount;

    private String entityName;


    public DTOEntityInfo(int initialAmount, int finalAmount, String entityName) {
        this.initialAmount = initialAmount;
        this.finalAmount = finalAmount;
        this.entityName = entityName;
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
