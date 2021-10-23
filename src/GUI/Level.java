package GUI;

import java.util.HashMap;
import java.util.Map;

public enum Level {
    START('A', "Start") {
        @Override
        public Level regress() {
            return BOSS;
        }

        @Override
        public Level progress() {
            return INTRO;
        }

        @Override
        public int asteroidDelay(){
            return Integer.MAX_VALUE;
        }

        @Override
        public int kamikazeDelay(){
            return Integer.MAX_VALUE;
        }

        @Override
        public int diveBomberDelay(){
            return Integer.MAX_VALUE;
        }
    },
    INTRO('B', "Intro") {
        @Override
        public Level regress() {
            return START;
        }

        @Override
        public Level progress() {
            return ASTEROIDS;
        }

        @Override
        public int asteroidDelay(){
            return 30;
        }

        @Override
        public int kamikazeDelay(){
            return Integer.MAX_VALUE;
        }

        @Override
        public int diveBomberDelay(){
            return Integer.MAX_VALUE;
        }
    },
    ASTEROIDS('C', "Asteroid Field") {
        @Override
        public Level regress() {
            return INTRO;
        }

        @Override
        public Level progress() {
            return KAMIKAZE;
        }

        @Override
        public int asteroidDelay(){
            return 10;
        }

        @Override
        public int kamikazeDelay(){
            return Integer.MAX_VALUE;
        }

        @Override
        public int diveBomberDelay(){
            return Integer.MAX_VALUE;
        }
    },
    KAMIKAZE('D', "Kamikaze") {
        @Override
        public Level regress() {
            return ASTEROIDS;
        }

        @Override
        public Level progress() {
            return DIVE_BOMB;
        }

        @Override
        public int asteroidDelay(){
            return 10;
        }

        @Override
        public int kamikazeDelay(){
            return 30;
        }

        @Override
        public int diveBomberDelay(){
            return Integer.MAX_VALUE;
        }
    },
    DIVE_BOMB('E', "Dive Bomb") {
        @Override
        public Level regress() {
            return KAMIKAZE;
        }

        @Override
        public Level progress() {
            return CONCERT;
        }

        @Override
        public int asteroidDelay(){
            return 10;
        }

        @Override
        public int kamikazeDelay(){
            return Integer.MAX_VALUE;
        }

        @Override
        public int diveBomberDelay(){
            return 30;
        }
    },
    CONCERT('F', "Concert") {
        @Override
        public Level regress() {
            return DIVE_BOMB;
        }

        @Override
        public Level progress() {
            return FRENZY;
        }

        @Override
        public int asteroidDelay(){
            return 10;
        }

        @Override
        public int kamikazeDelay(){
            return 30;
        }

        @Override
        public int diveBomberDelay(){
            return 30;
        }
    },
    FRENZY('G', "Frenzy") {
        @Override
        public Level regress() {
            return CONCERT;
        }

        @Override
        public Level progress() {
            return FLOOD;
        }

        @Override
        public int asteroidDelay(){
            return 10;
        }

        @Override
        public int kamikazeDelay(){
            return 15;
        }

        @Override
        public int diveBomberDelay(){
            return 15;
        }
    },
    FLOOD('H', "Flood") {
        @Override
        public Level regress() {
            return FRENZY;
        }

        @Override
        public Level progress(){
            return BOSS;
        }

        @Override
        public int asteroidDelay(){
            return 10;
        }

        @Override
        public int kamikazeDelay(){
            return 5;
        }

        @Override
        public int diveBomberDelay(){
            return 5;
        }
    },
    BOSS ('I', "Boss") {
        @Override
        public Level regress() {
            return FLOOD;
        }

        public Level progress() {
            return START;
        }

        @Override
        public int asteroidDelay(){
            return Integer.MAX_VALUE;
        }

        @Override
        public int kamikazeDelay(){
            return Integer.MAX_VALUE;
        }

        @Override
        public int diveBomberDelay(){
            return Integer.MAX_VALUE;
        }
    };

    private final char representative;
    private final String title;

    Level(final char representative, final String title){
        this.representative = representative;
        this.title = title;
    }

    @Override
    public String toString(){
        return title;
    }

    public static final Map<Character, Level> VALUES = initValues();

    public static Map<Character, Level> initValues(){
        final Map<Character, Level> values = new HashMap<>();
        for(Level l: values()){
            values.put(l.representative, l);
        }
        return values;
    }

    public abstract Level regress();
    public abstract Level progress();
    public abstract int asteroidDelay();
    public abstract int kamikazeDelay();
    public abstract int diveBomberDelay();

}
