import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 12/10/2016
 */
public class EasyKey implements KeyListener {

    private static List<Integer> keys;

    public EasyKey() {
        System.out.println("EasyKey initialised!");

        keys = new ArrayList<>();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(keys.contains(e.getKeyCode()))
            keys.removeIf(k -> k.equals(e.getKeyCode()));
    }

    public static boolean keyPressed(int c) {
        return keys.contains(c);
    }

    public static boolean keyPressed(int... ints) {
        for(int key : ints)
            if(keys.contains(key))
                return true;
        return false;
    }

    /**
     * Used to fix direction bug for the snake.
     */
    public static void emptyKeys() {
        keys.clear();
    }

    public static boolean noPress() {
        return keys.isEmpty();
    }

}
