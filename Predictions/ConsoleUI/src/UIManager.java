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
        List<String> entitiesDetails = engineManager.getEntitiesDetails();
        for(String entityDetail: entitiesDetails){
            System.out.println(entityDetail);
        }
    }

    private void printRulesDetails() {
        System.out.println("2.Rules:");
        for (String rule :  engineManager.getRulesDetails()) {
            System.out.println(rule);
        }
    }

    private void printTerminationDetails() {
        System.out.println("3.Termination:");
        System.out.println(engineManager.getTerminationDetails());
    }

    private void simulation() {
        try {
            int userIntegerInput = 0;
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
                System.out.println(engineManager.getEnvironmentDetails(userIntegerInput));
                checkValidationValue(userIntegerInput);
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

    private void checkValidationValue(int userIntegerInput) throws NumberFormatException, IndexOutOfBoundsException{
        Scanner scanner = new Scanner(System.in);
        String valueInput;
        switch (engineManager.getEnvironmentType(userIntegerInput)) {
            case FLOAT:
                valueInput = scanner.nextLine();
                engineManager.checkValidationFloatEnvironment(valueInput, userIntegerInput);
                break;
            case DECIMAL:
                valueInput = scanner.nextLine();
                engineManager.checkValidationDecimalEnvironment(valueInput, userIntegerInput);
                break;
            case BOOLEAN:
                Integer userInput = chooseBooleanValue();
                engineManager.checkValidationBoolEnvironment(userInput, userIntegerInput);
                break;
            case STRING:
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
        for (String environmentName : engineManager.getEnvironmentNamesList()) {
            stringBuilder.append(index);
            stringBuilder.append(".");
            stringBuilder.append(environmentName).append("\n");
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
        for (String environmentNameAndValue : engineManager.getEnvironmentNamesAndValues()) {
            System.out.print(index);
            System.out.print(".");
            System.out.println(environmentNameAndValue);
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
        stringBuilder.append(engineManager.getAllPastSimulation());
        return chooseInput(engineManager.getSortedMapSize(), stringBuilder.toString());
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
        for(String name: engineManager.getEntitiesList(userIntegerInput)){
            index += 1;
            stringBuilder.append(index + ".");
            stringBuilder.append(name + "\n");
            entities.put(index, name);
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
        for(String propertyName: engineManager.getPropertiesNamesList(userEntityName, userIntegerInput)){
            index += 1;
            stringBuilder.append(index + ".");
            stringBuilder.append(propertyName + "\n");
            properties.put(index, propertyName);
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