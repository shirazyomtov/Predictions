package world.rule.activation;

import jaxb.schema.generated.PRDActivation;

public class Activation {
    private int ticks = 1;

    private  double probability = 1;

    public Activation(PRDActivation prdActivation) {
        if(prdActivation != null){
            if(prdActivation.getProbability() != null){
                this.probability = prdActivation.getProbability();
            }
            if(prdActivation.getTicks() != null){
                this.ticks = prdActivation.getTicks();
            }
        }
    }

    @Override
    public String toString() {
        return "Activation{" +
                "ticks=" + ticks +
                ", probability=" + probability +
                '}';
    }

    public int getTicks() {
        return ticks;
    }

    public double getProbability() {
        return probability;
    }
}
