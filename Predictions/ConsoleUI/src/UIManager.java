

import com.sun.rowset.internal.XmlReaderContentHandler;
import xml.XMLReader;

import javax.sql.rowset.spi.XmlReader;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class UIManager {


    public void RunProgram()
    {
        int option = 0;
        option = chooseOption() ;
        runOptionSelectedByUser(MenuOptions.GetOptionByNumber(option));

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
        }
    }

    private void loadXML() {
        System.out.println("Enter the full path of the xml file");
        Scanner scanner = new Scanner(System.in);
        String xmlPath = scanner.nextLine();
        try{
            XMLReader.openXmlAndGetData();
            XMLReader.checkValidationXmlFile();
            System.out.println("The XML file has been loaded successfully");
        }
        catch (FileNotFoundException | JAXBException  e) {
            System.out.println("g");
        }
    }
}
