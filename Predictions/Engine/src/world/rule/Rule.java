package world.rule;

import world.rule.action.Action;
import world.rule.activation.ActivationImpl;

import java.util.List;

public interface Rule {
    String getRuleName();
    ActivationImpl getActivation();

    List<Action> nameActions();
}
