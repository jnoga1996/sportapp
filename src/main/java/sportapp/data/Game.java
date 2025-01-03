package sportapp.data;

import java.util.Objects;
import java.util.UUID;

public class Game {

    private final static Integer DEFAULT_SCORE = 0;

    private final UUID uuid;
    private final Team homeTeam;
    private final Team awayTeam;

    public Game(String homeTeamName, String awayTeamName) {
        Objects.requireNonNull(homeTeamName, "Home team name can't be null!");
        Objects.requireNonNull(awayTeamName, "Away team name can't be null!");
        this.homeTeam = new Team(homeTeamName, DEFAULT_SCORE);
        this.awayTeam = new Team(awayTeamName, DEFAULT_SCORE);
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(homeTeam, game.homeTeam) && Objects.equals(awayTeam, game.awayTeam);
    }
}
