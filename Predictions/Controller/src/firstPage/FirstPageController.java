package firstPage;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import DTO.DTOEntityInfo;
import DTO.DTOEnvironmentInfo;
import DTO.DTORuleInfo;
import DTO.DTOTerminationInfo;
import app.AppController;
import engineManager.EngineManager;
import firstPage.detailsOfSystem.DetailsController;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.List;

public class FirstPageController {
    private AppController mainController;
    @FXML
    private DetailsController detailsComponentController;

    @FXML
    private GridPane detailsComponent;

    @FXML
    private SplitPane smallSplitPane;

    @FXML
    private BorderPane codeCalibrationComponent;

    @FXML
    private BorderPane codeConfigComponent;
    private EngineManager engineManager;

    private List<DTOEntityInfo> entityDetails;

    private List<DTORuleInfo> rulesDetails;

    private List<DTOEnvironmentInfo> environmentDetails;

    private DTOTerminationInfo terminationDetails;

    public void setControllers(AppController mainController, EngineManager engineManager) throws IOException {
        setMainController(mainController);
        setEngineManager(engineManager);
        detailsComponentController.setFirstPageController(this);
        bindToIsDetailsClicked();
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
        //todo
        detailsComponentController.setComboBoxes();
    }

    public void bindToIsDetailsClicked(){
        SimpleBooleanProperty isFileLoaded = mainController.getIsDetailsClickedProperty();
        detailsComponentController.bindShowDetails(isFileLoaded);
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
}
