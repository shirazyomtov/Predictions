package allocationsPage;

import DTO.DTOAllRequests;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.HttpAdminClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class AllocationsRefresher extends TimerTask {
    private final Consumer<DTOAllRequests> allAllocations;

    public AllocationsRefresher(Consumer<DTOAllRequests> allAllocations) {
        this.allAllocations = allAllocations;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl.
                parse("http://localhost:8080/Server_Web_exploded/requestsDetails")
                .newBuilder()
                .build()
                .toString();

        HttpAdminClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson = new Gson();
                     DTOAllRequests allRequests = gson.fromJson(response.body().charStream(), DTOAllRequests.class);
                     allAllocations.accept(allRequests);
                }
            }
        });
    }
}
