package core.graphics;

import map.Map;
import map.Tile;

import java.awt.image.BufferedImage;

public class Palette {

    private final Map map;
    private final int[] pixels;
    private final Tile[] tiles;

    public Palette(Map map, BufferedImage image) {
        this.map = map;
        this.pixels = new int[image.getWidth() * image.getHeight()];
        this.tiles = new Tile[pixels.length];

        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] != 0xFF000000) {
                Tile tile = new Tile(i, map.getName());
                tile.init();
                tiles[i] = tile;
            }
        }
    }

    public int[] processImage(BufferedImage image) {
        int[] result = new int[image.getWidth() * image.getHeight()];
        int[] imagePixels = result;
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), imagePixels, 0, image.getWidth());

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Tile tile = getTileByCol(imagePixels[x + y * image.getWidth()]);
                result[x + y * image.getWidth()] = tile.getID();
            }
        }

        return result;
    }

    public Tile getTileByCol(int pixelCol) {
        for (int i = 0; i < pixels.length; i++) {
            if (pixelCol == pixels[i]) return tiles[i];
        }
        return null;
    }

    public Tile[] getTiles() {
        return tiles;
    }
}
