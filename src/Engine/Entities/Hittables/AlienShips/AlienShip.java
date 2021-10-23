package Engine.Entities.Hittables.AlienShips;

import Engine.Entities.Hittables.Hittable;
import Engine.Navigation.Point;
import Engine.Animation.Reel;
import Engine.Navigation.Vector;

/**
 * Alien Ship
 *
 * @author Ellie Moore
 * @version 12.12.2020
 */
public abstract class AlienShip extends Hittable {

    /**
     * Whether or not this {@code AlienShip} spawned at the right.
     */
    protected final boolean originatedAtRight;

    /**
     * The tail {@code Point} of this {@code AlienShip}.
     */
    protected final Point tailPoint;

    /**
     * The velocity of this {@code AlienShip}.
     */
    protected final Vector velocity;

    /**
     * A protected constructor for an {@code AlienShip}.
     *
     * @param location the location of this {@code AlienShip}
     * @param originatedAtRight whether this {@code AlienShip} spawned at right
     * @param impacted whether or not this {@code AlienShip} is impacted
     * @param tailPoint the tail point of this {@code AlienShip}
     * @param reel this {@code AlienShip}'s animation reel.
     * @param min the minimum point of this {@code AlienShip} (upper left corner)
     * @param max the maximum point of this {@code AlienShip} (lower right corner)
     * @param velocity the velocity of this {@code AlienShip}
     */
    protected AlienShip(final Point location,
                        final boolean originatedAtRight,
                        final boolean impacted,
                        final Point tailPoint,
                        final Reel reel,
                        final Point min,
                        final Point max,
                        final Vector velocity) {
        super(location, impacted, reel, min, max);
        this.originatedAtRight = originatedAtRight;
        this.tailPoint = tailPoint;
        this.velocity = velocity;
    }

    /**
     * A method to expose the tail {@code Point} of this {@code DiveBomber}.
     *
     * @return the tail {@code Point} of this {@code DiveBomber}
     */
    public Point getTailPoint() {
        return tailPoint;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isAlienShip(){
        return true;
    }

    /**
     * A method to indicate whether or not this {@code AlienShip} is a
     * {@code DiveBomber}.
     *
     * @return whether or not this {@code AlienShip} is a {@code DiveBomber}
     */
    public abstract boolean isDiveBomber();

    /**
     * A method to indicate whether or not this {@code AlienShip}
     * originated at right.
     *
     * @return whether or not this {@code AlienShip} originated at right
     */
    public boolean originatedAtRight(){
        return originatedAtRight;
    }

}
