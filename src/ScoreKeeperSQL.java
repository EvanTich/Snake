import java.sql.*;
import java.util.ArrayList;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 12/20/2016
 */
public class ScoreKeeperSQL extends ScoreKeeper {

    private static Connection connection;
    private static final String SERVER;
    private static final String USER;
    private static final String PASS;

    static { // static constructor!

        // pls don't kill this server (don't show to github)
        SERVER = "jdbc:postgresql://ec2-54-204-42-178.compute-1.amazonaws.com/d79jecqjig77mu?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
        USER = "zmghkhlgcfmwqj";
        PASS = "jlsFuxll2Qj8kTAYTzjrm2b5KI";
    }

    public ScoreKeeperSQL() {
        KEEPER = this;
        loadScores();
    }

    protected void loadScores() {
        connection = null;

        try {
            // register JDBC driver
            Class.forName("org.postgresql.Driver");

            // open a connection
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(SERVER, USER, PASS);
        } catch(SQLException se) {
            System.err.println("Connection error.");
        } catch(ClassNotFoundException c) {
            System.err.println("JDBC file not found.");

            return;
        }

        System.out.println("Connected to database.");
    }

    public boolean newScore(String name, int score) {
        try {
            Statement statement = connection.createStatement();
            statement.execute(
                String.format("insert into snakescores values ('%1s',%2d,now());", name, score)
            );

            statement.close();
        } catch (SQLException e) {
            System.err.println("Could not save score into database.");
            return false;
        }

        return true;
    }

    public ArrayList<Score> getScores() {
        ArrayList<Score> scores = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select gameuser, score from snakescores order by score desc limit 100");

            while(rs.next()) {
                String name = rs.getString("gameuser");
                int score = rs.getInt("score");

                scores.add(new Score(name, score));
            }

            rs.close();
            statement.close();
        } catch(SQLException e) {
            System.err.println("Could not get scores from database");
        }

        return scores;
    }

    public Score getHighscore() {
        Score high = null;

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select gameuser, score from snakescores order by score desc limit 1");

            rs.next(); // gets the next line

            String user = rs.getString("gameuser");
            int score = rs.getInt("score");

            high = new Score(user, score);

            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Could not get the highscore, SQL error.");
        }

        return high;
    }

    /**
     * Closes the statement and connection for the sql database.
     * @return true if successfully closed, false otherwise.
     */
    public boolean close() {
        try {
            if(connection != null)
                connection.close();

        } catch(SQLException se) {
            System.err.println("JDBC close error. (pretty bad methinks)");
            return false;
        }

        return true;
    }
}
