package entities;

import core.objects.Entity;

public abstract class MapObject extends Entity {

    public MapObject(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public synchronized void scroll(double xScroll, double yScroll) {
        x += xScroll;
        y += yScroll;
    }
}
