package threadManager;

import history.simulation.Simulation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
    ExecutorService threadExecutor;

    public ThreadManager(int numberOfThreads) {
        this.threadExecutor = Executors.newFixedThreadPool(numberOfThreads);
    }

    public void executeSimulation(Simulation simulation){
        threadExecutor.execute(simulation);
    }
}
