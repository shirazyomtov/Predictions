package thirdPage;

import DTO.DTOEntityInfo;
import DTO.DTOWorldInfo;
import engineManager.EngineManager;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;

import java.util.List;
import java.util.function.Consumer;

public class SimulationTask extends Task<Boolean> {
    private Consumer<List<DTOEntityInfo>>  updateTableViewConsumer;
    private SimpleBooleanProperty isFinishProperty;
    private int simulationId;

    private final EngineManager engineManager;

    private SimpleLongProperty currentTicksProperty;
    private SimpleLongProperty currentSecondsProperty;

    private SimpleBooleanProperty isFailed;
    private Consumer<Integer> pauseResumeStop;
    private Consumer<Boolean> resetPauseStopConsumer;

    private boolean past = false;
    public SimulationTask(int simulationId, EngineManager engineManager, SimpleLongProperty currentTicksProperty, SimpleLongProperty currentSecondsProperty, SimpleBooleanProperty isFinishProperty, Consumer<List<DTOEntityInfo>> updateTableViewConsumer, SimpleBooleanProperty isFailed, Consumer<Integer> pauseResumeStop, Consumer<Boolean> resetPauseResumeStop){
        this.simulationId = simulationId;
        this.engineManager = engineManager;
        this.currentTicksProperty = currentTicksProperty;
        this.currentSecondsProperty = currentSecondsProperty;
        this.isFinishProperty = isFinishProperty;
        this.updateTableViewConsumer = updateTableViewConsumer;
        this.isFailed = isFailed;
        this.pauseResumeStop = pauseResumeStop;
        this.resetPauseStopConsumer = resetPauseResumeStop;
    }

    @Override
    protected Boolean call() throws Exception {
        while (true){
            synchronized(this) {
                while (this.past) {
                    try {
                        this.wait();
                    } catch (InterruptedException var2) {
                        System.out.println("InterruptedException caught");
                    }
                }
            }
            DTOWorldInfo dtoWorldInfo = engineManager.getDTOWorldInfo(simulationId);
            Platform.runLater(()-> {
                currentTicksProperty.set(dtoWorldInfo.getCurrentTick());
                currentSecondsProperty.set(dtoWorldInfo.getCurrentSecond());
                isFailed.set(dtoWorldInfo.getIsFailed());
                isFinishProperty.set(dtoWorldInfo.getIsFinish());
                updateTableViewConsumer.accept(dtoWorldInfo.getCurrentAmountOfEntities());
                if(dtoWorldInfo.getIsFinish()){
                    resetPauseStopConsumer.accept(true);
                }
                else {
                    pauseResumeStop.accept(dtoWorldInfo.getCurrentTick());
                }
            });
            Thread.sleep(200);
        }

    }

    public void setSimulationId(int simulationId) {
        this.simulationId = simulationId;
    }

    public void setPast(boolean past) {
        this.past = past;
    }
}

