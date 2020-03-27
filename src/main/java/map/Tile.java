package map;

import core.graphics.PixelRenderer;
import core.graphics.Window;
import files.ImageTools;

import java.awt.image.BufferedImage;

import static core.VARS.TILE_SIZE;

public class Tile {

    private final int ID;
    private final String mapName;

    protected boolean collidable = false;
    protected BufferedImage image;

    public Tile(int ID, String mapName) {
        this.ID = ID;
        this.mapName = mapName;
    }

    public void init() {
        if (ID >= 0) image = ImageTools.getImage("images/maps/" + mapName + "/tiles/" + ID + ".png");
        else image = ImageTools.getImage("images/maps/nulltile.png");
    }

    public synchronized void render(PixelRenderer renderer, double x, double y) {
        if (x + TILE_SIZE < 0 || y + TILE_SIZE < 0 || x > Window.getWindowWidth() || y > Window.getWindowHeight()) return;
        renderer.renderImage(image, x, y);
    }

    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public int getID() {
        return ID;
    }
}
