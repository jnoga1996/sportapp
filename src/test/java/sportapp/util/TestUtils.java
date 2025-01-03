package sportapp.util;

import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.Assertions;
import sportapp.data.Game;

import java.util.List;
import java.util.UUID;

import static sportapp.data.Game.DEFAULT_SCORE;

@UtilityClass
public class TestUtils {

    public static void assertThatGamesHaveDefaultScoreValue(List<Game> games) {
        games.forEach(game -> {
            Assertions.assertEquals(DEFAULT_SCORE, game.getHomeTeamScore());
            Assertions.assertEquals(DEFAULT_SCORE, game.getAwayTeamScore());
        });
    }

    public static Game getGameByUUID(List<Game> games, UUID uuid) {
        return games.stream().filter(game -> game.getUuid().equals(uuid)).findFirst().orElseThrow();
    }

    public static void assertThatGameHaveCorrectValues(Game game, String homeTeamName, Integer homeTeamScore, String awayTeamName, Integer awayTeamScore) {
        Assertions.assertEquals(homeTeamName, game.getHomeTeamName());
        Assertions.assertEquals(homeTeamScore, game.getHomeTeamScore());
        Assertions.assertEquals(awayTeamName, game.getAwayTeamName());
        Assertions.assertEquals(awayTeamScore, game.getAwayTeamScore());
    }
}
