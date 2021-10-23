package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import Engine.Collision.SpaceConfig;
import Engine.Entities.Environment.SpaceDust;
import Engine.Entities.Hittables.*;
import Engine.Entities.Hittables.AlienShips.AlienShip;
import Engine.Entities.Hittables.AlienShips.DiveBomber;
import Engine.Entities.Hittables.AlienShips.Kamikaze;
import Engine.Entities.Environment.Laser;
import Engine.Entities.Entity;
import Engine.Navigation.Point;
import Engine.*;
import Engine.Navigation.Vector;

import javax.swing.Timer;
import static Engine.Navigation.Vector.*;
import static java.awt.event.KeyEvent.*;

public final class GridPanel extends JPanel {

    public static final int TIMER_DELAY = 10;
    public static final Point START_LOCATION = new Point(384, 384);
    public static final Color TEXT_COLOR = new Color(200, 200, 200);
    public static final Color HAZE_COLOR = new Color(35, 20, 20);
    public static final int VERTICAL_BUFF = 16;
    public static final int HORIZONTAL_BUFF = 8;
    public static final int INITIAL_HEARTS = 8;
    public static final int USER_LASER_DELAY = 5;
    public static final int START_TRANSITION_DELAY = 3000;
    public static final int TRANSITION_DELAY = 300;
    public static final int INITIAL_CAPACITY = 200;

    public static final GridPanel INSTANCE = new GridPanel();

    private enum KeyAction {
        MOVE_EAST {
            @Override
            public void perform() {
                INSTANCE.currentDirection = EAST;
            }
        },
        MOVE_WEST {
            @Override
            public void perform() {
                INSTANCE.currentDirection = WEST;
            }
        },
        MOVE_NORTH {
            @Override
            public void perform() {
                INSTANCE.currentDirection = NORTH;
            }
        },
        MOVE_SOUTH {
            @Override
            public void perform() {
                INSTANCE.currentDirection = SOUTH;
            }
        },
        PAUSE {
            @Override
            public void perform() {
                INSTANCE.gameStatus = INSTANCE.gameStatus.pause();
            }
        },
        FIRE {
            @Override
            public void perform() {
                INSTANCE.fireStatus = FireStatus.FIRING;
            }
        },
        NULL { @Override public void perform() {} };

        public abstract void perform();

        public static final HashMap<Integer, KeyAction> KEY_ACTIONS;

        static {
            KEY_ACTIONS = new ChainedMap<Integer, KeyAction>()
                    .place(VK_RIGHT, KeyAction.MOVE_EAST)
                    .place(VK_D, KeyAction.MOVE_EAST)
                    .place(VK_LEFT, KeyAction.MOVE_WEST)
                    .place(VK_A, KeyAction.MOVE_WEST)
                    .place(VK_UP, KeyAction.MOVE_NORTH)
                    .place(VK_W, KeyAction.MOVE_NORTH)
                    .place(VK_DOWN, KeyAction.MOVE_SOUTH)
                    .place(VK_S, KeyAction.MOVE_SOUTH)
                    .place(VK_ESCAPE, KeyAction.PAUSE)
                    .place(VK_SPACE, KeyAction.FIRE);
        }

        public static KeyAction get(final int keyEvent) {
            final KeyAction ka = KEY_ACTIONS.get(keyEvent);
            return ka == null? KeyAction.NULL: ka;
        }

    }

    private static final class ChainedMap<K,V> extends HashMap<K,V> {
        public final ChainedMap<K,V> place(K k, V v){ put(k,v); return this; }
    }

    private enum KeyReleaseAction {
        STOP_MOVING_EAST {
            @Override
            public void perform() {
                if(INSTANCE.currentDirection.isEast()) {
                    INSTANCE.currentDirection = Vector.NULL;
                }
            }
        },
        STOP_MOVING_WEST {
            @Override
            public void perform() {
                if(INSTANCE.currentDirection.isWest()) {
                    INSTANCE.currentDirection = Vector.NULL;
                }
            }
        },
        STOP_MOVING_NORTH {
            @Override
            public void perform() {
                if(INSTANCE.currentDirection.isNorth()) {
                    INSTANCE.currentDirection = Vector.NULL;
                }
            }
        },
        STOP_MOVING_SOUTH {
            @Override
            public void perform() {
                if(INSTANCE.currentDirection.isSouth()) {
                    INSTANCE.currentDirection = Vector.NULL;
                }
            }
        },
        STOP_FIRE {
            @Override
            public void perform() {
                if(INSTANCE.fireStatus.isFiring()) {
                    INSTANCE.fireStatus = FireStatus.NOT_FIRING;
                }
            }
        },
        NULL { @Override public void perform() {} };

