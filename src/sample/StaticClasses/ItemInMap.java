package sample.StaticClasses;

public class ItemInMap {

    public String color;
    public int height;

    public ItemInMap(String color, int height) {
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
