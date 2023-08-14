package enums;

public enum MenuOptions {
    LOAD_XML("Load and read XML file.", 1),
    SIMULATION_DETAILS("Displaying the simulation details", 2),
    SIMULATION("Running a simulation", 3),
    PAST_ACTIVATION("Displaying full details of past activation", 4),

    LOAD_SIMULATIONS_FROM_FILE ("Load simulation from file", 5),

    SAVE_SIMULATIONS_TO_FILE("Save simulation to file", 6),
    EXIT("Exiting the system", 7);

    private final String message;
    final Integer optionNumber;

    private MenuOptions(String message, Integer optionNumber)
    {
        this.message = message;
        this. optionNumber = optionNumber;
    }

    @Override
    public String toString() {
        return message;
    }

    public Integer getNumberOfOption() {return optionNumber;}

    public static Integer getCountOfOptions() {return MenuOptions.values().length;}

    public static MenuOptions getOptionByNumber(int num) throws IndexOutOfBoundsException {

        for(MenuOptions option: MenuOptions.values()){
            if (option.optionNumber == num)
                return option;
        }
        throw new IndexOutOfBoundsException();
    }

}
