package engineManager;

import DTO.*;
import exceptions.*;
import history.History;
import history.simulation.Simulation;
import world.entity.definition.EntityDefinition;
import world.entity.definition.EntityDefinitionImpl;
import world.entity.definition.PropertyDefinition;
import world.entity.instance.EntityInstance;
import world.environment.definition.EnvironmentDefinition;
import world.environment.instance.EnvironmentInstance;
import world.propertyInstance.api.Property;
import world.propertyInstance.impl.BooleanPropertyInstance;
import world.propertyInstance.impl.FloatPropertyInstance;
import world.propertyInstance.impl.IntegerPropertyInstance;
import world.propertyInstance.impl.StringPropertyInstance;
import world.value.generator.api.ValueGeneratorFactory;
import world.worldDefinition.WorldDefinition;
import world.worldInstance.WorldInstance;
import xml.XMLReader;
import xml.XMLValidation;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class EngineManager implements Serializable{
    private History history = null;
    private Integer numberOfTimesUserSelectSimulation = 0;

    private WorldDefinition world = null;

    private WorldInstance worldInstance = null;

    Map <String, EnvironmentInstance> environmentValuesByUser = new HashMap<>();

    public void loadXMLAAndCheckValidation(String xmlPath) throws Exception {
        try {
            XMLReader xmlReader = new XMLReader(xmlPath);
            XMLValidation xmlValidation;
            checkValidationXMLPath(xmlPath);
            xmlValidation = xmlReader.openXmlAndGetData();
            xmlValidation.checkValidationXmlFile();
            world = xmlReader.defineWorld();
            History.getInstance().getAllSimulations().clear();
            history = null;
            numberOfTimesUserSelectSimulation = 0;
        }
        catch (FileNotFoundException | JAXBException e) {
            throw new FileNotFoundException("File has not been found in this path " + xmlPath + ".");
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void checkValidationXMLPath(String path) throws FilePathException {
        if (path.length() <= 4) {
            throw new FilePathException(FilePathException.ErrorType.FILE_NAME_CONTAINS_LESS_THAN_5_CHARACTERS);
        } else if (!path.endsWith(".xml")) {
            throw new FilePathException(FilePathException.ErrorType.NOT_ENDS_WITH_XML);
        }
    }

    public void checkIfXmlLoaded() throws NullPointerException{
        Objects.requireNonNull(world);
    }

    public List<DTOEntityInfo> getEntitiesDetails(){
        return world.entitiesDetails();
    }

    public List<DTORuleInfo> getRulesDetails(){
        return world.rulesDetails();
    }

    public DTOTerminationInfo getTerminationDetails(){
        return new DTOTerminationInfo(world.getTermination().getTicks(), world.getTermination().getSecond());
    }

    public int getAmountOfEnvironmentDefinition() throws NullPointerException{
        return world.getEnvironmentDefinition().size();
    }

    public List<DTOEnvironmentInfo> getEnvironmentNamesList() {
        return world.createListEnvironmentDetails();
    }

    public DTOEnvironmentInfo getEnvironmentDetails(int userIntegerInput){
        return getEnvironmentNamesList().get(userIntegerInput -1);
    }

    public void checkValidationFloatEnvironment(String valueInput, int userIntegerInput)throws NumberFormatException, IndexOutOfBoundsException{
        String environmentName = getEnvironmentDetails(userIntegerInput).getName();
        EnvironmentDefinition environmentDefinition = world.getEnvironmentDefinition().get(environmentName);
        EnvironmentInstance environmentInstance = null;
        Float userInput = Float.parseFloat(valueInput);
        checkIfInputInRange(userInput, environmentDefinition, false);
        environmentInstance = new EnvironmentInstance(new FloatPropertyInstance(environmentDefinition.getName(), ValueGeneratorFactory.createFixed((float)userInput), environmentDefinition.getRange()));
        environmentValuesByUser.put(environmentInstance.getProperty().getName(), environmentInstance);
    }

    public void checkValidationDecimalEnvironment(String valueInput, int userIntegerInput)throws NumberFormatException, IndexOutOfBoundsException {
        String environmentName = getEnvironmentDetails(userIntegerInput).getName();
        EnvironmentDefinition environmentDefinition = world.getEnvironmentDefinition().get(environmentName);
        EnvironmentInstance environmentInstance = null;
        Integer userInput = Integer.parseInt(valueInput);
        checkIfInputInRange(userInput, environmentDefinition, true);
        environmentInstance= new EnvironmentInstance(new IntegerPropertyInstance(environmentDefinition.getName(), ValueGeneratorFactory.createFixed((int)userInput), environmentDefinition.getRange()));
        environmentValuesByUser.put(environmentInstance.getProperty().getName(), environmentInstance); // problem
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

    public void checkValidationBoolEnvironment(Integer userInput, int userIntegerInput)throws NumberFormatException, IndexOutOfBoundsException {
        String environmentName = getEnvironmentDetails(userIntegerInput).getName();
        EnvironmentDefinition environmentDefinition = world.getEnvironmentDefinition().get(environmentName);
        EnvironmentInstance environmentInstance = null;
        if(userInput == 1) {
            environmentInstance = new EnvironmentInstance(new BooleanPropertyInstance(environmentDefinition.getName(), ValueGeneratorFactory.createFixed(true), environmentDefinition.getRange()));
        }
        else{
            environmentInstance = new EnvironmentInstance(new BooleanPropertyInstance(environmentDefinition.getName(), ValueGeneratorFactory.createFixed(false), environmentDefinition.getRange()));
        }
        environmentValuesByUser.put(environmentInstance.getProperty().getName(), environmentInstance);
    }

    public void checkValidationStringEnvironment(String valueInput, int userIntegerInput)throws NumberFormatException, IndexOutOfBoundsException {
        String environmentName = getEnvironmentDetails(userIntegerInput).getName();
        EnvironmentDefinition environmentDefinition = world.getEnvironmentDefinition().get(environmentName);
        EnvironmentInstance environmentInstance = null;
        environmentInstance = new EnvironmentInstance(new StringPropertyInstance(environmentDefinition.getName(), ValueGeneratorFactory.createFixed((String) valueInput), environmentDefinition.getRange()));
        environmentValuesByUser.put(environmentInstance.getProperty().getName(), environmentInstance);
    }

    public void setSimulation(){
        Map<String, EnvironmentInstance> environmentInstanceMap = environmentValuesByUser;

        setRandomEnvironmentValues(environmentInstanceMap);
        setSimulationDetailsAndAddToHistory(environmentInstanceMap);
    }



    private void setRandomEnvironmentValues(Map<String, EnvironmentInstance> environmentInstanceMap) {

        for (String environmentName: world.getEnvironmentDefinition().keySet()){
            if(!environmentValuesByUser.containsKey(environmentName)){
                provideRandomValues(world.getEnvironmentDefinition().get(environmentName), environmentInstanceMap);
            }
        }
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
                environmentInstance = new EnvironmentInstance(new BooleanPropertyInstance(environmentDefinition.getName(), ValueGeneratorFactory.createRandomBoolean(), environmentDefinition.getRange()));
                break;
            case STRING:
                environmentInstance = new EnvironmentInstance(new StringPropertyInstance(environmentDefinition.getName(), ValueGeneratorFactory.createRandomString(), environmentDefinition.getRange()));
                break;

        }
        if (environmentInstance != null) {
            environmentInstanceMap.put(environmentInstance.getProperty().getName(), environmentInstance); // problem
        }
    }

    private EnvironmentInstance createEnvironmentFloat(EnvironmentDefinition environmentDefinition) {
        if (environmentDefinition.getRange() != null) {
            return new EnvironmentInstance(new FloatPropertyInstance(environmentDefinition.getName(),
                    ValueGeneratorFactory.createRandomFloat(environmentDefinition.getRange().getFrom(), environmentDefinition.getRange().getTo()), environmentDefinition.getRange()));
        }
        else {
            return new EnvironmentInstance(new FloatPropertyInstance(environmentDefinition.getName(),
                    ValueGeneratorFactory.createRandomFloat(null, null), environmentDefinition.getRange()));
        }
    }

    private EnvironmentInstance createEnvironmentDecimal(EnvironmentDefinition environmentDefinition) {
        if (environmentDefinition.getRange() != null) {
            return new EnvironmentInstance(new IntegerPropertyInstance(environmentDefinition.getName(),
                    ValueGeneratorFactory.createRandomInteger(environmentDefinition.getRange().getFrom().intValue(), environmentDefinition.getRange().getTo().intValue()), environmentDefinition.getRange()));
        }
        else {
            return new EnvironmentInstance(new FloatPropertyInstance(environmentDefinition.getName(),
                    ValueGeneratorFactory.createRandomFloat(null, null), environmentDefinition.getRange()));
        }
    }

    private void setSimulationDetailsAndAddToHistory(Map<String, EnvironmentInstance> environmentInstanceMap) {
        numberOfTimesUserSelectSimulation++;
        List<EntityInstance> entityInstanceList = initEntities();
        worldInstance =new WorldInstance(environmentInstanceMap, entityInstanceList, world);
        Simulation simulation = new Simulation(worldInstance, LocalDateTime.now());
        history = History.getInstance();
        history.setCurrentSimulationNumber(numberOfTimesUserSelectSimulation);
        history.addSimulation(simulation);
    }

    private List<EntityInstance> initEntities() {
        List<EntityInstance> entityInstanceList = new ArrayList<>();
        for (EntityDefinition entityDefinition: world.getEntityDefinition().values()){
            for (int count = 0; count < entityDefinition.getAmountOfPopulation(); count++){
                Map<String, Property> allProperty = new HashMap<>();
                for(PropertyDefinition propertyDefinition: entityDefinition.getProps()){
                    allProperty.put(propertyDefinition.getName(), initProperty(propertyDefinition));
                }
                entityInstanceList.add(new EntityInstance(entityDefinition.getName(), allProperty, world.getTwoDimensionalGrid().createNewLocation()));
            }
        }

        return entityInstanceList;
    }


    public static Property initProperty(PropertyDefinition propertyDefinition) {
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

    private static Property createPropertyFloat(PropertyDefinition propertyDefinition) {
        FloatPropertyInstance property;
        if(propertyDefinition.isRandomInitialize()) {
            if (propertyDefinition.getRange() != null) {
                property = new FloatPropertyInstance(propertyDefinition.getName(),
                        ValueGeneratorFactory.createRandomFloat( propertyDefinition.getRange().getFrom(), propertyDefinition.getRange().getTo()), propertyDefinition.getRange());
            }
            else {
                property = new FloatPropertyInstance(propertyDefinition.getName(),
                        ValueGeneratorFactory.createRandomFloat(null, null), propertyDefinition.getRange());
            }
        }
        else{
            String stringValue = (String) propertyDefinition.getInit();
            property = new FloatPropertyInstance(propertyDefinition.getName(),
                    ValueGeneratorFactory.createFixed(Float.parseFloat(stringValue)), propertyDefinition.getRange());
        }

        return property;
    }

    private static Property createPropertyDecimal(PropertyDefinition propertyDefinition) {
        String stringValue;
        IntegerPropertyInstance property;
        if(propertyDefinition.isRandomInitialize()) {
            if (propertyDefinition.getRange() != null) {
                property = new IntegerPropertyInstance(propertyDefinition.getName(),
                        ValueGeneratorFactory.createRandomInteger(propertyDefinition.getRange().getFrom().intValue(), propertyDefinition.getRange().getTo().intValue()), propertyDefinition.getRange());
            }
            else {
                property = new IntegerPropertyInstance(propertyDefinition.getName(),
                        ValueGeneratorFactory.createRandomInteger(null, null), propertyDefinition.getRange());
            }
        }
        else{
            stringValue = (String) propertyDefinition.getInit();
            property = new IntegerPropertyInstance(propertyDefinition.getName(),
                    ValueGeneratorFactory.createFixed(Integer.parseInt(stringValue)), propertyDefinition.getRange());
        }
        return property;
    }

    private static Property createPropertyBoolean(PropertyDefinition propertyDefinition) {
        BooleanPropertyInstance property;
        if(propertyDefinition.isRandomInitialize()) {
            property = new BooleanPropertyInstance(propertyDefinition.getName(), ValueGeneratorFactory.createRandomBoolean(), propertyDefinition.getRange());
        }
        else{
            property = new BooleanPropertyInstance(propertyDefinition.getName(),
                    ValueGeneratorFactory.createFixed((boolean)propertyDefinition.getInit()), propertyDefinition.getRange());
        }

        return property;
    }

    private static Property createPropertyString(PropertyDefinition propertyDefinition) {
        StringPropertyInstance property;
        if(propertyDefinition.isRandomInitialize()) {
            property = new StringPropertyInstance(propertyDefinition.getName(), ValueGeneratorFactory.createRandomString(), propertyDefinition.getRange());
        }
        else{
            property = new StringPropertyInstance(propertyDefinition.getName(),
                    ValueGeneratorFactory.createFixed((String) propertyDefinition.getInit()), propertyDefinition.getRange());
        }

        return property;
    }

    public List<DTOEnvironmentInfo> getEnvironmentNamesAndValues(){
        return worldInstance.createListEnvironmentNamesAndValues();
    }

    public String getRunSimulation() throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException, OperationNotSupportedType, OperationNotCompatibleTypes, FormatException, EntityNotDefine {
        return history.getSimulation().runSimulation();
    }

    public Integer getNumberOfTimesUserSelectSimulation() {
        return numberOfTimesUserSelectSimulation;
    }

    public void removeSimulationFromHistory(){
       history.removeCurrentSimulation();
    }

    public void checkIfThereSimulation() throws NullPointerException{
        if(History.getInstance().getAllSimulations().isEmpty()){
            throw new NullPointerException("You need to run at least one proper simulation ");
        }
    }

    public List<DTOSimulationInfo> getAllPastSimulation() {
        return history.getDTOSimulations();
    }

    public int getSortedMapSize(){
        return history.getSortMapOfSimulations().size();
    }

    public DTOEntityInfo getQuantityOfEachEntity(int userIntegerInput) {
        int count = 0;
        boolean flag = false;
        Map<String, Integer> entity = new HashMap<>();
        DTOEntityInfo dtoEntityInfo = null;

        for (EntityInstance entityInstance1 : history.getAllSimulations().get(userIntegerInput).getWorldInstance().getEntityInstanceList()) {
            flag = true;
            if (!entity.containsKey(entityInstance1.getName())) {
                entity.put(entityInstance1.getName(), 1);
                for (EntityInstance entityInstance2 : history.getAllSimulations().get(userIntegerInput).getWorldInstance().getEntityInstanceList()) {
                    if (entityInstance2.getName().equals(entityInstance1.getName())) {
                        count++;
                    }
                }
                int amount = history.getAllSimulations().get(userIntegerInput).getWorldInstance().getWorldDefinition().getEntityDefinition().get(entityInstance1.getName()).getAmountOfPopulation();
                String name = entityInstance1.getName();
                dtoEntityInfo = new DTOEntityInfo(amount, count, name);
                count = 0;
            }
        }

        if (!flag) {
            for (Map.Entry<String, EntityDefinitionImpl> entityDefinition : history.getAllSimulations().get(userIntegerInput).getWorldInstance().getWorldDefinition().getEntityDefinition().entrySet()) {
                dtoEntityInfo = new DTOEntityInfo(entityDefinition.getValue().getAmountOfPopulation(), 0, entityDefinition.getKey());
            }
        }
        return dtoEntityInfo;
    }

    public void checkIfThereIsEntity(int userIntegerInput) throws Exception{
        WorldInstance worldInstance1 = history.getAllSimulations().get(userIntegerInput).getWorldInstance();
        if(worldInstance1.getEntityInstanceList().isEmpty()){
            throw new Exception("All of the entities were killed during the simulation\n");
        }
    }

    public List<DTOEntityInfo> getEntitiesList(int userIntegerInput){
        WorldInstance currentWorld = history.getAllSimulations().get(userIntegerInput).getWorldInstance();
        return currentWorld.getWorldDefinition().entitiesDetails();
    }

    public int getEntitiesDefinitionSize(int userIntegerInput){
        WorldInstance currentWorld = history.getAllSimulations().get(userIntegerInput).getWorldInstance();
        return currentWorld.getWorldDefinition().getEntityDefinition().keySet().size();
    }

    public int getPropertiesSize(String entityName, int userIntegerInput) {
        return getPropertiesListFromSimulation(entityName, userIntegerInput).size();
    }

    public List<DTOPropertyInfo> getPropertiesListFromSimulation(String entityName, int userIntegerInput){
        WorldInstance currentWorld = history.getAllSimulations().get(userIntegerInput).getWorldInstance();
        return currentWorld.getWorldDefinition().getEntityDefinition().get(entityName).getDTOProperties();

    }

    public Map<Object, Integer> createPropertyValuesMap(int userIntegerInput, String entityName, String propertyName) {
        WorldInstance currentWorld = history.getAllSimulations().get(userIntegerInput).getWorldInstance();
        Map<Object, Integer> valuesProperty = new HashMap<>();
        for (EntityInstance entityInstance : currentWorld.getEntityInstanceList()) {
            if (entityInstance.getName().equals(entityName)) {
                for (Property property : entityInstance.getAllProperty().values()) {
                    if (property.getName().equals(propertyName)) {
                        if (!valuesProperty.containsKey(property.getValue())) {
                            valuesProperty.put(property.getValue(), 1);
                        } else {
                            valuesProperty.put(property.getValue(), valuesProperty.get(property.getValue()) + 1);
                        }
                    }
                }
            }
        }

        return valuesProperty;
    }

    public void loadFileAndSetHistory(String filePath, EngineManager engineManager) throws IOException, ClassNotFoundException{
        filePath += ".txt";
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(filePath))) {
            engineManager = (EngineManager) in.readObject();
            history = engineManager.getHistory();
            if(history != null) {
                History.getInstance().setAllSimulations(history.getAllSimulations());
                History.getInstance().setCurrentSimulationNumber(history.getCurrentSimulationNumber());
                world = history.getSimulation().getWorldInstance().getWorldDefinition();
                worldInstance = history.getSimulation().getWorldInstance();
                numberOfTimesUserSelectSimulation = history.getCurrentSimulationNumber();
            }
            else{
                world = engineManager.getWorld();
            }
        }
        catch (FileNotFoundException e){
            throw new FileNotFoundException("No such file exists in this path: " + filePath );
        }
        catch (IOException e) {
            throw new IOException("The file empty");
        }
    }

    public void saveFile(String filePath, EngineManager engineManager) throws IOException {
        filePath += ".txt";
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream out = new ObjectOutputStream(fos)) {
            out.writeObject(engineManager);
            out.flush();
        }
        catch (IOException e) {
            throw new IOException("Something went wrong while saving the file");
        }
    }

    private History getHistory() {
        return history;
    }

    private WorldDefinition getWorld() {
        return world;
    }

    public void clearPastValues() {
        environmentValuesByUser.clear();
    }
}
