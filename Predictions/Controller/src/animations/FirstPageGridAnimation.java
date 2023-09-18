package animations;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

public class FirstPageGridAnimation {
    private final TextField rowsTextField;
    private final TextField colsTextField;
    private Paint rowsColor;
    private Paint colColor;
    private Label rowsLabel;
    private Label colsLabel;
    private  Label gridLabel;
    private Color startColor;
    private Color endColor;
    private Timeline rowsLabelAnimation;
    private Timeline colsLabelAnimation;
    private Timeline gridLabelAnimation;

    private FadeTransition rowsTextFieldFadeTransition;
    private FadeTransition colsTextFieldFadeTransition;
    private Paint gridColor;

    private Timeline timeline;

    public FirstPageGridAnimation(Label rowsLabel, Label colsLabel, Label gridLabel, TextField rowsTextField, TextField colsTextField) {
        this.rowsLabel = rowsLabel;
        this.colsLabel = colsLabel;
        this.gridLabel = gridLabel;
        this.rowsTextField = rowsTextField;
        this.colsTextField = colsTextField;
        startColor = Color.BLUE;
        endColor = Color.RED;
        this.rowsColor = rowsLabel.getTextFill();
        this.colColor = colsLabel.getTextFill();
        this.gridColor = gridLabel.getTextFill();
        initializeColorAnimations(rowsLabel);
        initializeColorAnimations(colsLabel);
        initializeColorAnimations(gridLabel);
        initializeTransitions();
        Duration animationInterval = Duration.seconds(2);
        timeline = new Timeline(
                new KeyFrame(animationInterval, event -> {
                    rowsTextFieldFadeTransition.playFromStart();
                    colsTextFieldFadeTransition.playFromStart();
                    rowsLabelAnimation.playFromStart();
                    colsLabelAnimation.playFromStart();
                    gridLabelAnimation.playFromStart();

                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void initializeColorAnimations(Label label) {
        Duration animationDuration = Duration.seconds(1);

        KeyValue startKeyValue = new KeyValue(label.textFillProperty(), startColor);
        KeyValue endKeyValue = new KeyValue(label.textFillProperty(), endColor);

        KeyFrame startFrame = new KeyFrame(Duration.ZERO, startKeyValue);
        KeyFrame endFrame = new KeyFrame(animationDuration, endKeyValue);

        Timeline animation = new Timeline(startFrame, endFrame);
        animation.setCycleCount(2);
        animation.setAutoReverse(true);

        if (label == rowsLabel) {
            rowsLabelAnimation = animation;
        } else if (label == colsLabel) {
            colsLabelAnimation = animation;
        }
        else{
            gridLabelAnimation = animation;
        }
    }

    private void initializeTransitions() {
        Duration transitionDuration = Duration.seconds(1);

        rowsTextFieldFadeTransition = createFadeTransition(rowsTextField, transitionDuration);
        colsTextFieldFadeTransition = createFadeTransition(colsTextField, transitionDuration);
    }

    private FadeTransition createFadeTransition(Node node, Duration duration) {
        FadeTransition fadeTransition = new FadeTransition(duration, node);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.3);
        return fadeTransition;
    }

    public void playAnimations() {
        if(rowsLabel.getTextFill() != Color.BLUE && rowsLabel.getTextFill() != Color.RED) {
            this.rowsColor = rowsLabel.getTextFill();
        }
        if(colsLabel.getTextFill() != Color.BLUE && colsLabel.getTextFill() != Color.RED) {
            this.colColor = colsLabel.getTextFill();
        }
        if(gridLabel.getTextFill() != Color.BLUE && gridLabel.getTextFill() != Color.RED) {
            this.gridColor = gridLabel.getTextFill();
        }
        rowsLabelAnimation.play();
        colsLabelAnimation.play();
        gridLabelAnimation.play();
        rowsTextFieldFadeTransition.play();
        colsTextFieldFadeTransition.play();
        timeline.play();
    }

    public void stopAnimations() {
        rowsLabelAnimation.stop();
        colsLabelAnimation.stop();
        gridLabelAnimation.stop();
        rowsLabel.setTextFill(rowsColor);
        colsLabel.setTextFill(colColor);
        gridLabel.setTextFill(gridColor);
        rowsTextFieldFadeTransition.stop();
        colsTextFieldFadeTransition.stop();
        resetNodesToOriginalState();
        timeline.stop();
    }

    private void resetNodesToOriginalState() {
        rowsTextField.setOpacity(1.0);
        colsTextField.setOpacity(1.0);
    }
}
