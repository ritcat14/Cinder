import core.CinderEngine;
import states.game.Game;
import states.start.Start;

import java.awt.*;

public class Main {

    private static double WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static double HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    public static void main(String[] args) {
        CinderEngine cinderEngine = new CinderEngine(WIDTH, HEIGHT);
        cinderEngine.addState(new Start());
        cinderEngine.addState(new Game());
        cinderEngine.setState("game");
        cinderEngine.start();
    }

}
