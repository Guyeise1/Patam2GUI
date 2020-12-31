package sample.Map;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class MapItemView extends StackPane {

    public final int column;
    public final int row;

    private TextField childText;

    public MapItemView(int columm, int row) {
        this.column = columm;
        this.row = row;
        this.childText = new TextField();
        getChildren().add(childText);
        setAlignment(Pos.CENTER);
    }

    public void setText(String text){
        childText.setText(text);
    }
}
