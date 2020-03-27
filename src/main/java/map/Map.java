package map;

import core.events.Event;
import core.events.EventListener;
import core.graphics.Palette;
import core.graphics.PixelRenderer;
import core.graphics.Window;
import core.objects.Entity;
import core.objects.Object;
import entities.MapObject;
import entities.Player;
import entities.TurnstileHouse;
import files.FileReader;
import files.ImageTools;
import states.game.gui.Minimap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static core.VARS.*;

public class Map extends Object implements EventListener {

    private ConcurrentLinkedQueue<MapObject> mapObjects;

    private final String mapFile;
    private final String name;

    private double x, y, size;

    private Area[] areas;
    private List<Integer> indices;
    private int areaAmountW;

    private Rectangle areaBound;
    private String[] mapData;

    private Palette palette;
    private Player player;
    private Minimap minimap;

    public Map(String mapName, Player player) {
        this.name = mapName;
        this.mapFile = "maps/" + mapName + ".txt";
        this.player = player;
    }

    private void processObjects(BufferedImage objectImage) {
        int[] pixels = new int[objectImage.getWidth() * objectImage.getHeight()];
        objectImage.getRGB(0, 0, objectImage.getWidth(), objectImage.getHeight(), pixels, 0, objectImage.getWidth());

        for (int y = 0; y < objectImage.getHeight(); y++) {
            for (int x = 0; x < objectImage.getWidth(); x++) {
                int pixel = pixels[x + y * objectImage.getWidth()];
                if (isBlack(pixel)) {
                    // Check if we have already processed this object
                    boolean processed = false;
                    for (MapObject object : mapObjects) {
                        if (processed = object.getBounds().contains(x * TILE_SIZE, y * TILE_SIZE)) break;
                    }
                    if (processed) continue;
                    // If not, process object, scan in cardinal direction for object
                    int right = scanRight(x, y, objectImage.getWidth(), pixels);
                    int down = scanDown(x, y, objectImage.getWidth(), objectImage.getHeight(), pixels);
                    mapObjects.add(new TurnstileHouse(x * TILE_SIZE, y * TILE_SIZE, right * TILE_SIZE, down * TILE_SIZE));
                }
            }
        }
    }

    private int scanDown(int xp, int yp, int width, int height, int[] pixels) {
        int oh = 0;
        for (int i = yp; i < height; i++) {
            int pixel = pixels[xp + i * width];
            if (isBlack(pixel)) oh++;
            else break;
        }
        return oh;
    }

    private int scanRight(int xp, int yp, int width, int[] pixels) {
        int ow = 0;
        for (int i = xp; i < width; i++) {
            int pixel = pixels[i + yp * width];
            if (isBlack(pixel)) ow++;
            else break;
        }
        return ow;
    }

    private boolean isBlack(int pixel) {
        return pixel == 0xFF000000;
    }

    @Override
    public void init() {
        mapData = FileReader.readFile(mapFile);

        indices = new ArrayList<>();

        areaBound = new Rectangle(0, 0, (int)Window.getWindowWidth(), (int)Window.getWindowHeight());

        // Map data string processing //

        int firstArea = Integer.parseInt(mapData[0].split(":")[1]);
        areaAmountW = Integer.parseInt(mapData[1].split(":")[1]);

        size = AREA_SIZE * TILE_SIZE * areaAmountW;

        BufferedImage tileImage = ImageTools.getImage("images/maps/" + name + "/map.png");

        areas = new Area[areaAmountW * areaAmountW];

        int[] tileImagePixels = new int[tileImage.getWidth() * tileImage.getHeight()];
        tileImage.getRGB(0, 0, tileImage.getWidth(), tileImage.getHeight(), tileImagePixels, 0, tileImage.getWidth());
        BufferedImage[] images = ImageTools.splitImage(tileImage, areaAmountW, areaAmountW, (int)AREA_SIZE, (int)AREA_SIZE);

        palette = new Palette(this, ImageTools.getImage("images/maps/" + name + "/0a.png"));

        int[] coords = {0, 0};

        for (int i = 0; i < areaAmountW * areaAmountW; i++) {
            coords = coordsFromIndex(i);
            areas[i] = new Area(coords[0], coords[1], palette.processImage(images[i]), palette);
        }

        player.setX((Window.getWindowWidth() / 2) - (player.getWidth() / 2));
        player.setY((Window.getWindowHeight() / 2) - (player.getHeight() / 2));

        // load objects
        mapObjects = new ConcurrentLinkedQueue<>();
        processObjects(ImageTools.getImage("images/maps/" + name + "/objects.png"));
        for (MapObject object : mapObjects) object.init();

        // minimap
        minimap = new Minimap(ImageTools.getImage("images/maps/" + name + "/minimap.png"), player);
        minimap.init();

        Point location = areas[firstArea].getLocation();
        setScroll(-(location.getX() - player.getX()), -(location.getY() - player.getY()));

        //----------------------------//

        player.init();
        super.init();
    }

