import enums.DisplaySimulationOption;
import enums.MenuOptions;
import exceptions.FilePathException;
import exceptions.ObjectNotExist;
import exceptions.OperationNotSupportedType;
import history.History;
import history.simulation.Simulation;
import world.entity.definition.EntityDefinition;
import world.entity.definition.PropertyDefinition;
import world.entity.instance.EntityInstance;
import world.enums.ActionType;
import world.propertyInstance.api.Property;
import world.rule.action.Action;
import world.worldDefinition.WorldDefinition;
import world.entity.definition.EntityDefinitionImpl;
import world.environment.definition.EnvironmentDefinition;
import world.environment.instance.EnvironmentInstance;
import world.propertyInstance.impl.BooleanPropertyInstance;
import world.propertyInstance.impl.FloatPropertyInstance;
import world.propertyInstance.impl.IntegerPropertyInstance;
import world.propertyInstance.impl.StringPropertyInstance;
import world.rule.RuleImpl;
import world.value.generator.api.ValueGeneratorFactory;
import world.worldInstance.WorldInstance;
import xml.XMLReader;
import xml.XMLValidation;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.*;


public class UIManager {

    private WorldDefinition world = null;

    private WorldInstance worldInstance = null;
    private History history = null;
    private XMLReader xmlReader = null;
    private XMLValidation xmlValidation = null;

