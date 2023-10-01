package firstPage;

import DTO.*;
import com.google.gson.Gson;
import firstPage.presentDetails.presentEntities.PresentEntities;
import firstPage.presentDetails.presentEnvironment.PresentEnvironment;
import firstPage.presentDetails.presentGrid.PresentGrid;
import firstPage.presentDetails.presentRules.PresentRule;
import firstPage.presentDetails.presentTermination.PresentTermination;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import app.AppController;
import worldManager.WorldManager;
import firstPage.detailsOfSystem.DetailsController;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import okhttp3.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FirstPageController {
    //todo: check the path that there will not be a problem in the jar
    private static final String ENTITIES_DETAILS_PAGE = "/firstPage/presentDetails/presentEntities/presentEntity.fxml";

    private static final String RULES_DETAILS_PAGE = "/firstPage/presentDetails/presentRules/presentRule.fxml";

    private static final String ENVIRONMENT_DETAILS_PAGE = "/firstPage/presentDetails/presentEnvironment/presentEnvironment.fxml";

    private static final String TERMINATION_DETAILS_PAGE = "/firstPage/presentDetails/presentTermination/presentTermination.fxml";
    private static final String GRID_DETAILS_PAGE = "/firstPage/presentDetails/presentGrid/presentGrid.fxml";


    private AppController mainController;

    @FXML
    private DetailsController detailsComponentController;

    @FXML
    private GridPane firstPageGridPane;

    @FXML
    private ScrollPane firstPageScrollPane;

    @FXML
    private GridPane detailsComponent;

    @FXML
    private SplitPane firstPageSplitPane;

//    @FXML
//    private ScrollPane scrollPaneDetails;

    @FXML
    private VBox vboxDetails;

    @FXML
    private Label entityNameLabel;

    private WorldManager engineManager;

    private List<DTOEntityInfo> entityDetails;

    private List<DTORuleInfo> rulesDetails;

    private List<DTOEnvironmentInfo> environmentDetails;

    private DTOTerminationInfo terminationDetails;

    private DTOGrid gridDetails;

    private PresentEntities presentEntities;

    private PresentRule presentRule;

    private PresentEnvironment presentEnvironment;

    private PresentTermination presentTermination;

    private PresentGrid presentGrid;

    public void setControllers(AppController mainController, WorldManager engineManager) throws IOException {
        setMainController(mainController);
        setEngineManager(engineManager);
        detailsComponentController.setFirstPageController(this);
        bindToIsDetailsClicked();
        setPresentDetailsInvisibleAndSetDetails();
        firstPageSplitPane.setDividerPositions(0.3);
        firstPageSplitPane.getDividers().get(0).positionProperty().addListener((Observable, oldValue, newValue) -> {
            firstPageSplitPane.setDividerPositions(0.3);
        });
        firstPageGridPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            firstPageSplitPane.setDividerPositions(0.3);
        });
    }

    private void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    private void setEngineManager(WorldManager engineManager) {
        this.engineManager = engineManager;
    }

    public void setWorldDetailsFromEngine(){
        String finalUrl = HttpUrl
                .parse("http://localhost:8080/Server_Web_exploded/showDetails")
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
                    DTOEntityInfo moran = gson.fromJson(response.body().string(), DTOEntityInfo.class);
                    entityDetails = new ArrayList<>();
                    entityDetails.add(moran);
                    detailsComponentController.setEntitiesComboBox();
                }
            }
        });
//        entityDetails = engineManager.getEntitiesDetails();
//        rulesDetails = engineManager.getRulesDetails();
//        environmentDetails = engineManager.getEnvironmentNamesList();
//        terminationDetails = engineManager.getTerminationDetails();
//        gridDetails =  engineManager.getDTOGridDetails();
//        detailsComponentController.setComboBoxes();
    }

    public void bindToIsDetailsClicked() throws IOException {
        SimpleBooleanProperty isDetailsClicked = mainController.getIsDetailsClickedProperty();
        firstPageGridPane.visibleProperty().bind(isDetailsClicked);
        detailsComponentController.bindShowDetails(isDetailsClicked);
    }

    public List<DTOEntityInfo> getEntityDetails() {
        return entityDetails;
    }

    public List<DTORuleInfo> getRulesDetails() {
        return rulesDetails;
    }

    public List<DTOEnvironmentInfo> getEnvironmentDetails() {
        return environmentDetails;
    }

    public DTOTerminationInfo getTerminationDetails() {
        return terminationDetails;
    }

    private void setPresentDetailsInvisibleAndSetDetails() throws IOException {
        loadEntitiesDetailsResourced();
        loadRulesDetailsResourced();
        loadEnvironmentDetailsResourced();
        loadTerminationDetailsResourced();
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
    public void loadTerminationDetailsResourced() throws IOException {
        FXMLLoader fxmlLoader = loadResourced(TERMINATION_DETAILS_PAGE);
        presentTermination = fxmlLoader.getController();
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

    public void setEntitiesDetails(List<DTOPropertyInfo> allPropertiesOfEntity, String entityName) throws IOException {
        presentEntities.setVisibleEntitiesPage(true);
        vboxDetails.getChildren().clear();
        vboxDetails.getChildren().add(presentEntities.getEntitiesGridPane());
        presentEntities.setAllColumns(allPropertiesOfEntity);
        presentEntities.setEntityNameTextField(entityName);
    }

    public void setRulesDetails(DTORuleInfo specificRule) {
        presentRule.setVisibleEntitiesPage(true);
        vboxDetails.getChildren().clear();
        vboxDetails.getChildren().add(presentRule.getRulesGridPane());
        presentRule.setSpecificRuleDetails(specificRule);
    }

    public void SetTerminationDetails() {
        presentTermination.setVisibleTerminationPage(true);
        vboxDetails.getChildren().clear();
        vboxDetails.getChildren().add(presentTermination.getTerminationGridPane());
        presentTermination.setTerminationDetailsPage(terminationDetails);
    }


    public void setGridDetails() {
        presentGrid.setVisibleGridPage(true);
        vboxDetails.getChildren().clear();
        vboxDetails.getChildren().add(presentGrid.getGridPane());
        presentGrid.setGridDetails(gridDetails);
    }

    public void setEnvironmentDetails(DTOEnvironmentInfo dtoEnvironmentInfo) {
        presentEnvironment.setVisibleEnvironmentPage(true);
        vboxDetails.getChildren().clear();
        vboxDetails.getChildren().add(presentEnvironment.getEnvironmentGridPane());
        presentEnvironment.setEnvironmentDetailsPage(dtoEnvironmentInfo);
    }


    public void setMessageForUser(String message) {
        mainController.setErrorMessage(message);
    }

    public void resetAllComponentFirstPage() {
        presentEntities.setVisibleEntitiesPage(false);
        presentRule.setVisibleEntitiesPage(false);
        presentTermination.setVisibleTerminationPage(false);
        presentEnvironment.setVisibleEnvironmentPage(false);
        presentGrid.setVisibleGridPage(false);
    }

    public Node getFirstPageScrollPane() {
        return firstPageScrollPane;
    }

    public Node getGridPaneFirstPage() {
        return firstPageGridPane;
    }

    public void startAnimationInFirstPage() {
        presentGrid.startAnimationGrid();
        presentTermination.startAnimationTermination();
    }

    public void stopAnimationInFirstPage() {
        presentGrid.stopAnimationGrid();
        presentTermination.stopAnimationTermination();
    }
}