        public abstract void perform();

        public static final HashMap<Integer, KeyReleaseAction> KEY_ACTIONS;

        static {
            KEY_ACTIONS = new ChainedMap<Integer, KeyReleaseAction>()
                    .place(VK_RIGHT, KeyReleaseAction.STOP_MOVING_EAST)
                    .place(VK_D, KeyReleaseAction.STOP_MOVING_EAST)
                    .place(VK_LEFT, KeyReleaseAction.STOP_MOVING_WEST)
                    .place(VK_A, KeyReleaseAction.STOP_MOVING_WEST)
                    .place(VK_UP, KeyReleaseAction.STOP_MOVING_NORTH)
                    .place(VK_W, KeyReleaseAction.STOP_MOVING_NORTH)
                    .place(VK_DOWN, KeyReleaseAction.STOP_MOVING_SOUTH)
                    .place(VK_S, KeyReleaseAction.STOP_MOVING_SOUTH)
                    .place(VK_SPACE, KeyReleaseAction.STOP_FIRE);
        }

        public static KeyReleaseAction get(final int keyEvent) {
            final KeyReleaseAction ka = KEY_ACTIONS.get(keyEvent);
            return ka == null? KeyReleaseAction.NULL: ka;
        }

    }

    /**
     * Indicates whether or not the game is running/paused/done.
     */
    private GameStatus gameStatus;

    private FireStatus fireStatus;

    private UserShip userShip;

    /**
     * The current direction of the UserShip.
     */
    private Vector currentDirection;

    /**
     * The current location of the UserShip.
     */
    private Point currentLocation;

    private List<Asteroid> asteroids;

    private List<Laser> userLasers;

    private List<Kamikaze> kamikazes;

    private List<SpaceDust> spaceDust;

    private List<DiveBomber> diveBombers;

    private List<Laser> alienLasers;

    private SpaceConfig<Entity> spaceConfig;

    /**
     * The current level.
     */
    private Level level;

    /**
     * The current score;
     */
    private int score;

    private int levelStartScore;

    private int hearts;

    private int asteroidUpdateCount;

    private int laserUpdateCount;

    private int kamikazeUpdateCount;

    private int spaceDustUpdateCount;

    private int diveBomberUpdateCount;

    private int transitionCount;

    private int levelUpdateCount;

    private TransitionStatus levelTransition;

    private ButtonType buttonType;

    private long engineTime;

    private int frames;

