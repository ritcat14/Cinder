package entities;

import core.graphics.PixelRenderer;

import java.awt.*;

public class TurnstileHouse extends MapObject {

    public TurnstileHouse(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(PixelRenderer pixelRenderer) {
        pixelRenderer.fillRectangle(x, y, width, height, Color.CYAN);
    }
}
