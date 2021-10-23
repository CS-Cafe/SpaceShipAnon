package Engine.Entities.Hittables;

import Engine.Navigation.Point;
import Engine.Animation.Reel;
import Engine.Navigation.Vector;
import Engine.Utility.Image;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * User Ship
 */
public final class UserShip extends Hittable {

    private final BufferedImage image;

    /**
     * A public constructor for a {@code UserShip}.
     *
     * @param builder this {@code UserShip}'s {@code Builder}
     */
    private UserShip(final Builder builder){
        super(
                builder.location, builder.impacted, Reel.NULL,
                new Point(builder.location.x - 22, builder.location.y - 5),
                new Point(builder.location.x + 26, builder.location.y + 45)
        );
        image = impacted? Image.IMPACTED_USER_SHIP: builder.afterBurner?
                Image.AFTER_BURNER_USER_SHIP: Image.USER_SHIP;
    }

    public static UserShip defaultInstance(final Point location){
        return new Builder(location).build();
    }

    @Override
    public UserShip takeHit(){
        return new Builder(location).setImpacted(true).build();
    }

    @Override
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(
                    image, location.x - 37, location.y - 5,
                    78, 78, null
        );
        //g2.drawRect(location.x - 22, location.y - 5, 48, 50);
        //g2.drawRect(location.x -7, location.y - 5, 18, 50);
        //g2.drawRect(location.x - 27, location.y + 20, 20, 30);
        //g2.drawRect(location.x + 11, location.y + 20, 20, 30);
            /*for(double i = 0; i <= Math.PI * 2; i+= (Math.PI/8)) {
                Vector.newInstance(i, expandingMag).paint(g, location);
            }*/
    }

    @Override
    public boolean isHit(final Point loc){
        return location.x - 7 < loc.x && loc.x < location.x + 11 &&
               location.y - 5 < loc.y && loc.y < location.y + 50 ||
               (location.x - 27 < loc.x && loc.x < location.x - 7 ||
               location.x + 11 < loc.x && loc.x < location.x + 31) &&
               location.y + 20 < loc.y && loc.y < location.y + 50;
    }

    @Override
    public boolean isAsteroid(){
        return false;
    }

    public boolean isAlienShip() {
        return false;
    }

    public static final class Builder {

        private Point location;
        private boolean afterBurner;
        private boolean impacted;

        public Builder(final Point location){
            this.location = location;
            this.afterBurner = false;
            this.impacted = false;
        }

        public Builder setAfterBurner(final boolean afterBurner){
            this.afterBurner = afterBurner;
            return this;
        }

        public Builder setImpacted(final boolean impacted) {
            this.impacted = impacted;
            return this;
        }

        public UserShip build(){
            return new UserShip(this);
        }

    }

}
