package Engine.Entities.Environment;

import Engine.Navigation.Vector;
import Engine.Utility.Image;
import Engine.Navigation.Point;
import Engine.Entities.Entity;

import java.awt.*;

/**
 * Laser
 *
 * @author Ellie Moore
 * @version 12.12.2020
 */
public class Laser extends Entity {

    private static final int MIN_OFFSET = 4;
    private static final int IMAGE_SIZE = 72;
    private static final int PAINT_X_OFFSET = 34;
    private static final int PAINT_Y_OFFSET = 62;

    /**
     * The velocity of this {@code Laser}.
     */
    private final Vector velocity;

    /**
     * A private constructor for a {@code Laser}
     *
     * @param loc the location of this {@code Laser}
     * @param velocity the velocity of this {@code Laser}
     */
    private Laser(final Point loc, final Vector velocity){
        super(velocity.traverse(loc), loc, new Point(loc.x + MIN_OFFSET, loc.y + MIN_OFFSET));
        this.velocity = velocity;
    }

    /**
     * A static factory method for a user instance of {@code Laser}.
     *
     * @param loc the location of this {@code Laser}
     * @param velocity velocity the velocity of this {@code Laser}
     * @return a new user instance of {@code Laser}
     */
    public static Laser userInstance(final Point loc, final Vector velocity){
        return new Laser(loc, velocity);
    }

    /**
     * A method to instantiate a new {@code Laser} at a new location according
     * to this {@code Laser}'s velocity.
     *
     * @return a new {@code Laser}
     */
    public Laser pew() {
        return new Laser(location, velocity);
    }

    /**
     * A method to instantiate a new {@code Laser} at at a new location according
     * to this {@code Laser}'s velocity, toggling the used field to true.
     *
     * @return a new {@code Laser}
     */
    public Laser use() {
        return this;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(Image.LASER, location.x - PAINT_X_OFFSET, location.y, IMAGE_SIZE, IMAGE_SIZE, null);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isLaser(){
        return true;
    }

    /**
     * Specifies whether or not this {@code Laser} is a user instance.
     *
     * @return whether or not this {@code Laser} is a user instance.
     */
    public boolean isAlienLaser(){
        return false;
    }

    /**
     * Specifies whether or not this {@code Laser} has impacted its target.
     *
     * @return whether or not this {@code Laser} has impacted its target.
     */
    public boolean isUsed(){
        return false;
    }

    /**
     * A static factory method for an alien instance of {@code Laser}.
     *
     * @param loc the location of this {@code Laser}
     * @param velocity the velocity of this {@code Laser}
     * @return an alien instance of {@code Laser}
     */
    public static AlienLaser alienInstance(final Point loc, final Vector velocity){
        return new AlienLaser(loc, velocity, false);
    }

    /**
     * Alien Laser
     */
    private static final class AlienLaser extends Laser {

        /**
         * Whether or not this Laser has already dealt damage to its target.
         */
        private final boolean isUsed;

        /**
         * A public constructor for an {@code AlienLaser}. The specified
         * {@code Direction} is traversed once here and again in the super
         * constructor.
         *
         * @param loc the previous location of the {@code AlienLaser}
         * @param velocity the velocity of the {@code AlienLaser}
         */
        private AlienLaser(final Point loc, final Vector velocity, final boolean isUsed) {
            super(velocity.traverse(loc), velocity);
            this.isUsed = isUsed;
        }

        /**
         * @inheritDoc
         */
        @Override
        public Laser pew() {
            return new AlienLaser(location, super.velocity, isUsed);
        }

        /**
         * @inheritDoc
         */
        @Override
        public Laser use() {
            return new AlienLaser(location, super.velocity, true);
        }

        /**
         * @inheritDoc
         */
        @Override
        public void paint(Graphics g){
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(
                    Image.ALIEN_LASER, location.x - PAINT_X_OFFSET,
                    location.y - PAINT_Y_OFFSET, IMAGE_SIZE, IMAGE_SIZE, null
            );
        }

        /**
         * @inheritDoc
         */
        @Override
        public boolean isAlienLaser(){
            return true;
        }

        /**
         * @inheritDoc
         */
        @Override
        public boolean isUsed(){
            return isUsed;
        }

    }

}
