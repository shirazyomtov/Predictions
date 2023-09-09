package app;

import javafx.application.Platform;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class SimulationTask extends Task<Boolean> {
    private SimpleLongProperty secondsProperty;
    private SimpleLongProperty ticksProperty;

    public SimulationTask(SimpleLongProperty ticksProperty, SimpleLongProperty secondsProperty){
        this.ticksProperty = ticksProperty;
        this.secondsProperty = secondsProperty;
    }

    @Override
    protected Boolean call() throws Exception {
//        try {
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return Boolean.TRUE;
    }

    public SimpleLongProperty getSecondsPropertyProperty() {
        return secondsProperty;
    }

    public SimpleLongProperty getTicksPropertyProperty() {
        return ticksProperty;
    }
}
