package history.simulation;

import world.worldInstance.WorldInstance;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public  class Simulation {
    private WorldInstance worldInstance = null;

    private LocalDateTime dateTime;
    private final String formattedDateTime;

    public Simulation(WorldInstance worldInstance, LocalDateTime dateTime) {
        this.worldInstance = worldInstance;
        this.dateTime = dateTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH.mm.ss");
        this.formattedDateTime = dateTime.format(formatter);
    }

    public WorldInstance getWorldInstance() {
        return worldInstance;
    }

    public String getFormattedDateTime() {
        return this.formattedDateTime;
    }
}