    private void setScroll(double xScroll, double yScroll) {
        x += xScroll;
        y += yScroll;
        for (Area area : areas) area.setScroll(xScroll, yScroll);
        for (MapObject object : mapObjects) object.scroll(xScroll, yScroll);
    }

    private int[] coordsFromIndex(int index) {
        int ix = (int)(this.x + ((index % areaAmountW) * AREA_SIZE * TILE_SIZE));
        int iy = (int)(this.y + ((index / areaAmountW) * AREA_SIZE * TILE_SIZE));

        return new int[] { ix, iy };
    }

    private int indexFromXY(double x, double y) {
        int worldX = (int)((this.x * -1) + x);
        int worldY = (int)((this.y * -1) + y);
        int ix = (int)(worldX / (AREA_SIZE * TILE_SIZE));
        int iy = (int)(worldY / (AREA_SIZE * TILE_SIZE));

        if (ix > areaAmountW) ix = areaAmountW;
        if (iy > areaAmountW) iy = areaAmountW;
        if (ix < 0) ix = 0;
        if (iy < 0) iy = 0;

        int index = ix + iy * areaAmountW;
        if (index > areas.length) index = areas.length - 1;

        return index;
    }

    @Override
    public void update() {
        player.update();
        for (MapObject object : mapObjects) object.update();

        if (player.getX() < SCROLL_BOUND) {
            setScroll(SCROLL_BOUND - player.getX(), 0);
            player.setX(SCROLL_BOUND);
        } else if (player.getX() + player.getWidth() > SCROLL_BOUND + (Window.getWindowWidth() - (SCROLL_BOUND * 2))) {
            setScroll((SCROLL_BOUND + (Window.getWindowWidth() - (SCROLL_BOUND * 2))) - (player.getX() + player.getWidth()), 0);
            player.setX((SCROLL_BOUND + (Window.getWindowWidth() - (SCROLL_BOUND * 2))) - player.getWidth());
        }

        if (player.getY() < SCROLL_BOUND) {
            setScroll(0, SCROLL_BOUND - player.getY());
            player.setY(SCROLL_BOUND);
        } else if (player.getY() + player.getHeight() > SCROLL_BOUND + (Window.getWindowHeight() - (SCROLL_BOUND * 2))) {
            setScroll(0,(SCROLL_BOUND + (Window.getWindowHeight() - (SCROLL_BOUND * 2))) - (player.getY() + player.getHeight()));
            player.setY((SCROLL_BOUND + (Window.getWindowHeight() - (SCROLL_BOUND * 2))) - player.getHeight());
        }

        if (player.getX() < this.x) player.setX(this.x);
        if (player.getY() < this.y) player.setY(this.y);
        if (player.getX() + player.getWidth() > this.x + size)
            player.setX((this.x + size) - player.getWidth());
        if (player.getY() + player.getHeight() > this.y + size)
            player.setY((this.y + size) - player.getHeight());

        player.setScroll(x, y);
        minimap.update();
    }

    private boolean intersects(int[] coords, int size) {
        return areaBound.intersects(new Rectangle(coords[0], coords[1], size, size));
    }

    @Override
    public void render(PixelRenderer renderer) {
        indices.clear();

        for (int i = 0; i < areas.length; i++) {
            if (intersects(coordsFromIndex(i), (int)(AREA_SIZE * TILE_SIZE))) indices.add(i);
        }

        for (Integer index : indices) areas[index].render(renderer);

        for (Entity entity : mapObjects) entity.render(renderer);
        player.render(renderer);

        renderer.drawRectangle(areaBound, Color.BLUE);

        minimap.render(renderer);
    }

    @Override
    public void onEvent(Event event) {
        minimap.onEvent(event);
        player.onEvent(event);
    }

    public String getName() {
        return name;
    }
}
