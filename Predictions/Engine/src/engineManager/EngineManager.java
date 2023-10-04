package engineManager;

import DTO.*;
import allocations.Allocation;
import allocations.Allocations;
import allocations.StatusRequest;
import exceptions.NameAlreadyExist;
import threadManager.ThreadManager;
import worldManager.WorldManager;
import xml.XMLReader;
import xml.XMLValidation;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EngineManager {
    private Map<String, WorldManager> worldManagerMap = new HashMap<>();
    private Allocations allocations = new Allocations();
    private ThreadManager threadManager;

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

//    public ThreadManager getThreadManager() {
//        return threadManager;
//    }
}
