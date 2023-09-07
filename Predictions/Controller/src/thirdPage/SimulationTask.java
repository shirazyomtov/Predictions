package thirdPage;

import DTO.DTOWorldInfo;
import engineManager.EngineManager;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;

public class SimulationTask extends Task<Boolean> {
    private int simulationId;

    private final EngineManager engineManager;

    private SimpleLongProperty currentTicksProperty;
    private SimpleLongProperty currentSecondsProperty;
    public SimulationTask(int simulationId, EngineManager engineManager, SimpleLongProperty currentTicksProperty, SimpleLongProperty currentSecondsProperty){
        this.simulationId = simulationId;
        this.engineManager = engineManager;
        this.currentTicksProperty = currentTicksProperty;
        this.currentSecondsProperty = currentSecondsProperty;
    }

    @Override
    protected Boolean call() throws Exception {
        while (true){
            DTOWorldInfo dtoWorldInfo = engineManager.getDTOWorldInfo(simulationId);
            currentTicksProperty.set(dtoWorldInfo.getCurrentTick());
            currentSecondsProperty.set(dtoWorldInfo.getCurrentSecond());
            Thread.sleep(200);
        }

    }

    public void setSimulationId(int simulationId) {
        this.simulationId = simulationId;
    }
}
