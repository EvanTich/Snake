import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 12/20/2016
 */
public class ScoreKeeper {

    static class Score {
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

        public String toString() {
            if(time != null)
                return String.format("%1s|%2d|%3s", name, score, time);
            return String.format("%1s|%2d", name, score);
        }
    }

    private static void loadScores() {

    }
    public static boolean newScore(String name, int score) {
        return false;
    }
    public static ArrayList<Score> getScores() {
        return null;
    }
    public static Score getHighscore() {
        return null;
    }

    public static boolean close() {
        return false;
    }
}
