package xml;

import exceptions.NameAlreadyExist;
import exceptions.ObjectNotExist;
import jaxb.schema.generated.*;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static world.enums.AuxiliaryFunction.checkOptionByFunctionName;

public class XMLValidation {
    private static PRDWorld world = null;

    public static void setWorld(PRDWorld world) {
        XMLValidation.world = world;
    }

    public static void checkValidationXmlFile() throws NameAlreadyExist, ObjectNotExist, NumberFormatException {
        checkEnvironmentVariables();
        checkPropertyVariablesOfSpecificEntity(world.getPRDEntities().getPRDEntity().get(0));
        checkActionsEntitiesNames();
        checkRuleProperty();
        checkArgumentThatGivenToCalculationIncreaseDecrease();
    }

    private static void checkEnvironmentVariables() throws NameAlreadyExist {
        HashMap<String, Integer> map = new HashMap<>();
        for (PRDEnvProperty prdEnvironment : world.getPRDEvironment().getPRDEnvProperty()) {
            if (map.containsKey(prdEnvironment.getPRDName())) {
                throw new NameAlreadyExist((prdEnvironment.getPRDName()), "environment");
            } else {
                map.put(prdEnvironment.getPRDName(), 1);
            }
        }
    }

    private static void checkPropertyVariablesOfSpecificEntity(PRDEntity prdEntity) throws NameAlreadyExist {
        HashMap<String, Integer> map = new HashMap<>();
        for (PRDProperty prdProperty : prdEntity.getPRDProperties().getPRDProperty()) {
            if (map.containsKey(prdProperty.getPRDName())) {
                throw new NameAlreadyExist((prdProperty.getPRDName()), "property");
            } else {
                map.put(prdProperty.getPRDName(), 1);
            }
        }
    }

    private static void checkActionsEntitiesNames() throws ObjectNotExist {
        for (PRDRule rule : world.getPRDRules().getPRDRule()) {
            for (PRDAction action : rule.getPRDActions().getPRDAction()) {
                if (!action.getEntity().equals(world.getPRDEntities().getPRDEntity().get(0).getName())) {
                    throw new ObjectNotExist(action.getEntity(), "Entity");
                }
            }
        }
    }

    private static void checkRuleProperty() throws ObjectNotExist {
        for(PRDRule rule: world.getPRDRules().getPRDRule()){
            for(PRDAction action: rule.getPRDActions().getPRDAction()) {
                String entityAction = action.getEntity();
                if (action.getType().equals("condition")){
                    List<PRDCondition> conditions = action.getPRDCondition().getPRDCondition();
                    if (action.getPRDCondition().getSingularity().equals("single")) {
                        findProperty(action.getPRDCondition().getProperty(), entityAction);
                    }
                    else{
                        checkCondition(entityAction, conditions);
                    }
                }
                else if(action.getType().equals("calculation")){
                    findProperty(action.getResultProp(), entityAction);
                }
                else{
                    findProperty(action.getProperty(), entityAction);
                }
            }
        }
    }

    private static void checkCondition(String entityAction, List<PRDCondition> conditions) throws ObjectNotExist {
        if (conditions.equals("single")) {
            findProperty(conditions.get(0).getProperty(), entityAction);
        }
        else{
            for(PRDCondition condition: conditions){
                if (condition.getSingularity().equals("multiple")) {
                    checkCondition(entityAction, condition.getPRDCondition());
                }
                else{
                    findProperty(conditions.get(0).getProperty(), entityAction);

                }
            }
        }
    }

    private static void findProperty(String propertyName, String entityAction) throws ObjectNotExist {
        boolean flag = false;
        for(PRDEntity entity: world.getPRDEntities().getPRDEntity()){
            if(entityAction.equals(entity.getName())){
                for(PRDProperty property: entity.getPRDProperties().getPRDProperty()){
                    if(property.getPRDName().equals(propertyName)){
                        flag = true;
                        break;
                    }
                }
            }
        }
        if(!flag){
            throw new ObjectNotExist(propertyName, "property");
        }
    }

