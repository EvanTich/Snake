import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 12/10/2016
 */
public class Main extends JFrame {

    public static JFrame frame;
    private static boolean paused;
    // public static ScoreKeeper scoreKeeper;

    public static void main(String[] args) {
        frame = new JFrame("Snake");
        // scoreKeeper = new ScoreKeeperSQL();

        SnakeGame game = new SnakeGame();
        EasyKey key = new EasyKey();

        frame.add(game, BorderLayout.CENTER);
        frame.addKeyListener(key);
        frame.setFocusable(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(ScoreKeeperSQL.close())
                    System.out.println("SQL database closed.");

                System.out.println("Game closing...");
            }
        });

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                game.updateWindow(frame);
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                super.componentHidden(e);
                paused = true;
            }

            @Override
            public void componentShown(ComponentEvent e) {
                super.componentHidden(e);
                paused = false;
            }
        });

        frame.pack();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // set in the middle of the screen
        frame.setVisible(true);

        frame.requestFocus();

        long lastTime = System.nanoTime(),
                timer = System.currentTimeMillis();
        int frames = 0;

        final double ns = 1e+9 / 15; // nano seconds per frame, 15 fps

        double delta = 0;

        boolean running = true;
        paused = false;

        while(running) {
            if(!paused) {
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;

                while (delta >= 1) {
                    game.update();
                    delta--;
                    frames++;
                }

                // its been a second!
                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    frame.setTitle(String.format("Snake | Score: %1d | %2d fps", game.score(), frames));
                    frames = 0;
                }
            }

            if(EasyKey.getKey() == 'h')
                showHighscores();

            if(EasyKey.getKey() == 27)
                running = false;
        }

        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    public static void showHighscores() {
        paused = true;
        EasyKey.emptyKey();

        final JDialog dialog = new JDialog(Main.frame, "Highscores");

        dialog.setMinimumSize(new Dimension(275, 275));

        JPanel panel = new JPanel(new BorderLayout());
        JLabel header = new JLabel("<html><center>HIGHSCORES<br>NAME|SCORE<br>------------------------------<br></center></html>");

        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setPreferredSize(new Dimension(250, 50));

        panel.add(header, BorderLayout.NORTH);

        String scoreStrings = "<html><center>";
        for(ScoreKeeper.Score score : ScoreKeeperSQL.getScores())
            scoreStrings += score + "<br>";
        scoreStrings += "</center></html>";

        JLabel scoresLabel = new JLabel(scoreStrings);
        scoresLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JScrollPane scoresPane = new JScrollPane(scoresLabel);

        panel.add(scoresPane, BorderLayout.CENTER);

        JButton exit = new JButton("Close");

        exit.setPreferredSize(new Dimension(100, 25));
        exit.addActionListener(e -> {
            dialog.dispose();
            paused = false;
            EasyKey.emptyKey();
        });

        panel.add(exit, BorderLayout.SOUTH);

        dialog.add(panel);

        dialog.setFocusable(true);
        dialog.setLocationRelativeTo(Main.frame);
        dialog.setVisible(true);
        dialog.requestFocus();
    }
}
