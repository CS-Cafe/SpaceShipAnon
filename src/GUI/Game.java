package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Game.
 */
public final class Game {

    /**
     * A singleton instance of {@code Game}.
     */
    public static final Game INSTANCE;

    /**
     * The color of this{@code Game}'s {@code ScorePanel} component.
     */
    public static final Color SCORE_PANEL_COLOR;

    /**
     * The path to this {@code Game}'s Icon.
     */
    public static final String ICON_PATH;

    /**
     * The width of the game frame.
     */
    public static final int FRAME_WIDTH;

    /**
     * Dimension for use in game frame initialization.
     */
    public static final Dimension GAME_FRAME_SIZE;

    /* init */
    static {
        SCORE_PANEL_COLOR = new Color(30,0,0);
        FRAME_WIDTH = 768;
        GAME_FRAME_SIZE = new Dimension(FRAME_WIDTH, FRAME_WIDTH);
        ICON_PATH = "C:/Users/evcmo/IdeaProjects/SpaceShip-main/icon/meteor3.png";
        INSTANCE = new Game();
    }

    /**
     * A Frame to hold the GUI.
     */
    private final JFrame gameFrame;

    /**
     * A Panel on which the game will be painted and updated.
     */
    private final GridPanel gamePanel;

    /**
     * A Panel to keep track of level and score.
     */
    private final ScorePanel scorePanel;

    /**
     * A private constructor for {@code Game}.
     */
    private Game(){
        gameFrame = new JFrame("Starfall");
        try { gameFrame.setIconImage(ImageIO.read(new File(ICON_PATH))); }
        catch(IOException e){ e.printStackTrace(); }
        gameFrame.setSize(GAME_FRAME_SIZE);
        gameFrame.setLayout(new BorderLayout());
        gamePanel = GridPanel.INSTANCE;
        scorePanel = new ScorePanel(gamePanel);
        gameFrame.add(scorePanel, BorderLayout.NORTH);
        gameFrame.add(gamePanel, BorderLayout.CENTER);
        gameFrame.setResizable(false);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setVisible(true);
    }

    /**
     * Exposes the {@code Game}'s {@code JFrame}.
     *
     * @return the {@code JFrame}
     */
    public final JFrame getFrame(){
        return gameFrame;
    }

    /**
     * Exposes the {@code Game}'s {@code ScorePanel}.
     *
     * @return the {@code ScorePanel}
     */
    public final ScorePanel getScorePanel(){
        return scorePanel;
    }

    /**
     * Exposes the {@code Game}'s {@code GridPanel}.
     *
     * @return the {@code GridPanel}
     */
    public final GridPanel getGridPanel(){
        return gamePanel;
    }

    //Show.
    public static void main(String[] args){
        Game.INSTANCE.gameFrame.setVisible(true);
    }

    /**
     * A method to reset the {@code Game}'s GUI components.
     */
    public final void reset(){
        gamePanel.reset();
        scorePanel.reset();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gameFrame.getContentPane().revalidate();
                gameFrame.getContentPane().repaint();
            }
        });
        Set<Thread> threads = Thread.getAllStackTraces().keySet();
        for (Thread t : threads) {
            String name = t.getName();
            Thread.State state = t.getState();
            int priority = t.getPriority();
            String type = t.isDaemon() ? "Daemon" : "Normal";
            System.out.printf("%-20s \t %s \t %d \t %s\n", name, state, priority, type);
        }
        System.out.println("\n\n\n\n");
    }

    /**
     * Score Panel
     */
    public static final class ScorePanel extends JPanel {

        /**
         * A public constructor for a {@code ScorePanel}.
         */
        public ScorePanel(final GridPanel gamePanel) {
            super();
            setBackground(SCORE_PANEL_COLOR);
            add(updateLabel(gamePanel.getLevel(),gamePanel.getScore(), gamePanel.getHearts()));
            setVisible(true);
        }

        /**
         * A method to update the {@code ScorePanel}'s text label.
         *
         * @param level the current level
         * @param score the current score
         */
        public final void update(final Level level,
                                 final int score,
                                 final int hearts){
            removeAll();
            add(updateLabel(level, score, hearts));
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    validate();
                    repaint();
                }
            });
        }

        /**
         * A method to reset the {@code ScorePanel}.
         */
        public final void reset(){
            update(Level.START,0, 5);
        }

        // Returns a new label given a level and score.
        private static JLabel updateLabel(final Level level,
                                          final int score,
                                        final int hearts){

            final JLabel label = new JLabel();
            label.setText(String.format(
                    "Level: %s.  \"%s\"       Score: %s       Shield: %s",
                    level.ordinal(), level.toString(), score, assembleHearts(hearts)
            ));
            label.setForeground(Color.WHITE);
            return label;
        }

        private static final String assembleHearts(final int num){
            StringBuilder str = new StringBuilder();
            for(int i = 0; i < num; i++){
                str.append("\u2764");
            }
            return str.toString();
        }

    }

}
