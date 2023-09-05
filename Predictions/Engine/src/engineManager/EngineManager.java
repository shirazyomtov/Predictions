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
    Map <String, Integer> entitiesAmountByUser = new HashMap<>();

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
        return new DTOTerminationInfo(world.getTermination().getTicks(), world.getTermination().getSecond(), world.getTermination().getTerminationByUser());
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

    public void checkValidValueAndSetValue(String environmentName, String value) throws IndexOutOfBoundsException, IllegalArgumentException{
        world.checkValidationValue(environmentName, value, environmentValuesByUser);
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
        Map<String, Integer> initAmountOfEntities = createInitAmountOfEntities();
        Map<String, Integer> currentAmountOfEntities = createCurrentAmountOfEntities();
        worldInstance =new WorldInstance(environmentInstanceMap, entityInstanceList, world, initAmountOfEntities, currentAmountOfEntities);
        Simulation simulation = new Simulation(worldInstance, LocalDateTime.now());
        history = History.getInstance();
        history.setCurrentSimulationNumber(numberOfTimesUserSelectSimulation);
        history.addSimulation(simulation);
    }

    private Map<String, Integer> createInitAmountOfEntities() {
        Map<String, Integer> mapOfInitAmountOfEntities = new HashMap<>();
        for(String entityName: entitiesAmountByUser.keySet()){
            mapOfInitAmountOfEntities.put(entityName, entitiesAmountByUser.get(entityName));
        }

        return mapOfInitAmountOfEntities;
    }

    private Map<String, Integer> createCurrentAmountOfEntities() {
        Map<String, Integer> mapOfCurrentAmountOfEntities = new HashMap<>();
        for(String entityName: entitiesAmountByUser.keySet()){
            mapOfCurrentAmountOfEntities.put(entityName, entitiesAmountByUser.get(entityName));
        }

        return mapOfCurrentAmountOfEntities;
    }

    private List<EntityInstance> initEntities() {
        List<EntityInstance> entityInstanceList = new ArrayList<>();
        for (EntityDefinition entityDefinition: world.getEntityDefinition().values()){
            for (int count = 0; count < entitiesAmountByUser.get(entityDefinition.getName()); count++){
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

//    public DTOEntityInfo getQuantityOfEachEntity(int userIntegerInput) {
//        int count = 0;
//        boolean flag = false;
//        Map<String, Integer> entity = new HashMap<>();
//        DTOEntityInfo dtoEntityInfo = null;
//
//        for (EntityInstance entityInstance1 : history.getAllSimulations().get(userIntegerInput).getWorldInstance().getEntityInstanceList()) {
//            flag = true;
//            if (!entity.containsKey(entityInstance1.getName())) {
//                entity.put(entityInstance1.getName(), 1);
//                for (EntityInstance entityInstance2 : history.getAllSimulations().get(userIntegerInput).getWorldInstance().getEntityInstanceList()) {
//                    if (entityInstance2.getName().equals(entityInstance1.getName())) {
//                        count++;
//                    }
//                }
//                int amount = history.getAllSimulations().get(userIntegerInput).getWorldInstance().getWorldDefinition().getEntityDefinition().get(entityInstance1.getName()).getAmountOfPopulation();
//                String name = entityInstance1.getName();
//                dtoEntityInfo = new DTOEntityInfo(amount, count, name, history.getAllSimulations().get(userIntegerInput).getWorldInstance().getWorldDefinition().getEntityDefinition().get(name).getDTOProperties());
//                count = 0;
//            }
//        }
//
//        if (!flag) {
//            for (Map.Entry<String, EntityDefinitionImpl> entityDefinition : history.getAllSimulations().get(userIntegerInput).getWorldInstance().getWorldDefinition().getEntityDefinition().entrySet()) {
//                dtoEntityInfo = new DTOEntityInfo(entityDefinition.getValue().getAmountOfPopulation(), 0, entityDefinition.getKey(), history.getAllSimulations().get(userIntegerInput).getWorldInstance().getWorldDefinition().getEntityDefinition().get(entityDefinition.getKey()).getDTOProperties());
//            }
//        }
//        return dtoEntityInfo;
//    }

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

    public Map<Object, Integer> createPropertyValuesMap(int simulationId, String entityName, String propertyName) {
        WorldInstance currentWorld = history.getAllSimulations().get(simulationId).getWorldInstance();
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
        entitiesAmountByUser.clear();
    }

    public DTOGrid getDTOGridDetails(){return world.createDTOGridDetails();};

    public void setAmountOfEntities(String entityName, int amountOfEntityInstance) throws IndexOutOfBoundsException{
        if(amountOfEntityInstance >= 0) {
            setSpecificEntityAmount(entityName, amountOfEntityInstance);
        }
        else {
            throw new IndexOutOfBoundsException("You need to enter a amount of entity instance that is bigger or equal to 0");
        }
    }

    public void setSpecificEntityAmount(String entityName, int amountOfEntityInstance){
        int currentAmountOfEntity = 0;
        int spaceSize = world.getTwoDimensionalGrid().getRows() * world.getTwoDimensionalGrid().getCols();
        int amountOfAllPopulation = getAmountOfAllEntities();
        int currentAmountOfAllPopulation = amountOfAllPopulation + amountOfEntityInstance;
        if(currentAmountOfAllPopulation > spaceSize){
            throw new IndexOutOfBoundsException("The maximum number of entity instances you can insert is the size of the space which it " + spaceSize);
        }
        else{
            entitiesAmountByUser.put(entityName, amountOfEntityInstance);
        }
    }

    private int getAmountOfAllEntities() {
        int sum = 0;
        for (int value : entitiesAmountByUser.values()) {
            sum += value;
        }
        return sum;
    }

    public Object getValueOfEntity(String environmentName) {
        EnvironmentInstance environmentInstance = environmentValuesByUser.get(environmentName);
        if(environmentInstance != null){
            return environmentInstance.getProperty().getValue();
        }
        return null;
    }

    public int getEntityAmount(String entityName) {
        if (entitiesAmountByUser.containsKey(entityName)) {
            return entitiesAmountByUser.get(entityName);
        }
        else{
            return 0;
        }
    }

    public Integer getCurrentTick(int simulationId){
        return history.getAllSimulations().get(simulationId).getWorldInstance().getCurrentTick();
    }

    public Integer getCurrentSecond(int simulationId){
        return history.getAllSimulations().get(simulationId).getCurrentSecond();
    }
    private Map<String, Integer> getCurrentAmountOfEntities(int simulationId){
        Map<String, Integer> entityCount = new HashMap<>();
        for (EntityInstance entityInstance : history.getAllSimulations().get(simulationId).getWorldInstance().getEntityInstanceList()) {
            String entityName = entityInstance.getName();
            entityCount.put(entityName, entityCount.getOrDefault(entityName, 0) + 1);
        }

        return entityCount;
    }

    public List<DTOEntityInfo> getAllAmountOfEntities(int simulationId) {
        List<DTOEntityInfo> entityInfos = new ArrayList<>();
        Map<String, Integer> entityCount = getCurrentAmountOfEntities(simulationId);

        for(String entityName: history.getAllSimulations().get(simulationId).getWorldInstance().getWorldDefinition().getEntityDefinition().keySet()){
            addDTOEntity(simulationId, entityInfos, entityCount, entityName);
        }

        return entityInfos;
    }

    public List<DTOEntityInfo> getAllDetailsOfEndedSimulation(int simulationId){
        List<DTOEntityInfo> entityInfos = new ArrayList<>();
        Map<String, Integer> entityCount = getCurrentAmountOfEntities(simulationId);


        for(String entityName: entityCount.keySet()){
            addDTOEntity(simulationId, entityInfos, entityCount, entityName);
        }

        return entityInfos;
    }

    private void addDTOEntity(int simulationId, List<DTOEntityInfo> entityInfos, Map<String, Integer> entityCount, String entityName) {
        int initAmount = history.getAllSimulations().get(simulationId).getWorldInstance().getInitAmountOfEntities().get(entityName);
        List<DTOPropertyInfo> dtoPropertyInfos = history.getAllSimulations().get(simulationId).getWorldInstance().getWorldDefinition().getEntityDefinition().get(entityName).getDTOProperties();
        DTOEntityInfo dtoEntityInfo;
        dtoEntityInfo = new DTOEntityInfo(initAmount, entityCount.getOrDefault(entityName, 0), entityName, dtoPropertyInfos);
        entityInfos.add(dtoEntityInfo);
    }

    public List<DTOEnvironmentInfo> getEnvironmentValuesOfChosenSimulation(Integer simulationId){
        environmentValuesByUser = new HashMap<>();
        Map<String, EnvironmentInstance> environmentInstanceMapOfSpecificSimulation = history.getAllSimulations().get(simulationId).getWorldInstance().getEnvironmentInstanceMap();
        for(String environmentName: environmentInstanceMapOfSpecificSimulation.keySet()){
            world.checkValidationValue(environmentName, environmentInstanceMapOfSpecificSimulation.get(environmentName).getProperty().getValue().toString(), environmentValuesByUser);
        }
        return history.getAllSimulations().get(simulationId).getWorldInstance().createListEnvironmentNamesAndValues();
    }

    public List<DTOEntityInfo> getAllAmountOfEntitiesAndSetEntitiesByUser(Integer simulationId) {
        entitiesAmountByUser = history.getAllSimulations().get(simulationId).getWorldInstance().getInitAmountOfEntities();
        return getAllAmountOfEntities(simulationId);
    }
}
