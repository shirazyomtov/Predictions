package DTO;

public class DTORequestsOfSimulations {

    private Integer requestId;
    private String userName;
    private String worldName;
    private Integer totalAmount;
    private String termination;

    private String status;
     private Integer amountOfSimulationsCurrentlyRunning;
     private Integer amountOfFinishedSimulations;


    public DTORequestsOfSimulations(Integer requestId, String userName, String worldName, Integer totalAmount, DTOTerminationInfo termination, String status, Integer amountOfSimulationsCurrentlyRunning, Integer amountOfFinishedSimulations) {
        this.requestId = requestId;
        this.userName = userName;
        this.worldName = worldName;
        this.totalAmount = totalAmount;
        this.termination = createTerminationString(termination);
        this.status = status;
        this.amountOfSimulationsCurrentlyRunning = amountOfSimulationsCurrentlyRunning;
        this.amountOfFinishedSimulations = amountOfFinishedSimulations;
    }

    private String createTerminationString(DTOTerminationInfo termination) {
        String terminationString;
        String ticks = "";
        String seconds = "";
        if(termination.getTerminationByUser()){
            terminationString = "By user";
        }
        else{
            if(termination.getTicks() != null){
                ticks = "Ticks: ";
                ticks = ticks.concat(termination.getTicks().toString());
                ticks.concat(" ");
            }
            if(termination.getSecond() != null){
                seconds = "Seconds: ";
                seconds = seconds.concat(termination.getSecond().toString());
            }
            terminationString = ticks.concat(seconds);
        }
        return terminationString;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public String getUserName() {
        return userName;
    }

    public String getWorldName() {
        return worldName;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public String getTermination() {
        return termination;
    }

    public String getStatus() {
        return status;
    }

    public Integer getAmountOfSimulationsCurrentlyRunning() {
        return amountOfSimulationsCurrentlyRunning;
    }

    public Integer getAmountOfFinishedSimulations() {
        return amountOfFinishedSimulations;
    }
}