    /**
     * A public constructor for a {@code GUI.GridPanel}.
     */
    private GridPanel(){
        setSize(Utility.PANEL_SIZE);
        setBackground(Color.BLACK);
        //Initialize fields.
        init(Level.DIVE_BOMB, 0);
        //Add keyboard listener and hook up arrow keys + space key + esc key.
        setFocusable(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e){
                if(gameStatus.isGameOver()){
                    if(buttonType.isNo())
                        System.exit(0);
                    else if(buttonType.isYes()){
                        reset();
                        Game.INSTANCE.getScorePanel().update(level, levelStartScore, hearts);
                    }
                } else {
                    gameStatus = gameStatus.pause();
                    updateGUI();
                }
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(final MouseEvent e){
                if(gameStatus.isGameOver()) {
                    buttonType = ButtonType.select(e);
                    updateGUI();
                }
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                        long s = System.nanoTime();
                        final int kc = e.getKeyCode();
                        if(kc == VK_RIGHT || kc == VK_D) {
                            INSTANCE.currentDirection = EAST;
                        } else if(kc == VK_LEFT || kc == VK_A) {
                            INSTANCE.currentDirection = WEST;
                        } else if(kc == VK_UP || kc == VK_W) {
                            INSTANCE.currentDirection = NORTH;
                        } else if(kc == VK_DOWN || kc == VK_S) {
                            INSTANCE.currentDirection = SOUTH;
                        } else if (kc == VK_ESCAPE) {
                            INSTANCE.gameStatus = INSTANCE.gameStatus.pause();
                        } else if (kc == VK_SPACE) {
                            INSTANCE.fireStatus = FireStatus.FIRING;
                        }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                KeyReleaseAction.get(e.getKeyCode()).perform();
            }
        });
        //Start up game timer.
        new Timer(TIMER_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameStatus.isRunning()) update();
                frames++;
                if(System.currentTimeMillis() - engineTime >= 1000) { // For debugging purposes only. Delete this before posting.
                    engineTime = System.currentTimeMillis();
                    System.out.println(frames);
                    frames = 0;
                }
            }
        }).start();
    }

    private enum ButtonType {

        YES {
            @Override public boolean isYes(){  return true; }
            @Override public boolean isNo(){  return false; }
        },
        NO {
            @Override public boolean isYes(){ return false; }
            @Override public boolean isNo(){   return true; }
        },
        NONE {
            @Override public boolean isYes(){ return false; }
            @Override public boolean isNo(){  return false; }
        };

        public static ButtonType select(MouseEvent e){
            final int x = e.getX();
            final int y = e.getY();
            final boolean WITHIN_YES_X_INTERVAL = (x > 200 && x < 358);
            final boolean WITHIN_YES_Y_INTERVAL = (y > 400 && y < 500);
            final boolean WITHIN_NO_X_INTERVAL  = (x > 358 && x < 500);
            final boolean WITHIN_NO_Y_INTERVAL  = (y > 400 && y < 500);
            return (WITHIN_YES_X_INTERVAL && WITHIN_YES_Y_INTERVAL) ? YES:
                    (WITHIN_NO_X_INTERVAL && WITHIN_NO_Y_INTERVAL)? NO: NONE;
        }

        public abstract boolean isYes();
        public abstract boolean isNo();

    }

    /*
     * A method to initialize fields and avoid redundancy.
     */
    private void init(final Level level, final int score){
        this.asteroids = Collections.emptyList();
        this.userLasers = Collections.emptyList();
        this.kamikazes = Collections.emptyList();
        this.spaceDust = Collections.emptyList();
        this.diveBombers = Collections.emptyList();
        this.alienLasers = Collections.emptyList();
        this.spaceConfig = SpaceConfig.emptyConfig(Utility.GRID_WIDTH);
        this.currentLocation = START_LOCATION;
        this.currentDirection = Vector.NULL;
        this.fireStatus = FireStatus.NOT_FIRING;
        this.gameStatus = GameStatus.RUNNING;
        this.buttonType = ButtonType.NONE;
        this.level = level;
        this.score = score;
        this.levelStartScore = score;
        this.hearts = INITIAL_HEARTS;
        this.userShip = UserShip.defaultInstance(currentLocation);
        this.asteroidUpdateCount = 0;
        this.laserUpdateCount = 0;
        this.kamikazeUpdateCount = 0;
        this.spaceDustUpdateCount = 0;
        this.diveBomberUpdateCount = 0;
        this.levelUpdateCount = 0;
        this.transitionCount = 0;
        this.levelTransition = TransitionStatus.ACTIVE;
        this.engineTime = 0;
        this.frames = 0;
    }

    /**
     * Resets the {@code GUI.GridPanel}'s fields to their default values.
     */
    public synchronized void reset(){
        init(level.regress(), levelStartScore);
    }

    public synchronized Level getLevel(){
        return level;
    }

    public synchronized int getScore(){
        return score;
    }

    public synchronized int getHearts(){
        return hearts;
    }

    /**
     * GameStatus
     */
    private enum GameStatus {
        RUNNING {
            /** @inheritDoc */
            @Override
            public boolean isPaused() {
                return false;
            }

            @Override
            public boolean isRunning() {
                return true;
            }

            @Override
            public boolean isGameOver(){
                return false;
            }
        },
        PAUSED {
            /** @inheritDoc */
            @Override
            public boolean isPaused() {
                return true;
            }
            /**
             * {@code inheritDoc}
             *
             * @return the RUNNING {@code GameStatus}
             */
            @Override
            public GameStatus pause(){
                return RUNNING;
            }

            @Override
            public boolean isRunning() {
                return false;
            }

            @Override
            public boolean isGameOver(){
                return false;
            }
        },
        GAME_OVER{
            @Override
            public boolean isPaused() {
                return false;
            }

            @Override
            public boolean isRunning() {
                return false;
            }

            @Override
            public boolean isGameOver(){
                return true;
            }
        };

        /**
         * A method to indicate whether or not the {@code GameStatus} is PAUSED.
         *
         * @return whether or not the {@code GameStatus} is PAUSED
         */
        public abstract boolean isPaused();

        /**
         * A method to indicate whether or not the {@code GameStatus} is RUNNING.
         *
         * @return whether or not the {@code GameStatus} is RUNNING
         */
        public abstract boolean isRunning();

        public abstract boolean isGameOver();

        /**
         * A polymorphic approach to toggling the {@code GameStatus} between
         * PAUSED and RUNNING.
         *
         * @return the PAUSED game status
         */
        public GameStatus pause(){
            return PAUSED;
        }

    }

    private enum FireStatus {
        FIRING {     @Override public boolean isFiring() {  return true; }},
        NOT_FIRING { @Override public boolean isFiring() { return false; }};
        public abstract boolean isFiring();
    }

    private enum TransitionStatus {
        ACTIVE{   @Override public boolean isActive(){  return true; }},
        INACTIVE{ @Override public boolean isActive(){ return false; }};
        public abstract boolean isActive();
    }

    private synchronized void update() {
        if(userShipImpact()) {
            Game.INSTANCE.getScorePanel().update(level,  score, --hearts);
            if (hearts <= 0) {
                userShip = userShip.takeHit();
                updateGUI();
                gameStatus = GameStatus.GAME_OVER;
                return;
            }
        }
        if((currentLocation.y < Utility.VERTICAL_BOUND - 42 && currentDirection.isSouth()) ||
           (currentLocation.x < Utility.HORIZONTAL_BOUND - 20 && currentDirection.isEast()) ||
           (currentLocation.y > 8 && currentDirection.isNorth()) ||
           (currentLocation.x > 24 && currentDirection.isWest()))
            currentLocation = currentDirection.traverse(currentLocation);
        SpaceConfig.Builder<Entity> spaceConfigBuilder = new SpaceConfig.Builder<>(Utility.GRID_WIDTH);
        spaceConfigBuilder.registerEntity(
                userShip = new UserShip.Builder(currentLocation)
                        .setAfterBurner(currentDirection.isNorth()).build()
        );
        final List<SpaceDust> replacementSpaceDust = new ArrayList<>(INITIAL_CAPACITY);
        spaceDustUpdateCount++;
        if(spaceDustUpdateCount >= 5){
            spaceDustUpdateCount = 0;
            replacementSpaceDust.add(SpaceDust.defaultInstance());
        }
        final List<DiveBomber> replacementDiveBombers = new ArrayList<>(INITIAL_CAPACITY);
        final List<Kamikaze> replacementKamikazes = new ArrayList<>(INITIAL_CAPACITY);
        final List<Laser> replacementUserLasers = new ArrayList<>(INITIAL_CAPACITY);
        final List<Asteroid> replacementAsteroids = new ArrayList<>(INITIAL_CAPACITY);
        final List<Laser> replacementAlienLasers = new ArrayList<>(INITIAL_CAPACITY);
        if(levelTransition.isActive()){
            transitionCount++;
            if(transitionCount >= TRANSITION_DELAY) {
                transitionCount = 0;
                hearts = INITIAL_HEARTS;
                levelStartScore = score;
                levelTransition = TransitionStatus.INACTIVE;
                level = level.progress();
                Game.INSTANCE.getScorePanel().update(level, score, hearts);
            }
        }
        else {
            asteroidUpdateCount++;
            if (asteroidUpdateCount >= level.asteroidDelay()) {
                asteroidUpdateCount = 0;
                final Asteroid a = Asteroid.defaultInstance();
                replacementAsteroids.add(a);
                spaceConfigBuilder.registerEntity(a);
            }
            laserUpdateCount++;
            if (laserUpdateCount >= USER_LASER_DELAY) {
                laserUpdateCount = 0;
                if (fireStatus == FireStatus.FIRING) {
                    final Laser l = Laser.userInstance(currentLocation, Vector.NORTH);
                    replacementUserLasers.add(l);
                    spaceConfigBuilder.registerEntity(l);
                }
            }
            kamikazeUpdateCount++;
            if (kamikazeUpdateCount >= level.kamikazeDelay()) {
                kamikazeUpdateCount = 0;
                final Kamikaze k = Kamikaze.defaultInstance();
                replacementKamikazes.add(k);
                spaceConfigBuilder.registerEntity(k);
            }
            diveBomberUpdateCount++;
            if (diveBomberUpdateCount >= level.diveBomberDelay()) {
                diveBomberUpdateCount = 0;
                final DiveBomber d = DiveBomber.defaultInstance(currentLocation);
                replacementDiveBombers.add(d);
                spaceConfigBuilder.registerEntity(d);
            }
            levelUpdateCount++;
            if(levelUpdateCount >= START_TRANSITION_DELAY) {
                levelTransition = TransitionStatus.ACTIVE;
                levelUpdateCount = 0;
            }
        }
        for(SpaceDust sd: spaceDust){
            if(sd.getY() <= Utility.ABSOLUTE_VERTICAL_BOUND)
                replacementSpaceDust.add(sd.scroll());
        }
        for(Laser l: userLasers) {
            if (l.getY() >= 0) {
                final Laser lx = l.pew();
                replacementUserLasers.add(lx);
                spaceConfigBuilder.registerEntity(lx);
            }
        }
        for(Laser al: alienLasers){
            if(al.getY() <= Utility.ABSOLUTE_VERTICAL_BOUND) {
                final Laser alx = al.pew();
                replacementAlienLasers.add(alx);
                spaceConfigBuilder.registerEntity(alx);
            }
        }
        for(Asteroid a: asteroids){
            if (a.getY() <= Utility.ABSOLUTE_VERTICAL_BOUND) {
                final Asteroid ax;
                if(userLaserImpact(a)) ax = a.takeHit();
                else ax = a.scroll();
                replacementAsteroids.add(ax);
                if(!ax.isImpacted()) spaceConfigBuilder.registerEntity(ax);
            }
        }
        for(Kamikaze k: kamikazes){
            if (k.getY() <= Utility.ABSOLUTE_VERTICAL_BOUND + VERTICAL_BUFF &&
                    k.getX() >= -HORIZONTAL_BUFF && k.getX() <= Utility.HORIZONTAL_BOUND + HORIZONTAL_BUFF) {
                final Kamikaze kx;
                if(k.isImpacted()) replacementKamikazes.add(kx = k.scroll());
                else if(userLaserImpact(k)) replacementKamikazes.add(kx = k.takeHit());
                else if ((k.originatedAtRight() && k.getX() >= currentLocation.x + 17) ||
                        (!k.originatedAtRight() && k.getX() <= currentLocation.x - 17))
                    replacementKamikazes.add(kx = k.autopilot());
                else replacementKamikazes.add(kx = k.dive());
                if(!kx.isImpacted()) spaceConfigBuilder.registerEntity(kx);
            }
        }
        for(DiveBomber db: diveBombers){
            if (db.getY() >= -VERTICAL_BUFF && db.getY() <= Utility.ABSOLUTE_VERTICAL_BOUND + VERTICAL_BUFF &&
                    db.getX() >= -HORIZONTAL_BUFF && db.getX() <= Utility.HORIZONTAL_BOUND) {
                final DiveBomber dbx;
                if (db.isImpacted()) replacementDiveBombers.add(dbx = db.scroll());
                else if (userLaserImpact(db)) replacementDiveBombers.add(dbx = db.takeHit());
                else if ((db.isBombReleased() && !db.isImpacted()) ||
                        (db.getX() <= currentLocation.x + 27 && db.getX() >= currentLocation.x - 27) ||
                        db.getY() >= currentLocation.y - 107) {
                    replacementDiveBombers.add(dbx = db.climb());
                    if(!db.isBombReleased()) {
                        final Laser al = Laser.alienInstance(db.getLocation(), Vector.SOUTH);
                        replacementAlienLasers.add(al);
                        spaceConfigBuilder.registerEntity(al);
                    }
                }
                else replacementDiveBombers.add(dbx = db.autopilot());
                if(!dbx.isImpacted()) spaceConfigBuilder.registerEntity(dbx);
            }
        }
        asteroids = lockedList(replacementAsteroids);
        userLasers = lockedList(replacementUserLasers);
        kamikazes = lockedList(replacementKamikazes);
        spaceDust = lockedList(replacementSpaceDust);
        diveBombers = lockedList(replacementDiveBombers);
        alienLasers = lockedList(replacementAlienLasers);
        spaceConfig = spaceConfigBuilder.build();
        updateGUI();
    }

    private static <T> List<T> lockedList(final List<T> list){
        return Collections.synchronizedList(Collections.unmodifiableList(list));
    }

    private void updateGUI(){
       SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                revalidate();
                repaint();
            }
        });
    }

    private boolean userShipImpact(){
        boolean isHit = false;
        for(List<Entity> lp: spaceConfig.getEntitiesNear(userShip)){
            for(Entity p : lp){
                if(p.isAlienShip()) {
                    if ((userShip.isHit(p.getLocation()) || userShip.isHit(((AlienShip)p).getTailPoint()))
                            && !((AlienShip)p).isImpacted()){
                        if(((AlienShip)p).isDiveBomber()) {
                            final List<DiveBomber> replacementDiveBombers = new ArrayList<>(INITIAL_CAPACITY);
                            for(DiveBomber b: diveBombers) if(b != p) replacementDiveBombers.add(b);
                            replacementDiveBombers.add(((DiveBomber) p).takeHit());
                            diveBombers = lockedList(replacementDiveBombers);
                        } else {
                            final List<Kamikaze> replacementKamikazes = new ArrayList<>(INITIAL_CAPACITY);
                            for(Kamikaze k: kamikazes) if(k != p) replacementKamikazes.add(k);
                            replacementKamikazes.add(((Kamikaze) p).takeHit());
                            kamikazes = lockedList(replacementKamikazes);
                        }
                        isHit = true;
                    }
                }
                else if(p.isAsteroid()) {
                    if((userShip.isHit(p.getMin()) || userShip.isHit(p.getMax())) && !((Asteroid) p).isImpacted()) {
                        final List<Asteroid> replacementAsteroids = new ArrayList<>(INITIAL_CAPACITY);
                        for(Asteroid a: asteroids) if(a != p) replacementAsteroids.add(a);
                        replacementAsteroids.add(((Asteroid) p).takeHit());
                        asteroids = lockedList(replacementAsteroids);
                        isHit = true;
                    }
                }
                else if(p.isLaser() && ((Laser) p).isAlienLaser()) {
                    if(userShip.isHit(p.getLocation()) && !((Laser)p).isUsed()){
                        final List<Laser> replacementAlienLasers = new ArrayList<>(INITIAL_CAPACITY);
                        for(Laser a: alienLasers) if(a != p) replacementAlienLasers.add(a);
                        replacementAlienLasers.add(((Laser) p).use());
                        alienLasers = lockedList(replacementAlienLasers);
                        isHit = true;
                    }
                }
            }
        }
        return isHit;
    }

    private boolean userLaserImpact(final Hittable h) {
        if(h.isImpacted()) return false;
        if(!h.isAlienShip() && !h.isAsteroid()) return false;
        for(List<Entity> lp: spaceConfig.getEntitiesNear(h)){
            for(Entity p: lp) {
                if (p.isLaser() && !((Laser) p).isAlienLaser()) {
                    if (h.isHit(p.getLocation())) {
                        score += h.isAsteroid() ? 3000 : ((AlienShip) h).isDiveBomber() ? 1500 : 2000;
                        Game.INSTANCE.getScorePanel().update(level, score, hearts);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * This method is responsible for painting the {@code GridPanel}
     *
     * @param g the panel's {@code Graphics} Object.
     */
    @Override
    public void paintComponent(final Graphics g){
        g.clearRect(0,0, Game.FRAME_WIDTH, Game.FRAME_WIDTH);
        g.drawRect(0,0, Game.FRAME_WIDTH, Game.FRAME_WIDTH);
        g.setColor(Color.BLACK);
        g.fillRect(0,0, Game.FRAME_WIDTH, Game.FRAME_WIDTH);
        //if(level == Level.BOSS) (boss = boss.writhe()).paint(g);
        for(Laser l: userLasers) l.paint(g);
        for(Laser al: alienLasers) al.paint(g);
        for(SpaceDust sb: spaceDust) sb.paint(g);
        for(Asteroid a: asteroids) a.paint(g);
        for(Kamikaze k: kamikazes) k.paint(g);
        for(DiveBomber db: diveBombers) db.paint(g);
        userShip.paint(g);
        if(gameStatus == GameStatus.GAME_OVER) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(HAZE_COLOR);
            g2.setComposite(AlphaComposite.SrcOver.derive(0.4f));
            g2.fillRect(0, 0, Utility.GRID_WIDTH, Utility.GRID_WIDTH);
            g2.setComposite(AlphaComposite.SrcOver);
            g2.setColor(TEXT_COLOR);
            g2.setFont(new Font("Times_Roman", Font.BOLD, 50));
            g2.drawString("REMAIN CALM", 195, 300);
            g2.setFont(new Font("Times_Roman", Font.PLAIN, 20));
            g2.drawString("You are experiencing space death.", 217, 350);
            g2.drawString("Restart level?", 316, 400);
            if(buttonType.isYes()) g2.setColor(Color.DARK_GRAY);
            g2.drawString(" Yes", 316, 425);
            if(buttonType.isNo()) g2.setColor(Color.DARK_GRAY);
            else g2.setColor(TEXT_COLOR);
            g2.drawString("No", 400, 425);

        }
    }

}
