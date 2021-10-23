package Engine.Entities.Hittables;

import Engine.Entities.Autonomous;
import Engine.Navigation.Point;
import Engine.Animation.Reel;
import Engine.Navigation.Vector;
import Engine.Utility;
import Engine.Utility.Image;

import java.awt.*;

/**
 * Asteroid
 */
public final class Asteroid extends Hittable implements Autonomous {

    public static final int MAX_OFFSET      =  22;
    public static final int SPAWN_BOUND     = 186;
    public static final int LOG_4           =   2;
    public static final int WIDTH           =  74;
    public static final int CENTER_OFFSET   =  25;
    public static final int HITBOX_MAX      =  26;
    public static final int HITBOX_MIN      =   2;
    private static final Reel REEL = initReel();

    /**
     * A private constructor for an Asteroid.
     */
    private Asteroid(final Point loc, final boolean impacted, final Reel reel){
        super(
                loc, impacted, reel,
                new Point(loc.x, loc.y),
                new Point(loc.x + MAX_OFFSET, loc.y + MAX_OFFSET)
        );
    }

    public static Asteroid defaultInstance(){
        return new Asteroid(
                new Point((Utility.rgen.nextInt(SPAWN_BOUND) << LOG_4), 0), false, Reel.NULL
        );
    }

    private static Reel initReel(){
        Reel.Builder rb = new Reel.Builder();
        for(int i = 0; i < 26; i++)
            rb.addFrame("AsteroidI/frame00" + (i < 10? "0" + i: i) + ".png");
        return rb.build();
    }

    @Override
    public Asteroid scroll(){
        return new Asteroid(Vector.SOUTH.traverse(location), impacted, impactedReel);
    }

    @Override
    public Asteroid takeHit(){
        return new Asteroid(location, true, REEL);
    }

    /*
     * See PixelElement.paint(Graphics).
     */
    @Override
    public void paint(final Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        if(impacted) {
            impactedReel.paintFrame(
                    g2,
                    location.x - CENTER_OFFSET,
                    location.y - CENTER_OFFSET,
                    WIDTH, WIDTH
            );
        } else {
            g2.drawImage(
                    Image.ASTEROID,
                    location.x - CENTER_OFFSET,
                    location.y - CENTER_OFFSET,
                    WIDTH, WIDTH, null
            );
            //g2.setColor(Color.RED);
            //final int x = location.x - HITBOX_MIN, y = location.y - HITBOX_MIN;
            //g2.drawRect(x, y, HITBOX_MAX, HITBOX_MAX);
        }
    }

    @Override
    public boolean isHit(final Point p){
        final int x = location.x - HITBOX_MIN, y = location.y - HITBOX_MIN;
        return x < p.x && p.x < (x + HITBOX_MAX) && y < p.y && p.y < (y + HITBOX_MAX);
    }

    @Override
    public boolean isAsteroid(){
        return true;
    }

}
