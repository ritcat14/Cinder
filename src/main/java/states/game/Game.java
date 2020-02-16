package states.game;

import core.events.types.MouseEventFired;
import core.graphics.Window;
import core.graphics.gui.GuiButton;
import core.graphics.gui.GuiPanel;
import core.states.State;
import files.ImageTools;

import java.awt.*;

public class Game extends State {

    private GuiPanel selectionPanel;

    public Game() {
        super("game");
    }

    @Override
    public void init() {
        super.init();
        selectionPanel = new GuiPanel(50, 50, Window.getWindowWidth() - 100,
                Window.getWindowHeight() - 100, Color.CYAN);

        selectionPanel.addComponent(new RaiKiceButton());

        objectManager.addResource(selectionPanel);
    }
}
