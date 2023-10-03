package requestsPage;

import DTO.DTOAllRequests;
import DTO.DTOAllRequestsByUser;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class AllocationsRefresher extends TimerTask {
    private final Consumer<DTOAllRequestsByUser> allAllocations;
    private final String userName;

    public AllocationsRefresher(Consumer<DTOAllRequestsByUser> allAllocations, String userName) {
        this.allAllocations = allAllocations;
        this.userName = userName;
    }

    @Override
    public void run() {

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://localhost:8080/Server_Web_exploded/requestDetailsByUser").newBuilder();
        urlBuilder.addQueryParameter("username", userName);
        String finalUrl = urlBuilder.build().toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    DTOAllRequestsByUser allRequestsByUser = gson.fromJson(response.body().charStream(), DTOAllRequestsByUser.class);
                    allAllocations.accept(allRequestsByUser);
                }
            }
        });
    }
}

