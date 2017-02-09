import java.io.*;
import java.util.*;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 12/20/2016
 */
public class ScoreKeeperFile extends ScoreKeeper {

    private static List<ScoreKeeper.Score> scores;
    private static File scoreFile;

    static { // static constructor!
        scores = new ArrayList<>();
        scoreFile = new File("snake_highscores.txt"); // directory to the score file
    }

    public ScoreKeeperFile() {
        KEEPER = this;
        loadScores();
    }

    protected void loadScores() {
        try (BufferedReader scan = new BufferedReader(new FileReader(scoreFile))) {
            if(scoreFile.createNewFile())
                return; // if it didn't exist already, why read from it?

            String score;
            while( (score = scan.readLine()) != null ) {
                if(score.charAt(0) == ';') // never read if the first thing in a line is ";"
                    continue;

                String[] combo = score.split("[|]");

                // put scores where needed
                scores.add( new ScoreKeeper.Score( combo[0], Integer.parseInt(combo[1].replace(" ", "")) ) );
            }

        } catch (IOException e) {
            System.out.println("Problem reading scores.");
        }
    }

    public boolean saveScores() {
        Collections.sort(scores); // sort before saving

        try (BufferedWriter write = new BufferedWriter(new FileWriter(scoreFile))) {
            // always write these two statement at the top of the highscore file
            write.write("; SNAKE HIGHSCORES\n; name|score\n");

            // write all scores to file
            for (ScoreKeeper.Score score : scores)
                write.write(String.format("%1s|%2d%n", score.getName(), score.getScore()));

        } catch (IOException e) {
            System.out.println("Problem saving scores.");
            return false;
        }

        return true;
    }

    public boolean newScore(String name, int value) {
        Score score = new Score(name, value);
        scores.add(score);
        return saveScores();
    }

    public List<Score> getScores() {
        return scores;
    }

    public Score getHighscore() {
        if(scores.size() == 0)
            return new Score("Beel", 0);

        Score best = scores.get(0);
        for(ScoreKeeper.Score score : scores)
            if(score.getScore() > best.getScore())
                best = score;
        return best;
    }

    public boolean close() {
        return true;
    }
}
