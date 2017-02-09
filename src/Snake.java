import java.util.Arrays;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 12/10/2016
 */
public class Snake {

    private Point pos, dir;

    private int size;

    private Point[] parts;

    public Snake() {
        this(new Point(1, 1));
    }

    public Snake(Point pos) {
        this.pos = pos;
        dir = new Point(0, 0);

        size = 0;
        parts = new Point[size];
    }

    public synchronized void update() {
        // update other parts if they exist.
        if(size > 0) {
            parts = Arrays.copyOf(parts, size);
            // also update other parts, move down to index 0
            for (int i = 0; i < size - 1; i++)
                parts[i] = parts[i + 1];

            // add new point for current position
            parts[size - 1] = pos.clone();
        }

        // update current position
        pos.add(dir);

        // check to see if out of bounds
        int xBounds = SnakeGame.TILESX;
        if(pos.getX() >= xBounds)
            pos.setX(xBounds - 1);
        else if(pos.getX() < 0)
            pos.setX(0);

        int yBounds = SnakeGame.TILESY;
        if(pos.getY() >= yBounds)
            pos.setY(yBounds - 1);
        else if(pos.getY() < 0)
            pos.setY(0);
    }

    public boolean eat(Point food) {
        return pos.distance(food) < 1; // in game units
    }

    public synchronized boolean isDead() {
        for (int i = 0; i < parts.length; i++)
            if (parts[i] != null && pos.equals(parts[i]))
                return true;

        return false;
    }

    public int getX() {
        return pos.getX();
    }

    public int getY() {
        return pos.getY();
    }

    public synchronized void setDir(int x, int y) {
        dir.setX(x);
        dir.setY(y);
    }

    public Point getDir() {
        return dir;
    }

    public Point[] getParts() {
        return parts;
    }

    public synchronized boolean contains(Point point) {
        if(pos.equals(point))
            return true;

        for(Point p : parts)
            if(p != null && p.equals(point))
                return true;
        return false;
    }

    public void addSize(int num) {
        size += num;
    }

    public int length() {
        return parts.length;
    }
}
