package world.rule.activation;

import jaxb.schema.generated.PRDActivation;

import java.util.Random;

public class ActivationImpl implements Activation{
    private int ticks = 1;

    private  double probability = 1;

    public ActivationImpl(PRDActivation prdActivation) {
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

    @Override
    public boolean isActive(int tickNumber) { // not good
        Random random = new Random();
        double probabilityParam = random.nextDouble();
        return (tickNumber % ticks == 0) && (probability == 1 || probability > probabilityParam);    }
}
