package Engine.Entities.Hittables;

import Engine.Entities.Entity;
import Engine.Navigation.Point;
import Engine.Animation.Reel;

public abstract class Hittable extends Entity {

    protected final boolean impacted;
    protected final Reel impactedReel;

    protected Hittable(final Point location,
                       final boolean impacted,
                       final Reel reel,
                       final Point min,
                       final Point max) {
        super(location, min, max);
        this.impacted = impacted;
        this.impactedReel = reel.nextFrame();
    }

    public abstract Hittable takeHit();
    public abstract boolean isHit(Point loc);

    /**
     * A method to expose the impacted field of this {@code HittablePixelElement}.
     *
     * @return whether or not this {@code HittablePixelElement} has been impacted
     */
    public boolean isImpacted(){
        return impacted;
    }

}
