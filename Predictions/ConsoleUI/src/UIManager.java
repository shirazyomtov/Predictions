import exceptions.FilePathException;
import world.entity.definition.EntityDefiniton;
import world.entity.definition.PropertyDefinition;
import world.entity.instance.EntityInstance;
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

import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.*;

public class UIManager {

    private WorldDefinition world = null;

    private WorldInstance worldInstance = null;
    private XMLReader xmlReader = null;
    private XMLValidation xmlValidation = null;
    public void RunProgram()
    {
        int option = 0;
        while(option != MenuOptions.EXIT.GetNUmberOfOption()) {
            option = chooseOption();
            runOptionSelectedByUser(MenuOptions.GetOptionByNumber(option));
        }
    }

    private int chooseOption()
    {
        Menu menu = new Menu();
        boolean validInput = false;
        int userIntegerInput = 0;
        Scanner scanner = new Scanner(System.in);

        do {
            try{
                menu.ShowMenu();
                userIntegerInput = Integer.parseInt(scanner.nextLine());
                MenuOptions.GetOptionByNumber(userIntegerInput);
                validInput = true;
            }
            catch (NumberFormatException  | IndexOutOfBoundsException exception){
                System.out.println("Invalid input. Please insert a number between 1 and " + MenuOptions.GetCountOfOptons() + ".");
            }
        }while (!validInput);

        return  userIntegerInput;
    }

    private void runOptionSelectedByUser(MenuOptions option) {
        switch (option) {
            case LOAD_XML:
                loadXML();
                break;
            case SIMULATION_DETAILS:
                simulationDetails();
                break;
            case  SIMULATION:
                simulation();
                break;
        }
    }

    private void simulation() {
        Map <String, EnvironmentInstance> environmentValuesByUser = new HashMap<>();
        try {
            int userIntegerInput = 0;
            List<String> enviromentName = createListEnvironmentNames();
            while(userIntegerInput != world.getEnvironmentDefinition().size() + 1) {
                userIntegerInput = chooseEnvironmentProperty();
                if (userIntegerInput != world.getEnvironmentDefinition().size() + 1) {
                    setValueEnvironment(userIntegerInput, enviromentName, environmentValuesByUser);
                }
            }
           startSimulation(environmentValuesByUser);
           runSimulation();
        }
        catch (NullPointerException e){
            System.out.println("You cannot run the simulation before loading the xml file");
        }
    }

    private void runSimulation() {
        while (true){
            for (RuleImpl rule: world.getRules()){
                Random random = new Random();
                double probability = random.nextDouble();
                if(rule.getActivation().getTicks() == worldInstance.getCurrentTick() && (rule.getActivation().getProbability() == 1 || rule.getActivation().getTicks() > probability)){
                    for(Action action: rule.nameActions()){
                        performeOperation(action);
                    }
                }
            }

            worldInstance.setCurrentTick(worldInstance.getCurrentTick() + 1);
        }
    }

    private void performeOperation(Action action) {
        String entityName = action.getEntityName();
        for(EntityInstance entityInstance: worldInstance.getEntityInstanceList()){
            if(entityName.equals(entityInstance.getName())) {

            }
        }

    }

    private void startSimulation( Map <String, EnvironmentInstance> environmentValuesByUser) {
        Map<String, EnvironmentInstance> environmentInstanceMap = environmentValuesByUser;

        for (String environmentName: world.getEnvironmentDefinition().keySet()){
            if(!environmentValuesByUser.containsKey(environmentName)){
                provideRandomValues(world.getEnvironmentDefinition().get(environmentName), environmentInstanceMap);
            }
        }

        worldInstance = new WorldInstance(environmentInstanceMap, initEntites());
        worldInstance.setCurrentTick(worldInstance.getCurrentTick() + 1);
        printEnvironmentNamesAndValues();
    }

    private List<EntityInstance> initEntites() {
        List<EntityInstance> entityInstanceList = new ArrayList<>();
        List<Property> allProperty = new ArrayList<>();
        for (EntityDefiniton entityDefiniton: world.getEntityDefinition().values()){
            for (int count = 0; count< entityDefiniton.getAmountOfPopulation(); count++){
                for(PropertyDefinition propertyDefinition: entityDefiniton.getProps()){
                    allProperty.add(initProperty(propertyDefinition));
                }
                entityInstanceList.add(new EntityInstance(entityDefiniton.getName(), allProperty));
                allProperty.clear();
            }
        }

        return entityInstanceList;
    }

