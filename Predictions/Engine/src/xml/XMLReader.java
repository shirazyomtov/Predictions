package xml;

import jaxb.schema.generated.*;
import world.World;
import world.entity.definition.EntityDefinitionImpl;
import world.entity.definition.PropertyDefinition;
import world.rule.RuleImpl;
import world.termination.Termination;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class XMLReader {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.schema.generated";
    private static PRDWorld world = null;

    public static void setWorld(PRDWorld world) {
        XMLReader.world = world;
    }

    public static void openXmlAndGetData(String xmlPath) throws FileNotFoundException, JAXBException {
        InputStream inputStream = new FileInputStream(new File(xmlPath));
        PRDWorld world = deserializeFrom(inputStream);
        XMLValidation.setWorld(world);
        setWorld(world);
    }

    private static PRDWorld deserializeFrom(InputStream inputStream) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(inputStream);
    }

    public static World defineWorld()
    {
        PRDBySecond prdBySecond = null;
        PRDByTicks prdByTicks = null;
        Map<String, EntityDefinitionImpl> entityDefinition = defineEntities();
        Map<String, RuleImpl> ruleIml = defineRules();
        for (Object terminationParamter: world.getPRDTermination().getPRDByTicksOrPRDBySecond()){
            if(terminationParamter.getClass().getSimpleName().equals("PRDByTicks")){
                prdByTicks = (PRDByTicks) terminationParamter;
            }
            else if (terminationParamter.getClass().getSimpleName().equals("PRDBySecond")) {
                 prdBySecond = (PRDBySecond) terminationParamter;
            }
        }
        Termination termination = new Termination(prdByTicks, prdBySecond);
        return new World(entityDefinition, ruleIml, termination);
    }

    private static Map<String, RuleImpl> defineRules() {
        Map<String, RuleImpl> ruleIml = new HashMap<>();
        for(PRDRule rule: world.getPRDRules().getPRDRule()){
            ruleIml.put(rule.getName(), new RuleImpl(rule));
        }

        return ruleIml;
    }

    private static Map<String, EntityDefinitionImpl> defineEntities()
    {
        Map<String, EntityDefinitionImpl> entityDefinition = new HashMap<>();
        for (PRDEntity entity: world.getPRDEntities().getPRDEntity())
        {
            List<PropertyDefinition> allProperties = defineProperties(entity);
            entityDefinition.put(entity.getName(), new EntityDefinitionImpl(entity.getName(), entity.getPRDPopulation(), allProperties));
        }

        return entityDefinition;
    }

    private static List<PropertyDefinition> defineProperties(PRDEntity prdEntity) {
        List<PropertyDefinition> PropertyDefinition = new ArrayList<>();
        for(PRDProperty property: prdEntity.getPRDProperties().getPRDProperty()){
            PropertyDefinition.add(new PropertyDefinition(property));
        }

        return PropertyDefinition;
    }

}
