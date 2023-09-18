package animations;

import javafx.animation.FadeTransition;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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

    private Timeline timeline;

    public SecondPageAnimation(VBox choseValueVbox, VBox choseEnvironmentVbox)
    {
        this.choseValueVbox = choseValueVbox;
        this.choseEnvironmentVbox = choseEnvironmentVbox;
        initializeFadeTransitions();
        Duration animationInterval = Duration.seconds(2);
        timeline = new Timeline(
                new KeyFrame(animationInterval, event -> {
                    choseValueVboxFadeTransition.playFromStart();
                    choseEnvironmentVboxFadeTransition.playFromStart();
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
    }


    private void initializeFadeTransitions() {
        Duration fadeDuration = Duration.seconds(2);
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
        timeline.play();
    }

    public void stopAnimation(){
        choseValueVboxFadeTransition.stop();
        choseEnvironmentVboxFadeTransition.stop();
        resetNodesToOriginalState();
        timeline.stop();
    }

    private void resetNodesToOriginalState() {
        choseValueVbox.setOpacity(1.0);
        choseEnvironmentVbox.setOpacity(1.0);
    }
}
