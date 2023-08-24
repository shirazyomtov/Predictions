import DTO.*;
import DTO.DTOEntityInfo;
import engineManager.EngineManager;
import enums.DisplaySimulationOption;
import enums.MenuOptions;
import exceptions.ObjectNotExist;
import exceptions.OperationNotCompatibleTypes;
import exceptions.OperationNotSupportedType;

import java.io.*;
import java.util.*;


public class UIManager {

    private EngineManager engineManager = new EngineManager();
    private String filePath = null;

    public void RunProgram() {
        int option = 0;
        while (option != MenuOptions.EXIT.getNumberOfOption()) {
            option = chooseOption();
            runOptionSelectedByUser(MenuOptions.getOptionByNumber(option));
        }
    }

    private int chooseInput(int maxOptions, String promptMessage) {
        boolean validInput = false;
        int userInput = 0;
        Scanner scanner = new Scanner(System.in);
        do {
            try {
                System.out.println(promptMessage);
                userInput = Integer.parseInt(scanner.nextLine());
                checkValidationInput(userInput, maxOptions);
                validInput = true;
            } catch (NumberFormatException | IndexOutOfBoundsException exception) {
                System.out.println("Invalid input. Please insert a number between 1 and " + maxOptions + ".");
            }
        } while (!validInput);

        return userInput;
    }

    private void checkValidationInput(int userIntegerInput, int size) throws IndexOutOfBoundsException {
        if(userIntegerInput < 1 || userIntegerInput > size){
            throw new IndexOutOfBoundsException();
        }
    }

    private int chooseOption() {
        Menu menu = new Menu();
        int userIntegerInput = chooseInput(MenuOptions.getCountOfOptions(), menu.showMenu());
        MenuOptions.getOptionByNumber(userIntegerInput);
        return userIntegerInput;
    }

    private void runOptionSelectedByUser(MenuOptions option) {
        switch (option) {
            case LOAD_XML:
                loadXML();
                break;
            case SIMULATION_DETAILS:
                simulationDetails();
                break;
            case SIMULATION:
                simulation();
                break;
            case PAST_ACTIVATION:
                detailsOfPastRun();
                break;
            case LOAD_SIMULATIONS_FROM_FILE:
                loadSimulationsFromFile();
                break;
            case SAVE_SIMULATIONS_TO_FILE:
                saveSimulationsToFile();
                break;
        }
    }

    private void loadSimulationsFromFile() {
        System.out.println("Please choose the full path including the name of the file (without the extension) that he wanted to load the system from");
        Scanner scan = new Scanner(System.in);
        String filePath = scan.nextLine();
        try{
            engineManager.loadFileAndSetHistory(filePath, engineManager);
            System.out.println("The file was loaded successfully.");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void saveSimulationsToFile() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Please choose the full path including the name of the file (without the extension) that he wanted to save the system to");
            filePath = scanner.nextLine();
            engineManager.saveFile(filePath, engineManager);
            System.out.println("The file was saved successfully.");
        }
        catch (IOException e) {
            System.out.println("IOException");
        }
    }


