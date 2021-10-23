package Engine.Entities.Hittables.AlienShips;

import Engine.Entities.Autonomous;
import Engine.Navigation.Point;
import Engine.Animation.Reel;
import Engine.Navigation.Vector;
import Engine.Utility;
import Engine.Utility.Image;

import java.awt.*;

public final class DiveBomber extends AlienShip implements Autonomous {

    private static final Reel REEL = initReel();
    private static final Vector NORTH_TWO = Vector.north(2);
    private static final Vector SOUTH_EIGHT = Vector.south(8);

    private final boolean bombReleased;

    private DiveBomber(final Point location,
                       final boolean originatedAtRight,
                       final boolean impacted,
                       final boolean bombReleased,
                       final Reel reel,
                       final Vector velocity){
        super(
                location, originatedAtRight,
                impacted, new Point(location.x, location.y - 16), reel,
                new Point(location.x - 16, location.y - 20),
                new Point(location.x + 18, location.y + 4),
                velocity
        );
        this.bombReleased = bombReleased;
    }

    public static DiveBomber defaultInstance(final Point userLocation){
        final Point p = new Point(192 + (Utility.rgen.nextInt(96) << 2), 0);
        final boolean originatedAtRight = userLocation.x > Utility.HALF_GRID_WIDTH;
        return new DiveBomber(p, originatedAtRight,
                false, false, Reel.NULL, originatedAtRight?
                Vector.EAST: Vector.WEST
        );
    }

    private static Reel initReel(){
        Reel.Builder rb = new Reel.Builder();
        for(int i = 0; i < 26; i++){
            rb.addFrame("DiveBomberI/frame00" + (i < 10? "0" + i: i) + ".png");
        }
        return rb.build();
    }

    @Override
    public DiveBomber takeHit(){
        return new DiveBomber(
                location, originatedAtRight, true, bombReleased, REEL, Vector.NULL
        );
    }

    @Override
    public DiveBomber scroll(){
        return new DiveBomber(Vector.SOUTH.traverse(location),
                originatedAtRight, impacted, bombReleased, impactedReel, velocity
        );
    }

    @Override
    public DiveBomber autopilot(){
        return new DiveBomber(SOUTH_EIGHT.traverse(
                velocity.traverse(location)), originatedAtRight, impacted,
                bombReleased, impactedReel, velocity
        );
    }

    @Override
    public DiveBomber climb(){
        final Point p = NORTH_TWO.traverse(velocity.traverse(location));
        final double a = velocity.getAngle();
        final double ax = originatedAtRight? a + 0.075: a - 0.075;
        return new DiveBomber(
                p, originatedAtRight, impacted, true,
                impactedReel, Vector.traversableInstance(ax, 4)
        );
    }

    public boolean isBombReleased(){
        return bombReleased;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.RED);
        Graphics2D g2 = (Graphics2D) g;
        if(impacted) impactedReel.paintFrame(
                g2, location.x - 34, location.y - 68, 72, 72
        );
        else g2.drawImage(
                Image.DIVE_BOMBER, location.x - 34, location.y - 68,
                72, 72, null
        );

    }

    @Override
    public boolean isHit(final Point p){
        final int x = location.x - 16, y = location.y - 20;
        return x < p.x && p.x < (x + 34) && y < p.y && p.y < (y + 24);
    }

    public boolean isAsteroid(){
        return false;
    }

    public boolean isDiveBomber(){
        return true;
    }

}
