package world.worldDefinition;

import DTO.*;
import world.entity.definition.EntityDefinitionImpl;
import world.environment.definition.EnvironmentDefinition;
import world.environment.instance.EnvironmentInstance;
import world.rule.RuleImpl;
import world.termination.Termination;
import world.twoDimensionalGrid.TwoDimensionalGrid;

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
}
