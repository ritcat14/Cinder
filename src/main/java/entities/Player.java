package entities;

import core.events.Event;
import core.events.EventDispatcher;
import core.events.EventListener;
import core.events.types.KeyEventFired;
import core.graphics.PixelRenderer;
import core.objects.Entity;
import files.ImageTools;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public abstract class Player extends Entity implements EventListener {

    private final int imageCount = 19;
    private final double speed = 10;
    private final double diagX = Math.cos(45) * speed;
    private final double diagY = Math.sin(45) * speed;
    private final String characterName;

    private BufferedImage[] leftSprites;
    private BufferedImage[] rightSprites;
    private BufferedImage[] forwardSprites;
    private BufferedImage[] downSprites;

    private volatile BufferedImage sprite;
    private BufferedImage[] images;

    private int counter = 0;
    private int frame = 0;

    private boolean up, down, left, right;

    private int dir = -1;
    private int worldX, worldY;

    public Player(double x, double y, String character) {
        super(x, y, 50, 50);
        this.characterName = "RaiKice";
        //TODO: Change to argument ^^
    }

    @Override
    public void init() {
        images = new BufferedImage[imageCount];

        leftSprites = ImageTools.loadSprites("images/game/" + characterName + "/r", imageCount);

        rightSprites = ImageTools.loadSprites("images/game/" + characterName + "/l", imageCount);

        forwardSprites = ImageTools.loadSprites("images/game/" + characterName + "/f", imageCount);

        downSprites = ImageTools.loadSprites("images/game/" + characterName + "/d", imageCount);

        this.sprite = forwardSprites[0];
        super.init();
    }

    private void updateAnimation() {
        switch(dir) {
            case 0:
                images = forwardSprites;
                break;

            case 1:
                images = leftSprites;
                break;

            case 2:
                images = downSprites;
                break;

            case 3:
                images = rightSprites;
                break;
        }
        if (counter % 2 == 0) {
            if (frame >= imageCount - 1) frame = 0;
            frame++;
            sprite = images[frame];
            counter = 0;
        }
    }

    public void setScroll(double mapX, double mapY) {
        worldX = (int)((mapX * -1) + x);
        worldY = (int)((mapY * -1) + y);
    }

    @Override
    public synchronized void update() {

        boolean walking = up || down || left || right;

        if (walking) {
            if (up && left) {
                y -= diagY;
                x -= diagX;
                dir = 3;
            } else if (up && right) {
                y -= diagY;
                x += diagX;
                dir = 1;
            } else if (down && left) {
                y += diagY;
                x -= diagX;
                dir = 3;
            } else if (down && right) {
                y += diagY;
                x += diagX;
                dir = 1;
            } else if (up) {
                y -= speed;
                dir = 0;
            } else if (right) {
                x += speed;
                dir = 1;
            } else if (down) {
                y += speed;
                dir = 2;
            } else if (left) {
                x -= speed;
                dir = 3;
            }
        }

        if (walking) {
            counter++;
            updateAnimation();
        } else {
            counter = 0;
            frame = 0;

            if (isInitialised()) {
                switch (dir) {
                    case 0:
                        sprite = forwardSprites[0];
                        break;

                    case 1:
                        sprite = rightSprites[0];
                        break;

                    case 2:
                        sprite = downSprites[0];
                        break;

                    case 3:
                        sprite = leftSprites[0];
                        break;
                }
            }
        }
    }

    public int getWorldX() {
        return worldX;
    }

    public int getWorldY() {
        return worldY;
    }

    @Override
    public synchronized void render(PixelRenderer renderer) {
        renderer.renderImage(sprite, x, y);
    }

    private boolean keyPressed(KeyEventFired eventFired) {
        switch (eventFired.getKey()) {
            case KeyEvent.VK_W:
                up = true;
                return true;
            case KeyEvent.VK_A:
                left = true;
                return true;
            case KeyEvent.VK_S:
                down = true;
                return true;
            case KeyEvent.VK_D:
                right = true;
                return true;
        }
        return false;
    }

    private boolean keyReleased(KeyEventFired eventFired) {
        switch (eventFired.getKey()) {
            case KeyEvent.VK_W:
                up = false;
                return true;
            case KeyEvent.VK_A:
                left = false;
                return true;
            case KeyEvent.VK_S:
                down = false;
                return true;
            case KeyEvent.VK_D:
                right = false;
                return true;
        }
        return false;
    }

    @Override
    public void onEvent(Event event) {
        EventDispatcher dispatcher = new EventDispatcher(event);
        dispatcher.dispatch(Event.Type.KEY_PRESSED, (Event e) -> keyPressed((KeyEventFired)e));
        dispatcher.dispatch(Event.Type.KEY_RELEASED, (Event e) -> keyReleased((KeyEventFired)e));
    }
}
