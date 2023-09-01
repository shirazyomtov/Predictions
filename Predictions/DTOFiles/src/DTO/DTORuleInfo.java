package DTO;


import DTO.DTOActions.DTOActionInfo;

import java.util.List;

public class DTORuleInfo {

    private final String ruleName;

    private final DTOActivationInfo activation;

    private final Integer amountOfActions;

    private final List<DTOActionInfo> allAction;

    public DTORuleInfo(String ruleName, DTOActivationInfo activation, Integer amountOfActions, List<DTOActionInfo> allAction) {
        this.ruleName = ruleName;
        this.activation = activation;
        this.amountOfActions = amountOfActions;
        this.allAction = allAction;
    }

    public String getRuleName() {
        return ruleName;
    }

    public DTOActivationInfo getActivation() {
        return activation;
    }

    public Integer getAmountOfActions() {
        return amountOfActions;
    }

    public List<DTOActionInfo> getAllAction() {
        return allAction;
    }
}
