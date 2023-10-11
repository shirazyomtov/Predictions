package DTO;

import java.util.Map;

public class DTOAmountOfEntities {
    Map<Integer, Map<String, Integer>> amountOfAllEntities;

    public DTOAmountOfEntities(Map<Integer, Map<String, Integer>> amountOfAllEntities) {
        this.amountOfAllEntities = amountOfAllEntities;
    }

    public Map<Integer, Map<String, Integer>> getAmountOfAllEntities() {
        return amountOfAllEntities;
    }
}
