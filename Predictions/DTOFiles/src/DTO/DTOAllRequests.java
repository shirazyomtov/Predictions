package DTO;

import java.util.Map;

public class DTOAllRequests {
    Map<Integer, DTORequestsOfSimulations> requestsOfSimulationsMap;

    public DTOAllRequests(Map<Integer, DTORequestsOfSimulations> requestsOfSimulationsMap) {
        this.requestsOfSimulationsMap = requestsOfSimulationsMap;
    }

    public Map<Integer, DTORequestsOfSimulations> getRequestsOfSimulationsMap() {
        return requestsOfSimulationsMap;
    }
}
