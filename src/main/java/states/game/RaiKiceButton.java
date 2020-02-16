package states.game;

import core.events.types.MouseEventFired;
import core.graphics.Window;
import core.graphics.gui.GuiButton;
import files.ImageTools;

import java.awt.image.BufferedImage;

public class RaiKiceButton extends GuiButton {

    private BufferedImage[] images;
    private int timeToWait = 2;
    private int counter;
    private int currentImage = 0;

    public RaiKiceButton() {
        super(60, 60, (core.graphics.Window.getWindowWidth() - 100)/5,
                Window.getWindowHeight() - 120, ImageTools.getImage("images/game/RaiKicePortrait1.png"));
    }

    @Override
    public void init() {
        super.init();
        images = new BufferedImage[12];
        for (int i = 1; i < 12; i++) {
            images[i] = ImageTools.getImage("images/game/RaiKicePortrait" + i + ".png");
        }
    }

    @Override
    public void update() {
        super.update();
        counter++;
        if (counter / 120 == timeToWait) {
            if (currentImage > images.length) currentImage = 0;
            setImage(images[currentImage]);
            currentImage++;
            counter = 0;
        }
    }

}
