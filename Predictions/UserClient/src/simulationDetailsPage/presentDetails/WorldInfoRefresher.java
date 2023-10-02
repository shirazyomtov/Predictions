package simulationDetailsPage.presentDetails;

import DTO.DTOAllWorldsInfo;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class WorldInfoRefresher extends TimerTask {
    private final Consumer<DTOAllWorldsInfo> worldsNameConsumer;
    private final Consumer<DTOAllWorldsInfo> worldsNameToRequestPage;

    public WorldInfoRefresher(Consumer<DTOAllWorldsInfo> worldsNameConsumer, Consumer<DTOAllWorldsInfo> worldsNameToRequestPage) {
        this.worldsNameConsumer = worldsNameConsumer;
        this.worldsNameToRequestPage = worldsNameToRequestPage;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl.
                parse("http://localhost:8080/Server_Web_exploded/worldsName")
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    DTOAllWorldsInfo dtoWorldsName = gson.fromJson(response.body().charStream(), DTOAllWorldsInfo.class);
                    worldsNameConsumer.accept(dtoWorldsName);
                    worldsNameToRequestPage.accept(dtoWorldsName);
                }
            }
        });
    }
}
