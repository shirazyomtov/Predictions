package world.worldDefinition;

import DTO.*;
import world.entity.definition.EntityDefinitionImpl;
import world.enums.Type;
import world.environment.definition.EnvironmentDefinition;
import world.environment.instance.EnvironmentInstance;
import world.propertyInstance.impl.BooleanPropertyInstance;
import world.propertyInstance.impl.FloatPropertyInstance;
import world.propertyInstance.impl.IntegerPropertyInstance;
import world.propertyInstance.impl.StringPropertyInstance;
import world.rule.RuleImpl;
import world.termination.Termination;
import world.twoDimensionalGrid.TwoDimensionalGrid;
import world.value.generator.api.ValueGeneratorFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class WorldDefinition implements Serializable {
    private final Map<String, EntityDefinitionImpl> entityDefinition;

    private final List<RuleImpl> rules;

    private final Termination termination;


    private final Map<String, EnvironmentDefinition> environmentDefinition;

    private TwoDimensionalGrid twoDimensionalGrid;

    private Integer amountOfAllPopulation = 0;

    public WorldDefinition(Map<String, EntityDefinitionImpl> entityDefinition, List<RuleImpl> rules, Termination termination,
                           Map<String, EnvironmentDefinition> environmentDefinition, int rows, int columns)
    {
        this.entityDefinition = entityDefinition;
        this.rules = rules;
        this.termination = termination;
        this.environmentDefinition = environmentDefinition;
        this.twoDimensionalGrid = new TwoDimensionalGrid(rows,columns);
    }

    public Map<String, EntityDefinitionImpl> getEntityDefinition() {
        return this.entityDefinition;
    }

    public List<RuleImpl> getRules() {return  this.rules;}

    public Termination getTermination() {return  this.termination;}

    public Map<String, EnvironmentDefinition> getEnvironmentDefinition() {
        return environmentDefinition;
    }

    public List<DTOEnvironmentInfo>  createListEnvironmentDetails() {
        List<DTOEnvironmentInfo> environmentName = new ArrayList<>();
        Collection<EnvironmentDefinition> collectionOfEnvironment = environmentDefinition.values();
        for (EnvironmentDefinition environmentDefinition: collectionOfEnvironment) {
            if (environmentDefinition.getRange() != null) {
                environmentName.add(new DTOEnvironmentInfo(environmentDefinition.getName(), environmentDefinition.getType().toString(),
                        new DTORangeInfo(environmentDefinition.getRange().getFrom().toString(), environmentDefinition.getRange().getTo().toString())));
            }
            else {
                environmentName.add(new DTOEnvironmentInfo(environmentDefinition.getName(), environmentDefinition.getType().toString(), null));
            }
        }
        return environmentName;
    }

    public List<DTOEntityInfo> entitiesDetails(){
        List<DTOEntityInfo> entitiesDetails = new ArrayList<>();
        Collection<EntityDefinitionImpl> collectionOfEntity = entityDefinition.values();
        for (EntityDefinitionImpl entityDefinition: collectionOfEntity) {
            DTOEntityInfo dtoEntity = new DTOEntityInfo(entityDefinition.getName(), entityDefinition.getAmountOfPopulation(), entityDefinition.getDTOProperties());
            entitiesDetails.add(dtoEntity);
        }
        return entitiesDetails;
    }

    public List<DTORuleInfo> rulesDetails(){
        List<DTORuleInfo> dtoRuleInfos = new ArrayList<>();
        for(RuleImpl rule : rules){
            dtoRuleInfos.add(new DTORuleInfo(rule.getRuleName(), rule.getDTOActivation(), rule.getAmountOfActions(), rule.getDTOActions()));
        }

        return dtoRuleInfos;
    }

    public TwoDimensionalGrid getTwoDimensionalGrid() {
        return twoDimensionalGrid;
    }

    public DTOGrid createDTOGridDetails() {
        return new DTOGrid(twoDimensionalGrid.getRows().toString(), twoDimensionalGrid.getCols().toString());
    }

    public Type checkTypeOfEnvironmentProperty(String value) {
        if(environmentDefinition.containsKey(value)){
            return environmentDefinition.get(value).getType();
        }
        else {
            return Type.STRING;
        }
    }

    public void checkValidationValue(String environmentName, String value, Map<String, EnvironmentInstance> environmentValuesByUser) throws IndexOutOfBoundsException, IllegalArgumentException{
        EnvironmentDefinition specificEnvironmentDefinition = environmentDefinition.get(environmentName);
        EnvironmentInstance environmentInstance = null;
        switch (specificEnvironmentDefinition.getType()) {
            case FLOAT:
                environmentInstance = checkAndSetFloatEnvironment(value, specificEnvironmentDefinition);
                break;
            case DECIMAL:
                environmentInstance = checkAndSetIntegerEnvironment(value, specificEnvironmentDefinition);
                break;
            case BOOLEAN:
                environmentInstance = checkAndSetBooleanEnvironment(value, specificEnvironmentDefinition);
                break;
            case STRING:
                environmentInstance = new EnvironmentInstance(new StringPropertyInstance(specificEnvironmentDefinition.getName(), ValueGeneratorFactory.createFixed(value), specificEnvironmentDefinition.getRange()));
                break;
        }

        environmentValuesByUser.put(environmentInstance.getProperty().getName(), environmentInstance);
    }

    private EnvironmentInstance checkAndSetFloatEnvironment(String value, EnvironmentDefinition specificEnvironmentDefinition) throws NumberFormatException, IndexOutOfBoundsException{
        specificEnvironmentDefinition.checkValidationFloatEnvironment(value);
        return new EnvironmentInstance(new FloatPropertyInstance(specificEnvironmentDefinition.getName(), ValueGeneratorFactory.createFixed(Float.parseFloat(value)), specificEnvironmentDefinition.getRange()));
    }

    private EnvironmentInstance checkAndSetIntegerEnvironment(String value, EnvironmentDefinition specificEnvironmentDefinition) throws NumberFormatException, IndexOutOfBoundsException{
        specificEnvironmentDefinition.checkValidationDecimalEnvironment(value);
        return new EnvironmentInstance(new IntegerPropertyInstance(specificEnvironmentDefinition.getName(), ValueGeneratorFactory.createFixed(Integer.parseInt(value)), specificEnvironmentDefinition.getRange()));
    }

    private EnvironmentInstance checkAndSetBooleanEnvironment(String value, EnvironmentDefinition specificEnvironmentDefinition) throws IllegalArgumentException{
        specificEnvironmentDefinition.checkValidationBoolEnvironment(value);
        if(value.equals("true")) {
            return new EnvironmentInstance(new BooleanPropertyInstance(specificEnvironmentDefinition.getName(), ValueGeneratorFactory.createFixed(true), specificEnvironmentDefinition.getRange()));
        }
        else{
            return new EnvironmentInstance(new BooleanPropertyInstance(specificEnvironmentDefinition.getName(), ValueGeneratorFactory.createFixed(false), specificEnvironmentDefinition.getRange()));
        }
    }
}
