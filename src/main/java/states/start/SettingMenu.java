package states.start;

import core.graphics.Window;
import core.graphics.gui.GuiPanel;

import java.awt.*;


public class SettingMenu extends GuiPanel {

    public SettingMenu() {
        super(270, 150, Window.getWindowWidth() - 470, Window.getWindowHeight() - 300, Color.GREEN);
        setVisible(false);
    }
}
