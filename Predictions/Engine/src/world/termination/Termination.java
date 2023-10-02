package world.termination;

import jaxb.schema.generated.PRDBySecond;
import jaxb.schema.generated.PRDByTicks;

import java.io.Serializable;

public class Termination implements Serializable {

    private Integer ticks = null;
    private Integer second = null;

    private Boolean terminationByUser = false;

    public Termination(String ticks, String second, String byUser) {
        if (!ticks.isEmpty()) {
            this.ticks = Integer.parseInt(ticks);
        }
        if (!second.isEmpty()) {
            this.second = Integer.parseInt(second);
        }
        if(byUser.equals("true")){
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
