package managementPage;

import DTO.DTOAllRequestsByUser;
import DTO.DTOQueueManagementInfo;
import com.google.gson.Gson;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.HttpAdminClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class ManagementRefresher extends TimerTask {
    private final Consumer<DTOQueueManagementInfo> queueManagementInfoConsumer;

    public ManagementRefresher(Consumer<DTOQueueManagementInfo> queueManagementInfoConsumer) {
        this.queueManagementInfoConsumer = queueManagementInfoConsumer;
    }

    @Override
    public void run() {

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://localhost:8080/Server_Web_exploded/getQueueManagementInfo").newBuilder();
        String finalUrl = urlBuilder.build().toString();

        HttpAdminClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    DTOQueueManagementInfo queueManagementInfo = gson.fromJson(response.body().charStream(), DTOQueueManagementInfo.class);
                    Platform.runLater(() ->{
                        queueManagementInfoConsumer.accept(queueManagementInfo);
                    });
                }
            }
        });
    }
}
