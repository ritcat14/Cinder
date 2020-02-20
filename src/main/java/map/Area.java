package map;

import core.events.Event;
import core.events.EventListener;
import core.objects.Object;
import entities.Player;
import files.ImageTools;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Area extends Object implements EventListener {

    private final int ID;

    private String backgroundFile;
    private BufferedImage background;
    private Player player;

    private double x, y;

    public Area(int ID, Player player) {
        this.ID = ID;
        this.backgroundFile = "images/maps/0.png";
        this.player = player;
    }

    public void setScroll(double xScroll, double yScroll) {
        x += xScroll;
        y += yScroll;
    }

    @Override
    public void init() {
        background = ImageTools.getImage(backgroundFile);
        player.init();
        super.init();
    }

    @Override
    public void update() {
        player.update();
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(background, (int)x, (int)y, null);
        player.render(graphics);
    }

    @Override
    public void onEvent(Event event) {
        player.onEvent(event);
    }
}
