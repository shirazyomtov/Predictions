package login;

import app.AppController;
import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.HttpClientUtil;

import java.io.IOException;

public class HeaderLoginController {

    @FXML
    private Label predictionsLabel;

    @FXML
    private TextField userNameTextField;

    @FXML
    private GridPane headerLoginGridPane;

    private AppController mainController;

    @FXML
    public void initialize() {
    }

    public void setMainController(AppController appController){
        mainController = appController;
    }

    @FXML
    void loginButtonClicked(ActionEvent event) {
        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            mainController.setErrorMessage("User name is empty. You can't login with empty user name");
            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse("http://localhost:8080/Server_Web_exploded/login")
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();

        //updateHttpStatusLine("New request is launched for: " + finalUrl);

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                    mainController.setErrorMessage("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            mainController.setErrorMessage("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            mainController.updateUserName(userName);
                            mainController.switchToApplication();
                        } catch (IOException ignore) {}
                    });
                }
            }
        });
    }

    public Node getGridPaneLoginHeader() {
        return headerLoginGridPane;
    }
}

