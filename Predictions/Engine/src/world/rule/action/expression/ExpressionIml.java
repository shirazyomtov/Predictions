package world.rule.action.expression;

import exceptions.FormatException;
import exceptions.ObjectNotExist;
import exceptions.OperationNotCompatibleTypes;
import world.auxiliaryFunctions.AuxiliaryFunctionsImpl;
import world.entity.instance.EntityInstance;
import world.enums.Type;
import world.propertyInstance.api.Property;
import world.worldInstance.WorldInstance;

import java.io.Serializable;

import static world.enums.AuxiliaryFunction.*;

public class ExpressionIml implements Expression, Serializable {
    private  String expressionName;

    private String propertyName = null;//todo delete
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
        if (checkOptionByFunctionName(expressionName)) {
            int index = expressionName.indexOf("(");
            String functionName = expressionName.substring(0, index).trim();
            String value = expressionName.substring(index + 1, expressionName.length() - 1).trim();
            if (functionName.equals(ENVIRONMENT.getFunctionName())) {
                if(propertyName != null) {
                    object = AuxiliaryFunctionsImpl.environment(value, worldInstance, getTypeOfProperty(primaryEntity, propertyName, worldInstance, seocndEntity)).toString();
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
                        object = AuxiliaryFunctionsImpl.evaluate(value, worldInstance, getTypeOfProperty(primaryEntity, propertyName, worldInstance, seocndEntity), primaryEntity, seocndEntity).toString();
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
                Type propertyType = getTypeOfProperty(primaryEntity, propertyName, worldInstance, seocndEntity);
                Type valueType = getTypeOfProperty(primaryEntity, expressionName, worldInstance, seocndEntity);
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

    private Type getTypeOfProperty(EntityInstance primaryEntity, String property,  WorldInstance worldInstance, EntityInstance seocndEntity) throws ObjectNotExist, OperationNotCompatibleTypes, FormatException {
        for(Property propertyInstance: primaryEntity.getAllProperty().values()){
            if(propertyInstance.getName().equals(property)){
                return propertyInstance.getType();
            }
        }

        setPropertyName(null);
        String name = expressionName;
        setExpressionName(property);
        String value = decipher(primaryEntity, worldInstance, seocndEntity);
        setExpressionName(name);
        try {
            Float floatValue = Float.parseFloat(value);
            return Type.FLOAT;
        } catch (NumberFormatException ignore){

        }if(value.equals("true") || value.equals("false")){
            return Type.BOOLEAN;
        }else{
            return Type.STRING;
        }
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

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setExpressionName(String expressionName) {
        this.expressionName = expressionName;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