    private Integer numberOfTimesUserSelectSimulation = 0;

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

        }
    }

    private void loadXML() {
        System.out.println("Enter the full path of the XML file");
        Scanner scanner = new Scanner(System.in);
        String xmlPath = scanner.nextLine();
        xmlReader = new XMLReader(xmlPath);
        try{
            checkValidationXMLPath(xmlPath);
            xmlValidation = xmlReader.openXmlAndGetData();
            xmlValidation.checkValidationXmlFile();
            world = xmlReader.defineWorld();
            System.out.println("The XML file has been loaded successfully");
        }
        catch (FileNotFoundException | JAXBException  e) {
            //check JAXBException
            System.out.println("File has not been found in this path " + xmlPath + ".");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void checkValidationXMLPath(String path) throws FilePathException {
        if (path.length() <= 4) {
            throw new FilePathException(FilePathException.ErrorType.FILE_NAME_CONTAINS_LESS_THAN_4_CHARACTERS);
        } else if (!path.endsWith(".xml")) {
            throw new FilePathException(FilePathException.ErrorType.NOT_ENDS_WITH_XML);
        }

    }

    private void simulationDetails() {
        try{
            Objects.requireNonNull(world, "No simulation data is loaded from the XML file.");
            System.out.println("The information about the simulation defined in the xml file are:");
            printEntitiesDetails();
            printRulesDetails();
            printTerminationDetails();
        }
        catch (NullPointerException e){
            System.out.println(e.getMessage());
            System.out.println("You cannot see the simulation details before you have loaded the xml file");
        }
    }

    private void printEntitiesDetails() {
        System.out.println("1.Entities:");
        Map<String, EntityDefinitionImpl> entityDefinitions = world.getEntityDefinition();
        for (Map.Entry<String, EntityDefinitionImpl> entry : entityDefinitions.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    private void printRulesDetails() {
        System.out.println("2.Rules:");
        for (RuleImpl rule : world.getRules()) {
            System.out.println(rule);
        }
    }

    private void printTerminationDetails() {
        System.out.println("3.Termination:");
        System.out.println(world.getTermination());
    }

    private void simulation() {
        Map <String, EnvironmentInstance> environmentValuesByUser = new HashMap<>();
        try {
            int userIntegerInput = 0;
            List<String> environmentName = createListEnvironmentNames();
            while(userIntegerInput != world.getEnvironmentDefinition().size() + 1) {
                userIntegerInput = chooseEnvironmentProperty();
                if (userIntegerInput != world.getEnvironmentDefinition().size() + 1) {
                    setValueEnvironment(userIntegerInput, environmentName, environmentValuesByUser);
                }
            }
            startSimulation(environmentValuesByUser);
            runSimulation();
        }
        catch (NullPointerException e){
            System.out.println("You cannot run the simulation before loading the xml file");
        }
    }

    private  List<String>  createListEnvironmentNames() throws NullPointerException {
        List<String> environmentName = new ArrayList<>();
        for (Map.Entry<String, EnvironmentDefinition> environmentEntry : world.getEnvironmentDefinition().entrySet()) {
            environmentName.add(environmentEntry.getValue().getName());
        }

        return environmentName;
    }

    private int chooseEnvironmentProperty() {
        int maxOptions = (world != null) ? world.getEnvironmentDefinition().size() + 1 : 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Select an environment variable: \n");
        stringBuilder.append("These are all the environment variables defined in the xml file.\n");
        stringBuilder.append("You can provide a value for each of the environment variables.\n");
        stringBuilder.append("Please enter the number of the environment variable you want to provide a value for.\n");
        stringBuilder.append(printEnvironmentNames());
        return chooseInput(maxOptions, stringBuilder.toString());
    }

    private void setValueEnvironment(int userIntegerInput, List<String> environmentNameList,  Map <String, EnvironmentInstance> environmentValuesByUser) {
        String environmentName = environmentNameList.get(userIntegerInput -1);
        EnvironmentDefinition environmentDefinition = world.getEnvironmentDefinition().get(environmentName);
        boolean validInput = false;

        do {
            try {
                System.out.println("Please enter a value that stand in the required details of the this environment");
                System.out.println(environmentDefinition);
                checkValidationValue(environmentDefinition, environmentValuesByUser);
                validInput = true;
                System.out.println("success");
            }
            catch (NumberFormatException exception)
            {
                System.out.println("You did not enter a number for a numeric environment variable");
            }
            catch (IndexOutOfBoundsException exception)
            {
                System.out.println("The number you entered is not in the existing range");
            }
        }while(!validInput);
    }

    private void checkValidationValue( EnvironmentDefinition environmentDefinition,  Map <String, EnvironmentInstance> environmentValuesByUser) throws NumberFormatException, IndexOutOfBoundsException{
        Scanner scanner = new Scanner(System.in);
        Object userInput;
        EnvironmentInstance environmentInstance = null;
        switch (environmentDefinition.getType()) {
            case FLOAT:
                userInput = Float.parseFloat(scanner.nextLine());
                checkIfInputInRange(userInput, environmentDefinition, false);
                environmentInstance = new EnvironmentInstance(new FloatPropertyInstance(environmentDefinition.getName(), ValueGeneratorFactory.createFixed((float)userInput)));
                break;
            case DECIMAL:
                userInput = Integer.parseInt(scanner.nextLine());
                checkIfInputInRange(userInput, environmentDefinition, true);
                environmentInstance= new EnvironmentInstance(new IntegerPropertyInstance(environmentDefinition.getName(), ValueGeneratorFactory.createFixed((int)userInput)));
                break;
            case BOOLEAN:
                userInput = chooseBooleanValue();
                if((int)userInput == 1) {
                    environmentInstance = new EnvironmentInstance(new BooleanPropertyInstance(environmentDefinition.getName(), ValueGeneratorFactory.createFixed(true)));
                }
                else{
                    environmentInstance = new EnvironmentInstance(new BooleanPropertyInstance(environmentDefinition.getName(), ValueGeneratorFactory.createFixed(false)));
                }
                break;
            case STRING:
                userInput = scanner.nextLine();
                environmentInstance = new EnvironmentInstance(new StringPropertyInstance(environmentDefinition.getName(), ValueGeneratorFactory.createFixed((String) userInput)));
                break;

        }
        if (environmentInstance != null) {
            environmentValuesByUser.put(environmentInstance.getProperty().getName(), environmentInstance); // problem
        }
    }

    private void checkIfInputInRange(Object userInput, EnvironmentDefinition environmentDefinition, boolean isDecimal) throws IndexOutOfBoundsException{
        if (environmentDefinition.getRange() != null){
            if(!isDecimal) {
                if ((float) userInput < environmentDefinition.getRange().getFrom() || (float) userInput > environmentDefinition.getRange().getTo()) {
                    throw new IndexOutOfBoundsException();
                }
            }
            else {
                if ((int) userInput < environmentDefinition.getRange().getFrom().intValue() || (int) userInput > environmentDefinition.getRange().getTo().intValue()) {
                    throw new IndexOutOfBoundsException();
                }
            }
        }
    }

    private int chooseBooleanValue() {
        return chooseInput(2, "Select an option:\n1. True\n2. False");
    }

    private  String  printEnvironmentNames() throws NullPointerException {
        int index = 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, EnvironmentDefinition> environmentEntry : world.getEnvironmentDefinition().entrySet()) {
            stringBuilder.append(index);
            stringBuilder.append(".");
            stringBuilder.append(environmentEntry.getValue().getName() + "\n");
            index += 1;
        }
        stringBuilder.append(index);
        stringBuilder.append(".");
        stringBuilder.append("Start the simulation");

        return stringBuilder.toString();
    }

    private void startSimulation( Map <String, EnvironmentInstance> environmentValuesByUser) {
        Map<String, EnvironmentInstance> environmentInstanceMap = environmentValuesByUser;

        for (String environmentName: world.getEnvironmentDefinition().keySet()){
            if(!environmentValuesByUser.containsKey(environmentName)){
                provideRandomValues(world.getEnvironmentDefinition().get(environmentName), environmentInstanceMap);
            }
        }

        numberOfTimesUserSelectSimulation++;
        worldInstance =new WorldInstance(environmentInstanceMap, initEntities(), world);
        Simulation simulation = new Simulation(worldInstance, LocalDateTime.now());
        history = History.getInstance();
        history.setCurrentSimulationNumber(numberOfTimesUserSelectSimulation);
        history.addSimulation(simulation);
        printEnvironmentNamesAndValues();
    }

    private void provideRandomValues(EnvironmentDefinition environmentDefinition,  Map<String, EnvironmentInstance> environmentInstanceMap) {
        EnvironmentInstance environmentInstance = null;

        switch (environmentDefinition.getType()) {
            case FLOAT:
                environmentInstance = createEnvironmentFloat(environmentDefinition);
                break;
            case DECIMAL:
                environmentInstance = createEnvironmentDecimal(environmentDefinition);

                break;
            case BOOLEAN:
                environmentInstance = new EnvironmentInstance(new BooleanPropertyInstance(environmentDefinition.getName(), ValueGeneratorFactory.createRandomBoolean()));
                break;
            case STRING:
                environmentInstance = new EnvironmentInstance(new StringPropertyInstance(environmentDefinition.getName(), ValueGeneratorFactory.createRandomString()));
                break;

        }
        if (environmentInstance != null) {
            environmentInstanceMap.put(environmentInstance.getProperty().getName(), environmentInstance); // problem
        }
    }

    private EnvironmentInstance createEnvironmentFloat(EnvironmentDefinition environmentDefinition) {
        if (environmentDefinition.getRange() != null) {
            return new EnvironmentInstance(new FloatPropertyInstance(environmentDefinition.getName(),
                    ValueGeneratorFactory.createRandomFloat(environmentDefinition.getRange().getFrom(), environmentDefinition.getRange().getTo())));
        }
        else {
            return new EnvironmentInstance(new FloatPropertyInstance(environmentDefinition.getName(),
                    ValueGeneratorFactory.createRandomFloat(null, null)));
        }
    }

    private EnvironmentInstance createEnvironmentDecimal(EnvironmentDefinition environmentDefinition) {
        if (environmentDefinition.getRange() != null) {
            return new EnvironmentInstance(new IntegerPropertyInstance(environmentDefinition.getName(),
                    ValueGeneratorFactory.createRandomInteger(environmentDefinition.getRange().getFrom().intValue(), environmentDefinition.getRange().getTo().intValue())));
        }
        else {
            return new EnvironmentInstance(new FloatPropertyInstance(environmentDefinition.getName(),
                    ValueGeneratorFactory.createRandomFloat(null, null)));
        }
    }

    private List<EntityInstance> initEntities() {
        int index = 0;
        List<EntityInstance> entityInstanceList = new ArrayList<>();
        for (EntityDefinition entityDefinition: world.getEntityDefinition().values()){
            for (int count = 0; count < entityDefinition.getAmountOfPopulation(); count++){
                Map<String, Property> allProperty = new HashMap<>();
                for(PropertyDefinition propertyDefinition: entityDefinition.getProps()){
                    allProperty.put(propertyDefinition.getName(), initProperty(propertyDefinition));
                }
                entityInstanceList.add(new EntityInstance(entityDefinition.getName(), allProperty, index));
                index++;
            }
        }

        return entityInstanceList;
    }

    private Property initProperty(PropertyDefinition propertyDefinition) {
        Property property = null;

        switch (propertyDefinition.getType()) {
            case FLOAT:
                property = createPropertyFloat(propertyDefinition);
                break;
            case DECIMAL:
                property = createPropertyDecimal(propertyDefinition);
                break;
            case BOOLEAN:
                property = createPropertyBoolean(propertyDefinition);
                break;
            case STRING:
                property = createPropertyString(propertyDefinition);
                break;

        }
        return property;
    }

    private Property createPropertyFloat(PropertyDefinition propertyDefinition) {
        FloatPropertyInstance property;
        if(propertyDefinition.isRandomInitialize()) {
            if (propertyDefinition.getRange() != null) {
                property = new FloatPropertyInstance(propertyDefinition.getName(),
                        ValueGeneratorFactory.createRandomFloat( propertyDefinition.getRange().getFrom(), propertyDefinition.getRange().getTo()));
            }
            else {
                property = new FloatPropertyInstance(propertyDefinition.getName(),
                        ValueGeneratorFactory.createRandomFloat(null, null));
            }
        }
        else{
            property = new FloatPropertyInstance(propertyDefinition.getName(),
                    ValueGeneratorFactory.createFixed((float)propertyDefinition.getInit()));
        }

        return property;
    }

    private Property createPropertyDecimal(PropertyDefinition propertyDefinition) {
        String stringValue;
        IntegerPropertyInstance property;
        if(propertyDefinition.isRandomInitialize()) {
            if (propertyDefinition.getRange() != null) {
                property = new IntegerPropertyInstance(propertyDefinition.getName(),
                        ValueGeneratorFactory.createRandomInteger(propertyDefinition.getRange().getFrom().intValue(), propertyDefinition.getRange().getTo().intValue()));
            }
            else {
                property = new IntegerPropertyInstance(propertyDefinition.getName(),
                        ValueGeneratorFactory.createRandomInteger(null, null));
            }
        }
        else{
            stringValue = (String) propertyDefinition.getInit();
            property = new IntegerPropertyInstance(propertyDefinition.getName(),
                    ValueGeneratorFactory.createFixed(Integer.parseInt(stringValue)));
        }
        return property;
    }

    private Property createPropertyBoolean(PropertyDefinition propertyDefinition) {
        BooleanPropertyInstance property;
        if(propertyDefinition.isRandomInitialize()) {
            property = new BooleanPropertyInstance(propertyDefinition.getName(), ValueGeneratorFactory.createRandomBoolean());
        }
        else{
            property = new BooleanPropertyInstance(propertyDefinition.getName(),
                    ValueGeneratorFactory.createFixed((boolean)propertyDefinition.getInit()));
        }

        return property;
    }

    private Property createPropertyString(PropertyDefinition propertyDefinition) {
        StringPropertyInstance property;
        if(propertyDefinition.isRandomInitialize()) {
            property = new StringPropertyInstance(propertyDefinition.getName(), ValueGeneratorFactory.createRandomString());
        }
        else{
            property = new StringPropertyInstance(propertyDefinition.getName(),
                    ValueGeneratorFactory.createFixed((String) propertyDefinition.getInit()));
        }

        return property;
    }

    private void printEnvironmentNamesAndValues() {
        int index = 1;
        System.out.println("All the environment properties name and value: ");
        for (Map.Entry<String,  EnvironmentInstance> entry : worldInstance.getEnvironmentInstanceMap().entrySet()) {
            System.out.print(index);
            System.out.print(".");
            System.out.println(entry.getValue());
            index += 1;
        }
    }

    private void runSimulation() {
        long startMillisSeconds = System.currentTimeMillis();
        String message = null;
        int seconds = 0;
        try {
            while (seconds <= world.getTermination().getSecond() && worldInstance.getCurrentTick() <= world.getTermination().getTicks()) {
                for (RuleImpl rule : world.getRules()) {
                    if (rule.getActivation().isActive(worldInstance.getCurrentTick())) {
                        for (Action action : rule.nameActions()) {
                            performOperation(action);
                        }
                    }
                }

                worldInstance.setCurrentTick(worldInstance.getCurrentTick() + 1);
                long currentMilliSeconds = System.currentTimeMillis();
                seconds = (int) ((currentMilliSeconds - startMillisSeconds) / 1000);
            }

            if (seconds > world.getTermination().getSecond()) {
                message = "The simulation has ended because more than " + world.getTermination().getSecond() + " seconds have passed";
            } else if (worldInstance.getCurrentTick() > world.getTermination().getTicks()) {
                message = "The simulation has ended because more than " + world.getTermination().getTicks() + " ticks have passed";
            }
            printIdAndTerminationReason(message);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void performOperation(Action action) throws ObjectNotExist, OperationNotSupportedType {
        String entityName = action.getEntityName();
        List<EntityInstance> entitiesToRemove = new ArrayList<>();
        boolean flag = false;
        for(EntityInstance entityInstance: worldInstance.getEntityInstanceList()){
            if(entityName.equals(entityInstance.getName())) {
                flag = true;
                try {
                    if(!action.getActionType().equals(ActionType.KILL)) {
                        action.operation(entityInstance);
                    }
                    entitiesToRemove.add(entityInstance);
                }
                catch (ObjectNotExist | NumberFormatException | ClassCastException | ArithmeticException | OperationNotSupportedType exception){
                    System.out.println(exception.getMessage());
                    break;
                }
            }
        }

        if (action.getActionType().equals(ActionType.KILL)) {
            for (int i = entitiesToRemove.size() - 1; i >= 0; i--) {
                EntityInstance entityToRemove = entitiesToRemove.get(i);
                action.operation(entityToRemove);
            }
        }

        if(!flag){
            throw new ObjectNotExist(action.getEntityName(), "Entity");
        }
    }

    private void printIdAndTerminationReason(String message) {
        System.out.println("The simulation has ended");
        System.out.print("Simulation id: ");
        System.out.println(numberOfTimesUserSelectSimulation);
        System.out.println(message);
    }

    private void detailsOfPastRun() {
        TreeMap<Integer, Simulation> sortedMap = new TreeMap<>();
        int userIntegerInput = 0;
        int userDisplayModeInput ;
        try{
            sortedMap = sortMapOfSimulations();
            userIntegerInput = chooseOfPastSimulation(sortedMap);
            userDisplayModeInput = chooseTheDisplayMode();
            showDetailsOfSpecificPastRun(DisplaySimulationOption.getOptionByNumber(userDisplayModeInput), userIntegerInput);
        }
        catch (NullPointerException exception){
            System.out.println("You need to run at least one simulation first");
        }
    }

    private TreeMap<Integer, Simulation> sortMapOfSimulations() {
        return new TreeMap<>(history.getAllSimulations());
    }

    private int chooseOfPastSimulation(TreeMap<Integer, Simulation> sortedMap) {
        int index = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The list of all the runs performed by the system are:\n");
        stringBuilder.append("Please select the relevant run you want to see the results of\n");
        for (Map.Entry<Integer, Simulation> entry : sortedMap.entrySet()) {
            index++;
            stringBuilder.append(index + ". Unique identifier of the simulation : " + entry.getKey() + ", Running date: " + entry.getValue().getFormattedDateTime() + "\n");
        }
        return chooseInput(sortedMap.size(), stringBuilder.toString());
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
                displayByHistogrmOfProperty(userIntegerInput);
                break;
        }
    }

    private void displayByQuantity(int userIntegerInput) {
        int count = 0;
        WorldInstance world = history.getAllSimulations().get(userIntegerInput).getWorldInstance();
        Map<String , Integer> entity = new HashMap<>();
        System.out.println("The initial and final quantity of each entity: ");
        for (EntityInstance entityInstance1: world.getEntityInstanceList()) {
            if(!entity.containsKey(entityInstance1.getName())){
                entity.put(entityInstance1.getName(), 1);
                for (EntityInstance entityInstance2: world.getEntityInstanceList()) {
                    if(entityInstance2.getName().equals(entityInstance1.getName())){
                        count++;
                    }
                }
                System.out.println("Entity " + entityInstance1.getName());
                System.out.println("The initial quantity of this entity : " + world.getWorldDefinition().getEntityDefinition().get(entityInstance1.getName()).getAmountOfPopulation());
                System.out.println("The final quantity of this entity : " + count);
                count = 0;
            }
        }
    }

    private void displayByHistogrmOfProperty(int userIntegerInput) {
        WorldInstance worldInstance1 = history.getAllSimulations().get(userIntegerInput).getWorldInstance();
        Map<Object, Integer> valuesProperty = new HashMap<>();
        EntityDefinition userEntityInput;
        String propertyInput;
        userEntityInput = chooseEntity(userIntegerInput);
        propertyInput = chooseProperty(userEntityInput);
        createPropertyValuesMap(valuesProperty, worldInstance1, userEntityInput, propertyInput);
        printPropertiesValues(valuesProperty, propertyInput);
    }

    private EntityDefinition chooseEntity(int userIntegerInput) {
        Map<Integer, String> entities = new HashMap<>();
        WorldInstance world = history.getAllSimulations().get(userIntegerInput).getWorldInstance();
        int index = world.getWorldDefinition().getEntityDefinition().keySet().size();
        String message = getEntitiesMessage(world, entities);
        int userEntityInput = chooseInput(index, message);
        return world.getWorldDefinition().getEntityDefinition().get(entities.get(userEntityInput));
    }

    private String getEntitiesMessage(WorldInstance world, Map<Integer, String> entities)
    {
        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        stringBuilder.append("Please select one of the given entities\n");
        for(String name: world.getWorldDefinition().getEntityDefinition().keySet()){
            index += 1;
            stringBuilder.append(index + ".");
            stringBuilder.append(name + "\n");
            entities.put(index, name);
        }
        return stringBuilder.toString();
    }

    private String chooseProperty(EntityDefinition userEntityInput) {
        Map<Integer, String> properties = new HashMap<>();
        int index = userEntityInput.getProps().size();
        String message = printProperties(userEntityInput, properties);
        int userPropertyInput = chooseInput(index, message);
        return properties.get(userPropertyInput);
    }

    private String printProperties(EntityDefinition userEntityInput, Map<Integer, String> properties) {
        int index = 0;
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Please select one of the given properties\n");
        for(PropertyDefinition property: userEntityInput.getProps()){
            index += 1;
            stringBuilder.append(index + ".");
            stringBuilder.append(property.getName() + "\n");
            properties.put(index, property.getName());
        }
        return  stringBuilder.toString();
    }

    private void createPropertyValuesMap(Map<Object, Integer> valuesProperty, WorldInstance worldInstance1, EntityDefinition userEntityInput, String propertyInput) {
        for (EntityInstance entityInstance: worldInstance1.getEntityInstanceList()){
            if (entityInstance.getName().equals(userEntityInput.getName())){
                for (Property property: entityInstance.getAllProperty().values()){
                    if (property.getName().equals(propertyInput)){
                        if (!valuesProperty.containsKey(property.getValue())){
                            valuesProperty.put(property.getValue(), 1);
                        }
                        else{
                            valuesProperty.put(property.getValue(), valuesProperty.get(property.getValue()) + 1);
                        }
                    }
                }
            }
        }
    }

    private void printPropertiesValues(Map<Object, Integer> valuesProperty, String propertyInput) {
        System.out.println("The histogram of the property " + propertyInput);
        for(Object object: valuesProperty.keySet()){
            System.out.println("There are " + valuesProperty.get(object)+ " instances of the property " + propertyInput + " that is value is " + object);
        }
    }
}