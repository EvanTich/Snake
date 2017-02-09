import java.sql.Timestamp;
import java.util.List;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 12/20/2016
 */
public abstract class ScoreKeeper {

    public static ScoreKeeper KEEPER;

    static class Score implements Comparable<Score> {
        private String name;
        private int score;
        private Timestamp time;

        public Score(String name, int score) {
            this(name, score, null);
        }

        public Score(String name, int score, Timestamp time) {
            this.name = name;
            this.score = score;
            this.time = time;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }

        public Timestamp getTime() {
            return time;
        }

        @Override
        public int compareTo(Score o) {
            return score - o.score;
        }

        @Override
        public String toString() {
            if(time != null)
                return String.format("%1s|%2d|%3s", name, score, time);
            return String.format("%1s|%2d", name, score);
        }
    }

    public static ScoreKeeper getKeeper() {
        return KEEPER;
    }

    public static void setKeeper(ScoreKeeper keeper) {
        KEEPER = keeper;
    }

    protected abstract void loadScores();
    public abstract boolean newScore(String name, int score);
    public abstract List<Score> getScores();
    public abstract Score getHighscore();
    public abstract boolean close();
}
