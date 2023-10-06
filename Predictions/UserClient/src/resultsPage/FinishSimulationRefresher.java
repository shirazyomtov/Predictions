package resultsPage;

import DTO.DTOAllRequestsByUser;
import DTO.DTOAllSimulations;
import DTO.DTOSimulationInfo;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.util.Pair;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class FinishSimulationRefresher extends TimerTask {
    private final Consumer<List<DTOSimulationInfo>> allSimulation;
    private final String userName;

    public FinishSimulationRefresher(Consumer<List<DTOSimulationInfo>> allSimulation, String userName) {
        this.allSimulation = allSimulation;
        this.userName = userName;
    }

    @Override
    public void run() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://localhost:8080/Server_Web_exploded/getAllSimulationsByUser").newBuilder();
        urlBuilder.addQueryParameter("userName", userName);
        String finalUrl = urlBuilder.build().toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
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
                else{
                    System.out.println("error");
                }
            }
        });
    }
}
