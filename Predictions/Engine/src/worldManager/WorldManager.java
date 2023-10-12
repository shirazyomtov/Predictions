package worldManager;

import DTO.*;
import exceptions.*;
import history.History;
import history.simulation.Simulation;
import world.entity.definition.EntityDefinition;
import world.entity.definition.EntityDefinitionImpl;
import world.entity.definition.PropertyDefinition;
import world.entity.instance.EntityInstance;
import world.entity.instance.location.Location;
import world.enums.Type;
import world.environment.definition.EnvironmentDefinition;
import world.environment.instance.EnvironmentInstance;
import world.propertyInstance.api.Property;
import world.propertyInstance.impl.BooleanPropertyInstance;
import world.propertyInstance.impl.FloatPropertyInstance;
import world.propertyInstance.impl.IntegerPropertyInstance;
import world.propertyInstance.impl.StringPropertyInstance;
import world.termination.Termination;
import world.value.generator.api.ValueGeneratorFactory;
import world.worldDefinition.WorldDefinition;
import world.worldInstance.WorldInstance;
import xml.XMLReader;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class WorldManager implements Serializable{
    private History history = null;

    private String worldName;

    private WorldDefinition world = null;

    private WorldInstance worldInstance = null;

    Map<String, Map<Integer, Map <String, EnvironmentInstance>>>  environmentValuesByUser = new HashMap<>();
    Map<String, Map<Integer, Map <String, Integer>>>  entitiesAmountByUser = new HashMap<>();

    public String getWorldName() {
        return worldName;
    }

    public void loadXMLAAndCheckValidation(XMLReader xmlReader) throws Exception {
        world = xmlReader.defineWorld();
        history = new History();
//        numberOfTimesUserSelectSimulation = 0;
        worldName = xmlReader.getWorld().getName();
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

//    public DTOTerminationInfo getTerminationDetails(){
//        return new DTOTerminationInfo(world.getTermination().getTicks(), world.getTermination().getSecond(), world.getTermination().getTerminationByUser());
//    }

    public int getAmountOfEnvironmentDefinition() throws NullPointerException{
        return world.getEnvironmentDefinition().size();
    }

    public DTOWorldDefinitionInfo getWorldDefinition(){
        return new DTOWorldDefinitionInfo(worldName, getEntitiesDetails(), getRulesDetails(), getEnvironmentNamesList(), getDTOGridDetails());
    }
    public List<DTOEnvironmentInfo> getEnvironmentNamesList() {
        return world.createListEnvironmentDetails();
    }

    public DTOEnvironmentInfo getEnvironmentDetails(int userIntegerInput){
        return getEnvironmentNamesList().get(userIntegerInput -1);
    }

    public void checkValidValueAndSetValue(String environmentName, String value, String userName, Integer executeID) throws IndexOutOfBoundsException, IllegalArgumentException{
        world.checkValidationValue(environmentName, value, environmentValuesByUser.get(userName).get(executeID));
    }

    public void setRandomValuesOfEnvironments(String userName, Integer executeID){
        if(environmentValuesByUser.containsKey(userName)){
            if(!environmentValuesByUser.get(userName).containsKey(executeID)){
                environmentValuesByUser.get(userName).put(executeID, new HashMap<>());
            }
        }
        else{
            environmentValuesByUser.put(userName, new HashMap<>());
            environmentValuesByUser.get(userName).put(executeID, new HashMap<>());
        }
        setRandomEnvironmentValues(userName, executeID);
    }

    public void setFinalAmountOfEntities(String userName, Integer executeID){
        if(entitiesAmountByUser.containsKey(userName)){
            if(!entitiesAmountByUser.get(userName).containsKey(executeID)){
                entitiesAmountByUser.get(userName).put(executeID, new HashMap<>());
            }
        }
        else{
            entitiesAmountByUser.put(userName, new HashMap<>());
            entitiesAmountByUser.get(userName).put(executeID, new HashMap<>());
        }
        Map<String, EntityDefinitionImpl> entityDefinitionMap = world.getEntityDefinition();
        for(String entityName: entityDefinitionMap.keySet()){
            if(!entitiesAmountByUser.get(userName).get(executeID).containsKey(entityName)){
                entitiesAmountByUser.get(userName).get(executeID).put(entityName, 0);
            }
        }
    }

    public DTOEntitiesAndEnvironmentInfo getSummaryDetails(String userName, Integer executeID){
        List<DTOEntityInfo> dtoEntityInfos = createSummaryEntities(userName, executeID);
        List<DTOEnvironmentInfo> dtoEnvironmentInfoList = createSummaryEnvironment(userName, executeID);
        return new DTOEntitiesAndEnvironmentInfo(dtoEntityInfos, dtoEnvironmentInfoList);
    }


    private List<DTOEntityInfo> createSummaryEntities(String userName, Integer executeID) {
        List<DTOEntityInfo> dtoEntityInfos = new ArrayList<>();
        for(String entityName: entitiesAmountByUser.get(userName).get(executeID).keySet()){
            dtoEntityInfos.add(new DTOEntityInfo(entitiesAmountByUser.get(userName).get(executeID).get(entityName), entitiesAmountByUser.get(userName).get(executeID).get(entityName), entityName, null));
        }

        return dtoEntityInfos;
    }


    private List<DTOEnvironmentInfo> createSummaryEnvironment(String userName, Integer executeID) {
        List<DTOEnvironmentInfo> dtoEnvironmentInfo = new ArrayList<>();
        for(String environmentName: environmentValuesByUser.get(userName).get(executeID).keySet()){
            dtoEnvironmentInfo.add(new DTOEnvironmentInfo(environmentName, environmentValuesByUser.get(userName).get(executeID).get(environmentName).getProperty().getValue().toString()));
        }

        return dtoEnvironmentInfo;
    }

    private void setRandomEnvironmentValues(String userName, Integer executeID) {
        for (String environmentName: world.getEnvironmentDefinition().keySet()){
            if(!environmentValuesByUser.get(userName).get(executeID).containsKey(environmentName)){
                provideRandomValues(world.getEnvironmentDefinition().get(environmentName), environmentValuesByUser.get(userName).get(executeID));
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

    public  Simulation setSimulationDetailsAndAddToHistory(Integer currentSimulationId, String userName, Integer requestID, Integer executeID, Termination termination) {
        List<EntityInstance> entityInstanceList = initEntities(userName, executeID);
        Map<String, Integer> initAmountOfEntities = createInitAmountOfEntities(userName, executeID);
        Map<String, Integer> currentAmountOfEntities = createInitAmountOfEntities(userName, executeID);
        worldInstance =new WorldInstance(environmentValuesByUser.get(userName).get(executeID), entityInstanceList, world, initAmountOfEntities, currentAmountOfEntities, world.getRows(), world.getCols());
        worldInstance.initLocation();
        Simulation simulation = new Simulation(worldInstance, LocalDateTime.now(), userName, requestID, termination);
        history.setCurrentSimulationNumber(currentSimulationId);
        history.addSimulation(simulation);
        return history.getSimulation();
    }


    private Map<String, Integer> createInitAmountOfEntities(String userName, Integer executeID) {
        Map<String, Integer> mapOfInitAmountOfEntities = new HashMap<>();
        for(String entityName: entitiesAmountByUser.get(userName).get(executeID).keySet()){
            mapOfInitAmountOfEntities.put(entityName, entitiesAmountByUser.get(userName).get(executeID).get(entityName));
        }

        return mapOfInitAmountOfEntities;
    }

    private List<EntityInstance> initEntities(String userName, Integer executeID) {
        List<EntityInstance> entityInstanceList = new ArrayList<>();
        for (EntityDefinition entityDefinition: world.getEntityDefinition().values()){
            for (int count = 0; count < entitiesAmountByUser.get(userName).get(executeID).getOrDefault(entityDefinition.getName(), 0); count++){
                Map<String, Property> allProperty = new HashMap<>();
                for(PropertyDefinition propertyDefinition: entityDefinition.getProps()){
                    allProperty.put(propertyDefinition.getName(), initProperty(propertyDefinition));
                }
                entityInstanceList.add(new EntityInstance(entityDefinition.getName(), allProperty, new Location(0, 0)));
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
        String stringValue;
        if(propertyDefinition.isRandomInitialize()) {
            property = new BooleanPropertyInstance(propertyDefinition.getName(), ValueGeneratorFactory.createRandomBoolean(), propertyDefinition.getRange());
        }
        else{
            stringValue = (String) propertyDefinition.getInit();
            property = new BooleanPropertyInstance(propertyDefinition.getName(),
                    ValueGeneratorFactory.createFixed(Boolean.parseBoolean(stringValue)), propertyDefinition.getRange());
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

    public void removeSimulationFromHistory(){
        history.removeCurrentSimulation();
    }

    public void checkIfThereSimulation() throws NullPointerException{
        if(history.getAllSimulations().isEmpty()){
            throw new NullPointerException("You need to run at least one proper simulation ");
        }
    }

    public int getSortedMapSize(){
        return history.getSortMapOfSimulations().size();
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

    public Map<Object, Integer> createPropertyValuesMap(int simulationId, String entityName, String propertyName) {
        Simulation simulation = history.getAllSimulations().get(simulationId);
        return simulation.getPropertyValuesMapPerTick(entityName, propertyName);

    }

    public History getHistory() {
        return history;
    }

    private WorldDefinition getWorld() {
        return world;
    }

    public void clearPastValues(String userName, Integer executeID) {
        if(environmentValuesByUser.containsKey(userName) && environmentValuesByUser.get(userName).containsKey(executeID)){
            environmentValuesByUser.get(userName).get(executeID).clear();
        }
        if(entitiesAmountByUser.containsKey(userName) && entitiesAmountByUser.get(userName).containsKey(executeID)) {
            entitiesAmountByUser.get(userName).get(executeID).clear();
        }
    }

    public DTOGrid getDTOGridDetails(){return world.createDTOGridDetails();};

    public void setAmountOfEntities(String entityName, int amountOfEntityInstance, String userName, Integer executeID) throws IndexOutOfBoundsException{
        if(amountOfEntityInstance >= 0) {
            setSpecificEntityAmount(entityName, amountOfEntityInstance, userName, executeID);
        }
        else {
            throw new IndexOutOfBoundsException("You need to enter a amount of entity instance that is bigger or equal to 0");
        }
    }

    public void setSpecificEntityAmount(String entityName, int amountOfEntityInstance, String userName, Integer executeID){
        int currentAmountOfEntity = entitiesAmountByUser.get(userName).get(executeID).getOrDefault(entityName, 0);
        int spaceSize = world.getRows() * world.getCols();
        int amountOfAllPopulation = getAmountOfAllEntities(userName, executeID);
        amountOfAllPopulation -= currentAmountOfEntity;
        int currentAmountOfAllPopulation = amountOfAllPopulation + amountOfEntityInstance;
        if(currentAmountOfAllPopulation > spaceSize){
            throw new IndexOutOfBoundsException("The maximum number of entity instances you can insert is the size of the space which it " + spaceSize);
        }
        else{
            entitiesAmountByUser.get(userName).get(executeID).put(entityName, amountOfEntityInstance);
        }
    }

    private int getAmountOfAllEntities(String userName, Integer executeID) {
        int sum = 0;
        for (int value : entitiesAmountByUser.get(userName).get(executeID).values()) {
            sum += value;
        }
        return sum;
    }

    public Object getValueOfEnvironment(String environmentName, String userName, Integer executeID) {
        if(environmentValuesByUser.containsKey(userName)){
            if(!environmentValuesByUser.get(userName).containsKey(executeID)){
                environmentValuesByUser.get(userName).put(executeID, new HashMap<>());
            }
        }
        else{
            environmentValuesByUser.put(userName, new HashMap<>());
            environmentValuesByUser.get(userName).put(executeID, new HashMap<>());
        }
        EnvironmentInstance environmentInstance = environmentValuesByUser.get(userName).get(executeID).get(environmentName);
        if(environmentInstance != null){
            return environmentInstance.getProperty().getValue();
        }
        return null;
    }

    public int getEntityAmount(String entityName, String userName, Integer executeID) {
        if(entitiesAmountByUser.containsKey(userName)){
            if(!entitiesAmountByUser.get(userName).containsKey(executeID)){
                entitiesAmountByUser.get(userName).put(executeID, new HashMap<>());
            }
        }
        else{
            entitiesAmountByUser.put(userName, new HashMap<>());
            entitiesAmountByUser.get(userName).put(executeID, new HashMap<>());
        }
        if (entitiesAmountByUser.get(userName).get(executeID).containsKey(entityName)) {
            return entitiesAmountByUser.get(userName).get(executeID).get(entityName);
        }
        else{
            return 0;
        }
    }

    public Integer getCurrentTick(int simulationId){
        return history.getAllSimulations().get(simulationId).getCurrentTick();
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
            addDTOEntityToList(simulationId, entityInfos, entityCount, entityName);
        }

        return entityInfos;
    }

    public List<DTOEntityInfo> getAllDetailsOfEndedSimulation(int simulationId){
        List<DTOEntityInfo> entityInfos = new ArrayList<>();
        Map<String, Integer> entityCount = getCurrentAmountOfEntities(simulationId);


        for(String entityName: entityCount.keySet()){
            addDTOEntityToList(simulationId, entityInfos, entityCount, entityName);
        }

        return entityInfos;
    }

    private void addDTOEntityToList(int simulationId, List<DTOEntityInfo> entityInfos, Map<String, Integer> entityCount, String entityName) {
        int initAmount = history.getAllSimulations().get(simulationId).getWorldInstance().getInitAmountOfEntities().getOrDefault(entityName, 0);
        List<DTOPropertyInfo> dtoPropertyInfos = history.getAllSimulations().get(simulationId).getWorldInstance().getWorldDefinition().getEntityDefinition().get(entityName).getDTOProperties();
        DTOEntityInfo dtoEntityInfo;
        dtoEntityInfo = new DTOEntityInfo(initAmount, entityCount.getOrDefault(entityName, 0), entityName, dtoPropertyInfos);
        entityInfos.add(dtoEntityInfo);
    }

    public List<DTOEnvironmentInfo> getEnvironmentValuesOfChosenSimulation(Integer simulationId){
        return history.getAllSimulations().get(simulationId).getWorldInstance().createListEnvironmentNamesAndValues();
    }

    public List<DTOEntityInfo> getAllAmountOfEntitiesAndSetEntitiesByUser(Integer simulationId) {
        return null;
//        entitiesAmountByUser = new HashMap<>();
//        Map <String, Integer> initAmountOfEntities = history.getAllSimulations().get(simulationId).getWorldInstance().getInitAmountOfEntities();
//        for(String entityName: initAmountOfEntities.keySet()){
//            entitiesAmountByUser.put(entityName, initAmountOfEntities.get(entityName));
//        }
//        return getAllAmountOfEntities(simulationId);
    }

    public DTOWorldInfo getDTOWorldInfo(int simulationId) {
        Simulation simulation = history.getAllSimulations().get(simulationId);
        List<DTOEntityInfo> amountOfEntities = getCurrentEntities(simulationId);
        List<DTOEnvironmentInfo> environmentInfos = getEnvironmentValuesOfChosenSimulation(simulationId);
        return new DTOWorldInfo(amountOfEntities, simulation.getCurrentTick(), simulation.getCurrentSecond(), simulation.getIsFinish(), simulation.getIsFailed(), simulation.getMessage(), environmentInfos);
    }

    public List<DTOEntityInfo> getCurrentEntities(int simulationId) {
        List<DTOEntityInfo> entityInfos = new ArrayList<>();
        Map<String, Integer> entityCount = history.getAllSimulations().get(simulationId).getWorldInstance().getCurrentAmountOfEntities();

        for(String entityName: history.getAllSimulations().get(simulationId).getWorldInstance().getWorldDefinition().getEntityDefinition().keySet()){
            addDTOEntityToList(simulationId, entityInfos, entityCount, entityName);
        }

        return entityInfos;
    }

    public List<DTOEntityInfo> getInitAmountOfEntitiesAndSetEntitiesByUser(Integer simulationId) {
        return null;
//        entitiesAmountByUser = new HashMap<>();
//        Map <String, Integer> initAmountOfEntities = history.getAllSimulations().get(simulationId).getWorldInstance().getInitAmountOfEntities();
//        for(String entityName: initAmountOfEntities.keySet()){
//            entitiesAmountByUser.put(entityName, initAmountOfEntities.get(entityName));
//        }
//        return getAllAmountOfEntities(simulationId);
    }

    public Map<Integer, Map<String, Integer>> getAmountOfEntitiesPerTick(Integer simulationId) {
        return history.getAllSimulations().get(simulationId).getWorldInstance().getAmountOfEntitiesPerTick();
    }

    public List<DTOEntityInfo> getCurrentTickAmountOfEntities(Integer simulationId, Integer tick){
        List<DTOEntityInfo> dtoEntityInfos = new ArrayList<>();
        try {
            Map<String, Integer> entityCount = getAmountOfEntitiesPerTick(simulationId).get(tick);
            for (String entityName : entityCount.keySet()) {
                addDTOEntityToList(simulationId, dtoEntityInfos, entityCount, entityName);
            }
        }
        catch (Exception e){
            Map<String, Integer> entityCount = getTheHigherKey(getAmountOfEntitiesPerTick(simulationId));
            for (String entityName : entityCount.keySet()) {
                addDTOEntityToList(simulationId, dtoEntityInfos, entityCount, entityName);
            }
        }
        return dtoEntityInfos;
    }

    private Map<String, Integer> getTheHigherKey(Map<Integer, Map<String, Integer>> map){
        int highestKey = 0;

        for (Integer key : map.keySet()) {
            if (key > highestKey) {
                highestKey = key;
            }
        }

        return map.get(highestKey);
    }

    public Float getAverageTickOfSpecificProperty(Integer simulationId, String entityName, String propertyName){
        return history.getAllSimulations().get(simulationId).getAverageTickValueOfSpecificProperty(entityName, propertyName);
    }

    public void pause(Integer simulationId) {
        Simulation simulation = history.getAllSimulations().get(simulationId);
        simulation.setPause(true);
    }

    public void resume(Integer simulationId) {
        Simulation simulation = history.getAllSimulations().get(simulationId);
        synchronized(simulation) {
            simulation.setPause(false);
            simulation.notifyAll();
        }
    }

    public void stop(Integer simulationId){
        Simulation simulation = history.getAllSimulations().get(simulationId);
        synchronized(simulation) {
            simulation.setStop(true);
            simulation.setPause(false);
            simulation.notifyAll();
        }

    }

    public Map<Integer, Integer> getAllSecondsPerTick(Integer simulationId){
        return history.getAllSimulations().get(simulationId).getWorldInstance().getSecondsPerTick();
    }

    public DTOEntitiesAndEnvironmentInfo getEntitiesAndEnvironmentsInfo(){
        List<DTOEnvironmentInfo> dtoEnvironmentInfoList = getEnvironmentNamesList();
        List<DTOEntityInfo> dtoEntityInfos = getEntitiesDetails();
        return new DTOEntitiesAndEnvironmentInfo(dtoEntityInfos, dtoEnvironmentInfoList);
    }

    public boolean checkIfPropertyIsFloat(Integer simulationId, String entityName, String propertyName) {
        EntityInstance entityInstance = history.getAllSimulations().get(simulationId).getWorldInstance().isEntityExists(entityName);
        Type type = entityInstance.getAllProperty().get(propertyName).getType();
        return type.equals(Type.FLOAT);
    }
}
