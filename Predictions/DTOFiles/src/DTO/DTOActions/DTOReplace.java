package DTO.DTOActions;

public class DTOReplace extends DTOActionInfo{
    private String  createEntityName;
    private String mode;

    public DTOReplace(String actionName, String killEntityName, String secondEntityName, String createEntityName, String mode) {
        super(actionName, killEntityName, secondEntityName);
        this.createEntityName = createEntityName;
        this.mode = mode;
    }

    public String getCreateEntityName() {
        return createEntityName;
    }

    public String getMode() {
        return mode;
    }
}
