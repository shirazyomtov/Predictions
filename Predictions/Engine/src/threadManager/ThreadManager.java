package threadManager;

import history.simulation.Simulation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
    ExecutorService threadExecutor;
    private Integer numberOfThreads;

    public ThreadManager(Integer numberOfThreads) {
        this.threadExecutor = Executors.newFixedThreadPool(numberOfThreads);
        this.numberOfThreads = numberOfThreads;
    }

    public void executeSimulation(Simulation simulation){
        threadExecutor.execute(simulation);
    }

    public void setThreadExecutor(Integer numberOfThreads) {
        shutdownThreadExecutor();
        this.threadExecutor = Executors.newFixedThreadPool(numberOfThreads);
        this.numberOfThreads = numberOfThreads;
    }

    public void shutdownThreadExecutor() {
        if (threadExecutor != null && !threadExecutor.isShutdown()) {
            threadExecutor.shutdown();
        }
    }

    public Integer getNumberOfThreads() {
        return numberOfThreads;
    }
}
