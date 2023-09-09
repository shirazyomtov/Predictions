package thirdPage;

import DTO.DTOEntityInfo;
import DTO.DTOWorldInfo;
import engineManager.EngineManager;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SimulationTask extends Task<Boolean> {
    private Consumer<List<DTOEntityInfo>>  updateTableViewConsumer;
    private SimpleBooleanProperty isFinishProperty;
    private int simulationId;

    private final EngineManager engineManager;

    private SimpleLongProperty currentTicksProperty;
    private SimpleLongProperty currentSecondsProperty;
    public SimulationTask(int simulationId, EngineManager engineManager, SimpleLongProperty currentTicksProperty, SimpleLongProperty currentSecondsProperty, SimpleBooleanProperty isFinishProperty, Consumer<List<DTOEntityInfo>> updateTableViewConsumer){
        this.simulationId = simulationId;
        this.engineManager = engineManager;
        this.currentTicksProperty = currentTicksProperty;
        this.currentSecondsProperty = currentSecondsProperty;
        this.isFinishProperty = isFinishProperty;
        this.updateTableViewConsumer = updateTableViewConsumer;
    }

    @Override
    protected Boolean call() throws Exception {
        while (true){
            DTOWorldInfo dtoWorldInfo = engineManager.getDTOWorldInfo(simulationId);
            currentTicksProperty.set(dtoWorldInfo.getCurrentTick());
            currentSecondsProperty.set(dtoWorldInfo.getCurrentSecond());
            isFinishProperty.set(dtoWorldInfo.getIsFinish());
            updateTableViewConsumer.accept(dtoWorldInfo.getCurrentAmountOfEntities());
            Thread.sleep(200);
        }

    }

    public void setSimulationId(int simulationId) {
        this.simulationId = simulationId;
    }
}
