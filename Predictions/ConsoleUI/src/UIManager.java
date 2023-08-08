import exceptions.FilePathException;
import world.World;
import world.entity.definition.EntityDefinitionImpl;
import world.entity.definition.EntityDefiniton;
import world.rule.RuleImpl;
import xml.XMLReader;
import xml.XMLValidation;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.WeakHashMap;

public class UIManager {

    private World world = null;
    private XMLReader xmlReader = null;
    private XMLValidation xmlValidation = null;
    public void RunProgram()
    {
        int option = 0;
        while(option != MenuOptions.EXIT.GetNUmberOfOption()) {
            option = chooseOption();
            runOptionSelectedByUser(MenuOptions.GetOptionByNumber(option));
        }
    }

    private int chooseOption()
    {
        Menu menu = new Menu();
        boolean validInput = false;
        int userIntegerInput = 0;
        Scanner scanner = new Scanner(System.in);

        do {
            try{
                menu.ShowMenu();
                userIntegerInput = Integer.parseInt(scanner.nextLine());
                MenuOptions.GetOptionByNumber(userIntegerInput);
                validInput = true;
            }
            catch (NumberFormatException  | IndexOutOfBoundsException exception){
                System.out.println("Invalid input. Please insert a number between 1 and " + MenuOptions.GetCountOfOptons() + ".");
            }
        }while (!validInput);

        return  userIntegerInput;
    }

    private void runOptionSelectedByUser(MenuOptions option) {
        switch (option) {
            case LOAD_XML:
                loadXML();
                break;
            case SIMULATION_DETAILS:
                simulationDetails();
                break;
            case  SIMULATION:
                simulation();
                break;
        }
    }

    private void simulation() {
        try {
            if (world != null) {
                System.out.println("These are all the environment variables defined in the xml file");
            }
            System.out.println(world.getEnvironmentDefinition());
        }
        catch (NullPointerException e){
            System.out.println("You cannot run the simulation before loading the xml file");
        }
    }

    private void simulationDetails() {
        try{
            if (world != null) {
                System.out.println("The information about the simulation defined in the xml file are:");
                printEntitiesDetalis();
                printRulesDetalis();
                printTerminationDetalis();
            }
        }
       catch (NullPointerException e){
            System.out.println("You cannot see the simulation details before you have loaded the xml file");
        }

    }

    private void printTerminationDetalis() {
        System.out.println("3.Termination:");
        System.out.println(world.getTermination());
    }

    private void printRulesDetalis() {
        System.out.println("2.Rules:");
        Map<String, RuleImpl> entityDefinitions = world.getRules();
        for (Map.Entry<String, RuleImpl> entry : entityDefinitions.entrySet()) {
            System.out.println(entry.getValue());
        }

    }

    private void printEntitiesDetalis() {
        System.out.println("1.Entities:");
        Map<String, EntityDefinitionImpl> entityDefinitions = world.getEntityDefinition();
        for (Map.Entry<String, EntityDefinitionImpl> entry : entityDefinitions.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    private void loadXML() {
        System.out.println("Enter the full path of the XML file");
        Scanner scanner = new Scanner(System.in);
        String xmlPath = scanner.nextLine();
        xmlReader = new XMLReader(xmlPath);
        try{
            checkValidationXMLPath(xmlPath);
            xmlValidation = xmlReader.openXmlAndGetData();
            xmlValidation.checkValidationXmlFile();
            world = xmlReader.defineWorld();
            System.out.println("The XML file has been loaded successfully");
        }
        catch (FileNotFoundException | JAXBException  e) {
            //check JAXBException
            System.out.println("File has not been found in this path " + xmlPath + ".");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void checkValidationXMLPath(String path) throws FilePathException {
         if (path.length() <= 4) {
            throw new FilePathException(FilePathException.ErrorType.FILE_NAME_CONTAINS_LESS_THAN_4_CHARACTERS);
        } else if (!path.endsWith(".xml")) {
            throw new FilePathException(FilePathException.ErrorType.NOT_ENDS_WITH_XML);
        }

    }
}
