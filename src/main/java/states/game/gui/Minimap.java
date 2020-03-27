package states.game.gui;

import core.graphics.PixelRenderer;
import core.graphics.gui.GuiPanel;
import entities.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

import static core.VARS.*;

public class Minimap extends GuiPanel {

    private BufferedImage image;
    private Player player;

    private int mx, my, playerX, playerY, imageX, imageY;
    private double dx = 0, dy = 0;
    private Rectangle playerMarker, viewingArea;
    private BufferedImage scaledImage;

    public Minimap(BufferedImage image, Player player) {
        super(50, 50, MINIMAP_WIDTH, MINIMAP_HEIGHT, Color.BLUE);
        this.image = image;
        this.player = player;
        mx = (int)x;
        my = (int)y;
    }

    @Override
    public void init() {
        viewingArea = new Rectangle(mx, my, (int)width, (int)height);

        scaleImage();

        int pw = 10;
        int ph = 10;
        int px = (int)(mx + ((width - pw)/2));
        int py = (int)(my + ((height - ph)/2));

        playerMarker = new Rectangle(px, py, pw, ph);

        playerX = (int)(mx + ((player.getWorldX() / TILE_SIZE) * MINIMAP_SCALE));
        playerY = (int)(my + ((player.getWorldY() / TILE_SIZE) * MINIMAP_SCALE));

        double centerX = mx + (width/2);
        double centerY = my + (height/2);

        dx = centerX - playerX;
        dy = centerY - playerY;

        x += centerX - playerX;
        y += centerY - playerY;

        imageX = (int)x;
        imageY = (int)y;

        super.init();
    }

    private void scaleImage() {
        scaledImage = new BufferedImage((int)(image.getWidth() * MINIMAP_SCALE),
                (int)(image.getHeight() * MINIMAP_SCALE), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(image, 0, 0,
                (int)(image.getWidth() * MINIMAP_SCALE), (int)(image.getHeight() * MINIMAP_SCALE), null);
        graphics2D.dispose();
    }

    @Override
    public void update() {
        double preX = playerX;
        double preY = playerY;
        playerX = mx + (int)((player.getWorldX() / TILE_SIZE) * MINIMAP_SCALE);
        playerY = my + (int)((player.getWorldY() / TILE_SIZE) * MINIMAP_SCALE);

        dx = preX - playerX;
        dy = preY - playerY;

        x += dx;
        y += dy;

        super.update();
    }

    private BufferedImage extractPixels() {
        double xp = viewingArea.x - this.x;
        double yp = viewingArea.y - this.y;
        double iw = viewingArea.width;
        double ih = viewingArea.height;

        int rightBound = scaledImage.getWidth() - viewingArea.width;
        int bottomBound = scaledImage.getHeight() - viewingArea.height;

        if (xp < 0) {
            xp = 0;
            iw = viewingArea.width - (this.x - viewingArea.x);
            imageX = viewingArea.x + (viewingArea.width - (int)iw);
        } else if (xp >= 0 && xp < rightBound) {
            xp -= dx;
            iw = viewingArea.width;
            imageX = viewingArea.x;
        }
        if (xp >= rightBound) {
            iw = viewingArea.width - ((int)xp - (scaledImage.getWidth() - viewingArea.width));
            imageX = viewingArea.x;
        }

        if (yp < 0) {
            yp = 0;
            ih = viewingArea.height - (this.y - viewingArea.y);
            imageY = viewingArea.x + (viewingArea.height - (int)ih);
        } else if (yp >= 0 && yp < bottomBound) {
            yp -= dy;
            ih = viewingArea.height;
            imageY = viewingArea.y;
        }
        if (yp >= bottomBound) {
            ih = viewingArea.height - ((int)yp - (scaledImage.getHeight() - viewingArea.height));
            imageY = viewingArea.y;
        }

        if (iw < 0) iw = Math.abs(iw);
        if (ih < 0) ih = Math.abs(ih);
        if (xp < 0) xp = Math.abs(xp);
        if (yp < 0) yp = Math.abs(yp);

        BufferedImage result = new BufferedImage((int)iw, (int)ih, BufferedImage.TYPE_INT_RGB);

        try {
            result = scaledImage.getSubimage((int)xp, (int)yp, (int)iw, (int)ih);
        } catch (Exception e) {
            //TODO: Log to game engine something bad has happened
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void render(PixelRenderer renderer) {
        renderer.fillRectangle(mx, my, MINIMAP_WIDTH, MINIMAP_HEIGHT, Color.GREEN);

        BufferedImage image = extractPixels();
        if (image != null) renderer.renderImage(image, imageX, imageY);
        renderer.fillRectangle(playerMarker, Color.BLUE);

        renderer.drawRectangle(mx, my, MINIMAP_WIDTH, MINIMAP_HEIGHT, Color.BLUE);
    }

}
