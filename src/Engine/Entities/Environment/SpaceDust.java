package Engine.Entities.Environment;

import Engine.Entities.Autonomous;
import Engine.Entities.Entity;
import Engine.Navigation.Point;
import Engine.Navigation.Vector;
import Engine.Utility;

import java.awt.*;

/**
 * Space Dust
 *
 * @author Ellie Moore
 * @version 12.12.2020
 */
public final class SpaceDust extends Entity implements Autonomous {

    /*
     * The color of Space Dust.
     */
    private static final Color DUST_COLOR = new Color(250, 10, 10);

    /*
     * The horizontal and vertical dimension of SpaceDust.
     */
    private static final int DUST_SIZE = 2;

    /**
     * A private constructor for {@code SpaceDust}.
     *
     * @param location the location of this {@code SpaceDust}
     */
    private SpaceDust(final Point location){
        super(location, Point.NULL, Point.NULL);
    }

    /**
     * A static factory method for a default instance of {@code SpaceDust}.
     *
     * @return a new, default instance of {@code SpaceDust}
     */
    public static SpaceDust defaultInstance(){
        return new SpaceDust(new Point(Utility.rgen.nextInt(Utility.HORIZONTAL_BOUND), 0));
    }

    /**
     * @inheritDoc
     */
    @Override
    public SpaceDust scroll(){
        return new SpaceDust(Vector.SOUTH.traverse(Vector.SOUTH.traverse(location)));
    }

    /**
     * A method to paint this {@code SpaceDust}.
     *
     * @param g the {@code Graphics object to use}
     */
    @Override
    public void paint(final Graphics g){
        g.setColor(DUST_COLOR);
        g.fillRect(getX(), getY(), DUST_SIZE, DUST_SIZE);
    }

}
