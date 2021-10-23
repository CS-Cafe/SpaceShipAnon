package Engine.Entities.Hittables.AlienShips;

import Engine.Navigation.Point;
import Engine.Animation.Reel;
import Engine.Navigation.Vector;

import java.awt.*;

/**
 * UNDER CONSTRUCTION !!!
 *
 * ToDo:
 *  Add functionality, integrate Boss into the GridPanel.
 */
public final class Boss extends AlienShip {

    private static final Reel REEL = initReel();

    private Boss(final Reel r){
        super(new Point(300, 300), false,
                false, new Point(301, 301), r,
                new Point(300, 300), new Point(300, 300), Vector.NULL);
    }

    private static Reel initReel(){
        Reel.Builder rb = new Reel.Builder();
        for(int i = 0; i < 28; i++){
            rb.addFrame("BossM/frame00" + (i < 10? "0" + i: i) + ".png");
        }
        rb.setIsLooping(true);
        return rb.build();
    }

    public static Boss defaultInstance(){
        return new Boss(REEL);
    }

    public Boss writhe(){
        return new Boss(impactedReel);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        impactedReel.paintFrame(g2,200, 200, 312, 312);
    }

    @Override
    public boolean isAsteroid() {
        return false;
    }

    @Override
    public boolean isDiveBomber() {
        return false;
    }

    @Override
    public boolean isHit(Point loc) {
        return false;
    }

    @Override
    public Boss takeHit(){
        return this;
    }

}
