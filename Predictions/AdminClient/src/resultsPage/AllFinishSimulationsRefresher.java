package resultsPage;

import DTO.DTOAllSimulations;
import DTO.DTOSimulationInfo;
import DTO.DTOWorldInfo;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.HttpAdminClientUtil;

import java.io.IOException;
import java.io.PipedReader;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class AllFinishSimulationsRefresher extends TimerTask {
    private Consumer<List<DTOSimulationInfo>> allSimulation;

    public AllFinishSimulationsRefresher(Consumer<List<DTOSimulationInfo>> allSimulation) {
        this.allSimulation = allSimulation;
    }

    @Override
    public void run() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://localhost:8080/Server_Web_exploded/getFinishSimulationsForAdmin").newBuilder();
        String finalUrl = urlBuilder.build().toString();

        HttpAdminClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    DTOAllSimulations allSimulationsByUser  = gson.fromJson(response.body().charStream(), DTOAllSimulations.class);
                    Platform.runLater(()->{
                        allSimulation.accept(allSimulationsByUser.getDtoSimulationInfos());
                    });
                }
            }
        });
    }
}
