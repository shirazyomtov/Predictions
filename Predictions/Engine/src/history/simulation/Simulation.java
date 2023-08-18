package history.simulation;

import exceptions.ObjectNotExist;
import exceptions.OperationNotCompatibleTypes;
import exceptions.OperationNotSupportedType;
import world.entity.instance.EntityInstance;
import world.enums.ActionType;
import world.rule.RuleImpl;
import world.rule.action.Action;
import world.rule.action.Kill;
import world.worldInstance.WorldInstance;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public  class Simulation implements Serializable {
    private WorldInstance worldInstance = null;

    private LocalDateTime dateTime;
    private final String formattedDateTime;

    public Simulation(WorldInstance worldInstance, LocalDateTime dateTime) {
        this.worldInstance = worldInstance;
        this.dateTime = dateTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH.mm.ss");
        this.formattedDateTime = dateTime.format(formatter);
    }

    public WorldInstance getWorldInstance() {
        return worldInstance;
    }

    public String getFormattedDateTime() {
        return this.formattedDateTime;
    }

    public String runSimulation() throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException, OperationNotSupportedType, OperationNotCompatibleTypes {
        long startMillisSeconds = System.currentTimeMillis();
        String message = null;
        int seconds = 0;
        while ((worldInstance.getWorldDefinition().getTermination().getSecond() == null || seconds <= worldInstance.getWorldDefinition().getTermination().getSecond()) &&
                (worldInstance.getWorldDefinition().getTermination().getTicks() == null || worldInstance.getCurrentTick() <= worldInstance.getWorldDefinition().getTermination().getTicks())) {
            for (RuleImpl rule : worldInstance.getWorldDefinition().getRules()) {
                if (rule.getActivation().isActive(worldInstance.getCurrentTick())) {
                    for (Action action : rule.nameActions()) {
                        performOperation(action);
                    }
                }
            }

            worldInstance.setCurrentTick(worldInstance.getCurrentTick() + 1);
            long currentMilliSeconds = System.currentTimeMillis();
            seconds = (int) ((currentMilliSeconds - startMillisSeconds) / 1000);
        }

        if (worldInstance.getWorldDefinition().getTermination().getSecond() != null && seconds > worldInstance.getWorldDefinition().getTermination().getSecond()) {
            message = "The simulation has ended because more than " + worldInstance.getWorldDefinition().getTermination().getSecond() + " seconds have passed";
        } else if (worldInstance.getWorldDefinition().getTermination().getTicks() != null && worldInstance.getCurrentTick() > worldInstance.getWorldDefinition().getTermination().getTicks()) {
            message = "The simulation has ended because more than " + worldInstance.getWorldDefinition().getTermination().getTicks() + " ticks have passed";
        }

        return message;
    }

    private void performOperation(Action action) throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException, OperationNotSupportedType, OperationNotCompatibleTypes {
        boolean flag = false;
        String entityName = action.getEntityName();
        List<EntityInstance> entitiesToRemove = new ArrayList<>();
        for (EntityInstance entityInstance : worldInstance.getEntityInstanceList()) {
            if (entityName.equals(entityInstance.getName())) {
                if (!action.getActionType().equals(ActionType.KILL)) {
                    flag = action.operation(entityInstance, worldInstance);
                    if (flag){
                        entitiesToRemove.add(entityInstance);
                    }
                }
                else {
                    entitiesToRemove.add(entityInstance);
                }
            }
        }

        for (EntityInstance entityInstance : entitiesToRemove) {
            Kill killAction = new Kill(entityInstance.getName());
            killAction.operation(entityInstance, worldInstance);
        }
    }

}
