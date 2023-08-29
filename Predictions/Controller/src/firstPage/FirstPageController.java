package firstPage;

import DTO.*;
import firstPage.presentDetails.presentEntities.PresentEntities;
import firstPage.presentDetails.presentTermination.PresentTermination;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import app.AppController;
import engineManager.EngineManager;
import firstPage.detailsOfSystem.DetailsController;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class FirstPageController {
    private static final String ENTITIES_DETAILS_PAGE = "/firstPage/presentDetails/presentEntities/presentEntity.fxml";

    private static final String TERMINATION_DETAILS_PAGE = "/firstPage/presentDetails/presentTermination/presentTermination.fxml";

    private AppController mainController;

    @FXML
    private DetailsController detailsComponentController;

    @FXML
    private GridPane detailsComponent;

    @FXML
    private SplitPane firstPageSplitPane;

    @FXML
    private ScrollPane scrollPaneDetails;


    //
//    @FXML
//    private SplitPane smallSplitPane;
//
//    @FXML
//    private BorderPane codeCalibrationComponent;
//
//    @FXML
//    private BorderPane codeConfigComponent;
    private EngineManager engineManager;

    private List<DTOEntityInfo> entityDetails;

    private List<DTORuleInfo> rulesDetails;

    private List<DTOEnvironmentInfo> environmentDetails;

    private DTOTerminationInfo terminationDetails;

    private PresentEntities presentEntities;

    private PresentTermination presentTermination;

    public void setControllers(AppController mainController, EngineManager engineManager) throws IOException {
        setMainController(mainController);
        setEngineManager(engineManager);
        detailsComponentController.setFirstPageController(this);
        bindToIsDetailsClicked();
        setPresentDetailsInvisibleAndSetDetails();
    }

    private void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    private void setEngineManager(EngineManager engineManager) {
        this.engineManager = engineManager;
    }

    public void setWorldDetailsFromEngine(){
        entityDetails = engineManager.getEntitiesDetails();
        rulesDetails = engineManager.getRulesDetails();
        environmentDetails = engineManager.getEnvironmentNamesList();
        terminationDetails = engineManager.getTerminationDetails();
        //grid - when we move to other schema
        //todo: add grid after we add the new schema
        detailsComponentController.setComboBoxes();
    }

    public void bindToIsDetailsClicked() throws IOException {
        SimpleBooleanProperty isFileLoaded = mainController.getIsDetailsClickedProperty();
        detailsComponentController.bindShowDetails(isFileLoaded);
    }

    private void setPresentDetailsInvisibleAndSetDetails() throws IOException {
        loadEntitiesDetailsResourced();
        loadTerminationDetailsResourced();
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


    public void setEntitiesDetails(List<DTOPropertyInfo> allPropertiesOfEntity, String entityName) throws IOException {
        presentEntities.setVisibleEntitiesPage(true);
        scrollPaneDetails.setContent(presentEntities.getEntitiesGridPane());
        presentEntities.setAllColumns(allPropertiesOfEntity);
        presentEntities.setEntityNameTextField(entityName);
    }
    private void loadEntitiesDetailsResourced() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(ENTITIES_DETAILS_PAGE);
        fxmlLoader.setLocation(url);
        InputStream inputStream = url.openStream();
        GridPane gridPane = fxmlLoader.load(inputStream);
        presentEntities = fxmlLoader.getController();
        scrollPaneDetails.setContent(gridPane);
    }

    public void loadTerminationDetailsResourced() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(TERMINATION_DETAILS_PAGE);
        fxmlLoader.setLocation(url);
        InputStream inputStream = url.openStream();
        GridPane gridPane = fxmlLoader.load(inputStream);
        presentTermination = fxmlLoader.getController();
        scrollPaneDetails.setContent(gridPane);
    }

    private void loadResourced(String path){

    }

    public void SetTerminationDetails() {
        presentTermination.setVisibleTerminationPage(true);
        scrollPaneDetails.setContent(presentTermination.getTerminationGridPane());
        presentTermination.setTerminationDetails(terminationDetails);
    }

    public void resetAllComponent() {
        presentEntities.setVisibleEntitiesPage(false);
        presentTermination.setVisibleTerminationPage(false);
    }
}
