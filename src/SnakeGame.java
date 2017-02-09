import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 12/10/2016
 */
public class SnakeGame extends JPanel {

    public static int
            TILESX = 25,
            TILESY = 25,
            SIZE = 25, // block SIZE
            EATSIZE = 2;

    private Snake snake;
    private Point food;

    private static boolean paused;

    public static SnakeGame instance;

    public SnakeGame() {
        super();
        System.out.println("Game initialized!");

        setPreferredSize( new Dimension(TILESX*SIZE, TILESY*SIZE) );

        instance = this;

        startGame();
    }

    public void update() {
        // get keys
        if(!paused) {
            keys();

            // if the snake ate the food
            if (snake.eat(food)) {
                // add the size and make a new food
                snake.addSize(EATSIZE);
                food = makeFood();
            }

            snake.update(); // update the snake
            if (snake.isDead() || score() > TILESX*TILESY - EATSIZE)
                reset();

            repaint();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        setOpaque(true);
        setBackground(Color.BLACK);

        g2d.setColor(Color.WHITE);

        // used to make snake parts smaller than an entire SIZE*SIZE square
        final int snakeSizer = 2;

        int snakeLoc = snakeSizer / 2;
        int snakeSize = SIZE - snakeSizer;

        // repaint
        g2d.fillRect(snake.getX() * SIZE + snakeLoc, snake.getY() * SIZE + snakeLoc, snakeSize, snakeSize);

        // draw snakes tail
        for (Point p : snake.getParts())
            if(p != null)
                g2d.fillRect(p.getX() * SIZE + snakeLoc, p.getY() * SIZE + snakeLoc, snakeSize, snakeSize);

        // draw food
        g2d.setColor(Color.RED);
        g2d.fillRect(food.getX() * SIZE, food.getY() * SIZE, SIZE, SIZE);
    }

    public void keys() {
        if(!paused) {
            switch (EasyKey.getKey()) {
                case 'w':
                case 38: // up arrow
                    if (!snake.getDir().equals(0, 1)) // for making sure you cant go backwards
                        snake.setDir(0, -1);
                    break;
                case 'a':
                case 37: // left arrow
                    if (!snake.getDir().equals(1, 0))
                        snake.setDir(-1, 0);
                    break;
                case 's':
                case 40: // down arrow
                    if (!snake.getDir().equals(0, -1))
                        snake.setDir(0, 1);
                    break;
                case 'd':
                case 39: // right arrow
                    if (!snake.getDir().equals(-1, 0))
                        snake.setDir(1, 0);
                    break;
                default:
                    break;
            }
        } else
            snake.setDir(0, 0);
    }

    public void updateWindow(JFrame frame) {
        Dimension size = frame.getBounds().getSize();

        TILESX = (int)(size.getWidth() / SIZE);
        TILESY = (int)(size.getHeight() / SIZE) - 1;

        food = makeFood();
    }

    private void reset() { // wah wah game over
        paused = true;

        ScoreKeeper.Score highscore = ScoreKeeperSQL.getHighscore();

        String special = "";
        if(score() > TILESX*TILESY - EATSIZE) // huge rainbow text below
            special = "<div><span style=\"color:#ff0000;\">Y</span><span style=\"color:#ff1900;\">o</span><span style=\"color:#ff3300;\">u</span><span style=\"color:#ff4c00;\"> </span><span style=\"color:#ff6600;\">b</span><span style=\"color:#ff7f00;\">e</span><span style=\"color:#ff9900;\">a</span><span style=\"color:#ffb200;\">t</span><span style=\"color:#ffcc00;\"> </span><span style=\"color:#ffe500;\">t</span><span style=\"color:#ffff00;\">h</span><span style=\"color:#ccff00;\">e</span><span style=\"color:#99ff00;\"> </span><span style=\"color:#66ff00;\">g</span><span style=\"color:#33ff00;\">a</span><span style=\"color:#00ff00;\">m</span><span style=\"color:#00ff33;\">e</span><span style=\"color:#00ff66;\">!</span><span style=\"color:#00ff99;\"> </span><span style=\"color:#00ffcc;\">A</span><span style=\"color:#00ffff;\">w</span><span style=\"color:#00ccff;\">e</span><span style=\"color:#0099ff;\">s</span><span style=\"color:#0066ff;\">o</span><span style=\"color:#0033ff;\">m</span><span style=\"color:#0000ff;\">e</span><span style=\"color:#1c00ff;\"> </span><span style=\"color:#3800ff;\">j</span><span style=\"color:#5300ff;\">o</span><span style=\"color:#6f00ff;\">b</span><span style=\"color:#8b00ff;\">!</span></div>";
        else if(highscore != null && score() > highscore.getScore())
            special = "You beat " + highscore.getName() + "'s highscore of " + highscore.getScore() + " good job!<br>";

        gameOver(special, score());
    }

    private Point makeFood() {
        Point point = Point.randomPoint(TILESX, TILESY);

        while(snake.contains(point))
            point = Point.randomPoint(TILESX, TILESY);

        return point;
    }

    public int score() {
        return snake.length();
    }

    public void gameOver(String special, int score) {
        // press any key to start
        final JDialog dialog = new JDialog(Main.frame, "You died.");

        JLabel label = new JLabel(
                String.format("<html><center>Final score: %1d<br>%2sClose this dialog to start again. <br>Click save to save your score.</center></html>", score, special)
        );

        label.setPreferredSize(new Dimension(210, 75));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField text = new JTextField("name", 15);
        text.setPreferredSize(new Dimension(150, 75));
        text.setHorizontalAlignment(SwingConstants.CENTER);
        text.setDocument(new PlainDocument() {
            public void insertString( int offset, String str, AttributeSet attr ) throws BadLocationException {
                if (str == null)
                    return;

                if(str.contains("|"))
                    str = str.replace("|", "");

                if ((getLength() + str.length()) <= 15) {
                    super.insertString(offset, str, attr);
                }
            }
        });

        JButton exit = new JButton("Close");

        exit.setPreferredSize(new Dimension(100, 25));

        JButton save = new JButton("Save");
        save.setPreferredSize(new Dimension(100, 25));

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(label, BorderLayout.NORTH);
        panel.add(text, BorderLayout.WEST);
        panel.add(save, BorderLayout.EAST);
        panel.add(exit, BorderLayout.SOUTH);

        dialog.add(panel);

        ActionListener action = e -> {
            ScoreKeeperSQL.newScore(
                text.getText().isEmpty() ? System.getProperty("user.name") : text.getText(),
                score
            );
            dialog.dispose();
            startGame();
        };

        save.addActionListener(action);
        exit.addActionListener(action);

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                startGame();
            }
        });

        dialog.pack();
        dialog.setResizable(false);

        dialog.setFocusable(true);
        dialog.setLocationRelativeTo(Main.frame);
        dialog.setVisible(true);
        dialog.requestFocus();
    }

    public void startGame() {
        paused = false;
        snake = new Snake(Point.randomPoint(TILESX, TILESY));
        food = makeFood();

        EasyKey.emptyKey();
    }
}
