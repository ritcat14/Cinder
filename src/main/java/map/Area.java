package map;

import core.graphics.Palette;
import core.graphics.PixelRenderer;
import core.graphics.Window;
import core.objects.Entity;

import java.awt.*;

import static core.VARS.AREA_SIZE;
import static core.VARS.TILE_SIZE;

public class Area extends Entity {

    private int[] areaData;
    private Point location;
    private Palette palette;

    public Area(double x, double y, int[] areaData, Palette palette) {
        super(x, y, AREA_SIZE * TILE_SIZE, AREA_SIZE * TILE_SIZE);
        this.areaData = areaData;
        location = new Point((int)x, (int)y);
        this.palette = palette;
    }

    @Override
    public void init() {
        super.init();
    }

    public synchronized void setScroll(double xScroll, double yScroll) {
        x += xScroll;
        y += yScroll;
    }

    @Override
    public void update() {
        location.setLocation((int)x, (int)y);
    }

    @Override
    public void render(PixelRenderer renderer) {
        for (int ya = 0; ya < AREA_SIZE; ya++) {
            for (int xa = 0; xa < AREA_SIZE; xa++) {
                int tx = (int) (x + (xa * TILE_SIZE));
                int ty = (int) (y + (ya * TILE_SIZE));
                if (tx < -TILE_SIZE || ty < -TILE_SIZE || tx >= core.graphics.Window.getWindowWidth() || ty >= Window.getWindowHeight())
                    continue;
                Tile tile = palette.getTiles()[areaData[xa + ya * (int) AREA_SIZE]];
                tile.render(renderer, tx, ty);
            }
        }
    }

    public Point getLocation() {
        return location;
    }

}
