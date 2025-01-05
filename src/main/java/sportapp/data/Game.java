package sportapp.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;
import java.util.UUID;

@Getter
@ToString
public class Game {

    public final static Integer DEFAULT_SCORE = 0;

    private final UUID uuid;
    private final String homeTeamName;
    private final String awayTeamName;
    @Setter
    private Integer homeTeamScore;
    @Setter
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

    public void updateScore(Integer homeTeamScore, Integer awayTeamScore) {
        validateScore(homeTeamScore);
        validateScore(awayTeamScore);
        setHomeTeamScore(homeTeamScore);
        setAwayTeamScore(awayTeamScore);
    }

    private void validateScore(Integer score) {
        if (score == null) {
            throw new IllegalArgumentException("Score is null!");
        }
        if (score < 0) {
            throw new IllegalArgumentException("Score can't be a negative number!");
        }
    }
}
