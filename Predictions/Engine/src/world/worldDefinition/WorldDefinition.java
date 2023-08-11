package world.worldDefinition;

import world.entity.definition.EntityDefinitionImpl;
import world.environment.definition.EnvironmentDefinition;
import world.environment.instance.EnvironmentInstance;
import world.rule.RuleImpl;
import world.termination.Termination;

import java.util.List;
import java.util.Map;

public final class WorldDefinition {
    private final Map<String, EntityDefinitionImpl> entityDefinition;

    private final List<RuleImpl> rules;

    private final Termination termination;


    private final Map<String, EnvironmentDefinition> environmentDefinition;

    public WorldDefinition(Map<String, EntityDefinitionImpl> entityDefinition, List<RuleImpl> rules, Termination termination, Map<String, EnvironmentDefinition> environmentDefinition)
    {
        this.entityDefinition = entityDefinition;
        this.rules = rules;
        this.termination = termination;
        this.environmentDefinition = environmentDefinition;
    }

    public Map<String, EntityDefinitionImpl> getEntityDefinition() {
        return this.entityDefinition;
    }

    public List<RuleImpl> getRules() {return  this.rules;}

    public Termination getTermination() {return  this.termination;}

    public Map<String, EnvironmentDefinition> getEnvironmentDefinition() {
        return environmentDefinition;
    }

}
