package Engine.Navigation;

/**
 * Point
 *
 * <p>
 * An immutable re-creation of awt Point.
 */
public final class Point {

    public static final Point NULL = new Point(-1, -1);

    /**
     * Public coordinate fields
     */
    public final int x;
    public final int y;

    /**
     * A constructor for a {@code Point}.
     */
    public Point(final int x, final int y){
        this.x = x;
        this.y = y;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean equals(Object other){
        if(this == other) return true;
        if(other == null) return false;
        if(!(other instanceof Point)) return false;
        Point cast = (Point) other;
        return this.x == cast.x && this.y == cast.y;
    }

    /**
     * @inheritDoc
     */
    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + x;
        return 31 * hash + y;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString(){
        return "[" + x + ", " + y + "]";
    }

}
