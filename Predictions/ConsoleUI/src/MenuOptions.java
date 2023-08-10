public enum MenuOptions {
    LOAD_XML("Load and read XML file.", 1),
    SIMULATION_DETAILS("Displaying the simulation details", 2),
    SIMULATION("Running a simulation", 3),
    PAST_ACTIVATION("Displaying full details of past activation", 4),
    EXIT("Exiting the system", 5);

    private final String message;
    private final Integer optionNumber;

    private MenuOptions(String message, Integer optionNumber)
    {
        this.message = message;
        this. optionNumber = optionNumber;
    }

    @Override
    public String toString() {
        return message;
    }

    public Integer GetNUmberOfOption() {return optionNumber;}

    public static Integer GetCountOfOptions() {return MenuOptions.values().length;}

    public static MenuOptions GetOptionByNumber(int num) throws IndexOutOfBoundsException {

        for(MenuOptions option: MenuOptions.values()){
            if (option.optionNumber == num)
                return option;
        }
        throw new IndexOutOfBoundsException();
    }

}
