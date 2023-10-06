package resultsPage;

import DTO.DTOEntityInfo;
import DTO.DTOWorldInfo;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class SimulationRefresher extends TimerTask {
    private final Consumer<DTOWorldInfo> updateDTOWorld;
    private Consumer<DTOWorldInfo>  updateTableViewConsumer;
    private SimpleBooleanProperty isFinishProperty;
    private Integer simulationId;

//    private final EngineManager engineManager;
//
    private SimpleLongProperty currentTicksProperty;
    private SimpleLongProperty currentSecondsProperty;

    private SimpleBooleanProperty isFailed;
    private Consumer<Integer> pauseResumeStop;
    private Consumer<Boolean> resetPauseStopConsumer;
    private String worldName;

    public SimulationRefresher(Integer simulationId, String worldName, SimpleLongProperty currentTicksProperty, SimpleLongProperty currentSecondsProperty, SimpleBooleanProperty isFinishProperty, Consumer<DTOWorldInfo> updateTableViewConsumer, SimpleBooleanProperty isFailed, Consumer<Integer> pauseResumeStop, Consumer<Boolean> resetPauseResumeStop, Consumer<DTOWorldInfo> updateDTOWorld){
        this.simulationId = simulationId;
        this.worldName = worldName;
        this.currentTicksProperty = currentTicksProperty;
        this.currentSecondsProperty = currentSecondsProperty;
        this.isFinishProperty = isFinishProperty;
        this.updateTableViewConsumer = updateTableViewConsumer;
        this.isFailed = isFailed;
        this.pauseResumeStop = pauseResumeStop;
        this.resetPauseStopConsumer = resetPauseResumeStop;
        this.updateDTOWorld = updateDTOWorld;
    }

    @Override
    public void run() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://localhost:8080/Server_Web_exploded/getWorldInfo").newBuilder();
        urlBuilder.addQueryParameter("worldName", worldName);
        urlBuilder.addQueryParameter("simulationId", simulationId.toString());
        String finalUrl = urlBuilder.build().toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    DTOWorldInfo dtoWorldInfo  = gson.fromJson(response.body().charStream(), DTOWorldInfo.class);
                    Platform.runLater(()-> {
                        updateDTOWorld.accept(dtoWorldInfo);
                        currentTicksProperty.set(dtoWorldInfo.getCurrentTick());
                        currentSecondsProperty.set(dtoWorldInfo.getCurrentSecond());
                        isFailed.set(dtoWorldInfo.getIsFailed());
                        isFinishProperty.set(dtoWorldInfo.getIsFinish());
                        updateTableViewConsumer.accept(dtoWorldInfo);
                        if(dtoWorldInfo.getIsFinish()){
                            resetPauseStopConsumer.accept(true);
                        }
                        else {
                            pauseResumeStop.accept(dtoWorldInfo.getCurrentTick());
                        }
                    });
                }
            }
        });
    }
    public void setSimulationId(int simulationId) {
        this.simulationId = simulationId;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }
}

