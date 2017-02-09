import java.io.*;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 12/20/2016
 */
public class ScoreKeeperFile {

    private static Map<String, Integer> scores;
    private static File scoreFile;

    static { // static constructor!
        scores = new LinkedHashMap<>();
        scoreFile = new File("snake_highscores.txt"); // directory to the score file

        loadScores();
    }

    private static void loadScores() {
        try (BufferedReader scan = new BufferedReader(new FileReader(scoreFile))) {
            if(scoreFile.createNewFile())
                return; // if it didn't exist already, why read from it?

            String score;
            while( (score = scan.readLine()) != null ) {
                if(score.charAt(0) == ';') // never read if the first thing in a line is ";"
                    continue;

                String[] combo = score.split("[|]");

                // put scores where needed
                scores.put(combo[0], Integer.parseInt( combo[1].replace(" ", "") ));
            }

        } catch (IOException e) {
            System.out.println("Problem reading scores.");
        }
    }

    public static boolean saveScores() {
        scores = sortByValue(scores); // sort before saving

        try (BufferedWriter write = new BufferedWriter(new FileWriter(scoreFile))) {
            // always write these two statement at the top of the highscore file
            write.write("; SNAKE HIGHSCORES\n; name|score\n");

            // write all scores to file
            for (Map.Entry<String, Integer> score : scores.entrySet())
                write.write(String.format("%1s|%2d%n", score.getKey(), score.getValue()));

        } catch (IOException e) {
            System.out.println("Problem saving scores.");
            return false;
        }

        return true;
    }

    public static boolean newScore(String name, int score) {
        scores.put(name, score);
        return saveScores();
    }

    public static Map<String, Integer> getScores() {
        return scores;
    }

    public static int getHighscore() {
        int best = 0;
        for(Map.Entry<String, Integer> score : scores.entrySet())
            if(score.getValue() > best)
                best = score.getValue();
        return best;
    }

    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
