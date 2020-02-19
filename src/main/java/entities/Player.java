package entities;

import core.events.Event;
import core.events.EventDispatcher;
import core.events.EventListener;
import core.events.types.KeyEventFired;
import core.objects.Entity;
import files.ImageTools;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player extends Entity implements EventListener {

    private final int imageCount = 19;
    private final double speed = 2.5;
    private final String characterName;

    private BufferedImage[] leftSprites;
    private BufferedImage[] rightSprites;
    private BufferedImage[] forwardSprites;
    private BufferedImage[] downSprites;

    private volatile BufferedImage sprite;
    private BufferedImage[] images;

    private volatile double preX, preY;

    private int counter = 0;
    private int frame = 0;

    private boolean walking = false;
    private boolean up, down, left, right;
    private int dir;

    public Player(double x, double y, String character) {
        super(x, y, 50, 50);
        this.characterName = character;
        this.preX = x;
        this.preY = y;
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

    private void updateAnimation(int dir) {
        this.dir = dir;
        switch(dir) {
            case 0:
                images = forwardSprites;
                preY = y;
                break;

            case 1:
                images = leftSprites;
                preX = x;
                break;

            case 2:
                images = downSprites;
                preY = y;
                break;

            case 3:
                images = rightSprites;
                preX = x;
                break;
        }
        if (counter % 2 == 0) {
            if (frame >= imageCount - 1) frame = 0;
            frame++;
            sprite = images[frame];
            counter = 0;
        }
    }

    @Override
    public synchronized void update() {
        if (up) y -= speed;
        if (down) y += speed;
        if (left) x -= speed;
        if (right) x += speed;

        walking = (preX != x || preY != y);

        if (walking) {
            counter++;

            if (y < preY) updateAnimation(0);
            if (x > preX) updateAnimation(1);
            if (y > preY) updateAnimation(2);
            if (x < preX) updateAnimation(3);
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

    @Override
    public synchronized void render(Graphics graphics) {
        graphics.drawImage(sprite, (int)x, (int)y, (int)getWidth(), (int)getHeight(), null);
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
