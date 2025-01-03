package sportapp.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sportapp.data.Game;

import java.util.List;
import java.util.UUID;

public class ScoreboardServiceTest {

    private final static Integer DEFAULT_SCORE = 0;
    private ScoreboardService scoreboardService;

    @BeforeEach
    public void cleanUp() {
        scoreboardService = new ScoreboardService();
    }

    @Test
    void startGameShouldAddNewGameAndReturnNotNullUUID() {
        var uuid = scoreboardService.startGame("POL", "GER");
        var games = scoreboardService.getGames();

        Assertions.assertNotNull(uuid);
        Assertions.assertEquals(1, games.size());
        assertThatGamesHaveDefaultScoreValue(games);
    }

    @Test
    void startGameShouldNotAddNewGameIfGameAlreadyExists() {
        var firstGameUUID = scoreboardService.startGame("POL", "GER");
        var games = scoreboardService.getGames();

        Assertions.assertThrows(IllegalStateException.class, () -> scoreboardService.startGame("POL", "GER"));
        Assertions.assertNotNull(firstGameUUID);
        assertThatGamesHaveDefaultScoreValue(games);
    }

    @Test
    void startGameShouldNotAddNewGameIfGameWithSwappedHomeAndAwayTeamNameAlreadyExists() {
        var firstGameUUID = scoreboardService.startGame("POL", "GER");
        var games = scoreboardService.getGames();

        Assertions.assertThrows(IllegalStateException.class, () -> scoreboardService.startGame("GER", "POL"));
        Assertions.assertNotNull(firstGameUUID);
        assertThatGamesHaveDefaultScoreValue(games);
    }

    @Test
    void startGameShouldNotAddNewGameIfTeamIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> scoreboardService.startGame(null, "GER"), "Home team name can't be null!");
        Assertions.assertThrows(NullPointerException.class, () -> scoreboardService.startGame("POL", null), "Away team name can't be null!");
    }

    @Test
    void finishGameShouldRemoveGameFromScoreboard() {
        var uuid = scoreboardService.startGame("POL", "GER");
        var uuid2 = scoreboardService.startGame("USA", "SLO");
        var notExistingUUID = UUID.randomUUID();
        var games = scoreboardService.getGames();
        Assertions.assertEquals(2, games.size());

        var result = scoreboardService.finishGame(uuid);

        Assertions.assertTrue(result);
        Assertions.assertEquals(1, scoreboardService.getGames().size());

        var result2 = scoreboardService.finishGame(notExistingUUID);

        Assertions.assertFalse(result2);

        var result3 = scoreboardService.finishGame(uuid2);
        Assertions.assertTrue(result3);
        Assertions.assertEquals(0, games.size());

        var result4 = scoreboardService.finishGame(null);
        Assertions.assertFalse(result4);
    }

    @Test
    void updateScoreShouldUpdateScoreForProvidedUUID() {
        var uuid = UUID.randomUUID();

        scoreboardService.updateScore(uuid, 1, 0);
    }

    @Test
    void getSummaryByTotalScoreShouldReturnListOfGames() {
        var summary = scoreboardService.getSummaryByTotalScore();

        Assertions.assertNotNull(summary);
    }

    private void assertThatGamesHaveDefaultScoreValue(List<Game> games) {
        games.forEach(game -> {
            Assertions.assertEquals(DEFAULT_SCORE, game.getHomeTeam().score());
            Assertions.assertEquals(DEFAULT_SCORE, game.getAwayTeam().score());
        });
    }
}
