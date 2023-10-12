package managementPage;

import DTO.*;
import DTO.DTOActions.DTOActionDeserialize;
import DTO.DTOActions.DTOActionInfo;
import app.AppController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import managementPage.presentDetails.presentEntities.PresentEntities;
import managementPage.presentDetails.presentEnvironment.PresentEnvironment;
import managementPage.presentDetails.presentGrid.PresentGrid;
import managementPage.presentDetails.presentRules.PresentRule;
import okhttp3.*;
import utils.HttpAdminClientUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManagementPageController {
    private static final String ENTITIES_DETAILS_PAGE = "/managementPage/presentDetails/presentEntities/presentEntity.fxml";

    private static final String RULES_DETAILS_PAGE = "/managementPage/presentDetails/presentRules/presentRule.fxml";

    private static final String ENVIRONMENT_DETAILS_PAGE = "/managementPage/presentDetails/presentEnvironment/presentEnvironment.fxml";

    private static final String GRID_DETAILS_PAGE = "/managementPage/presentDetails/presentGrid/presentGrid.fxml";

    @FXML
    private TextField XMLFileTextField;

    @FXML
    private Button loadFileButton;

    @FXML
    private GridPane managementPageGridPane;

    @FXML
    private SplitPane managementPageSplitPane;

    @FXML
    private SplitPane detailsSplitPane;

    @FXML
    private ScrollPane scrollPaneWrapper;

    @FXML
    private TreeView<String> simulationTreeView;

    @FXML
    private Label simulationsCompletedLabel;

    @FXML
    private Label simulationsInProgressLabel;

    @FXML
    private Label simulationsInQueueLabel;

    @FXML
    private TextField threadCountTextField;

    @FXML
    private VBox vboxDetails;
    private AppController mainController;

    private PresentEntities presentEntities;

    private PresentRule presentRule;

    private PresentEnvironment presentEnvironment;

    private PresentGrid presentGrid;

    private List<DTOEntityInfo> entityDetails;

    private List<DTORuleInfo> rulesDetails;

    private List<DTOEnvironmentInfo> environmentDetails;

    private DTOGrid gridDetails;

    private Map<String, DTOWorldDefinitionInfo> allSimulation = new HashMap<>();

    private ManagementRefresher managementRefresher;
    private Timer timer;

    @FXML
    public void initialize() {
        managementPageSplitPane.setDividerPositions(0.75);
        managementPageSplitPane.getDividers().get(0).positionProperty().addListener((Observable, oldValue, newValue) -> {
            managementPageSplitPane.setDividerPositions(0.75);
        });

        detailsSplitPane.setDividerPositions(0.3);
        detailsSplitPane.getDividers().get(0).positionProperty().addListener((Observable, oldValue, newValue) -> {
            detailsSplitPane.setDividerPositions(0.3);
        });
    }

    public void createManagementRefresher() {
        managementRefresher = new ManagementRefresher(this::updateQueueManagement);
        timer = new Timer();
        timer.schedule(managementRefresher, 500, 500);
    }

    private void updateQueueManagement(DTOQueueManagementInfo dtoQueueManagementInfo){
        simulationsCompletedLabel.setText(dtoQueueManagementInfo.getNumberOfFinishSimulations().toString());
        simulationsInQueueLabel.setText(dtoQueueManagementInfo.getNumberOfSimulationsInQueue().toString());
        simulationsInProgressLabel.setText(dtoQueueManagementInfo.getNumberOfRunningSimulations().toString());
    }

    public void setControllers(AppController appController) throws IOException {
        setMainController(appController);
        setPresentDetailsInvisibleAndSetDetails();
        setRootTreeView();
    }

    private void setRootTreeView(){
        TreeItem<String> rootItem = new TreeItem<>("Worlds");
        simulationTreeView.setRoot(rootItem);
    }

    private void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    private void setPresentDetailsInvisibleAndSetDetails() throws IOException {
        loadEntitiesDetailsResourced();
        loadRulesDetailsResourced();
        loadEnvironmentDetailsResourced();
        loadGridDetailsResourced();
    }

    private void loadEntitiesDetailsResourced() throws IOException {
        FXMLLoader fxmlLoader = loadResourced(ENTITIES_DETAILS_PAGE);
        presentEntities = fxmlLoader.getController();
    }

    private void loadRulesDetailsResourced() throws IOException {
        FXMLLoader fxmlLoader = loadResourced(RULES_DETAILS_PAGE);
        presentRule = fxmlLoader.getController();
    }

    private void loadEnvironmentDetailsResourced() throws IOException {
        FXMLLoader fxmlLoader = loadResourced(ENVIRONMENT_DETAILS_PAGE);
        presentEnvironment = fxmlLoader.getController();
    }

    public void loadGridDetailsResourced() throws IOException {
        FXMLLoader fxmlLoader = loadResourced(GRID_DETAILS_PAGE);
        presentGrid = fxmlLoader.getController();
    }

    public FXMLLoader loadResourced(String path) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(path);
        fxmlLoader.setLocation(url);
        InputStream inputStream = url.openStream();
        GridPane gridPane = fxmlLoader.load(inputStream);
        return fxmlLoader;
    }

    @FXML
    void SetThreadsCountClicked(ActionEvent event) {
        String threadCountString = threadCountTextField.getText();
        try{
            Integer threadCount = Integer.parseInt(threadCountString);
            if (threadCount > 0) {
                HttpUrl.Builder urlBuilder = HttpUrl.parse("http://localhost:8080/Server_Web_exploded/setThreadCount").newBuilder();
                urlBuilder.addQueryParameter("threadCount", threadCountString);
                String finalUrl = urlBuilder.build().toString();

                HttpAdminClientUtil.runAsyncGet(finalUrl, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.isSuccessful()) {
                            Platform.runLater(() -> {
                                mainController.setSuccessMessage("The thread count was set successfully");
                            });
                        }
                    }
                });
            }
            else{
                mainController.setErrorMessage("You need to enter a number of threads greater than 0");
            }
        }
        catch (Exception e){
            mainController.setErrorMessage("You need to enter a number of threads greater than 0");
        }
    }

    @FXML
    void loadFileButtonClicked(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open XML File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
            File f = fileChooser.showOpenDialog(mainController.getPrimaryStage());

            RequestBody body =
                    new MultipartBody.Builder()
                            .addFormDataPart("xmlFile", f.getAbsolutePath(), RequestBody.create(f, MediaType.parse("application/octet-stream")))
                            .build();

            String finalUrl = HttpUrl
                    .parse("http://localhost:8080/Server_Web_exploded/loadXml")
                    .newBuilder()
                    .build()
                    .toString();

            HttpAdminClientUtil.runAsyncPost(finalUrl, body, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("d");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()) {
                        Gson gson = new GsonBuilder().registerTypeAdapter(DTOActionInfo.class, new DTOActionDeserialize()).create();
                        XMLFileTextField.setText(f.getAbsolutePath());
                        DTOWorldDefinitionInfo dtoWorldDefinitionInfo = gson.fromJson(response.body().string(), DTOWorldDefinitionInfo.class);
                        addSimulationDetails(dtoWorldDefinitionInfo);
                        Platform.runLater(() -> {
                            if(managementRefresher == null){
                                createManagementRefresher();
                            }
                            mainController.setSuccessMessage("The file was loaded Successfully");
                        });
                    }
                    else{
                        Platform.runLater(()->{
                            try {
                                String responseBody = response.body().string();
                                Pattern pattern = Pattern.compile("<b>Message</b>\\s*(.*?)</p>");
                                Matcher matcher = pattern.matcher(responseBody);

                                if (matcher.find()) {
                                    String errorMessage = matcher.group(1).trim();
                                    Platform.runLater(() -> {
                                        mainController.setErrorMessage(errorMessage);
                                    });
                                }
                            } catch (IOException ignore) {}
                        });
                    }
                }
            });
        }
        catch (Exception e){
            //resetAllComponentFirstPage();
        }
    }

    private void addSimulationDetails(DTOWorldDefinitionInfo dtoWorldDefinitionInfo) {
        TreeItem<String> simulationItem = createSimulationTreeItem(dtoWorldDefinitionInfo.getWorldName(), dtoWorldDefinitionInfo);

        Platform.runLater(() -> {
            simulationTreeView.getRoot().getChildren().addAll(simulationItem);
            mainController.setVisbleAlloctionPage();
        });
    }

    private TreeItem<String> createSimulationTreeItem(String simulationName, DTOWorldDefinitionInfo dtoWorldDefinitionInfo) {
        TreeItem<String> simulationItem = new TreeItem<>(simulationName);
        allSimulation.put(simulationName, dtoWorldDefinitionInfo);
        entityDetails = dtoWorldDefinitionInfo.getEntitiesList();
        rulesDetails = dtoWorldDefinitionInfo.getRulesList();
        environmentDetails = dtoWorldDefinitionInfo.getEnvironmentsList();
        gridDetails =  dtoWorldDefinitionInfo.getGrid();
        TreeItem<String> entitiesBranch = createEntityBranch();
        TreeItem<String> rulesBranch = createRulesBranch();
        TreeItem<String> environmentBranch = createEnvironmentBranch();
        TreeItem<String> gridBranch = new TreeItem<>("Grid");
        simulationItem.getChildren().addAll(entitiesBranch, rulesBranch, environmentBranch, gridBranch);

        return simulationItem;
    }

    private TreeItem<String> createEntityBranch() {
        TreeItem<String> entitiesBranch = new TreeItem<>("Entities");
        for (DTOEntityInfo entityInfo : entityDetails) {
            entitiesBranch.getChildren().add(new TreeItem<>(entityInfo.getEntityName()));
        }

        return entitiesBranch;
    }

    private TreeItem<String> createRulesBranch() {
        TreeItem<String> rulesBranch = new TreeItem<>("Rules");
        for (DTORuleInfo ruleInfo : rulesDetails) {
            rulesBranch.getChildren().add(new TreeItem<>(ruleInfo.getRuleName()));
        }

        return rulesBranch;
    }

    private TreeItem<String> createEnvironmentBranch() {
        TreeItem<String> environmentBranch = new TreeItem<>("Environments");
        for (DTOEnvironmentInfo environmentInfo : environmentDetails) {
            environmentBranch.getChildren().add(new TreeItem<>(environmentInfo.getName()));
        }

        return environmentBranch;
    }

    @FXML
    void simulationTreeViewClicked(MouseEvent event) throws IOException {
        TreeItem<String> selectedItem = simulationTreeView.getSelectionModel().getSelectedItem();
        String simulationName;
        if (selectedItem != null && selectedItem.getParent() != null && selectedItem.getParent().getParent() != null) {
            String selectedValue = selectedItem.getValue();
            if (selectedValue.equals("Grid")) {
                simulationName = selectedItem.getParent().getValue();
                gridDetails =  allSimulation.get(simulationName).getGrid();
                setGridDetails();
            }
            else if (selectedItem.getParent().getValue().equals("Entities")) {
                simulationName = selectedItem.getParent().getParent().getValue();
                entityDetails = allSimulation.get(simulationName).getEntitiesList();
                setEntitiesDetails(getAllPropertiesOfEntity(selectedValue), selectedValue);
            }
            else if (selectedItem.getParent().getValue().equals("Rules")) {
                simulationName = selectedItem.getParent().getParent().getValue();
                rulesDetails = allSimulation.get(simulationName).getRulesList();
                setRulesDetails(getSpecificRule(selectedValue));
            }
            else if (selectedItem.getParent().getValue().equals("Environments")) {
                simulationName = selectedItem.getParent().getParent().getValue();
                environmentDetails = allSimulation.get(simulationName).getEnvironmentsList();
                setEnvironmentDetails(getSpecificEnvironment(selectedValue));
            }
        }
    }

    private List<DTOPropertyInfo> getAllPropertiesOfEntity(String entityName)
    {
        List<DTOPropertyInfo> propertyInfoList = null;
        for(DTOEntityInfo dtoEntityInfo: entityDetails){
            if(dtoEntityInfo.getEntityName().equals(entityName)){
                propertyInfoList = dtoEntityInfo.getProperties();
            }
        }
        return propertyInfoList;
    }

    private void setEntitiesDetails(List<DTOPropertyInfo> allPropertiesOfEntity, String entityName) throws IOException {
        presentEntities.setVisibleEntitiesPage(true);
        vboxDetails.getChildren().clear();
        vboxDetails.getChildren().add(presentEntities.getEntitiesGridPane());
        presentEntities.setAllColumns(allPropertiesOfEntity);
        presentEntities.setEntityNameTextField(entityName);
    }

    private DTORuleInfo getSpecificRule(String ruleName)
    {
        DTORuleInfo ruleInfo = null;
        for(DTORuleInfo dtoRuleInfo: rulesDetails){
            if(dtoRuleInfo.getRuleName().equals(ruleName)){
                ruleInfo = dtoRuleInfo;
            }
        }
        return ruleInfo;
    }

    private void setRulesDetails(DTORuleInfo specificRule) {
        presentRule.setVisibleEntitiesPage(true);
        vboxDetails.getChildren().clear();
        vboxDetails.getChildren().add(presentRule.getRulesGridPane());
        presentRule.setSpecificRuleDetails(specificRule);
    }

    private void setGridDetails() {
        presentGrid.setVisibleGridPage(true);
        vboxDetails.getChildren().clear();
        vboxDetails.getChildren().add(presentGrid.getGridPane());
        presentGrid.setGridDetails(gridDetails);
    }

    private DTOEnvironmentInfo getSpecificEnvironment(String environmentName)
    {
        DTOEnvironmentInfo environmentInfo = null;
        for(DTOEnvironmentInfo dtoEnvironmentInfo: environmentDetails){
            if(dtoEnvironmentInfo.getName().equals(environmentName)){
                environmentInfo = dtoEnvironmentInfo;
            }
        }
        return environmentInfo;
    }

    private void setEnvironmentDetails(DTOEnvironmentInfo dtoEnvironmentInfo) {
        presentEnvironment.setVisibleEnvironmentPage(true);
        vboxDetails.getChildren().clear();
        vboxDetails.getChildren().add(presentEnvironment.getEnvironmentGridPane());
        presentEnvironment.setEnvironmentDetailsPage(dtoEnvironmentInfo);
    }

    public Node getGridPaneManagementPage() {
        return managementPageGridPane;
    }
}
