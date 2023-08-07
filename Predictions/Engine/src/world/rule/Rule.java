package world.rule;

import world.rule.action.Action;
import world.rule.activation.Activation;

import java.util.List;

public interface Rule {
    String getRuleName();
    Activation getActivation();

    List<Action> nameActions();
}
