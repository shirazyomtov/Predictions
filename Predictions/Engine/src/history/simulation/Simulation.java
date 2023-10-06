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
import world.termination.Termination;
import world.worldInstance.WorldInstance;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public  class Simulation implements Serializable, Runnable {
    private WorldInstance worldInstance = null;

    private LocalDateTime dateTime;
    private final String formattedDateTime;

    private Boolean isFinish = false;

    private Boolean isFailed = false;

    private Integer currentSecond = 0;
    private int currentTick = 1;

    private boolean pause = false;

    private boolean stop = false;

    private String message = "";

    private long totalTimePause = 0;
    private Map<Integer, List<EntityInstance>> valuesPerTick = new HashMap<>();

    private String userName;

    private Integer requestID;

    private  Termination termination;

    public Simulation(WorldInstance worldInstance, LocalDateTime dateTime, String userName, Integer requestID, Termination termination) {
        this.worldInstance = worldInstance;
        this.dateTime = dateTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH.mm.ss");
        this.formattedDateTime = dateTime.format(formatter);
        this.userName = userName;
        this.requestID = requestID;
        this.termination = termination;
    }

    public WorldInstance getWorldInstance() {
        return worldInstance;
    }

    public String getFormattedDateTime() {
        return this.formattedDateTime;
    }

    @Override
    public void run() throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException, OperationNotSupportedType, OperationNotCompatibleTypes, FormatException, EntityNotDefine {
        long startMillisSeconds = System.currentTimeMillis();
        long pauseTime;
        if (termination.getTerminationByUser())
        {
            while (!stop){
                worldInstance.setSecondsPerTick(currentTick, currentSecond);
                try {
                    pauseTime = runSimulation();
                    totalTimePause += pauseTime;
                }
                catch (Exception e){
                    isFinish = true;
                    isFailed = true;
                    message = e.getMessage();
                    break;
                }
                long currentMilliSeconds = System.currentTimeMillis();
                currentSecond = (int) ((currentMilliSeconds - totalTimePause - startMillisSeconds) / 1000);
            }
        }
        else {
            while ((termination.getSecond() == null || currentSecond <= termination.getSecond()) &&
                    (termination.getTicks() == null || currentTick <= termination.getTicks())) {
                if (!stop) {
                    worldInstance.setSecondsPerTick(currentTick, currentSecond);
                    try {
                        pauseTime = runSimulation();
                        totalTimePause += pauseTime;
                    }
                    catch (Exception e){
                        isFinish = true;
                        isFailed = true;
                        message = e.getMessage();
                        break;
                    }
                    long currentMilliSeconds = System.currentTimeMillis();
                    currentSecond = (int) ((currentMilliSeconds - totalTimePause - startMillisSeconds) / 1000);
                }
                else {
                    break;
                }
            }
        }
        worldInstance.addAmountOfEntitiesPerTick(currentTick);
        isFinish = true;
    }

    private long runSimulation() throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException, OperationNotSupportedType, OperationNotCompatibleTypes, FormatException, EntityNotDefine{
        List<Action> activationAction ;
        List<EntityInstance> secondaryEntities = null;
        List<EntityInstance> entitiesToRemove = new ArrayList<>();
        List<Pair<EntityInstance,Action>> replaceActions = new ArrayList<>();
        if(currentTick % 5000 == 0 || currentTick == 1){
            worldInstance.addAmountOfEntitiesPerTick(currentTick);
        }
        long beforePauseMilliSeconds = System.currentTimeMillis();
        synchronized(this) {
            while (this.pause) {
                try {
                    this.wait();
                } catch (InterruptedException var2) {
                    System.out.println("InterruptedException caught");
                }
            }
        }
        long afterPauseMilliSeconds = System.currentTimeMillis();
        long time = afterPauseMilliSeconds - beforePauseMilliSeconds;
        moveEntities();
        activationAction = createActivationActionsList();
        for (EntityInstance entityInstance : worldInstance.getEntityInstanceList()) {
            for (Action activeAction: activationAction){
                if (activeAction.getEntityName().equals(entityInstance.getName())){
                    if(activeAction.getSecondaryEntity() == null){
                        performOperation(activeAction, entityInstance, null, entitiesToRemove, replaceActions, null);
                    }
                    else {
                        secondaryEntities = calculateTotalNumberOfSecondaryInstances(activeAction);
                        if (secondaryEntities != null) {
                            for (EntityInstance secondaryEntity : secondaryEntities) {
                                performOperation(activeAction, entityInstance, secondaryEntity, entitiesToRemove, replaceActions, activeAction.getSecondaryEntity().getSecondaryEntityName());
                            }
                        }
                        else{
                            performOperation(activeAction, entityInstance, null, entitiesToRemove, replaceActions, activeAction.getSecondaryEntity().getSecondaryEntityName());
                        }
                    }
                }
            }
        }

        for (EntityInstance entityInstanceToRemove : entitiesToRemove) {
            Kill killAction = new Kill(entityInstanceToRemove.getName(), null);
            killAction.operation(entityInstanceToRemove, worldInstance, null, null, null);
        }
        for (Pair<EntityInstance, Action> tuple : replaceActions) {
            if(tuple.getValue() instanceof Replace) {
                Replace replaceAction = new Replace(tuple.getKey().getName(), ((Replace) tuple.getValue()).getCreateEntityName(), ((Replace) tuple.getValue()).getMode(), null);
                replaceAction.operation(tuple.getKey(), worldInstance, null, null, null);
            }
        }

        entitiesToRemove.clear();
        replaceActions.clear();
        updateTickProperty();
        currentTick = currentTick + 1;
        return time;
    }

        public Map<Object, Integer> getPropertyValuesMapPerTick(String entityName, String propertyName, int tick) {
        Map<Object, Integer> valuesProperty = new HashMap<>();
        List<EntityInstance> entityInstanceListValuesPerTick;
        entityInstanceListValuesPerTick = worldInstance.getEntityInstanceList();
        for (EntityInstance entityInstance : entityInstanceListValuesPerTick) {
            if (entityInstance.getName().equals(entityName)) {
                for (Property property : entityInstance.getAllProperty().values()) {
                    if (property.getName().equals(propertyName)) {
                        if (!valuesProperty.containsKey(property.getValue())) {
                            valuesProperty.put(property.getValue(), 1);
                        } else {
                            valuesProperty.put(property.getValue(), valuesProperty.get(property.getValue()) + 1);
                        }
                    }
                }
            }
        }

        return valuesProperty;
    }

    public Float getAverageTickValueOfSpecificProperty(String entityName, String propertyName, int tick){
        float sum = 0;
        float count = 0;
        List<EntityInstance> entityInstanceListValuesPerTick;
        entityInstanceListValuesPerTick = worldInstance.getEntityInstanceList();
        for (EntityInstance entityInstance: entityInstanceListValuesPerTick){
            if(entityInstance.getName().equals(entityName)){
                sum = sum + entityInstance.getAvgAmountOfTickTheValueDosentChange(propertyName);
                count++;
            }
        }
        return  sum/count;
    }
    private List<EntityInstance> calculateTotalNumberOfSecondaryInstances(Action activeAction) throws ObjectNotExist, OperationNotCompatibleTypes, OperationNotSupportedType, FormatException, EntityNotDefine {
        List<EntityInstance> secondaryEntitiesPassedCondition = null;
        List<EntityInstance> secondaryEntities;
        Integer numberOfSecondaryInstance;
        if(activeAction.getSecondaryEntity().getCondition() != null) {
            secondaryEntitiesPassedCondition = getAllEntitiesThatPassedTheCondition(getAllInstanceOfSpecificEntity(activeAction), activeAction);
        }
        else{
            secondaryEntitiesPassedCondition = getAllInstanceOfSpecificEntity(activeAction);
        }
        if(!secondaryEntitiesPassedCondition.isEmpty()) {
            if (activeAction.getSecondaryEntity().getCount().equals("ALL")) {
                secondaryEntities = secondaryEntitiesPassedCondition;
            }
            else {
                numberOfSecondaryInstance = Integer.parseInt(activeAction.getSecondaryEntity().getCount());
                if (numberOfSecondaryInstance > secondaryEntitiesPassedCondition.size()) {
                    secondaryEntities = secondaryEntitiesPassedCondition;
                } else {
                    secondaryEntities = getRandomInstancesOfSpecificEntity(secondaryEntitiesPassedCondition, numberOfSecondaryInstance);
                }
            }

            return secondaryEntities;
        }
        else {
            return null;
        }
    }

    private List<EntityInstance> getAllEntitiesThatPassedTheCondition(List<EntityInstance> secondaryEntities, Action activeAction) throws ObjectNotExist, OperationNotCompatibleTypes, OperationNotSupportedType, FormatException, EntityNotDefine {
        List<EntityInstance> entitiesThatPassedTheCondition = new ArrayList<>();
        boolean flag;
        for(EntityInstance entityInstance: secondaryEntities) {
            AbstractCondition abstractCondition = activeAction.getSecondaryEntity().getCondition();
            if (abstractCondition.getSingularity().equals("single")) {
                flag = ((SingleCondition) abstractCondition).checkIfConditionIsTrue(entityInstance, worldInstance, null, null);
            }
            else {
                MultipleCondition multipleCondition = (MultipleCondition) abstractCondition;
                flag = multipleCondition.checkCondition(entityInstance, multipleCondition.getConditions(), multipleCondition.getLogical(), worldInstance, null, null);
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
            if (rule.getActivation().isActive(currentTick)) {
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
             location = worldInstance.getTwoDimensionalGrid().getNewLocation(entityInstance.getLocation());
             if (location != null) {
                 entityInstance.getLocation().setRow(location.getRow());
                 entityInstance.getLocation().setCol(location.getCol());
             }
        }
    }

    private void performOperation(Action action, EntityInstance entityInstance, EntityInstance secondaryEntity, List<EntityInstance> entitiesToRemove,  List<Pair<EntityInstance,Action>> replaceActions, String secondEntityName) throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException, OperationNotSupportedType, OperationNotCompatibleTypes, FormatException, EntityNotDefine {
        List<Action> action1;
        List<EntityInstance> proximity = new ArrayList<>();
        if (!action.getActionType().equals(ActionType.KILL) && !action.getActionType().equals(ActionType.REPLACE)) {
            action1 = action.operation(entityInstance, worldInstance, secondaryEntity, secondEntityName, proximity);
            if(action1 != null) {
                for (Action actionFromList : action1) {
                    if (actionFromList.getActionType().equals(ActionType.KILL)) {
                        if (actionFromList.getEntityName().equals(entityInstance.getName())) {
                            entitiesToRemove.add(entityInstance);
                        } else if (secondaryEntity != null) {
                            entitiesToRemove.add(secondaryEntity);
                        }
                        else if (!proximity.isEmpty()) {
                            entitiesToRemove.add(proximity.get(0));
                        }
                    } else if (actionFromList.getActionType().equals(ActionType.REPLACE)) {
                        if (actionFromList.getEntityName().equals(entityInstance.getName())) {
                            replaceActions.add(new Pair<>(entityInstance, actionFromList));
                        } else if (secondaryEntity != null) {
                            replaceActions.add(new Pair<>(secondaryEntity, actionFromList));
                        }
                        else if (!proximity.isEmpty()) {
                            replaceActions.add(new Pair<>(proximity.get(0), actionFromList));
                        }
                    }
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

    public int getCurrentTick() {
        return currentTick;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public Boolean getIsFailed() {
        return isFailed;
    }

    public String getMessage() {
        return message;
    }

    public String getUserName() {
        return userName;
    }
}
