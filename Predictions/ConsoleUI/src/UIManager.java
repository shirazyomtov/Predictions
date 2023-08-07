import exceptions.FilePathException;
import world.World;
import xml.XMLReader;
import xml.XMLValidation;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.WeakHashMap;

public class UIManager {

    private World world = null;
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
        }
    }

    private void simulationDetails() {
        try{
            System.out.println("The information about the simulation defined in the xml file are:");
            System.out.println("1.Entities");
            System.out.println(world.getEntityDefinition());
            System.out.println("2.Rules");
            System.out.println(world.getRules());
            System.out.println(world.getTermination());
        }
       catch (NullPointerException e){
            System.out.println("You cannot see the simulation details before you have loaded the xml file");
        }

    }

    private void loadXML() {
        System.out.println("Enter the full path of the XML file");
        Scanner scanner = new Scanner(System.in);
        String xmlPath = scanner.nextLine();
        try{
            checkValidationXMLPath(xmlPath);
            XMLReader.openXmlAndGetData(xmlPath);
            XMLValidation.checkValidationXmlFile();
            world = XMLReader.defineWorld();
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
