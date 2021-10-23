package Engine;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public final class Utility {

    /** No touchie~! */
    private Utility(){
    }

    /**
     * Constants.
     */
    public static final Dimension PANEL_SIZE = new Dimension(768, 768);
    public static final int HORIZONTAL_BOUND = 744;
    public static final int VERTICAL_BOUND = 696;
    public static final int ABSOLUTE_VERTICAL_BOUND = 718;
    public static final int ALIEN_SHIP_VERTICAL_RGEN_BOUND = 64;
    public static final int GRID_WIDTH =  768;
    public static final int HALF_GRID_WIDTH = 384;
    public static final Random rgen = new Random();
    public static final String IMAGE_PATH;

    /* init */
    static {
        IMAGE_PATH = "C:/Users/evcmo/IdeaProjects/SpaceShip/icon/";
    }

    public static final class Image {

        /** No no! */
        private Image(){
        }

        public static BufferedImage USER_SHIP              =    read("UserShip.png");
        public static BufferedImage IMPACTED_USER_SHIP     =   read("UserShipI.png");
        public static BufferedImage AFTER_BURNER_USER_SHIP =   read("UserShipA.png");
        public static BufferedImage ASTEROID               =    read("Asteroid.png");
        public static BufferedImage KAMIKAZE               =    read("Kamikaze.png");
        public static BufferedImage DIVE_BOMBER            =  read("DiveBomber.png");
        public static BufferedImage IMPACTED_DIVE_BOMBER   = read("DiveBomberI.png");
        public static BufferedImage LASER                  =     read(  "Laser.png");
        public static BufferedImage ALIEN_LASER            =  read("AlienLaser.png");
        public static BufferedImage BOSS                   =        read("Boss.png");

        public static BufferedImage read(final String path){
            BufferedImage buffy;
            try {
                buffy = ImageIO.read(new File(Utility.IMAGE_PATH + path));
            } catch(IOException e) {
                buffy = null;
                e.printStackTrace();
            }
            return buffy;
        }

    }
}
