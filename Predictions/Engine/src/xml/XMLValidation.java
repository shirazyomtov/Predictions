package xml;

import exceptions.NameAlreadyExist;
import exceptions.ObjectNotExist;
import jaxb.schema.generated.*;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static world.enums.AuxiliaryFunction.checkOptionByFunctionName;

public final class XMLValidation {
    private static PRDWorld world = null;

    public static void setWorld(PRDWorld world) {
        XMLValidation.world = world;
    }

    public static void checkValidationXmlFile() throws NameAlreadyExist, ObjectNotExist, NumberFormatException {
        checkEnvironmentVariables();
        checkPropertyVariablesOfSpecificEntity(world.getPRDEntities().getPRDEntity().get(0));
        checkRuleActions(true);
        checkRuleActions(false);
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
        // maybe do it generics
        HashMap<String, Integer> map = new HashMap<>();
        for (PRDProperty prdProperty : prdEntity.getPRDProperties().getPRDProperty()) {
            if (map.containsKey(prdProperty.getPRDName())) {
                throw new NameAlreadyExist((prdProperty.getPRDName()), "property");
            } else {
                map.put(prdProperty.getPRDName(), 1);
            }
        }
    }

    private static void checkRuleActions(boolean isEntityCheck) throws ObjectNotExist {
        for (PRDRule rule : world.getPRDRules().getPRDRule()) {
            for (PRDAction action : rule.getPRDActions().getPRDAction()) {
                if (isEntityCheck) {
                    checkActionEntity(action);
                } else {
                    checkActionProperty(action);
                }
            }
        }
    }

    private static void checkActionEntity(PRDAction action) throws  ObjectNotExist{
        if (action.getType().equals("condition")) {
            if (action.getPRDCondition().getSingularity().equals("single")) {
                findEntity(action.getPRDCondition().getEntity());
            }
            else{
                List<PRDCondition> conditions = action.getPRDCondition().getPRDCondition();
                checkCondition("", conditions, true);
            }
            checkConditionPropertyThenAndElse(action, true);

        }
        else {
            findEntity(action.getEntity());
        }
    }




    private  static void checkActionProperty(PRDAction action)throws ObjectNotExist
    {
        String entityAction = action.getEntity();
        if (action.getType().equals("condition")){
            if (action.getPRDCondition().getSingularity().equals("single")) {
                findProperty(action.getPRDCondition().getProperty(), entityAction);
            }
            else{
                List<PRDCondition> conditions = action.getPRDCondition().getPRDCondition();
                checkCondition(entityAction, conditions, false);
            }
            checkConditionPropertyThenAndElse(action, false);
        }
        else if(action.getType().equals("calculation")){
            findProperty(action.getResultProp(), entityAction);
        }
        else if (action.getType().equals("kill")) {
        }
        else{
            findProperty(action.getProperty(), entityAction);
        }
    }

    private static void checkConditionPropertyThenAndElse(PRDAction action, boolean isEntityCheck) throws ObjectNotExist {
        for(PRDAction actionThen: action.getPRDThen().getPRDAction()){
            if (isEntityCheck){
                checkActionEntity(actionThen);
            }
            else{
                checkActionProperty(actionThen);
            }
        }
        if(action.getPRDElse() != null){
            for(PRDAction actionElse: action.getPRDElse().getPRDAction()){
                if (isEntityCheck){
                    checkActionEntity(actionElse);
                }
                else{
                    checkActionProperty(actionElse);
                }
            }
        }
    }

    private static void checkCondition(String entityAction, List<PRDCondition> conditions, boolean isEntityCheck) throws ObjectNotExist {
        for (PRDCondition condition : conditions) {
            if (condition.getSingularity().equals("multiple")) {
                checkCondition(entityAction, condition.getPRDCondition(), isEntityCheck);
            }
            else {
                if (isEntityCheck){
                    findEntity(condition.getEntity());
                }
                else{
                    findProperty(condition.getProperty(), entityAction);
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

    private static void findEntity(String entityName) throws ObjectNotExist {
        boolean flag = false;
        for(PRDEntity entity: world.getPRDEntities().getPRDEntity()){
            if(entityName.equals(entity.getName())){
                flag = true;
                break;
            }
        }
        if(!flag){
            throw new ObjectNotExist(entityName, "Entity");
        }
    }

    private static void checkArgumentThatGivenToCalculationIncreaseDecrease() throws NumberFormatException
    {
        for (PRDRule rule : world.getPRDRules().getPRDRule()) {
            for (PRDAction action : rule.getPRDActions().getPRDAction()) {
                checkArgument(action);
            }
        }
    }

    private static void checkArgument(PRDAction action)
    {
        String entityName = action.getEntity();
        if(action.getType().equals("decrease") || action.getType().equals("increase")){
            String by = action.getBy();
            checkTypeOfArg(by, entityName);
            checkTypeOfArg(action.getProperty(), entityName);
        }
        else if(action.getType().equals("calculation")){
            checkTypeOfArg(action.getResultProp(), entityName);
            checkTypeOfArgCalculation(action, entityName);
        }
        else if(action.getType().equals("condition")){
            checkTypeOfArgCondition(action);
        }
    }
    private static void checkTypeOfArgCondition(PRDAction action) throws NumberFormatException{
        for(PRDAction actionSubThen: action.getPRDThen().getPRDAction()){
            checkArgument(actionSubThen);
        }
        if(action.getPRDElse() != null){
            for(PRDAction actionSubElse: action.getPRDElse().getPRDAction()){
               checkArgument(actionSubElse);
            }
        }
    }

    private static void checkTypeOfArgCalculation(PRDAction action, String entityName) throws NumberFormatException{
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
            if(!checkTypeOfEnvironmentProperty(value) && !isNumber(value)){
                throw new NumberFormatException("Not a number: " + value);
            }
        }
        else{
            if(!isNumber(arg) && !checkValueOfProperty(arg, entityName)){
                throw new NumberFormatException("Not a number: " + arg);
            }
        }
    }

    private  static boolean isNumber(String number){
        try {
            Float.parseFloat(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
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
            for (PRDEnvProperty envProperty : world.getPRDEvironment().getPRDEnvProperty()) {
                if (envProperty.getPRDName().equals(value)) {
                    if (envProperty.getType().equals("decimal") || envProperty.getType().equals("float")) {
                        flag = true;
                    }
                }
            }

        return flag;
    }

    private static boolean checkValueOfProperty(String value, String entityName) {
        boolean flag = false;
        for (PRDEntity entity : world.getPRDEntities().getPRDEntity()) {
            if (entity.getName().equals(entityName)) {
                for(PRDProperty property: entity.getPRDProperties().getPRDProperty()) {
                    if(property.getPRDName().equals(value)) {
                        if (property.getType().equals("decimal") || property.getType().equals("float")) {
                            flag = true;
                            break;
                        }
                    }
                }
            }
        }

        return flag;
    }
}
