package xml;

import exceptions.NameAlreadyExist;
import exceptions.ObjectNotExist;
import jaxb.schema.generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.HashMap;

import static java.lang.Float.parseFloat;
import static world.enums.AuxiliaryFunction.checkOptionByFunctionName;


public final class XMLReader {
    //maybe static
    private static String xmlPath = "src/resources/check/master-ex1.xml";

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.schema.generated";
    private static PRDWorld world = null;

    public XMLReader() {
    }

    public static void setWorld(PRDWorld world) {
        XMLReader.world = world;
    }

    public static void openXmlAndGetData() throws FileNotFoundException, JAXBException {
        InputStream inputStream = new FileInputStream(new File(xmlPath));
        setWorld(deserializeFrom(inputStream));
    }

    private static PRDWorld deserializeFrom(InputStream inputStream) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(inputStream);
    }

    public static void checkValidationXmlFile() throws NameAlreadyExist, ObjectNotExist {
        checkEnvironmentVariables();
        checkPropertyVariablesOfSpecificEntity(world.getPRDEntities().getPRDEntity().get(0));
        checkActionsEntitiesNames();
        checkArgumentThatGivenToCalculationIncreaseDecrease();
    }

    private static void checkEnvironmentVariables() throws NameAlreadyExist {
        HashMap<String, Integer> map = new HashMap<>();
        for (PRDEnvProperty prdEnvironment : world.getPRDEvironment().getPRDEnvProperty()) {
            if (map.containsKey(prdEnvironment.getPRDName())) {
                throw new NameAlreadyExist((prdEnvironment.getPRDName()), "enviroment");
            } else {
                map.put(prdEnvironment.getPRDName(), 1);
            }
        }
    }

    private static void checkPropertyVariablesOfSpecificEntity(PRDEntity prdEntity) throws NameAlreadyExist {
        HashMap<String, Integer> map = new HashMap<>();
        for (PRDProperty prdProperty : prdEntity.getPRDProperties().getPRDProperty()) {
            if (map.containsKey(prdProperty.getPRDName())) {
                throw new NameAlreadyExist((prdProperty.getPRDName()), "property");
            } else {
                map.put(prdProperty.getPRDName(), 1);
            }
        }
    }

    private static void checkActionsEntitiesNames() throws ObjectNotExist {
        for (PRDRule rule : world.getPRDRules().getPRDRule()) {
            for (PRDAction action : rule.getPRDActions().getPRDAction()) {
                if (!action.getEntity().equals(world.getPRDEntities().getPRDEntity().get(0).getName())) {
                    throw new ObjectNotExist(action.getEntity(), "Entity");
                }
            }

        }

    }

    private static void checkArgumentThatGivenToCalculationIncreaseDecrease() throws
    {
        for (PRDRule rule : world.getPRDRules().getPRDRule()) {
            for (PRDAction action : rule.getPRDActions().getPRDAction()) {
               if(action.getType().equals("decrease") || action.getType().equals("increase")){
                   if(checkOptionByFunctionName(action.getBy())){

                   }

                   try
                   {
                        parseFloat(action.getBy());
                   }
                   catch(NumberFormatException e){
                       throw new
                   }
               }
            }

        }

    }
}
