package module1.guimodule;


import processing.core.PApplet;
import processing.core.PImage;

public class MyDisplay extends PApplet {
    private PImage bg;

    @Override
    public void setup() {
        size(400, 400);
        bg = loadImage("palmTrees.jpg");
        bg.resize(0, height);
//        background(bg);
        image(bg, 0, 0);
//        background(200, 200, 200);
    }

    @Override
    public void draw() {
        bg = loadImage("palmTrees.jpg");
        bg.resize(0, height);
        image(bg, 0, 0);
    }
}
