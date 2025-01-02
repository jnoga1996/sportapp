package sportapp.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class ScoreboardServiceTest {

    private final ScoreboardService scoreboardService;

    public ScoreboardServiceTest() {
        scoreboardService = new ScoreboardService();
    }

    @Test
    void startGameShouldAddNewGameAndReturnNotNullUUID() {
        var uuid = scoreboardService.startGame("POL", "GER");

        Assertions.assertNotNull(uuid);
    }

    @Test
    void finishGameShouldRemoveGameFromScoreboard() {
        var uuid = UUID.randomUUID();

        var result = scoreboardService.finishGame(uuid);

        Assertions.assertTrue(result);
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
}
