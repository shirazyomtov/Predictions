package DTO;

public class DTOActivationInfo {

    private int ticks;

    private  double probability;

    public DTOActivationInfo(int ticks, double probability) {
        this.ticks = ticks;
        this.probability = probability;
    }

    public int getTicks() {
        return ticks;
    }

    public double getProbability() {
        return probability;
    }
}
