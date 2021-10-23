package Engine.Entities;

import Engine.Navigation.Point;
import GUI.Paintable;

/**
 * Entity
 */
public abstract class Entity implements Paintable {

    protected final Point location;
    protected final Point min;
    protected final Point max;

    protected Entity(final Point location,
                     final Point min,
                     final Point max){
        this.location = location;
        this.min = min;
        this.max = max;
    }

    /**
     * Exposes the horizontal coordinate.
     */
    public final int getX(){
        return location.x;
    }

    /**
     * Exposes the vertical coordinate.
     */
    public final int getY(){
        return location.y;
    }

    public Point getMax(){
        return max;
    }

    public Point getMin(){
        return min;
    }

    /**
     * Exposes the location.
     */
    public final Point getLocation(){
        return location;
    }

    public boolean isAsteroid(){
        return false;
    }

    public boolean isAlienShip(){
        return false;
    }

    public boolean isLaser(){
        return false;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean equals(Object other){
        if(this == other) return true;
        if(other == null) return false;
        if(!(other instanceof Entity)) return false;
        Entity cast = (Entity) other;
        return this.location.equals(cast.location);
    }

    /**
     * @inheritDoc
     */
    @Override
    public int hashCode(){
        return location.hashCode();
    }

}
