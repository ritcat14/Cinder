package states.game;

import core.events.types.MouseEventFired;
import core.graphics.PixelRenderer;
import core.graphics.Window;
import core.graphics.gui.GuiButton;
import files.ImageTools;

import java.awt.image.BufferedImage;

public class RaiKiceButton extends GuiButton {

    private final int amount = 18;

    private BufferedImage[] images;
    private int counter;
    private int currentImage = 0;

    private boolean canAnimate = false;
    private boolean selected = false;

    public RaiKiceButton() {
        super(60, 60, (core.graphics.Window.getWindowWidth() - 100)/5,
                Window.getWindowHeight() - 120, ImageTools.getImage("images/game/RaiKice/RaiKicePortrait1.png"));
    }

    @Override
    public void init() {
        images = new BufferedImage[amount];
        for (int i = 0; i < amount; i++) images[i] = ImageTools.getImage("images/game/RaiKice/RaiKicePortrait" + (i + 1) + ".png");
        super.init();
    }

    @Override
    protected boolean mouseMoved(MouseEventFired event) {
        canAnimate = super.mouseMoved(event);
        return canAnimate;
    }

    @Override
    protected boolean mousePressed(MouseEventFired event) {
        return (selected = (super.mousePressed(event)));
    }

    @Override
    public void update() {
        super.update();
        if (images != null) {
            if (canAnimate) {
                counter++;
                if (counter % 60 == 0) {
                    setImage(images[currentImage]);
                    currentImage++;
                    if (currentImage > amount - 1) currentImage = 0;
                    counter = 0;
                }
            } else {
                currentImage = 0;
                setImage(images[currentImage]);
                counter = 0;
            }
        }
    }

    public boolean isSelected() {
        return selected;
    }
}
