package DTO;

import java.util.List;

public class DTOAllRequestsByUser {
    List<DTORequestsOfSimulations> allRequestsByUser;

    public DTOAllRequestsByUser(List<DTORequestsOfSimulations> allRequestsByUser) {
        this.allRequestsByUser = allRequestsByUser;
    }

    public List<DTORequestsOfSimulations> getAllRequestsByUser() {
        return allRequestsByUser;
    }
}
