import enums.MenuOptions;

public class Menu {
    public void ShowMenu()
    {
        System.out.println("Please choose one of the following options:");

        for (MenuOptions option : MenuOptions.values()) {
            System.out.println(option.getNumberOfOption() + ":" + option);
        }
    }
}
