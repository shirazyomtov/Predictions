package engineManager;

import DTO.DTOAllWorldsInfo;
import DTO.DTOWorldDefinitionInfo;
import exceptions.NameAlreadyExist;
import threadManager.ThreadManager;
import worldManager.WorldManager;
import xml.XMLReader;
import xml.XMLValidation;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EngineManager {
    private Map<String, WorldManager> worldManagerMap = new HashMap<>();

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


//    public ThreadManager getThreadManager() {
//        return threadManager;
//    }
}