    private static void checkArgumentThatGivenToCalculationIncreaseDecrease() throws NumberFormatException
    {
        for (PRDRule rule : world.getPRDRules().getPRDRule()) {
            for (PRDAction action : rule.getPRDActions().getPRDAction()) {
                String entityName = action.getEntity();
                if(action.getType().equals("decrease") || action.getType().equals("increase")){
                    String by = action.getBy();
                    checkTypeOfArg(by, entityName);
                    checkTypeOfArg(action.getProperty(), entityName);
                }
                else if(action.getType().equals("calculation")){
                    checkTypeOfArgCalculation(action, entityName);
                }
                else if(action.getType().equals("condition")){
                    checkTypeOfArgCondition(action, entityName);
                }
            }
        }
    }

    private static void checkTypeOfArgCondition(PRDAction action, String entityName) throws NumberFormatException{
        for(PRDAction actionSubThen: action.getPRDThen().getPRDAction()){
            if(actionSubThen.getType().equals("decrease") || actionSubThen.getType().equals("increase")){
                String bySub = actionSubThen.getBy();
                checkTypeOfArg(bySub, entityName);
                checkTypeOfArg(actionSubThen.getProperty(), entityName);
            }
        }
        if(action.getPRDElse() != null){
            for(PRDAction actionSubElse: action.getPRDElse().getPRDAction()){
                if(actionSubElse.getType().equals("decrease") || actionSubElse.getType().equals("increase")){
                    String bySub = actionSubElse.getBy();
                    checkTypeOfArg(bySub, entityName);
                    checkTypeOfArg(actionSubElse.getProperty(), entityName);
                }
            }
        }
    }

    private static void checkTypeOfArgCalculation(PRDAction action, String entityName) throws NumberFormatException{
        if(!checkTypeOfEntityProperty(action.getResultProp(), action.getEntity())){
            throw new NumberFormatException("Not a number: " + action.getBy());
        }
        if(action.getPRDDivide() != null){
            checkTypeOfArg(action.getPRDDivide().getArg1(), entityName);
            checkTypeOfArg(action.getPRDDivide().getArg2(), entityName);
        }
        else{
            checkTypeOfArg(action.getPRDMultiply().getArg1(), entityName);
            checkTypeOfArg(action.getPRDMultiply().getArg2(), entityName);
        }
    }

    private static void checkTypeOfArg(String arg, String entityName){
        if(checkOptionByFunctionName(arg)){
            String value = extractValueInParentheses(arg);
            if(!checkTypeOfEnvironmentProperty(value)){
                throw new NumberFormatException("Not a number: " + value);
            }
        }
        else{
            if(!checkTypeOfEnvironmentProperty(arg) && !checkTypeOfEntityProperty(arg, entityName)){
                throw new NumberFormatException("Not a number: " + arg);
            }
        }
    }

    private static String extractValueInParentheses(String input) {
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    private static boolean checkTypeOfEnvironmentProperty(String value) {
        boolean flag = false;
        if(isFloat(value)){
            flag = true;
        }
        else {
            for (PRDEnvProperty envProperty : world.getPRDEvironment().getPRDEnvProperty()) {
                if (envProperty.getPRDName().equals(value)) {
                    if (envProperty.getType().equals("decimal") || envProperty.getType().equals("float")) {
                        flag = true;
                    }
                }
            }
        }

        return flag;
    }

    private static boolean checkTypeOfEntityProperty(String value, String entityName) {
        boolean flag = false;
        for (PRDEntity entity : world.getPRDEntities().getPRDEntity()) {
            if (entity.getName().equals(entityName)) {
                for(PRDProperty property: entity.getPRDProperties().getPRDProperty()) {
                    if(property.getPRDName().equals(value)) {
                        if (property.getType().equals("decimal") || property.getType().equals("float")) {
                            flag = true;
                        }
                    }
                }
            }
        }

        return flag;
    }

    private static boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}