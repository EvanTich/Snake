import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 12/10/2016
 */
public class EasyKey implements KeyListener {

    private static char key; // will always be lowercase
    private static boolean pressed;

    public EasyKey() {
        System.out.println("EasyKey initialised!");

        key = ' ';
        pressed = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        key = Character.toLowerCase(e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressed = true;
        key = (char)e.getKeyCode();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressed = false;
    }

    public static synchronized char getKey() {
        return key;
    }

    /**
     * Used to fix direction bug for the snake.
     */
    public static void emptyKey() {
        EasyKey.key = 19;
    }

    public static synchronized boolean getPressed() {
        return pressed;
    }
}
