package xml;


import jaxb.schema.generated.*;
import world.worldDefinition.WorldDefinition;
import world.entity.definition.EntityDefinitionImpl;
import world.entity.definition.PropertyDefinition;
import world.environment.definition.EnvironmentDefinition;
import world.rule.RuleImpl;
import world.termination.Termination;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.*;

public  final class XMLReader implements  Serializable {
    private final  String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.schema.generated";
    private  PRDWorld world = null;
    private String xmlPath ;

    public XMLReader(String xmlPath){
        this.xmlPath = xmlPath;
    }
    public  void setWorld(PRDWorld world) {
        this.world = world;
    }

    public  XMLValidation openXmlAndGetData() throws FileNotFoundException, JAXBException {
        InputStream inputStream = new FileInputStream(new File(xmlPath));
        PRDWorld world = deserializeFrom(inputStream);
        setWorld(world);
        return new XMLValidation(world);
    }

    private  PRDWorld deserializeFrom(InputStream inputStream) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(inputStream);
    }

    public WorldDefinition defineWorld()
    {
        Map<String, EntityDefinitionImpl> entityDefinition = defineEntities();
        List<RuleImpl> ruleIml = defineRules();
        Termination termination = defineTermination();
        Map <String, EnvironmentDefinition> environmentDefinition = defineEnvironment();
        return new WorldDefinition(entityDefinition, ruleIml, termination, environmentDefinition,world.getPRDGrid().getRows(), world.getPRDGrid().getColumns(), world.getPRDThreadCount());
    }

    private  Map<String, EnvironmentDefinition> defineEnvironment() {
        Map<String, EnvironmentDefinition> environmentDefinition = new HashMap<>();
        for(PRDEnvProperty environment: world.getPRDEnvironment().getPRDEnvProperty()){
            environmentDefinition.put(environment.getPRDName(), new EnvironmentDefinition(environment));
        }

        return environmentDefinition;
    }

    private  Termination defineTermination()
    {
        PRDBySecond prdBySecond = null;
        PRDByTicks prdByTicks = null;
        for (Object terminationParameter: world.getPRDTermination().getPRDBySecondOrPRDByTicks()){
            if(terminationParameter.getClass().getSimpleName().equals("PRDByTicks")){
                prdByTicks = (PRDByTicks) terminationParameter;
            }
            else if (terminationParameter.getClass().getSimpleName().equals("PRDBySecond")) {
                prdBySecond = (PRDBySecond) terminationParameter;
            }
        }
        return new Termination(prdByTicks, prdBySecond, world.getPRDTermination().getPRDByUser());
    }
    private  List<RuleImpl> defineRules() {
        List<RuleImpl> ruleIml = new ArrayList<>();
        for(PRDRule rule: world.getPRDRules().getPRDRule()){
            ruleIml.add(new RuleImpl(rule));
        }

        return ruleIml;
    }

    private  Map<String, EntityDefinitionImpl> defineEntities()
    {
        Map<String, EntityDefinitionImpl> entityDefinition = new HashMap<>();
        for (PRDEntity entity: world.getPRDEntities().getPRDEntity())
        {
            List<PropertyDefinition> allProperties = defineProperties(entity);
            entityDefinition.put(entity.getName(), new EntityDefinitionImpl(entity.getName(), allProperties));
        }

        return entityDefinition;
    }

    private  List<PropertyDefinition> defineProperties(PRDEntity prdEntity) {
        List<PropertyDefinition> PropertyDefinition = new ArrayList<>();
        for(PRDProperty property: prdEntity.getPRDProperties().getPRDProperty()){
            PropertyDefinition.add(new PropertyDefinition(property));
        }

        return PropertyDefinition;
    }

}