    private void loadXML() {
        System.out.println("Enter the full path of the XML file");
        Scanner scanner = new Scanner(System.in);
        String xmlPath = scanner.nextLine();
        try{
            engineManager.loadXMLAAndCheckValidation(xmlPath);
            System.out.println("The XML file has been loaded successfully");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void simulationDetails() {
        try{
            engineManager.checkIfXmlLoaded();
            System.out.println("The information about the simulation defined in the xml file are:");
            printEntitiesDetails();
            printRulesDetails();
            printTerminationDetails();
        }
        catch (NullPointerException e){
            System.out.println("You cannot see the simulation details before you have loaded the xml file");
        }
    }

    private void printEntitiesDetails() {
        System.out.println("1.Entities:");
        List<DTOEntityInfo> entitiesDetails = engineManager.getEntitiesDetails();
        for(DTOEntityInfo entityDetail: entitiesDetails){
            System.out.println(getEntityDetails(entityDetail));
        }
    }

    private String getEntityDetails(DTOEntityInfo dtoEntityInfo) {
        StringBuilder entityDetails = new StringBuilder();
        entityDetails.append("    Entity ").append(dtoEntityInfo.getEntityName()).append(" details: ").append("\n");
        entityDetails.append("        Name = '").append(dtoEntityInfo.getEntityName()).append("',").append("\n");
        entityDetails.append("        Amount of population = ").append(dtoEntityInfo.getInitialAmount()).append(",").append("\n");
        entityDetails.append("        All properties: ").append("\n");
        for (DTOPropertyInfo property : dtoEntityInfo.getProperties()) {
            entityDetails.append("    ").append(getPropertyDetails(property)).append("\n");
        }
        return entityDetails.toString();
    }

    private String getPropertyDetails(DTOPropertyInfo dtoPropertyInfo){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("        Property ").append(dtoPropertyInfo.getName()).append(" details: ").append("\n");
        stringBuilder.append("                Name = '").append(dtoPropertyInfo.getName()).append("',").append("\n");
        stringBuilder.append("                Type = ").append(dtoPropertyInfo.getType()).append(",").append("\n");
        stringBuilder.append("                Is random initialize = ").append(dtoPropertyInfo.getIsRandom());

        stringBuilder.append(getRangeDetails(dtoPropertyInfo.getRange(), "                Range="));
        return stringBuilder.toString();
    }

    private String getRangeDetails(DTORangeInfo dtoRangeInfo, String rangeMessage){
        StringBuilder stringBuilder = new StringBuilder();
        if (dtoRangeInfo != null) {
            stringBuilder.append(",").append("\n").append(rangeMessage)
                    .append("from=").append(dtoRangeInfo.getFrom())
                    .append(", to=").append(dtoRangeInfo.getTo());
        }
        return stringBuilder.toString();
    }

    private void printRulesDetails() {
        System.out.println("2.Rules:");
        for (DTORuleInfo rule :  engineManager.getRulesDetails()) {
            System.out.println(getRuleDetails(rule));
        }
    }

    private String getRuleDetails(DTORuleInfo rule){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("    Rule ").append(rule.getRuleName()).append(" details: ").append("\n");
        stringBuilder.append("        Rule Name: ").append(rule.getRuleName()).append(",").append("\n");
        stringBuilder.append("        Activation: ").append(getActivation(rule.getActivation())).append(",").append("\n");
        stringBuilder.append("        Amount of Actions: ").append(rule.getAmountOfActions()).append(",").append("\n");
        stringBuilder.append("        All Actions:\n");
        if (rule.getAllAction() != null) {
            for (DTOActionInfo action : rule.getAllAction()) {
                stringBuilder.append("        ");
                stringBuilder.append(getActionDetails(action)).append("\n");
            }
        } else {
            stringBuilder.append("    No actions defined.\n");
        }
        return stringBuilder.toString();
    }

    private String getActivation(DTOActivationInfo dtoActivation){
        return "{ticks=" + dtoActivation.getTicks() + ", probability=" + dtoActivation.getProbability() + '}';
    }

    private String getActionDetails(DTOActionInfo action){
        return "    Action: " + "actionType = " + action.getActionName();
    }

    private void printTerminationDetails() {
        System.out.println("3.Termination:");
        System.out.println(getDTOTerminationInfo());
    }

    private String getDTOTerminationInfo(){
        DTOTerminationInfo dtoTerminationInfo = engineManager.getTerminationDetails();
        StringBuilder stringBuilder = new StringBuilder("    Termination: ");
        if (dtoTerminationInfo.getTicks() != null) {
            stringBuilder.append("ticks = ").append(dtoTerminationInfo.getTicks());
        }

        if (dtoTerminationInfo.getTicks() != null && dtoTerminationInfo.getSecond() != null) {
            stringBuilder.append(", ");
        }

        if (dtoTerminationInfo.getSecond() != null) {
            stringBuilder.append("seconds = ").append(dtoTerminationInfo.getSecond());
        }

        return stringBuilder.toString();
    }

    private void simulation() {
        try {
            int userIntegerInput = 0;
            engineManager.clearPastValues();
            while(userIntegerInput != engineManager.getAmountOfEnvironmentDefinition() + 1) {
                userIntegerInput = chooseEnvironmentProperty();
                if (userIntegerInput != engineManager.getAmountOfEnvironmentDefinition() + 1) {
                    setValueEnvironment(userIntegerInput);
                }
            }
            startSimulation();
            runSimulationAndPrintTerminationReason();
        }
        catch (NullPointerException e){
            System.out.println("You cannot run the simulation before loading the xml file");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            engineManager.removeSimulationFromHistory();
        }
    }


    private int chooseEnvironmentProperty() {
        int maxOptions =  engineManager.getAmountOfEnvironmentDefinition() + 1;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("These are all the environment variables defined in the xml file.\n");
        stringBuilder.append("You can provide a value for each of the environment variables.\n");
        stringBuilder.append("Please enter the number of the environment variable you want to provide a value for.\n");
        stringBuilder.append(printEnvironmentNames());
        return chooseInput(maxOptions, stringBuilder.toString());
    }

    private void setValueEnvironment(int userIntegerInput) {
        boolean validInput = false;

        do {
            try {
                System.out.println("Please enter a value that stand in the required details of the this environment");
                DTOEnvironmentInfo dtoEnvironmentInfo = engineManager.getEnvironmentDetails(userIntegerInput);
                printEnvironmentDetails(dtoEnvironmentInfo);
                checkValidationValue(dtoEnvironmentInfo, userIntegerInput);
                validInput = true;
            }
            catch (NumberFormatException exception)
            {
                System.out.println("You did not enter a input for a same type like the environment variable");
            }
            catch (IndexOutOfBoundsException exception)
            {
                System.out.println("The number you entered is not in the existing range");
            }
        }while(!validInput);
    }

    private void printEnvironmentDetails(DTOEnvironmentInfo dtoEnvironmentInfo){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Environment ").append(dtoEnvironmentInfo.getName()).append(" details: ").append("\n");
        stringBuilder.append("    name: '").append(dtoEnvironmentInfo.getName()).append("\n");
        stringBuilder.append("    type: ").append(dtoEnvironmentInfo.getType());
        stringBuilder.append(getRangeDetails(dtoEnvironmentInfo.getRange(),"    Range="));
        System.out.println(stringBuilder);
    }

    private void checkValidationValue(DTOEnvironmentInfo dtoEnvironmentInfo, int userIntegerInput) throws NumberFormatException, IndexOutOfBoundsException{
        Scanner scanner = new Scanner(System.in);
        String valueInput;
        switch (dtoEnvironmentInfo.getType()) {
            case "FLOAT":
                valueInput = scanner.nextLine();
                engineManager.checkValidationFloatEnvironment(valueInput, userIntegerInput);
                break;
            case "DECIMAL":
                valueInput = scanner.nextLine();
                engineManager.checkValidationDecimalEnvironment(valueInput, userIntegerInput);
                break;
            case "BOOLEAN":
                Integer userInput = chooseBooleanValue();
                engineManager.checkValidationBoolEnvironment(userInput, userIntegerInput);
                break;
            case "STRING":
                valueInput = scanner.nextLine();
                engineManager.checkValidationStringEnvironment(valueInput, userIntegerInput);
                break;
        }
    }

    private int chooseBooleanValue() {
        return chooseInput(2, "Select an option:\n1. True\n2. False");
    }

    private  String  printEnvironmentNames(){
        int index = 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (DTOEnvironmentInfo environment: engineManager.getEnvironmentNamesList()) {
            stringBuilder.append(index);
            stringBuilder.append(".");
            stringBuilder.append(environment.getName()).append("\n");
            index += 1;
        }
        stringBuilder.append(index);
        stringBuilder.append(".");
        stringBuilder.append("Start the simulation");

        return stringBuilder.toString();
    }

    private void startSimulation() {
        engineManager.setSimulation();
        printEnvironmentNamesAndValues();
    }


    private void printEnvironmentNamesAndValues() {
        int index = 1;
        System.out.println("All the environment properties name and value: ");
        for (DTOEnvironmentInfo environment : engineManager.getEnvironmentNamesAndValues()) {
            System.out.print(index);
            System.out.print(".");
            System.out.println("Name: " +  environment.getName() + ", value: " + environment.getValue());
            index += 1;
        }
    }

    private void runSimulationAndPrintTerminationReason() throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException, OperationNotSupportedType, OperationNotCompatibleTypes {
        String message = engineManager.getRunSimulation();
        printIdAndTerminationReason(message);
    }

    private void printIdAndTerminationReason(String message) {
        System.out.println("The simulation has ended");
        System.out.print("Simulation id: ");
        System.out.println(engineManager.getNumberOfTimesUserSelectSimulation());
        System.out.println(message);
    }

    private void detailsOfPastRun() {
        int userIntegerInput = 0;
        int userDisplayModeInput ;
        try{
            engineManager.checkIfThereSimulation();
            userIntegerInput = chooseOfPastSimulation();
            userDisplayModeInput = chooseTheDisplayMode();
            showDetailsOfSpecificPastRun(DisplaySimulationOption.getOptionByNumber(userDisplayModeInput), userIntegerInput);
        }
        catch (NullPointerException exception){
            System.out.println(exception.getMessage());
        }
    }

    private int chooseOfPastSimulation() {
        int index = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The list of all the runs performed by the system are:\n");
        stringBuilder.append("Please select the relevant run you want to see the results of\n");
        stringBuilder.append(getAllSimulationAsString());
        return chooseInput(engineManager.getSortedMapSize(), stringBuilder.toString());
    }

    private String getAllSimulationAsString(){
        List<DTOSimulationInfo> dtoSimulations = engineManager.getAllPastSimulation();
        int index = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for(DTOSimulationInfo dtoSimulation : dtoSimulations){
            index++;
            stringBuilder.append(index + ". Unique identifier of the simulation : " + dtoSimulation.getSimulationId() + ", Running date: " + dtoSimulation.getSimulationDate() + "\n");
        }
        return stringBuilder.toString();
    }

    private int chooseTheDisplayMode() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Please select the display mode\n");
        for (DisplaySimulationOption displaySimulationOption : DisplaySimulationOption.values()) {
            stringBuilder.append(displaySimulationOption.getOptionNumber() + ". " + displaySimulationOption + "\n");
        }
        return chooseInput(2, stringBuilder.toString());
    }

    private void showDetailsOfSpecificPastRun(DisplaySimulationOption userDisplayModeInput, int userIntegerInput) {
        switch (userDisplayModeInput) {
            case DISPLAYBYQUANTITY:
                displayByQuantity(userIntegerInput);
                break;
            case DISPLAYBYHISTOGRMOFPROPERTY:
                displayByHistogramOfProperty(userIntegerInput);
                break;
        }
    }

    private void displayByQuantity(int userIntegerInput) {
        System.out.println("The initial and final quantity of each entity: ");
        printInitAndFinalEntities(engineManager.getQuantityOfEachEntity(userIntegerInput));
    }

    private void printInitAndFinalEntities(DTOEntityInfo dtoEntityInfo){
        System.out.println("Entity " + dtoEntityInfo.getEntityName());
        System.out.println("The initial quantity of this entity : " + dtoEntityInfo.getInitialAmount());
        System.out.println("The final quantity of this entity : " + dtoEntityInfo.getFinalAmount());
    }

    private void displayByHistogramOfProperty(int userIntegerInput) {
        String chosenEntityName;
        String chosenPropertyName;
        Map<Object, Integer> valuesProperty;
        try {
            engineManager.checkIfThereIsEntity(userIntegerInput);
            chosenEntityName = chooseEntity(userIntegerInput);
            chosenPropertyName = chooseProperty(chosenEntityName, userIntegerInput);
            valuesProperty = engineManager.createPropertyValuesMap(userIntegerInput, chosenEntityName, chosenPropertyName);
            printPropertiesValues(valuesProperty, chosenPropertyName);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private String chooseEntity(int userIntegerInput) {
        Map<Integer, String> entities = new HashMap<>();
        int index = engineManager.getEntitiesDefinitionSize(userIntegerInput);
        String message = getEntitiesMessage(userIntegerInput, entities);
        int userEntityInput = chooseInput(index, message);
        return entities.get(userEntityInput);
    }

    private String getEntitiesMessage(int userIntegerInput, Map<Integer, String> entities)
    {
        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        stringBuilder.append("Please select one of the given entities\n");
        for(DTOEntityInfo dtoEntity: engineManager.getEntitiesList(userIntegerInput)){
            index += 1;
            stringBuilder.append(index + ".");
            stringBuilder.append(dtoEntity.getEntityName() + "\n");
            entities.put(index, dtoEntity.getEntityName());
        }
        return stringBuilder.toString();
    }

    private String chooseProperty(String userEntityName, int userIntegerInput) {
        Map<Integer, String> properties = new HashMap<>();
        int index = engineManager.getPropertiesSize(userEntityName, userIntegerInput);
        String message = printProperties(userEntityName, properties, userIntegerInput);
        int userPropertyInput = chooseInput(index, message);
        return properties.get(userPropertyInput);
    }

    private String printProperties(String userEntityName, Map<Integer, String> properties, int userIntegerInput) {
        int index = 0;
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Please select one of the given properties\n");
        for(DTOPropertyInfo property: engineManager.getPropertiesListFromSimulation(userEntityName, userIntegerInput)){
            index += 1;
            stringBuilder.append(index + ".");
            stringBuilder.append(property.getName() + "\n");
            properties.put(index, property.getName());
        }
        return  stringBuilder.toString();
    }

    private void printPropertiesValues(Map<Object, Integer> valuesProperty, String propertyName) {
        System.out.println("The histogram of the property " + propertyName);
        for(Object object: valuesProperty.keySet()){
            System.out.println("There are " + valuesProperty.get(object)+ " instances of the property " + propertyName + " that is value is " + object);
        }
    }
}