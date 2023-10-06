package engineManager;

import DTO.*;
import allocations.Allocation;
import allocations.Allocations;
import exceptions.NameAlreadyExist;
import history.History;
import history.simulation.Simulation;
import threadManager.ThreadManager;
import world.termination.Termination;
import worldManager.WorldManager;
import xml.XMLReader;
import xml.XMLValidation;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class EngineManager {
    private Map<String, WorldManager> worldManagerMap = new HashMap<>();
    private Allocations allocations = new Allocations();
    private ThreadManager threadManager;


    public EngineManager() {
      this.threadManager = new ThreadManager(1);
    }

    public DTOAllWorldsInfo getDTOAllWorlds(){
        Map<String, DTOWorldDefinitionInfo> dtoWorldDefinitionInfoMap = new HashMap<>();
        for(String worldName: worldManagerMap.keySet()){
            dtoWorldDefinitionInfoMap.put(worldName, worldManagerMap.get(worldName).getWorldDefinition());
        }
        return new DTOAllWorldsInfo(dtoWorldDefinitionInfoMap);
    }

    public DTOWorldDefinitionInfo loadXMLAAndCheckValidation(InputStream xmlContent) throws Exception {
        try {
            XMLReader xmlReader = new XMLReader(xmlContent);
            XMLValidation xmlValidation;
            xmlValidation = xmlReader.openXmlAndGetData();
            xmlValidation.checkValidationXmlFile();
            WorldManager worldManager = new WorldManager();
            worldManager.loadXMLAAndCheckValidation(xmlReader);
            if(worldManagerMap.containsKey(worldManager.getWorldName())){
                throw new NameAlreadyExist("world", worldManager.getWorldName());
            }
            worldManagerMap.put(worldManager.getWorldName(), worldManager);
            return worldManager.getWorldDefinition();
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void getWorldManager(String name){
        worldManagerMap.get(name);
    }

    public void addAllocation(String simulationName, String numberOfSimulationRun, String ticks, String second, String byUser, String username){
        allocations.addAllocation(simulationName, numberOfSimulationRun, ticks, second, byUser, username);
    }

    public DTOAllRequests getAllRequest(){
        Map<Integer, DTORequestsOfSimulations> allRequest = new HashMap<>();
        for (Integer requestID : allocations.getAllAllocation().keySet()){
            Allocation allocation = allocations.getAllAllocation().get(requestID);
            String status = allocation.getStatusRequest().toString();
            DTOTerminationInfo dtoTerminationInfo = new DTOTerminationInfo(allocation.getTermination().getTicks(), allocation.getTermination().getSecond(), allocation.getTermination().getTerminationByUser());
            allRequest.put(requestID, new DTORequestsOfSimulations(requestID, allocation.getUserName(), allocation.getSimulationName(), allocation.getNumberOfSimulationRun(), dtoTerminationInfo, status, allocation.getNumberOfRunningSimulationNow(), allocation.getNumberFinishSimulation()));
        }
        return  new DTOAllRequests(allRequest);
    }

    public DTOAllRequestsByUser getAllRequestByUser(String userName){
        List<DTORequestsOfSimulations> allRequestByUser = new ArrayList<>();
        for (Integer requestID : allocations.getAllAllocation().keySet()){
            Allocation allocation = allocations.getAllAllocation().get(requestID);
            String status = allocation.getStatusRequest().toString();
            if((status.equals("APPROVED") || status.equals("DECLINED")) && userName.equals(allocation.getUserName())) {
                DTOTerminationInfo dtoTerminationInfo = new DTOTerminationInfo(allocation.getTermination().getTicks(), allocation.getTermination().getSecond(), allocation.getTermination().getTerminationByUser());
                allRequestByUser.add(new DTORequestsOfSimulations(requestID, allocation.getUserName(), allocation.getSimulationName(), allocation.getNumberOfSimulationRun(), dtoTerminationInfo, status, allocation.getNumberOfRunningSimulationNow(), allocation.getNumberFinishSimulation()));
            }
        }
        return  new DTOAllRequestsByUser(allRequestByUser);
    }
    public void updateRequestStatus(String requestId, String status) {
        Integer requestIdInt = Integer.parseInt(requestId);
        Allocation allocation = allocations.getAllAllocation().get(requestIdInt);
        allocation.updateStatus(status);
    }

    public DTOEntitiesAndEnvironmentInfo getEntitiesAndEnvironment(String worldName){
        WorldManager worldManager = worldManagerMap.get(worldName);
        return worldManager.getEntitiesAndEnvironmentsInfo();
    }

    public Integer getCurrentAmountOfEntity(String worldName, String entityName, String userName, Integer executeID){
        return worldManagerMap.get(worldName).getEntityAmount(entityName, userName, executeID);
    }

    public void setAmountOfEntity(String worldName, String entityName, String amount, String userName, Integer executeID){
        Integer amountOfEntityInt = Integer.parseInt(amount);
        worldManagerMap.get(worldName).setAmountOfEntities(entityName, amountOfEntityInt, userName, executeID);
    }

    public Object getCurrentValueOfEnvironment(String worldName, String environmentName, String userName, Integer executeID){
        return worldManagerMap.get(worldName).getValueOfEnvironment(environmentName, userName, executeID);
    }

    public void setCurrentValueOfEnvironment(String worldName, String environmentName, String valueOfEnvironment, String userName, Integer executeID){
        worldManagerMap.get(worldName).checkValidValueAndSetValue(environmentName, valueOfEnvironment, userName, executeID);
    }

    public void clearPastValuesOfEntitiesAndEnvironments(String worldName, String userName, Integer executeID){
        worldManagerMap.get(worldName).clearPastValues(userName, executeID);
    }

    public DTOEntitiesAndEnvironmentInfo getFinalDetailsSummaryPage(String worldName, String userName, Integer executeID) {
         worldManagerMap.get(worldName).setRandomValuesOfEnvironments(userName, executeID);
         worldManagerMap.get(worldName).setFinalAmountOfEntities(userName, executeID);
         return worldManagerMap.get(worldName).getSummaryDetails(userName, executeID);
    }

    public void defineSimulation(String worldName, String userName, Integer requestID, Integer executeID){
        Termination termination = allocations.getAllAllocation().get(requestID).getTermination();
        Simulation simulation = worldManagerMap.get(worldName).setSimulationDetailsAndAddToHistory(userName, requestID, executeID, termination);
        threadManager.executeSimulation(simulation);
    }

    public void setThreadCount(Integer threadCount) {
        threadManager.setThreadExecutor(threadCount);
    }

    public DTOAllSimulations getDetailsAboutEndSimulation(String userName){
        List<DTOSimulationInfo> detailsAboutEndSimulation = new ArrayList<>();
        History history;
        for(String worldName: worldManagerMap.keySet()) {
            history = worldManagerMap.get(worldName).getHistory();
            for (Integer simulationId : history.getAllSimulations().keySet()) {
                Simulation simulation = history.getAllSimulations().get(simulationId);
                if(simulation.getUserName().equals(userName)) {
                    detailsAboutEndSimulation.add(new DTOSimulationInfo(worldName, simulationId, simulation.getFormattedDateTime(),
                            simulation.getIsFinish(), simulation.getIsFailed(), simulation.getMessage()));
                }
            }
        }
        return new DTOAllSimulations(detailsAboutEndSimulation);
    }

    public DTOWorldInfo getDTOWorldInfo(String worldName, Integer simulationID){
       return  worldManagerMap.get(worldName).getDTOWorldInfo(simulationID);
    }

    public void stop(String worldName, Integer simulationId){
        worldManagerMap.get(worldName).stop(simulationId);
    }
    public void resume(String worldName, Integer simulationId){
        worldManagerMap.get(worldName).resume(simulationId);
    }
    public void pause(String worldName, Integer simulationId){
        worldManagerMap.get(worldName).pause(simulationId);
    }
}
