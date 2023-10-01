package thirdPage;

import DTO.DTOSimulationInfo;
import worldManager.WorldManager;
import javafx.concurrent.Task;

import java.util.List;
import java.util.function.Consumer;

public class FinishSimulationTask extends Task<Boolean> {
    private Consumer<List<DTOSimulationInfo>> updateFinishSimulationConsumer;
    private final WorldManager engineManager;

    public FinishSimulationTask(Consumer<List<DTOSimulationInfo>> updateFinishSimulationConsumer, WorldManager engineManager) {
        this.updateFinishSimulationConsumer = updateFinishSimulationConsumer;
        this.engineManager = engineManager;
    }

    @Override
    protected Boolean call() throws Exception {
        while (true){
            updateFinishSimulationConsumer.accept(engineManager.getDetailsAboutEndSimulation());
            Thread.sleep(200);
        }
    }
}
