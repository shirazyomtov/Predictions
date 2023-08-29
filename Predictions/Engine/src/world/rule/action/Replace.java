package world.rule.action;

import exceptions.*;
import jaxb.schema.generated.PRDAction;
import world.entity.definition.EntityDefinition;
import world.entity.definition.PropertyDefinition;
import world.entity.instance.EntityInstance;
import world.enums.ActionType;
import world.propertyInstance.api.Property;
import world.worldInstance.WorldInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static engineManager.EngineManager.initProperty;

public class Replace extends Action implements Serializable {

    private final String createEntityName;
    private final String mode;


    public Replace(String entityName, String createEntityName, String mode, PRDAction.PRDSecondaryEntity prdSecondaryEntity){
        super(entityName, ActionType.REPLACE, prdSecondaryEntity);
        this.createEntityName = createEntityName;
        this.mode = mode;
    }

    @Override
    public Action operation(EntityInstance entity, WorldInstance worldInstance, EntityInstance secondaryEntity) throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException, OperationNotSupportedType, OperationNotCompatibleTypes, FormatException, EntityNotDefine {
        EntityInstance entityInstance = checkAndGetAppropriateInstance(entity, secondaryEntity);
        if (mode.equals("scratch")){
            createEntityInstanceFromScratch(worldInstance, null);
        }
        else if (mode.equals("derived")){
            createEntityInstanceFromDerived(entityInstance, worldInstance);
        }
        Kill killAction = new Kill(entityInstance.getName(), null);
        killAction.operation(entityInstance, worldInstance, null);
        return null;
    }

    private void createEntityInstanceFromDerived(EntityInstance entity, WorldInstance worldInstance) {
        List <Property> samePropertyName = new ArrayList<>();
        for (Property property: entity.getAllProperty().values()){
            EntityInstance createEntity = worldInstance.isEntityExists(createEntityName);
            if (createEntity != null) {
                for (Property createEntityProperty : createEntity.getAllProperty().values()){
                    if(createEntityProperty.getName().equals(property.getName()) && createEntityProperty.getType().equals(property.getType())){
                        createEntityProperty.setValue(property.getValue());
                        samePropertyName.add(createEntityProperty);
                    }
                }
            }
        }
        createEntityInstanceFromScratch(worldInstance, samePropertyName);
    }

    private void createEntityInstanceFromScratch(WorldInstance worldInstance, List<Property> samePropertyName) {
        boolean flag = false;
        for (EntityDefinition entityDefinition: worldInstance.getWorldDefinition().getEntityDefinition().values()){
            if(entityDefinition.getName().equals(createEntityName)){
                Map<String, Property> allProperty = new HashMap<>();
                for(PropertyDefinition propertyDefinition: entityDefinition.getProps()){
                    if(samePropertyName == null) {
                        allProperty.put(propertyDefinition.getName(), initProperty(propertyDefinition));
                    }
                    else {
                        for (Property property: samePropertyName){
                            if (property.getName().equals(propertyDefinition.getName())){
                                flag = true;
                            }
                        }
                        if (!flag){
                            allProperty.put(propertyDefinition.getName(), initProperty(propertyDefinition));
                        }
                        flag = false;
                    }
                }
                worldInstance.addEntityInstanceToEntityInstanceList(new EntityInstance(entityDefinition.getName(), allProperty,  worldInstance.getWorldDefinition().getTwoDimensionalGrid().createNewLocation()));
                break;
            }
        }

    }

    public String getCreateEntityName() {
        return createEntityName;
    }

    public String getMode() {
        return mode;
    }
}
