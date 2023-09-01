package world.rule.action.expression;

import exceptions.FormatException;
import exceptions.ObjectNotExist;
import exceptions.OperationNotCompatibleTypes;
import world.auxiliaryFunctions.AuxiliaryFunctionsImpl;
import world.entity.instance.EntityInstance;
import world.enums.Type;
import world.propertyInstance.api.Property;
import world.worldInstance.WorldInstance;
import xml.XMLValidation;

import java.io.Serializable;

import static world.enums.AuxiliaryFunction.*;

public class ExpressionIml implements Expression, Serializable {
    private final String expressionName;

    private String propertyName = null;
    private Type type = null;

    public ExpressionIml(String expressionName, String propertyName) {
        this.expressionName = expressionName;
        this.propertyName = propertyName;
    }
    public ExpressionIml(String expressionName) {
        this.expressionName = expressionName;
    }
    @Override
    public String decipher(EntityInstance primaryEntity, WorldInstance worldInstance, EntityInstance seocndEntity) throws ObjectNotExist, NumberFormatException, OperationNotCompatibleTypes, FormatException {
        String object = null ;
        if (propertyName != null) {
            Type propertyNameType = decipherType(primaryEntity, worldInstance);
            setType(propertyNameType);
        }
        if (checkOptionByFunctionName(expressionName)) {
            int index = expressionName.indexOf("(");
            String functionName = expressionName.substring(0, index).trim();
            String value = expressionName.substring(index + 1, expressionName.length() - 1).trim();
            if (functionName.equals(ENVIRONMENT.getFunctionName())) {
                if(propertyName != null) {
                    object = AuxiliaryFunctionsImpl.environment(value, worldInstance, type).toString();
                }
                else {
                    object = AuxiliaryFunctionsImpl.environment(value, worldInstance, null).toString();
                }
            }
            else if (functionName.equals(RANDOM.getFunctionName())) {
                object = AuxiliaryFunctionsImpl.random(value).toString();
            }
            else if (functionName.equals(EVALUATE.getFunctionName())) {
                if(propertyName != null) {
                        object = AuxiliaryFunctionsImpl.evaluate(value, worldInstance, type, primaryEntity, seocndEntity).toString();
                }
                else{
                    object = AuxiliaryFunctionsImpl.evaluate(value, worldInstance, null, primaryEntity, seocndEntity).toString();
                }
            }
            else if (functionName.equals(PERCENT.getFunctionName())) {
                if (propertyName != null) {
                    object = AuxiliaryFunctionsImpl.percent(value, primaryEntity, worldInstance, propertyName, seocndEntity).toString();
                }
                else{
                    object = AuxiliaryFunctionsImpl.percent(value, primaryEntity, worldInstance, null, seocndEntity).toString();
                }
            }
            else if (functionName.equals(TICKS.getFunctionName())) {
                object = AuxiliaryFunctionsImpl.ticks(value, worldInstance, primaryEntity, seocndEntity).toString();
            }
        }
        else if (checkIfValueIsProperty(primaryEntity) != null) {
            if (propertyName != null) {
                Type propertyType = type;
                Type valueType = checkProperty(primaryEntity, expressionName);
                if (checkSameType(propertyType, valueType)) {
                    object = checkIfValueIsProperty(primaryEntity).toString();
                } else {
                    throw new OperationNotCompatibleTypes(propertyType.toString(), valueType.toString());
                }
            }
            else {
                object = checkIfValueIsProperty(primaryEntity).toString();
            }
        }
        else {
            object = expressionName;
        }
        return object;
    }

    private Type decipherType(EntityInstance primaryEntity, WorldInstance worldInstance) {
        Type type1;
        type1 = checkProperty(primaryEntity, propertyName);
        if(type1 == null) {
            if (checkOptionByFunctionName(propertyName)) {
                int functionIndex = propertyName.indexOf("(");
                if(functionIndex == -1){
                    //todo:handle error when it not function
                }
                String value = propertyName.substring(functionIndex + 1, propertyName.length() - 1).trim();
                if (extractFunctionName(propertyName).equals("environment")) {
                    type1 = worldInstance.getWorldDefinition().checkTypeOfEnvironmentProperty(value);
                } else if (extractFunctionName(propertyName).equals("evaluate")) {
                    int index = value.indexOf(".");
                    if (index == -1) {
                        type1 = Type.STRING;
                    }
                    String entity = value.substring(0, index).trim();
                    String property = value.substring(index + 1).trim();
                    if (entity.equals(primaryEntity.getName())) {
                        type1 = checkProperty(primaryEntity, propertyName);
                    }
                    if (type1 == null) {
                        type1 = Type.STRING;
                    }
                } else {
                    type1 = Type.FLOAT;
                }
            } else {
                type1 = Type.STRING;
            }
        }
        return  type1;
    }


    private Type checkProperty(EntityInstance primaryEntity, String propertyName){
        for(Property propertyInstance: primaryEntity.getAllProperty().values()){
            if(propertyInstance.getName().equals(propertyName)){
                return propertyInstance.getType();
            }
        }
        return  null;
    }
    private static boolean checkSameType(Type propertyType, Type valueType) {
        if ((propertyType.equals(Type.DECIMAL) || (propertyType.equals(Type.FLOAT))) && (valueType.equals(Type.DECIMAL) || (valueType.equals(Type.FLOAT)))) {
            return  true;
        }
        return propertyType.equals(valueType);
    }


    private Object checkIfValueIsProperty(EntityInstance entity) {
        return entity.getPropertyValue(expressionName, true);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
