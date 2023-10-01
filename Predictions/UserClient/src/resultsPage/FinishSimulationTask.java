//package resultsPage;
//
//import DTO.DTOSimulationInfo;
//import engineManager.EngineManager;
//import javafx.concurrent.Task;
//
//import java.util.List;
//import java.util.function.Consumer;
//
//public class FinishSimulationTask extends Task<Boolean> {
//    private Consumer<List<DTOSimulationInfo>> updateFinishSimulationConsumer;
//    private final EngineManager engineManager;
//
//    public FinishSimulationTask(Consumer<List<DTOSimulationInfo>> updateFinishSimulationConsumer, EngineManager engineManager) {
//        this.updateFinishSimulationConsumer = updateFinishSimulationConsumer;
//        this.engineManager = engineManager;
//    }
//
//    @Override
//    protected Boolean call() throws Exception {
//        while (true){
//            updateFinishSimulationConsumer.accept(engineManager.getDetailsAboutEndSimulation());
//            Thread.sleep(200);
//        }
//    }
//}
