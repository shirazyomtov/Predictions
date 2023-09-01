package xml;

import exceptions.NameAlreadyExist;
import exceptions.ObjectNotExist;
import jaxb.schema.generated.*;
import sun.nio.ch.SelectorImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static world.enums.AuxiliaryFunction.checkOptionByFunctionName;
import static world.enums.AuxiliaryFunction.extractFunctionName;

public final class XMLValidation {
    private  PRDWorld world = null;

    public XMLValidation(PRDWorld world) {
        this.world = world;
    }

    public void checkValidationXmlFile() throws NameAlreadyExist, ObjectNotExist, NumberFormatException {
        checkEnvironmentVariables();
        checkPropertyVariablesOfSpecificEntity();
        checkRuleActions(true);
        checkRuleActions(false);
        checkArgumentThatGivenToCalculationIncreaseDecrease();
        CheckingTheSizeOfRowsAndColumns();
    }

    private void CheckingTheSizeOfRowsAndColumns() throws IndexOutOfBoundsException{
        int column = world.getPRDGrid().getColumns();
        int rows = world.getPRDGrid().getRows();
        if( column > 100 || column < 10 || rows > 100 || rows < 10){
            throw new IndexOutOfBoundsException("The size of the rows and columns should be between 10 and 100 (inclusive).");
        }
    }

    private void checkEnvironmentVariables() throws NameAlreadyExist {
        HashMap<String, Integer> map = new HashMap<>();
        for (PRDEnvProperty prdEnvironment : world.getPRDEnvironment().getPRDEnvProperty()) {
            if (map.containsKey(prdEnvironment.getPRDName())) {
                throw new NameAlreadyExist((prdEnvironment.getPRDName()), "environment");
            } else {
                map.put(prdEnvironment.getPRDName(), 1);
            }
        }
    }

    private void checkPropertyVariablesOfSpecificEntity() throws NameAlreadyExist {
        Map<String, Integer> map = new HashMap<>();
        for(PRDEntity prdEntity:world.getPRDEntities().getPRDEntity()) {
            for (PRDProperty prdProperty : prdEntity.getPRDProperties().getPRDProperty()) {
                if (map.containsKey(prdProperty.getPRDName())) {
                    throw new NameAlreadyExist((prdProperty.getPRDName()), "property");
                } else {
                    map.put(prdProperty.getPRDName(), 1);
                }
            }
            map.clear();
        }
    }

