package states.game;

import core.events.Event;
import core.graphics.PixelRenderer;
import entities.Turnstile;
import core.graphics.Window;
import core.graphics.gui.GuiPanel;
import core.states.State;
import entities.Player;
import map.Map;

import java.awt.*;

public class Game extends State {

    private GuiPanel selectionPanel;
    private RaiKiceButton raiKiceButton;

    public Game() {
        super("game");
    }

    @Override
    public void init() {
        selectionPanel = new GuiPanel(50, 50, Window.getWindowWidth() - 100,
                Window.getWindowHeight() - 100, Color.CYAN);

        selectionPanel.addComponent(raiKiceButton = new RaiKiceButton());

        objectManager.addResource(selectionPanel);
        super.init();
    }

    @Override
    protected void eventFired(Event event) {

    }

    @Override
    public void update() {
        super.update();
        if (raiKiceButton != null && raiKiceButton.isSelected() && !raiKiceButton.isRemoved()) {
            selectionPanel.remove();
            Player player = new Turnstile((Window.getWindowWidth()/2) - 25, (Window.getWindowHeight()/2) - 25);
            objectManager.addResource(new Map("turnstile", player));
        }
    }
}
