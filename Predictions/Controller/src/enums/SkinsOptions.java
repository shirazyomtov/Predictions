package enums;

import javafx.scene.paint.Color;

public enum SkinsOptions {

    DOGS("/SkinsResourced/resource/Dogs/Dogs.css", "/SkinsResourced/resource/Dogs/dogsBackground.png", 232, 223, 216),

    FLOWERS("/SkinsResourced/resource/Flowers/Flowers.css", "/SkinsResourced/resource/Flowers/flowersBackground.png", 243, 238, 235),

    DEFAULT("", "", 211, 211, 211);

    private String path;
    private String imagePath;
    private int red;
    private int green;
    private int blue;

    SkinsOptions(String path, String imagePath, int red, int green, int blue){
        this.path = path;
        this.imagePath = imagePath;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public String getCSS() {
        return this.getClass().getResource(path).toExternalForm();
    }

    public String getImagePath() {
        return imagePath;
    }

    public Color getColor(){
        return Color.rgb(red, green, blue);
    }
}
