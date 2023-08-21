package world.termination;

import jaxb.schema.generated.PRDBySecond;
import jaxb.schema.generated.PRDByTicks;

import java.io.Serializable;

public class Termination implements Serializable {

    private Integer ticks = null;
    private Integer second = null;

    public Termination(PRDByTicks prdByTicks, PRDBySecond prdBySecond) {
        if (prdByTicks != null) {
            this.ticks = prdByTicks.getCount();
        }
        if (prdBySecond != null) {
            this.second = prdBySecond.getCount();
        }
    }

    public Integer getTicks() {
        return ticks;
    }

    public Integer getSecond() {
        return second;
    }
}
