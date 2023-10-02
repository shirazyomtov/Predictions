package DTO;

public class DTORequestsOfSimulations {
    private String requestId;
    private String userName;
    private String worldName;
    private String totalAmount;
    private String termination;
    private String status;
    private String amountOfSimulationsCurrentlyRunning;
    private String amountOfFinishedSimulations;


    public DTORequestsOfSimulations(String requestId, String userName, String worldName, String totalAmount, String termination, String status, String amountOfSimulationsCurrentlyRunning, String amountOfFinishedSimulations) {
        this.requestId = requestId;
        this.userName = userName;
        this.worldName = worldName;
        this.totalAmount = totalAmount;
        this.termination = termination;
        this.status = status;
        this.amountOfSimulationsCurrentlyRunning = amountOfSimulationsCurrentlyRunning;
        this.amountOfFinishedSimulations = amountOfFinishedSimulations;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getUserName() {
        return userName;
    }

    public String getWorldName() {
        return worldName;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getTermination() {
        return termination;
    }

    public String getStatus() {
        return status;
    }

    public String getAmountOfSimulationsCurrentlyRunning() {
        return amountOfSimulationsCurrentlyRunning;
    }

    public String getAmountOfFinishedSimulations() {
        return amountOfFinishedSimulations;
    }
}
