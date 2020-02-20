package map;

import core.events.Event;
import core.events.EventListener;
import core.graphics.Window;
import core.objects.Object;
import entities.Player;
import files.FileReader;

import java.awt.*;

public class Map extends Object implements EventListener {

    private final String mapFile;
    private Area[] areas;
    private String[] mapData;

    private Player player;

    public Map(String mapFile, Player player) {
        this.mapFile = mapFile;
        this.player = player;
    }

    @Override
    public void init() {
        mapData = FileReader.readFile(mapFile);

        // Map data string processing //

        int areaNum = Integer.parseInt(mapData[0].split(":")[1]);
        areas = new Area[areaNum];
        int areaCount = 0;

        for (String line : mapData) {
            if (!line.startsWith("AREA:")) {
                continue;
            }
            String[] parts = line.split(":");
            areas[areaCount] = new Area(Integer.parseInt(parts[1]), player);
            areas[areaCount].init();
            areaCount++;
        }

        //----------------------------//

        super.init();
    }

    @Override
    public void update() {
        areas[0].update();
        if (player.getX() < 100) {
            areas[0].setScroll(100 - player.getX(), 0);
            player.setX(100);
        } else if (player.getX() > Window.getWindowWidth() - 100) {
            areas[0].setScroll(Window.getWindowWidth() - 100 - player.getX(), 0);
            player.setX(Window.getWindowWidth() - 100);
        }
        if (player.getY() < 100) {
            areas[0].setScroll(0, 100 - player.getY());
            player.setY(100);
        } else if (player.getY() > Window.getWindowHeight() - 100) {
            areas[0].setScroll(0, Window.getWindowHeight() - 100 - player.getY());
            player.setY(Window.getWindowHeight() - 100);
        }
    }

    @Override
    public void render(Graphics graphics) {
        areas[0].render(graphics);
    }

    @Override
    public void onEvent(Event event) {
        areas[0].onEvent(event);
    }
}
