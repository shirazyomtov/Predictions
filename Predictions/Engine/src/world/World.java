package world;

import world.entity.definition.EntityDefinitionImpl;
import world.environment.definition.EnvironmentDefinition;
import world.environment.instance.EnvironmentInstance;
import world.rule.RuleImpl;
import world.termination.Termination;

import java.util.Map;

public final class World {
    private final Map<String, EntityDefinitionImpl> entityDefinition;

    private final Map<String, RuleImpl> rules;

    private final Termination termination;

    private  Map<String, EnvironmentInstance> environmentInstanceMap = null;
    private final Map<String, EnvironmentDefinition> environmentDefinition;

    public World(Map<String, EntityDefinitionImpl> entityDefinition, Map<String, RuleImpl> rules, Termination termination, Map<String, EnvironmentDefinition> environmentDefinition)
    {
        this.entityDefinition = entityDefinition;
        this.rules = rules;
        this.termination = termination;
        this.environmentDefinition = environmentDefinition;
    }

    public Map<String, EntityDefinitionImpl> getEntityDefinition() {
        return this.entityDefinition;
    }

    public Map<String, RuleImpl> getRules() {return  this.rules;}

    public Termination getTermination() {return  this.termination;}

    public Map<String, EnvironmentDefinition> getEnvironmentDefinition() {
        return environmentDefinition;
    }

    public void setEnvironmentInstanceMap(Map<String, EnvironmentInstance> environmentInstanceMap) {
        this.environmentInstanceMap = environmentInstanceMap;
    }

    public Map<String, EnvironmentInstance> getEnvironmentInstanceMap() {
        return environmentInstanceMap;
    }
}
