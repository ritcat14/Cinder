package states.start;

import core.events.Event;
import core.events.types.MouseEventFired;
import core.graphics.Window;
import core.graphics.gui.GuiButton;
import core.graphics.gui.GuiPanel;
import core.graphics.gui.Scene;
import core.states.State;
import files.ImageTools;

import java.awt.*;

public class Start extends State {

    private Scene scene;
    private SettingMenu settingMenu;

    public Start() {
        super("start");
    }

    @Override
    public void init() {
        objectManager.addResource(scene = new Scene(0, 0, "scenes/startup/startup.txt"));
        scene.play();
        super.init();
    }

    private void initMenu() {
        objectManager.addResource(new GuiPanel(50, 50, Window.getWindowWidth() - 100, Window.getWindowHeight() - 100,
                Color.CYAN));
        objectManager.addResource(new GuiButton(70, 70, 200, 75,
                ImageTools.getImage("images/startButton1.png"),
                ImageTools.getImage("images/startButton2.png")) {
            @Override
            protected boolean mousePressed(MouseEventFired event) {
                if (super.mousePressed(event)) {
                    requestChange("game");
                    return true;
                }
                return false;
            }
        });

        objectManager.addResource(new GuiButton(70, 150, 200, 75,
                ImageTools.getImage("images/stuffButton1.png"),
                ImageTools.getImage("images/stuffButton2.png")) {
            @Override
            protected boolean mousePressed(MouseEventFired event) {
                if (super.mousePressed(event)) {
                    settingMenu.toggleVisible();
                    return true;
                }
                return false;
            }
        });

        objectManager.addResource(settingMenu = new SettingMenu());
    }

    @Override
    public void update() {
        super.update();

        if (scene.isFinished() && !scene.isRemoved()) {
            scene.remove();
            initMenu();
        }
    }

    @Override
    protected void eventFired(Event event) {

    }
}
