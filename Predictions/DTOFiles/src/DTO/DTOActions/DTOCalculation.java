package DTO.DTOActions;

public class DTOCalculation extends DTOActionInfo{

    private String typeOfCalculation;
    private String resultProp;
    private String arg1;
    private String arg2;
    public DTOCalculation(String actionName, String entityName, String secondEntityName, String typeOfCalculation, String resultProp, String arg1, String arg2) {
        super(actionName, entityName, secondEntityName);
        this.typeOfCalculation = typeOfCalculation;
        this.resultProp = resultProp;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public String getTypeOfCalculation() {
        return typeOfCalculation;
    }

    public String getResultProp() {
        return resultProp;
    }

    public String getArg1() {
        return arg1;
    }

    public String getArg2() {
        return arg2;
    }
}
