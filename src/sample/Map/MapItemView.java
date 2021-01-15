package sample.Map;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class MapItemView extends BorderPane {

    public final int column;
    public final int row;

    public String text;

    private Text heightTextView;

    private ImageView iconImageView;

    final static String AIRPLANE_LOCATION = "file:Images/airplaneLogo.png";
    final static String X_LOCATION = "file:Images/x.png";
    final static Image AIRPLANE_IMAGE = new Image(AIRPLANE_LOCATION);
    final static Image X_IMAGE = new Image(X_LOCATION);

    public MapItemView(int column, int row, int minWidth, int minHeight) {
        this.column = column;
        this.row = row;
        setMinWidth(minWidth);
        setMinHeight(minHeight);
        createImageView();
        createTextView();
        super.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.5))));
    }

    private void createTextView() {
        this.heightTextView = new Text();
        heightTextView.setText(text);
    }

    private void createImageView() {
        this.iconImageView = new ImageView();
        iconImageView.setFitHeight(super.getMinHeight());
        iconImageView.setFitWidth(super.getMinHeight());
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
        super.setCenter(heightTextView);
    }

    public void markAsViaSquare() {
        String blue = "#0000FF";
        setBackground(new Background(new BackgroundFill(Paint.valueOf(blue), null, null)));
    }

    public void displayX() {
        getChildren().clear();
        iconImageView.setFitHeight(50);
        iconImageView.setFitWidth(50);
        iconImageView.setImage(X_IMAGE);
        getChildren().add(iconImageView);
    }

    public void clear() {
        setBackground(Background.EMPTY);
        displayText();
    }
}
