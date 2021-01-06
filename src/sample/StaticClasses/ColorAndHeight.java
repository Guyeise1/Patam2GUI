package sample.StaticClasses;

import javafx.scene.paint.Color;

public class ColorAndHeight {

    public Color color;
    public double height;

    public ColorAndHeight(Color color, double height) {
        this.color = color;
        this.height = height;
    }

    @Override
    public String toString() {
        return "Item{" +
                height + '\'' +
                color +
                '}';
    }
}
