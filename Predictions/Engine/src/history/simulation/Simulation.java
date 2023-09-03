package history.simulation;

import exceptions.*;
import javafx.util.Pair;
import world.entity.instance.EntityInstance;
import world.entity.instance.location.Location;
import world.enums.ActionType;
import world.propertyInstance.api.Property;
import world.rule.RuleImpl;
import world.rule.action.Action;
import world.rule.action.Kill;
import world.rule.action.Replace;
import world.rule.action.condition.AbstractCondition;
import world.rule.action.condition.MultipleCondition;
import world.rule.action.condition.SingleCondition;
import world.worldInstance.WorldInstance;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public  class Simulation implements Serializable {
    private WorldInstance worldInstance = null;

    private LocalDateTime dateTime;
    private final String formattedDateTime;

    private Boolean isFinish = false;

    private Integer currentSecond = 0;

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

    public String runSimulation() throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException, OperationNotSupportedType, OperationNotCompatibleTypes, FormatException, EntityNotDefine {
        long startMillisSeconds = System.currentTimeMillis();
        String message = null;
        int seconds = 0;
        List<Action> activationAction ;
        List<EntityInstance> secondaryEntities = null;
        List<EntityInstance> entitiesToRemove = new ArrayList<>();
        List<Pair<EntityInstance,Action>> replaceActions = new ArrayList<>();

        while ((worldInstance.getWorldDefinition().getTermination().getSecond() == null || seconds <= worldInstance.getWorldDefinition().getTermination().getSecond()) &&
                (worldInstance.getWorldDefinition().getTermination().getTicks() == null || worldInstance.getCurrentTick() <= worldInstance.getWorldDefinition().getTermination().getTicks())) {
            currentSecond = seconds;
            moveEntities();
            activationAction = createActivationActionsList();
            for (EntityInstance entityInstance : worldInstance.getEntityInstanceList()) {
                for (Action activeAction: activationAction){
                    if (activeAction.getEntityName().equals(entityInstance.getName())){
                        if(activeAction.getSecondaryEntity() == null){
                            performOperation(activeAction, entityInstance, null, entitiesToRemove, replaceActions); //check
                        }
                        else {
                            secondaryEntities = calculateTotalNumberOfSecondaryInstances(activeAction);
                            for (EntityInstance secondaryEntity: secondaryEntities){
                                performOperation(activeAction, entityInstance, secondaryEntity, entitiesToRemove, replaceActions);
                            }
                        }
                    }
                }
            }

            for (EntityInstance entityInstanceToRemove : entitiesToRemove) {
                Kill killAction = new Kill(entityInstanceToRemove.getName(), null);
                killAction.operation(entityInstanceToRemove, worldInstance, null);
            }
            for (Pair<EntityInstance, Action> tuple : replaceActions) {
                if(tuple.getValue() instanceof Replace) {
                    Replace replaceAction = new Replace(tuple.getKey().getName(), ((Replace) tuple.getValue()).getCreateEntityName(), ((Replace) tuple.getValue()).getMode(), null);
                    replaceAction.operation(tuple.getKey(), worldInstance, null);
                }
            }

            entitiesToRemove.clear();
            replaceActions.clear();
            updateTickProperty();
            worldInstance.setCurrentTick(worldInstance.getCurrentTick() + 1);
            long currentMilliSeconds = System.currentTimeMillis();
            seconds = (int) ((currentMilliSeconds - startMillisSeconds) / 1000);
        }

        isFinish = true;
        if (worldInstance.getWorldDefinition().getTermination().getSecond() != null && seconds > worldInstance.getWorldDefinition().getTermination().getSecond()) {
            message = "The simulation has ended because more than " + worldInstance.getWorldDefinition().getTermination().getSecond() + " seconds have passed";
        } else if (worldInstance.getWorldDefinition().getTermination().getTicks() != null && worldInstance.getCurrentTick() > worldInstance.getWorldDefinition().getTermination().getTicks()) {
            message = "The simulation has ended because more than " + worldInstance.getWorldDefinition().getTermination().getTicks() + " ticks have passed";
        }

        return message;
        // todo: functions
    }

    private List<EntityInstance> calculateTotalNumberOfSecondaryInstances(Action activeAction) throws ObjectNotExist, OperationNotCompatibleTypes, OperationNotSupportedType, FormatException, EntityNotDefine {
        List<EntityInstance> secondaryEntities;
        int count = 0;
        Integer numberOfSecondaryInstance;
        if (activeAction.getSecondaryEntity().getCount().equals("ALL")){
            secondaryEntities = getAllInstanceOfSpecificEntity(activeAction);
        }
        else {
            for (EntityInstance entityInstance : worldInstance.getEntityInstanceList()){
                if(entityInstance.getName().equals(activeAction.getSecondaryEntity().getSecondaryEntityName())){
                    count++;
                }
            }
            numberOfSecondaryInstance = Integer.parseInt(activeAction.getSecondaryEntity().getCount());
            if (numberOfSecondaryInstance > count){
                secondaryEntities = getAllInstanceOfSpecificEntity(activeAction); // todo
            }
            else{
                secondaryEntities = getRandomInstancesOfSpecificEntity(getAllInstanceOfSpecificEntity(activeAction), numberOfSecondaryInstance);
            }
            if(activeAction.getSecondaryEntity().getCondition() != null) {
                secondaryEntities = getAllEntitiesThatPassedTheCondition(secondaryEntities, activeAction);
            }
        }

        return  secondaryEntities;
    }

    private List<EntityInstance> getAllEntitiesThatPassedTheCondition(List<EntityInstance> secondaryEntities, Action activeAction) throws ObjectNotExist, OperationNotCompatibleTypes, OperationNotSupportedType, FormatException, EntityNotDefine {
        List<EntityInstance> entitiesThatPassedTheCondition = new ArrayList<>();
        boolean flag;
        for(EntityInstance entityInstance: secondaryEntities) {
            AbstractCondition abstractCondition = activeAction.getSecondaryEntity().getCondition();
            if (abstractCondition.getSingularity().equals("single")) {
                flag = ((SingleCondition) abstractCondition).checkIfConditionIsTrue(entityInstance, worldInstance, null);
            }
            else {
                MultipleCondition multipleCondition = (MultipleCondition) abstractCondition;
                flag = multipleCondition.checkCondition(entityInstance, multipleCondition.getConditions(), multipleCondition.getLogical(), worldInstance, null);
            }
            if (flag) {
                entitiesThatPassedTheCondition.add(entityInstance);
            }
        }

        return entitiesThatPassedTheCondition;
    }

    private List<EntityInstance> getRandomInstancesOfSpecificEntity(List<EntityInstance> allInstanceOfSpecificEntity, Integer numberOfSecondaryInstance) {
        List<EntityInstance> secondaryEntities = new ArrayList<>();
        Random random = new Random();
        int randomIndexOfList;
        for(int i = 0; i < numberOfSecondaryInstance; i++){
            randomIndexOfList = random.nextInt(allInstanceOfSpecificEntity.size());
            secondaryEntities.add(allInstanceOfSpecificEntity.get(randomIndexOfList));
        }

        return secondaryEntities;
    }

    private List<EntityInstance> getAllInstanceOfSpecificEntity(Action activeAction){
        List<EntityInstance> secondaryEntities = new ArrayList<>();
        for (EntityInstance entityInstance : worldInstance.getEntityInstanceList()){
            if(entityInstance.getName().equals(activeAction.getSecondaryEntity().getSecondaryEntityName())){
                secondaryEntities.add(entityInstance);
            }
        }
        return  secondaryEntities;
    }

    private List<Action> createActivationActionsList(){
        List<Action> activationAction = new ArrayList<>();

        for (RuleImpl rule : worldInstance.getWorldDefinition().getRules()) {
            if (rule.getActivation().isActive(worldInstance.getCurrentTick())) {
                for (Action action : rule.nameActions()) {
                    activationAction.add(action);
                }
            }
        }
        return  activationAction;
    }

    private void updateTickProperty() {
        for (EntityInstance entityInstance: worldInstance.getEntityInstanceList()){
            for(Property property: entityInstance.getAllProperty().values()){
                if(!property.isValueUpdate()){
                    property.setTimeTheValueDosentChange(property.getTimeTheValueDosentChange() + 1);
                }
                property.setValueUpdate(false);
            }
        }
    }

    private void moveEntities() {
        Location location = null;
        for (EntityInstance entityInstance: worldInstance.getEntityInstanceList()) {
             location = worldInstance.getWorldDefinition().getTwoDimensionalGrid().getNewLocation(entityInstance.getLocation());
             if (location != null) {
                 entityInstance.getLocation().setRow(location.getRow());
                 entityInstance.getLocation().setCol(location.getCol());
             }
        }
    }

    private void performOperation(Action action, EntityInstance entityInstance, EntityInstance secondaryEntity, List<EntityInstance> entitiesToRemove,  List<Pair<EntityInstance,Action>> replaceActions) throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException, OperationNotSupportedType, OperationNotCompatibleTypes, FormatException, EntityNotDefine {
        Action action1;
        if (!action.getActionType().equals(ActionType.KILL) && !action.getActionType().equals(ActionType.REPLACE)) {
            action1 = action.operation(entityInstance, worldInstance, secondaryEntity);
            if(action1 != null) {
                if (action1.getActionType().equals(ActionType.KILL)) {
                    entitiesToRemove.add(entityInstance);
                } else if (action1.getActionType().equals(ActionType.REPLACE)) {
                    replaceActions.add(new Pair<>(entityInstance ,action1));
                }
            }
        }
        else if (action.getActionType().equals(ActionType.REPLACE)) {
            replaceActions.add(new Pair<>(entityInstance, action));
        }
        else {
            entitiesToRemove.add(entityInstance);
        }
    }

    public Boolean getIsFinish() {
        return isFinish;
    }

    public Integer getCurrentSecond() {
        return currentSecond;
    }
}
