package DTO;

public class DTORequestsOfSimulations {

    private Integer requestId;
   // private String userName;
    private String worldName;
    private Integer totalAmount;
    private DTOTerminationInfo termination;

    private String status;
   // private String amountOfSimulationsCurrentlyRunning;
   // private String amountOfFinishedSimulations;


    public DTORequestsOfSimulations(Integer requestId, String worldName, Integer totalAmount, DTOTerminationInfo termination, String status) {
        this.requestId = requestId;
        //  this.userName = userName;
        this.worldName = worldName;
        this.totalAmount = totalAmount;
        this.termination = termination;
        this.status = status;
//        this.amountOfSimulationsCurrentlyRunning = amountOfSimulationsCurrentlyRunning;
//        this.amountOfFinishedSimulations = amountOfFinishedSimulations;
    }

    public Integer getRequestId() {
        return requestId;
    }

    //    public String getUserName() {
//        return userName;
//    }

    public String getWorldName() {
        return worldName;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public DTOTerminationInfo getTermination() {
        return termination;
    }

    public String getStatus() {
        return status;
    }

//    public String getAmountOfSimulationsCurrentlyRunning() {
//        return amountOfSimulationsCurrentlyRunning;
//    }

//    public String getAmountOfFinishedSimulations() {
//        return amountOfFinishedSimulations;
//    }
}
