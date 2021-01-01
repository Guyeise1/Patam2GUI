package sample.Map;

import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MapItemView extends Pane {

    public final int column;
    public final int row;

    public String text;

    private TextArea heightTextView;

    private ImageView planeImageView;

    final static String AIRPLANE_LOCATION = "file:airplaneLogo.png";


    public MapItemView(int column, int row) {
        this.column = column;
        this.row = row;
        createImageView();
        createTextView();
    }

    private void createTextView() {
        this.heightTextView = new TextArea();
        this.heightTextView.setBackground(Background.EMPTY);
    }

    private void createImageView() {
        this.planeImageView = new ImageView();
        planeImageView.setImage(new Image(AIRPLANE_LOCATION));
    }

    public void setText(String text){
        this.text = text;
        displayText();
        //displayAirplane();
    }

    public void displayAirplane() {
        getChildren().clear();
        planeImageView.setFitHeight(50);
        planeImageView.setFitWidth(50);
        getChildren().add(planeImageView);
    }

    public void displayText() {
        getChildren().clear();
        heightTextView.setText(text);
        heightTextView.setPrefRowCount(3);
        heightTextView.setPrefRowCount(1);
        getChildren().add(heightTextView);
    }
}
