package states.start;

import core.events.types.MouseEventFired;
import core.graphics.Window;
import core.graphics.gui.GuiButton;
import core.graphics.gui.GuiPanel;
import core.loading.Resource;
import core.objects.Object;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class SettingMenu extends GuiPanel {

    private GuiButton exit;
    private List<Resource> objects = new ArrayList<>();
    private boolean clickedExit = false;

    public SettingMenu() {
        super(270, 150, Window.getWindowWidth() - 470, Window.getWindowHeight() - 300, Color.GREEN);
        objects.add(new GuiButton(Window.getWindowWidth() - 225, 155, 20, 20, Color.RED) {
            @Override
            protected boolean mousePressed(MouseEventFired event) {
                if (super.mousePressed(event)) {
                    clickedExit = true;
                }
                return false;
            }
        });
    }

    @Override
    public void update() {
        super.update();
        if (clickedExit) {
            remove();
        }
    }

    public List<Resource> getObjects() {
        return objects;
    }
}
