package xml;

import jaxb.schema.generated.*;
import world.World;
import world.entity.definition.EntityDefinition;
import world.entity.definition.PropertyDefinition;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.HashMap;
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
        Map<String, EntityDefinition> entityDefinition = defineEntities();
        return new World(entityDefinition);
    }

    private static Map<String, EntityDefinition> defineEntities()
    {
        Map<String, EntityDefinition> entityDefinition = new HashMap<>();
        for (PRDEntity entity: world.getPRDEntities().getPRDEntity())
        {
            Map<String, PropertyDefinition> allProperties = defineProperties(entity);
            entityDefinition.put(entity.getName(), new EntityDefinition(entity.getName(), entity.getPRDPopulation(), allProperties));
        }

        return entityDefinition;
    }

    private static Map<String, PropertyDefinition> defineProperties(PRDEntity prdEntity) {
        Map<String, PropertyDefinition> PropertyDefinition = new HashMap<>();
        for(PRDProperty property: prdEntity.getPRDProperties().getPRDProperty()){
            PropertyDefinition.put(property.getPRDName(), new PropertyDefinition(property));
        }

        return PropertyDefinition;
    }

}