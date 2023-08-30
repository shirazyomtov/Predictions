package world.rule.action;

import exceptions.*;
import jaxb.schema.generated.PRDAction;
import world.entity.instance.EntityInstance;
import world.enums.ActionType;
import world.worldInstance.WorldInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Proximity extends  Action implements Serializable {

    private final String targetEntity;
    private final String of;
    private List<Action> actions = null;

    public Proximity(PRDAction prdAction){
        super(prdAction.getPRDBetween().getSourceEntity(), ActionType.PROXIMITY, prdAction.getPRDSecondaryEntity());
        this.targetEntity = prdAction.getPRDBetween().getTargetEntity();
        this.of =  prdAction.getPRDEnvDepth().getOf();
        if (prdAction.getPRDActions().getPRDAction() != null){
            this.actions = ActionFactory.createListActions(prdAction.getPRDActions().getPRDAction());
        }
    }

    @Override
    public Action operation(EntityInstance sourceEntity, WorldInstance worldInstance, EntityInstance secondaryEntity) throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException, OperationNotSupportedType, OperationNotCompatibleTypes, FormatException, EntityNotDefine {
        EntityInstance entityInstance = checkAndGetAppropriateInstance(sourceEntity, secondaryEntity);
        boolean isProximity = false;
        List<EntityInstance> proximityEntityInstance = new ArrayList<>();
        for (EntityInstance targetEntityInstance: worldInstance.getEntityInstanceList()){
            if(targetEntity.equals(targetEntityInstance.getName())){
                if (checkTheProximity(entityInstance, targetEntityInstance, worldInstance)){
                    isProximity = true;
                    proximityEntityInstance.add(targetEntityInstance);
                }
            }
        }
        if (isProximity){
            for(EntityInstance targetEntity: proximityEntityInstance) {
                if (actions != null) {
                    for (Action action : actions) {
                            action.operation(entityInstance, worldInstance, targetEntity);
                        }
                    }
                }
            }
        return null;
    }

    private boolean checkTheProximity(EntityInstance entityInstanceSource, EntityInstance entityInstanceTarget,  WorldInstance worldInstance) throws NumberFormatException {
        int row;
        int rowPositive;
        int colPositive;
        //String ofString =
        int ofInt = Integer.parseInt(of);
        int entityInstanceSourceRow = entityInstanceSource.getLocation().getRow();
        int entityInstanceSourceCol = entityInstanceSource.getLocation().getCol();
        int entityInstanceTargetRow = entityInstanceTarget.getLocation().getRow();
        int entityInstanceTargetCol = entityInstanceTarget.getLocation().getCol();
        for(row = entityInstanceSourceRow - ofInt; row <= entityInstanceSourceRow + ofInt; row++){
            if (row < 0){
                rowPositive = worldInstance.getWorldDefinition().getTwoDimensionalGrid().getRows() + row;
            }
            else if (row > worldInstance.getWorldDefinition().getTwoDimensionalGrid().getRows() -1){
                rowPositive = row - worldInstance.getWorldDefinition().getTwoDimensionalGrid().getRows();
            }
            else {
                rowPositive = row;
            }
            for(int col = entityInstanceSourceCol - ofInt; col<=entityInstanceSourceCol + ofInt; col++){
                if (col < 0){
                    colPositive = worldInstance.getWorldDefinition().getTwoDimensionalGrid().getCols() + col;
                }
                else if (col > worldInstance.getWorldDefinition().getTwoDimensionalGrid().getCols() -1){
                    colPositive = col - worldInstance.getWorldDefinition().getTwoDimensionalGrid().getCols();
                }
                else {
                    colPositive = col;
                }
                if (rowPositive == entityInstanceTargetRow && colPositive == entityInstanceTargetCol){
                    return  true;
                }
            }
        } //todo
        return false;
    }

    public String getTargetEntity() {
        return targetEntity;
    }

    public String getOf() {
        return of;
    }

    public List<Action> getActions() {
        return actions;
    }
}
