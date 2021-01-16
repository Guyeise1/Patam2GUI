package sample.ManualControl;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import sample.StaticClasses.Point;
import simulator.Parameters;

public class JoystickController{

    @FXML
    private Circle circleArea;

    @FXML
    private Circle joystickCircle;

    @FXML
    private StackPane joystickPane;

    @FXML
    private Slider throttleSlider;

    @FXML
    private Slider rudderSlider;

    private DoubleProperty aileronRatio = new SimpleDoubleProperty();

    private DoubleProperty elevatorRatio = new SimpleDoubleProperty();

    public DoubleProperty getRudderValue() {
        return rudderSlider.valueProperty();
    }

    public DoubleProperty getThrottleValue() {
        return throttleSlider.valueProperty();
    }

    public DoubleProperty getAileronValue() {
        return aileronRatio;
    }

    public DoubleProperty getElevatorRatio() {
        return elevatorRatio;
    }

    @FXML
    private void onJoystickMoved(MouseEvent event) {
        final Point circleCenter = new Point(circleArea.getCenterX(), circleArea.getCenterY());
        final double circleRadius = circleArea.getRadius();
        final Point joystickPoint = new Point(event.getX(), event.getY());
        if (Point.distance(circleCenter, joystickPoint) <= circleRadius) {
            joystickCircle.setCenterX(joystickPoint.x);
            joystickCircle.setCenterY(joystickPoint.y);
            this.aileronRatio.set((joystickPoint.x - circleCenter.x) / circleRadius);
            this.elevatorRatio.set((joystickPoint.y - circleCenter.y) / circleRadius);
            simulator.Parameters.setDoubleValue(Parameters.SimulatorParam.AILERON, this.aileronRatio.get());
            simulator.Parameters.setDoubleValue(Parameters.SimulatorParam.ELEVATOR, this.elevatorRatio.get());
        }

    }

    @FXML
    private void onJoystickReleased(MouseEvent event) {
        final Point circleCenter = new Point(circleArea.getCenterX(), circleArea.getCenterY());
        joystickCircle.setCenterX(circleCenter.x);
        joystickCircle.setCenterY(circleCenter.y);
    }


    @FXML
    private void initialize() {
        throttleSlider.valueProperty().addListener(value -> {
            simulator.Parameters.setDoubleValue(Parameters.SimulatorParam.THROTTLE, throttleSlider.getValue());
        });
        rudderSlider.valueProperty().addListener(value -> {
            simulator.Parameters.setDoubleValue(Parameters.SimulatorParam.RUDDER, rudderSlider.getValue());
        });
    }

}
