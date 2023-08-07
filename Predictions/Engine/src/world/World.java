package world;

import world.entity.definition.EntityDefinitionImpl;
import world.rule.RuleImpl;
import world.termination.Termination;

import java.util.Map;

public final class World {
    private final Map<String, EntityDefinitionImpl> entityDefinition;

    private final Map<String, RuleImpl> rules;

    private final Termination termination;

    public World(Map<String, EntityDefinitionImpl> entityDefinition, Map<String, RuleImpl> rules, Termination termination)
    {
        this.entityDefinition = entityDefinition;
        this.rules = rules;
        this.termination = termination;
    }

    public Map<String, EntityDefinitionImpl> getEntityDefinition() {
        return this.entityDefinition;
    }

    public Map<String, RuleImpl> getRules() {return  this.rules;}

    public Termination getTermination() {return  this.termination;}
}
