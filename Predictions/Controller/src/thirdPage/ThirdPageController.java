package thirdPage;

import DTO.DTOSimulationInfo;
import app.AppController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.List;

public class ThirdPageController {

    @FXML
    private ListView<DTOSimulationInfo> executionListView;

    @FXML
    private GridPane thirdPageGridPane;

    private AppController mainController;

//    private List<DTOSimulationInfo> simulationInfoList = new ArrayList<>();

    public void setMainController(AppController appController) {
        this.mainController = appController;
    }

    public void setThirdPageDetails(List<DTOSimulationInfo> simulationInfos) {
        executionListView.getItems().clear();
//        executionListView.getItems().addAll(simulationInfos);
    }

    @FXML
    void executionListViewClicked(MouseEvent event) {

    }


    public void setVisible(boolean state) {
        thirdPageGridPane.visibleProperty().set(state);
    }


    public Node getThirdPageGridPane() {
        return thirdPageGridPane;
    }
}
