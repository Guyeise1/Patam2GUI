package sample.StaticClasses;

import javafx.scene.paint.Color;

public class ColorAndHeight {

    public Color color;
    public int height;

    public ColorAndHeight(Color color, int height) {
        this.color = color;
        this.height = height;
    }

    @Override
    public String toString() {
        return "Item{" +
                height + '\'' +
                "" + color +
                '}';
    }
}
