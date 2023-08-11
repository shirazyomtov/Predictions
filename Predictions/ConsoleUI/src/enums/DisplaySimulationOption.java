package enums;

public enum DisplaySimulationOption {
    DISPLAYBYQUANTITY("According to quantity.", 1),
    DISPLAYBYHISTOGRMOFPROPERTY ("According to the histogram of property", 2);

    private final String message;
    private final Integer optionNumber;

    private DisplaySimulationOption(String message, Integer optionNumber)
    {
        this.message = message;
        this. optionNumber = optionNumber;
    }

    public Integer getOptionNumber() {
        return optionNumber;
    }

    @Override
    public String toString() {
        return message;
    }

    public static DisplaySimulationOption getOptionByNumber(int num) throws IndexOutOfBoundsException {

        for(DisplaySimulationOption option: DisplaySimulationOption.values()){
            if (option.optionNumber == num)
                return option;
        }
        throw new IndexOutOfBoundsException();
    }
}
