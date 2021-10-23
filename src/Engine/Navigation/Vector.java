package Engine.Navigation;

import GUI.Paintable;

import java.awt.*;

public class Vector {

    public static final Vector NULL = new Vector(0, 0){
        @Override
        public Point traverse(Point p) {
            return p;
        }
    };

    public static final int DEFAULT_MAGNITUDE = 4;
    public static final double PI_2 = Math.toRadians(90);
    public static final double PI_3_2 = Math.toRadians(270);
    public static final Vector NORTH = north(DEFAULT_MAGNITUDE);
    public static final Vector SOUTH = south(DEFAULT_MAGNITUDE);
    public static final Vector EAST = east(DEFAULT_MAGNITUDE);
    public static final Vector WEST = west(DEFAULT_MAGNITUDE);

    private final Point head;
    private final double angle;
    private final int magnitude;

    private Vector(final double angle, final int magnitude){
        this.head = new Point(
                (int) (Math.floor(Math.cos(angle) * magnitude)),
                (int) (Math.floor(Math.sin(angle) * magnitude))
        );
        this.angle = angle;
        this.magnitude = magnitude;
    }

    private Vector(final int x, final int y, final double angle, final int magnitude){
        this.head = new Point(x, y);
        this.angle = angle;
        this.magnitude = magnitude;
    }

    public static Vector traversableInstance(final double angle, final int magnitude){
        return new Vector(angle, magnitude);
    }

    public static Vector south(final int magnitude){
        return new Vector(0, magnitude, PI_2, magnitude){
            @Override
            public boolean isNorth() {
                return false;
            }

            @Override
            public boolean isSouth() {
                return true;
            }

            @Override
            public boolean isEast() {
                return false;
            }

            @Override
            public boolean isWest() {
                return false;
            }
        };
    }

    public static Vector north(final int magnitude) {
        return new Vector(0, -magnitude, PI_3_2, magnitude) {
            @Override
            public boolean isNorth() {
                return true;
            }

            @Override
            public boolean isSouth() {
                return false;
            }

            @Override
            public boolean isEast() {
                return false;
            }

            @Override
            public boolean isWest() {
                return false;
            }
        };
    }

    public static Vector east(final int magnitude) {
        return new Vector(magnitude, 0, 0, magnitude){
            @Override
            public boolean isNorth() {
                return false;
            }

            @Override
            public boolean isSouth() {
                return false;
            }

            @Override
            public boolean isEast() {
                return true;
            }

            @Override
            public boolean isWest() {
                return false;
            }
        };
    }

    public static Vector west(final int magnitude) {
        return new Vector(-magnitude, 0, Math.PI, magnitude){
            @Override
            public boolean isNorth() {
                return false;
            }

            @Override
            public boolean isSouth() {
                return false;
            }

            @Override
            public boolean isEast() {
                return false;
            }

            @Override
            public boolean isWest() {
                return true;
            }
        };
    }

    public double getAngle(){
        return angle;
    }

    public double getMagnitude() {
        return magnitude;
    }
    
    public Point traverse(final Point p) {
        return new Point(p.x + head.x, p.y + head.y);
    }
    
    public boolean isNorth() {
        return head.x == 0 && head.y < 0;
    }
    
    public boolean isSouth() {
        return head.x == 0 && head.y > 0;
    }
    
    public boolean isEast() {
        return head.x > 0 && head.y == 0;
    }
    
    public boolean isWest() {
        return head.x < 0 && head.y == 0;
    }

    public static PaintVector paintableInstance(final Point origin, final double angle, final int mag){
        return new PaintVector(origin, angle, mag);
    }

    private static final class PaintVector extends Vector implements Paintable {

        private final Point origin;

        private PaintVector(final Point origin, final double angle, final int magnitude){
            super(angle, magnitude);
            this.origin = origin;
        }

        @Override
        public Point traverse(final Point p) {
            throw new UnsupportedOperationException("Paint Vector is not traversable.");
        }

        @Override
        public boolean isNorth() {
            return super.head.x == origin.x && super.head.y < origin.y;
        }

        @Override
        public boolean isSouth() {
            return super.head.x == origin.x && super.head.y > origin.y;
        }

        @Override
        public boolean isEast() {
            return super.head.x > origin.x && super.head.y == origin.y;
        }

        @Override
        public boolean isWest() {
            return super.head.x > origin.x && super.head.y == origin.y;
        }

        @Override
        public void paint(Graphics g) {
            g.setColor(Color.RED);
            g.drawLine(origin.x, origin.y, super.head.x, super.head.y);
        }

    }

}
