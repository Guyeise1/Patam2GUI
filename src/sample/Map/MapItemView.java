package sample.Map;

import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;

public class MapItemView extends Pane {

    public final int column;
    public final int row;

    public String text;

    private TextArea heightTextView;

    private ImageView iconImageView;

    final static String AIRPLANE_LOCATION = "file:Images/airplaneLogo.png";
    final static String X_LOCATION = "file:Images/x.png";
    final static Image AIRPLANE_IMAGE = new Image(AIRPLANE_LOCATION);
    final static Image X_IMAGE = new Image(X_LOCATION);

    public MapItemView(int column, int row) {
        this.column = column;
        this.row = row;
        createImageView();
        createTextView();
    }

    private void createTextView() {
        this.heightTextView = new TextArea();
        this.heightTextView.setBackground(Background.EMPTY);
        heightTextView.setText(text);
        heightTextView.setPrefRowCount(3);
        heightTextView.setPrefRowCount(1);
    }

    private void createImageView() {
        this.iconImageView = new ImageView();
        iconImageView.setFitHeight(50);
        iconImageView.setFitWidth(50);
    }

    public void setText(String text){
        this.text = text;
        heightTextView.setText(text);
        displayText();
    }

    public void displayAirplane() {
        getChildren().clear();
        iconImageView.setImage(AIRPLANE_IMAGE);
        getChildren().add(iconImageView);
    }

    public void displayText() {
        getChildren().clear();
        getChildren().add(heightTextView);
    }

    public void displayX() {
        getChildren().clear();
        iconImageView.setFitHeight(50);
        iconImageView.setFitWidth(50);
        iconImageView.setImage(X_IMAGE);
        getChildren().add(iconImageView);
    }

    public void clear() {
        displayText();
    }
}
