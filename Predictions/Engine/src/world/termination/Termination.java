package world.termination;

import jaxb.schema.generated.PRDBySecond;
import jaxb.schema.generated.PRDByTicks;

import java.io.Serializable;

public class Termination implements Serializable {

    private Integer ticks = null;
    private Integer second = null;

    private Boolean terminationByUser = false;

    public Termination(PRDByTicks prdByTicks, PRDBySecond prdBySecond, Object prdByUser) {
        if (prdByTicks != null) {
            this.ticks = prdByTicks.getCount();
        }
        if (prdBySecond != null) {
            this.second = prdBySecond.getCount();
        }
        if(prdByUser != null){
            this.terminationByUser = true;
        }
    }

    public Integer getTicks() {
        return ticks;
    }

    public Integer getSecond() {
        return second;
    }

    public Boolean getTerminationByUser() {
        return terminationByUser;
    }
}
