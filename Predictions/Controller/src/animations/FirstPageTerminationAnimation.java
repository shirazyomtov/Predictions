package animations;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class FirstPageTerminationAnimation {
    private final Label terminationLabel;
    private final Label secondsLabel;
    private final Label ticksLabel;

    private final RotateTransition rotateTransitionTerminationLabel;
    private final RotateTransition rotateTransitionSecondsLabel;
    private final RotateTransition rotateTransitionTicksLabel;
    private final TextField ticksTextField;
    private final TextField secondsTextField;
    private FadeTransition ticksTextFieldFadeTransition;
    private FadeTransition secondsTextFieldFadeTransition;

    public FirstPageTerminationAnimation(Label terminationLabel, Label secondsLabel, Label ticksLabel, TextField ticksTextField, TextField secondsTextField) {
        this.terminationLabel = terminationLabel;
        this.secondsLabel = secondsLabel;
        this.ticksLabel = ticksLabel;
        this.ticksTextField = ticksTextField;
        this.secondsTextField = secondsTextField;
        rotateTransitionTerminationLabel = applyRotateTransition(terminationLabel, 10);
        rotateTransitionSecondsLabel = applyRotateTransition(secondsLabel, -10);
        rotateTransitionTicksLabel = applyRotateTransition(ticksLabel, 20);
        initializeTransitions();
    }

    private RotateTransition applyRotateTransition(Label label, double angle) {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.3), label);
        rotateTransition.setByAngle(angle);
        rotateTransition.setAutoReverse(true);
        rotateTransition.setCycleCount(2);
        return rotateTransition;
    }

    private void initializeTransitions() {
        Duration transitionDuration = Duration.seconds(1);

        ticksTextFieldFadeTransition = createFadeTransition(ticksTextField, transitionDuration);
        secondsTextFieldFadeTransition = createFadeTransition(secondsTextField, transitionDuration);
    }

    private FadeTransition createFadeTransition(Node node, Duration duration) {
        FadeTransition fadeTransition = new FadeTransition(duration, node);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.3);
        return fadeTransition;
    }

    public void playAnimations() {
        rotateTransitionTerminationLabel.play();
        rotateTransitionSecondsLabel.play();
        rotateTransitionTicksLabel.play();
        ticksTextFieldFadeTransition.play();
        secondsTextFieldFadeTransition.play();
    }

    public void stopAnimations() {
        rotateTransitionTerminationLabel.stop();
        rotateTransitionSecondsLabel.stop();
        rotateTransitionTicksLabel.stop();
        terminationLabel.setRotate(0);
        secondsLabel.setRotate(0);
        ticksLabel.setRotate(0);
        ticksTextFieldFadeTransition.stop();
        secondsTextFieldFadeTransition.stop();
        ticksTextField.setOpacity(1.0);
        secondsTextField.setOpacity(1.0);
    }
}
