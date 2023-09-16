package animations;

import javafx.animation.FadeTransition;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class SecondPageAnimation {
    @FXML
    private final VBox choseValueVbox;
    @FXML
    private final VBox choseEnvironmentVbox;

    private FadeTransition choseValueVboxFadeTransition;
    private FadeTransition choseEnvironmentVboxFadeTransition;

    public SecondPageAnimation(VBox choseValueVbox, VBox choseEnvironmentVbox)
    {
        this.choseValueVbox = choseValueVbox;
        this.choseEnvironmentVbox = choseEnvironmentVbox;
        initializeFadeTransitions();
    }


    private void initializeFadeTransitions() {
        Duration fadeDuration = Duration.seconds(1);
        choseValueVboxFadeTransition = createFadeTransition(choseValueVbox, fadeDuration);
        choseEnvironmentVboxFadeTransition = createFadeTransition(choseEnvironmentVbox, fadeDuration);
    }

    private FadeTransition createFadeTransition(Node node, Duration duration) {
        FadeTransition fadeTransition = new FadeTransition(duration, node);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.3);
        return fadeTransition;
    }

    public void playAnimation(){
        choseValueVboxFadeTransition.play();
        choseEnvironmentVboxFadeTransition.play();
    }

    public void stopAnimation(){
        choseValueVboxFadeTransition.stop();
        choseEnvironmentVboxFadeTransition.stop();
        resetNodesToOriginalState();
    }

    private void resetNodesToOriginalState() {
        choseValueVbox.setOpacity(1.0);
        choseEnvironmentVbox.setOpacity(1.0);
    }
}