    private void checkRuleActions(boolean isEntityCheck) throws ObjectNotExist {
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

    private void checkActionEntity(PRDAction action) throws  ObjectNotExist{
        if (action.getType().equals("condition")) {
            if (action.getPRDCondition().getSingularity().equals("single")) {
                findEntity(action.getPRDCondition().getEntity());
            }
            else{
                findEntity(action.getEntity());
                List<PRDCondition> conditions = action.getPRDCondition().getPRDCondition();
                checkCondition("", conditions, true);
            }
            checkConditionPropertyThenAndElse(action, true);

        }
        else if (action.getType().equals("replace")) {
            findEntity(action.getCreate());
            findEntity(action.getKill());
        }
        else if (action.getType().equals("proximity")) {
            findEntity(action.getPRDBetween().getSourceEntity());
            findEntity(action.getPRDBetween().getTargetEntity());
            for(PRDAction actionProximity: action.getPRDActions().getPRDAction()){
                checkActionEntity(actionProximity);
            }
        }
        else {
            findEntity(action.getEntity());
        }
    }




    private  void checkActionProperty(PRDAction action)throws ObjectNotExist
    {
        String entityAction = action.getEntity();
        if (action.getType().equals("condition")){
            if (action.getPRDCondition().getSingularity().equals("single")) {
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
        else if (action.getType().equals("kill") || action.getType().equals("replace") ) {
        }
        else if (action.getType().equals("proximity")) {
            for(PRDAction actionProximity: action.getPRDActions().getPRDAction()){
                checkActionProperty(actionProximity);
            }

        }
        else{
            findProperty(action.getProperty(), entityAction);
        }
    }

    private void checkConditionPropertyThenAndElse(PRDAction action, boolean isEntityCheck) throws ObjectNotExist {
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

    private void checkCondition(String entityAction, List<PRDCondition> conditions, boolean isEntityCheck) throws ObjectNotExist {
        for (PRDCondition condition : conditions) {
            if (condition.getSingularity().equals("multiple")) {
                checkCondition(entityAction, condition.getPRDCondition(), isEntityCheck);
            }
            else {
                if (isEntityCheck){
                    findEntity(condition.getEntity());
                }
            }
        }
    }

    private void findProperty(String propertyName, String entityAction) throws ObjectNotExist {
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

    private void findEntity(String entityName) throws ObjectNotExist {
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

    private void checkArgumentThatGivenToCalculationIncreaseDecrease() throws NumberFormatException
    {
        for (PRDRule rule : world.getPRDRules().getPRDRule()) {
            for (PRDAction action : rule.getPRDActions().getPRDAction()) {
                checkArgument(action);
            }
        }
    }

    private void checkArgument(PRDAction action)
    {
        String entityName = action.getEntity();
        if(action.getType().equals("decrease") || action.getType().equals("increase")){
            String by = action.getBy();
            checkTypeOfArg(by, entityName, action.getType());
            if (!checkValueOfProperty(action.getProperty(), entityName)){
                throw new NumberFormatException("You provide a property that is not a number, the property is " + action.getProperty() + " in the action " + action.getType());
            }
        }
        else if(action.getType().equals("calculation")){
            checkTypeOfArgCalculation(action, entityName);
            if (!checkValueOfProperty(action.getResultProp(), entityName)){
                throw new NumberFormatException("You provide a property that is not a number, the property is " + action.getResultProp()+ " in the action " + action.getType());
            }
        }
        else if(action.getType().equals("condition")){
            checkTypeOfArgCondition(action);
        }
        else if (action.getType().equals("proximity")) {
            checkTypeOfArgProximity(action);
        }
    }

    private void checkTypeOfArgProximity(PRDAction action) {
        for(PRDAction actionSub: action.getPRDActions().getPRDAction()){
            checkArgument(actionSub);
        }
    }

    private void checkTypeOfArg(String arg, String entityName, String actionType){
        if(checkOptionByFunctionName(arg)){
            String value = extractValueInParentheses(arg);
            if (extractFunctionName(arg).equals("random") && !isNumber(value))
            {
                throw new NumberFormatException("You provide a value that is not a Integer number, the value is " + value + " in the randomFunction " );
            }
            else if(extractFunctionName(arg).equals("environment") && !checkTypeOfEnvironmentProperty(value) ){
                throw new NumberFormatException("You provide a value that is not a number, the value is " + value + " in the action " + actionType);
            }
        }
        else{
            if(!isNumber(arg) && !checkValueOfProperty(arg, entityName)){
                throw new NumberFormatException("You provide a value that is not a number the value is " + arg + " in the action " + actionType);
            }
        }
    }

    private boolean checkTypeOfEnvironmentProperty(String value) {
        boolean flag = false;
        for (PRDEnvProperty envProperty : world.getPRDEnvironment().getPRDEnvProperty()) {
            if (envProperty.getPRDName().equals(value)) {
                if (envProperty.getType().equals("decimal") || envProperty.getType().equals("float")) {
                    flag = true;
                }
            }
        }

        return flag;
    }


    private void checkTypeOfArgCondition(PRDAction action) throws NumberFormatException{
        for(PRDAction actionSubThen: action.getPRDThen().getPRDAction()){
            checkArgument(actionSubThen);
        }
        if(action.getPRDElse() != null){
            for(PRDAction actionSubElse: action.getPRDElse().getPRDAction()){
               checkArgument(actionSubElse);
            }
        }
    }

    private void checkTypeOfArgCalculation(PRDAction action, String entityName) throws NumberFormatException{
        if(action.getPRDDivide() != null){
            checkTypeOfArg(action.getPRDDivide().getArg1(), entityName, action.getType());
            checkTypeOfArg(action.getPRDDivide().getArg2(), entityName, action.getType());
        }
        else{
            checkTypeOfArg(action.getPRDMultiply().getArg1(), entityName, action.getType());
            checkTypeOfArg(action.getPRDMultiply().getArg2(), entityName, action.getType());
        }
    }


    private boolean isNumber(String number){
        try {
            Float.parseFloat(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String extractValueInParentheses(String input) {
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }


    private boolean checkValueOfProperty(String value, String entityName) {
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
