package world.enums;

public enum AuxiliaryFunction {
    ENVIRONMENT("environment"),

    RANDOM("random");

    private final String functionName;

    AuxiliaryFunction(String functionName) {
        this.functionName = functionName;
    }env()

    public static boolean checkOptionByFunctionName(String functionName){
        for(AuxiliaryFunction option: AuxiliaryFunction.values()){
            if (option.functionName.equals(functionName.split("(")))
                return true;
        }
        return false;
    }
}
