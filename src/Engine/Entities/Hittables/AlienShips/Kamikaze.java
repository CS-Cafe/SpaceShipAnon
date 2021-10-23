package Engine.Entities.Hittables.AlienShips;

import Engine.Entities.Autonomous;
import Engine.Navigation.Vector;
import Engine.Utility.Image;
import Engine.Navigation.Point;
import Engine.Entities.Entity;
import Engine.Animation.Reel;
import Engine.Utility;

import java.awt.*;

/**
 * Kamikaze
 */
public final class Kamikaze extends AlienShip implements Autonomous {

    private static final Reel REEL = initReel();
    private static final Vector SOUTH_SIX = Vector.south(6);

    /**
     * A private constructor for a {@code Kamikaze}.
     *
     * @param location the location of this {@code Kamikaze}
     * @param impacted whether or not this {@code Kamikaze} has been impacted
     */
    private Kamikaze(final Point location,
                     final boolean originatedAtRight,
                     final boolean impacted,
                     final Reel reel,
                     final Vector velocity){
        super(
                location, originatedAtRight,
                impacted, new Point(location.x, location.y - 24), reel,
                new Point(location.x - 10, location.y - 24),
                new Point(location.x + 14, location.x + 6),
                velocity
        );
    }

    /**
     * A static factory method for a default {@code DiveBomber}.
     *
     * @return a default {@code DiveBomber} with a randomly generated origin {@code Point}.
     */
    public static Kamikaze defaultInstance(){
        final boolean originatedAtRight = Utility.rgen.nextInt(2) == 0;
        return new Kamikaze(new Point(
                originatedAtRight? Utility.HORIZONTAL_BOUND: 0,
                Utility.rgen.nextInt(Utility.ALIEN_SHIP_VERTICAL_RGEN_BOUND) << 2
        ), originatedAtRight, false, Reel.NULL, originatedAtRight? Vector.WEST: Vector.EAST);
    }

    private static Reel initReel(){
        Reel.Builder rb = new Reel.Builder();
        for(int i = 0; i < 26; i++){
            rb.addFrame("KamikazeI/frame00" + (i < 10? "0" + i: i) + ".png");
        }
        return rb.build();
    }

    @Override
    public Kamikaze takeHit() {
        return new Kamikaze(location, originatedAtRight,true, REEL, Vector.NULL);
    }

    @Override
    public Kamikaze autopilot(){
        final double a = velocity.getAngle();
        final double ax = originatedAtRight? a - 0.01: a + 0.01;
        return new Kamikaze(
                Vector.SOUTH.traverse(velocity.traverse(location)),
                originatedAtRight, impacted, impactedReel, velocity != Vector.NULL?
                Vector.traversableInstance(ax, 4): velocity
        );
    }

    @Override
    public Kamikaze dive(){
        final double a = velocity.getAngle();
        final double ax;
        if(originatedAtRight) ax = a > 0? a - 0.075: a;
        else ax = a < Math.PI? a + 0.075: a;
        final Point p = SOUTH_SIX.traverse(velocity.traverse(location));
        return new Kamikaze(p, originatedAtRight, impacted, impactedReel,
                Vector.traversableInstance(ax,4)
        );
    }

    @Override
    public Kamikaze scroll(){
        return new Kamikaze(Vector.SOUTH.traverse(location),
                originatedAtRight, impacted, impactedReel, velocity
        );
    }

    /**
     * A method to paint the dive bomber given a {@code Graphics} Object.
     *
     * @see Entity#paint(Graphics)
     * @see java.awt.Graphics;
     * @param g a {@code Graphics} Object
     */
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.RED);
        Graphics2D g2 = (Graphics2D) g;
        if(impacted) impactedReel.paintFrame(g2,location.x - 34, location.y - 66, 72, 72);
        else {
            g2.drawImage(Image.KAMIKAZE, location.x - 34, location.y - 66, 72, 72, null);
            //g2.drawRect(location.x - 10, location.y - 24, 24, 30);
        }
    }

    @Override
    public boolean isAsteroid(){
        return false;
    }

    @Override
    public boolean isDiveBomber(){
        return false;
    }

    @Override
    public boolean isHit(final Point p){
        final int x = location.x - 20, y = location.y - 24;
        return p.x > x && p.x < x + 34 && p.y > y && p.y < y + 30;
    }

}
