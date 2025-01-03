package sportapp.data;

import java.util.Objects;
import java.util.UUID;

public class Game {

    private final static Integer DEFAULT_SCORE = 0;

    private final UUID uuid;
    private final String homeTeamName;
    private final String awayTeamName;
    private Integer homeTeamScore;
    private Integer awayTeamScore;

    public Game(String homeTeamName, String awayTeamName) {
        Objects.requireNonNull(homeTeamName, "Home team name can't be null!");
        Objects.requireNonNull(awayTeamName, "Away team name can't be null!");
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.homeTeamScore = DEFAULT_SCORE;
        this.awayTeamScore = DEFAULT_SCORE;
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    public Integer getAwayTeamScore() {
        return awayTeamScore;
    }

    public Integer getHomeTeamScore() {
        return homeTeamScore;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setAwayTeamScore(Integer awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }

    public void setHomeTeamScore(Integer homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public void updateScore(Integer homeTeamScore, Integer awayTeamScore) {
        if (isValidScore(homeTeamScore) && isValidScore(awayTeamScore)) {
            setHomeTeamScore(homeTeamScore);
            setAwayTeamScore(awayTeamScore);
        }
    }

    private boolean isValidScore(Integer score) {
        if (score == null) {
            throw new IllegalArgumentException("Score is null!");
        }
        if (score < 0) {
            throw new IllegalArgumentException("Score can't be a negative number!");
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(homeTeamName, game.homeTeamName) && Objects.equals(awayTeamName, game.awayTeamName);
    }
}
