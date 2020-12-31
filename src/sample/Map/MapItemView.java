package sample.Map;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.StackPane;

public class MapItemView extends StackPane {

    public final int column;
    public final int row;

    public String text;

    private TextField childText;

    public MapItemView(int columm, int row) {
        this.column = columm;
        this.row = row;
        this.childText = new TextField();
        getChildren().add(childText);
        setAlignment(Pos.CENTER);
    }

    public void setText(String text){
        this.text = text;
        displayText();
    }

    public void displayAirplane() {
        final String AIRPLANE_LOCATION = "file://airplaneLogo.png";
        setBackground(new Background(new BackgroundImage(new Image(AIRPLANE_LOCATION), null, null, null, null)));
    }
    public void displayText() {
        childText.setText(text);
    }
}
