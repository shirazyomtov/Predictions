import enums.MenuOptions;

public class Menu {
    public String showMenu() {
        StringBuilder menuString = new StringBuilder("Please choose one of the following options:\n");

        for (MenuOptions option : MenuOptions.values()) {
            menuString.append(option.getNumberOfOption()).append(": ").append(option).append("\n");
        }

        return menuString.toString();
    }
}