    private Property initProperty(PropertyDefinition propertyDefinition) {
        Property property = null;
        switch (propertyDefinition.getType()) {
            case FLOAT:
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
                break;
            case DECIMAL:
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
                    property = new IntegerPropertyInstance(propertyDefinition.getName(),
                            ValueGeneratorFactory.createFixed((int)propertyDefinition.getInit()));
                }
                break;
            case BOOLEAN:
                if(propertyDefinition.isRandomInitialize()) {
                    property = new BooleanPropertyInstance(propertyDefinition.getName(), ValueGeneratorFactory.createRandomBoolean());
                }
                else{
                    property = new BooleanPropertyInstance(propertyDefinition.getName(),
                            ValueGeneratorFactory.createFixed((boolean)propertyDefinition.getInit()));
                }
                break;
            case STRING:
                if(propertyDefinition.isRandomInitialize()) {
                    property = new StringPropertyInstance(propertyDefinition.getName(), ValueGeneratorFactory.createRandomString());
                }
                else{
                    property = new StringPropertyInstance(propertyDefinition.getName(),
                            ValueGeneratorFactory.createFixed((String) propertyDefinition.getInit()));
                }
                break;

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

    private void provideRandomValues(EnvironmentDefinition environmentDefinition,  Map<String, EnvironmentInstance> environmentInstanceMap) {
        EnvironmentInstance environmentInstance = null;

        switch (environmentDefinition.getType()) {
            case FLOAT:
                if (environmentDefinition.getRange() != null) {
                    environmentInstance = new EnvironmentInstance(new FloatPropertyInstance(environmentDefinition.getName(),
                            ValueGeneratorFactory.createRandomFloat( environmentDefinition.getRange().getFrom(), environmentDefinition.getRange().getTo())));
                }
                else {
                    environmentInstance = new EnvironmentInstance(new FloatPropertyInstance(environmentDefinition.getName(),
                            ValueGeneratorFactory.createRandomFloat(null, null)));
                }
                break;
            case DECIMAL:
                if (environmentDefinition.getRange() != null) {
                    environmentInstance = new EnvironmentInstance(new IntegerPropertyInstance(environmentDefinition.getName(),
                            ValueGeneratorFactory.createRandomInteger(environmentDefinition.getRange().getFrom().intValue(), environmentDefinition.getRange().getTo().intValue())));
                }
                else {
                    environmentInstance = new EnvironmentInstance(new FloatPropertyInstance(environmentDefinition.getName(),
                            ValueGeneratorFactory.createRandomFloat(null, null)));
                }
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

    private void setValueEnvironment(int userIntegerInput, List<String> enviromentName,  Map <String, EnvironmentInstance> environmentValuesByUser) {
        String environmentName = enviromentName.get(userIntegerInput -1);
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
                System.out.println("NumberFormatException");
            }
            catch (IndexOutOfBoundsException exception)
            {
                System.out.println("IndexOutOfBoundsException");
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

    private int chooseBooleanValue() {
        boolean validInput = false;
        int userIntegerInput = 0;
        Scanner scanner = new Scanner(System.in);

        do {
            try{
                System.out.println("Please select one of the following options using a number.");
                System.out.println("1. True");
                System.out.println("2. False");
                userIntegerInput = Integer.parseInt(scanner.nextLine());
                checkValidationBooleanInput(userIntegerInput);
                validInput = true;
            }
            catch (NumberFormatException  | IndexOutOfBoundsException exception){
                System.out.println("Invalid input. Please insert a number between 1 and " + 2 + ".");
            }
        }while (!validInput);

        return userIntegerInput;

    }

    private void checkValidationBooleanInput(int userIntegerInput) throws IndexOutOfBoundsException{
        if(userIntegerInput < 1 || userIntegerInput > 2){
            throw new IndexOutOfBoundsException();
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

    private int chooseEnvironmentProperty() throws  NullPointerException{
        // change duplicate code
        boolean validInput = false;
        int userIntegerInput = 0;
        int index = 0;
        Scanner scanner = new Scanner(System.in);

        do {
            try{
                if (world != null) {
                    System.out.println("These are all the environment variables defined in the xml file.");
                    System.out.println("You can provide a value for each of the environment variables.");
                    System.out.println("Please enter the number of the environment variable you want to provide a value for.");
                }
                index = printEnvironmentNames();
                userIntegerInput = Integer.parseInt(scanner.nextLine());
                checkValidationInput(userIntegerInput);
                validInput = true;
            }
            catch (NumberFormatException  | IndexOutOfBoundsException exception){
                System.out.println("Invalid input. Please insert a number between 1 and " + index + ".");
            }
        }while (!validInput);

        return userIntegerInput;
    }

    private void checkValidationInput(int userIntegerInput) throws IndexOutOfBoundsException {
        if(userIntegerInput < 1 || userIntegerInput > world.getEnvironmentDefinition().size() + 1){
           throw new IndexOutOfBoundsException();
        }

    }

    private  int  printEnvironmentNames() throws NullPointerException {
        int index = 1;

        for (Map.Entry<String, EnvironmentDefinition> environmentEntry : world.getEnvironmentDefinition().entrySet()) {
            System.out.print(index);
            System.out.print(".");
            System.out.println(environmentEntry.getValue().getName());
            index += 1;
        }
        System.out.print(index);
        System.out.print(".");
        System.out.println("Start the simulation");

        return index;
    }

    private  List<String>  createListEnvironmentNames() throws NullPointerException {
        List<String> enviromentName = new ArrayList<>();
        for (Map.Entry<String, EnvironmentDefinition> environmentEntry : world.getEnvironmentDefinition().entrySet()) {
            enviromentName.add(environmentEntry.getValue().getName());
        }

        return enviromentName;
    }


    private void simulationDetails() {
        try{
            if (world != null) {
                System.out.println("The information about the simulation defined in the xml file are:");
                printEntitiesDetalis();
                printRulesDetalis();
                printTerminationDetalis();
            }
        }
       catch (NullPointerException e){
            System.out.println("You cannot see the simulation details before you have loaded the xml file");
        }

    }

    private void printTerminationDetalis() {
        System.out.println("3.Termination:");
        System.out.println(world.getTermination());
    }

    private void printRulesDetalis() {
        System.out.println("2.Rules:");
        for (RuleImpl rule : world.getRules()) {
            System.out.println(rule);
        }

    }

    private void printEntitiesDetalis() {
        System.out.println("1.Entities:");
        Map<String, EntityDefinitionImpl> entityDefinitions = world.getEntityDefinition();
        for (Map.Entry<String, EntityDefinitionImpl> entry : entityDefinitions.entrySet()) {
            System.out.println(entry.getValue());
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
}
