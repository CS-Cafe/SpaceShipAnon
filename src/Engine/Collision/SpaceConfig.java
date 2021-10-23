package Engine.Collision;

import Engine.Entities.Entity;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

/**
 * Space Config
 *
 * <p>
 * A {@code SpaceConfig} is a manager for all of the space on a game panel.
 * Each entity is stored within a bucket (or buckets) that corresponds to its
 * location. This configuration allows for the optimization of client collision
 * checking algorithms. Initialization is O(N) and can be accomplished in parallel
 * to entity movement routines. Checking an entity for collision requires O(B)
 * time to cycle through the corresponding bucket(s). Checking all entities
 * for collision takes O(NB) time.
 *
 * <p>
 * Collision checking without a {@code SpaceConfig} is potentially an O(N^2)
 * operation. For example, if 100 objects occupy the panel space, each object
 * must be checked against all 99 of its co-inhabitants. However, with a
 * {@code SpaceConfig}, each entity only needs to be checked against a handful
 * of its neighbours, requiring at most as many checks as the naive algorithm
 * in a worst-case scenario.
 *
 * <p>
 * Each bucket represents a 64x64 cell on a {@code JPanel}.
 *
 * @author Ellie Moore
 * @version 10.30.2020
 * @param <E>
 */
public final class SpaceConfig<E extends Entity> {

    /*
     * Logarithm, base 2, of 64.
     */
    private static final int LOG_64 = 6;

    /*
     * 00111111, for calculating mod 64.
     */
    private static final int MASK = 0x3F;

    /**
     * The list of buckets within this {@code SpaceConfig}.
     */
    private final List<List<E>> activeEntities;

    /**
     * The number of columns in this {@code SpaceConfig}.
     */
    private final int columns;

    /**
     * A private constructor for {@code Builder} use.
     *
     * @param builder the builder to be used in construction.
     */
    private SpaceConfig(final Builder<E> builder){
        this.activeEntities = Collections.unmodifiableList(builder.activeEntities);
        this.columns = builder.columns;
    }

    /**
     * A private constructor for factory use.
     *
     * @param width the width of this {@code SpaceConfig}.
     */
    private SpaceConfig(final int width){
        this.activeEntities = Collections.unmodifiableList((new Builder<E>(width)).activeEntities);
        this.columns = width >>> LOG_64;
    }

    /**
     * A factory method to initialize an empty {@code SpaceConfig}.
     *
     * @param width the width of this {@code SpaceConfig}. Must be a multiple of 64.
     * @param <E> the type parameter
     * @return an empty {@code SpaceConfig}.
     */
    public static <E extends Entity> SpaceConfig<E> emptyConfig(final int width) {
        if((width & MASK) != 0) throw new IllegalArgumentException();
        return new SpaceConfig<>(width);
    }

    /**
     * A method to return a list of lists representative of each bucket in which the given
     * entity resides.
     *
     * @param e the entity to look for.
     * @return a two-dimensional {@code List} of entities.
     */
    public List<List<E>> getEntitiesNear(final E e){
        if(e.getMin().x >= 0 && e.getMin().y >= 0) {
            final int bucketMin = (e.getMin().x >>> LOG_64) + ((e.getMin().y >>> LOG_64) * columns);
            final int bucketMax = (e.getMax().x >>> LOG_64) + ((e.getMax().y >>> LOG_64) * columns);
            if (bucketMin == bucketMax)
                return List.of(Collections.unmodifiableList(activeEntities.get(bucketMin)));
            else if (bucketMax - bucketMin == columns + 1)
                return List.of(
                        Collections.unmodifiableList(activeEntities.get(bucketMin)),
                        Collections.unmodifiableList(activeEntities.get(bucketMax)),
                        Collections.unmodifiableList(activeEntities.get(bucketMax - 1)),
                        Collections.unmodifiableList(activeEntities.get(bucketMin + 1))
                );
            else return List.of(
                    Collections.unmodifiableList(activeEntities.get(bucketMin)),
                    Collections.unmodifiableList(activeEntities.get(bucketMax))
            );
        }
        return Collections.emptyList();
    }

    /**
     * Space Config - Builder
     *
     * <p>
     * A {@code Builder} is responsible for the dynamic construction of a {@code SpaceConfig}.
     *
     * @version 10.30.2020
     * @param <E>
     */
    public static final class Builder<E extends Entity> {

        /**
         * The entities to be passed on to the {@code SpaceConfig}.
         */
        private final List<List<E>> activeEntities;

        /**
         * The number of columns to be passed on to the {@code SpaceConfig}.
         * (Equivalent to the number of rows)
         */
        private final int columns;

        /**
         * The width of the panel that the {@code SpaceConfig} will manage.
         * (Equivalent to the length of said panel)
         */
        private final int width;

        /**
         * A public constructor for a {@code Builder}
         *
         * @param width the width of the panel that the {@code SpaceConfig} will manage.
         *              Must be a multiple of 64.
         */
        public Builder(final int width){
            if((width & MASK) != 0) throw new IllegalArgumentException();
            this.columns = width >>> LOG_64;
            final int bucketCount = columns * columns;
            this.activeEntities = new ArrayList<>(bucketCount);
            for(int i = 0; i < bucketCount; i++) activeEntities.add(new ArrayList<>());
            this.width = width;
        }

        /**
         * A method to register an entity for the {@code SpaceConfig}.
         *
         * @param e the entity to register
         */
        public void registerEntity(final E e){
            if(e.getMin().x >= 0 && e.getMin().y >= 0 && e.getMax().x < width && e.getMax().x < width) {
                final int bucketMin = (e.getMin().x >>> LOG_64) + ((e.getMin().y >>> LOG_64) * columns);
                final int bucketMax = (e.getMax().x >>> LOG_64) + ((e.getMax().y >>> LOG_64) * columns);
                if (bucketMin == bucketMax)
                    activeEntities.get(bucketMin).add(e);
                else if (bucketMax - bucketMin == columns + 1) {
                    activeEntities.get(bucketMin).add(e);
                    activeEntities.get(bucketMax).add(e);
                    activeEntities.get(bucketMax - 1).add(e);
                    activeEntities.get(bucketMin + 1).add(e);
                } else {
                    activeEntities.get(bucketMin).add(e);
                    activeEntities.get(bucketMax).add(e);
                }
            }
        }

        /**
         * A method to instantiate a {@code SpaceConfig} from this builder.
         *
         * @return a new {@code SpaceConfig}.
         */
        public SpaceConfig<E> build(){
            return new SpaceConfig<>(this);
        }

    }

}
