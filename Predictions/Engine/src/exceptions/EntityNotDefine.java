package exceptions;

public class EntityNotDefine extends RuntimeException{
    private final String entityName;
    private final String primaryEntity;
    private  String secondaryEntity = null;

    private final String EXCEPTION_MESSAGE = "The entity name: %s that you define in the action does not appropriate to primary entity: %s " + ((secondaryEntity != null) ? " and to secondary Entity: %s" : "");

    public EntityNotDefine(String entityName, String primaryEntity, String secondaryEntity) {
        this.entityName = entityName;
        this.primaryEntity = primaryEntity;
        this.secondaryEntity = secondaryEntity;
    }
    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,entityName , primaryEntity, secondaryEntity);
    }
}
