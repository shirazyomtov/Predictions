package DTO;

public class DTOHistogram {
    private Object value;
    private int amount;

    public DTOHistogram(Object value, int amount) {
        this.value = value;
        this.amount = amount;
    }

    public Object getValue() {
        return value;
    }

    public int getAmount() {
        return amount;
    }
}
