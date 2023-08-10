package world.enums;

public enum AuxiliaryFunction {
    ENVIRONMENT("environment"),

    RANDOM("random");

    private final String functionName;

    AuxiliaryFunction(String functionName) {
        this.functionName = functionName;
    }

    public static boolean checkOptionByFunctionName(String functionName){
        int index = functionName.indexOf("(");
        if (index != -1) {
            String name = functionName.substring(0, index).trim();
            for(AuxiliaryFunction option: AuxiliaryFunction.values()){
                if (option.functionName.equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getFunctionName() {
        return functionName;
    }
}
