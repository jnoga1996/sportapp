package sportapp.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static sportapp.util.TestUtils.*;

public class ScoreboardServiceTest {

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
    void startGameShouldNotAddNewGameIfTeamIsAlreadyParticipatingInAnotherMatch() {
        scoreboardService.startGame("POL", "USA");
        Assertions.assertThrows(IllegalStateException.class, () -> scoreboardService.startGame("POL", "GER"), "Team POL is already participating in another match!");
        Assertions.assertThrows(IllegalStateException.class, () -> scoreboardService.startGame("FRA", "USA"), "Team USA is already participating in another match!");
    }

    @Test
    void startGameShouldNotAddNewGameIfHomeTeamEqualsAwayTeam() {
        Assertions.assertThrows(IllegalStateException.class, () -> scoreboardService.startGame("POL", "POL"), "Game can't contain match with the same home team name and away team name: [POL]!");
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
        var uuid = scoreboardService.startGame("POL", "GER");
        var games = scoreboardService.getGames();

        scoreboardService.updateScore(uuid, 1, 0);
        var updatedGame = getGameByUUID(games, uuid);

        Assertions.assertEquals(1, updatedGame.getHomeTeamScore());
        Assertions.assertEquals(0, updatedGame.getAwayTeamScore());

        scoreboardService.updateScore(uuid, 1, 1);
        updatedGame = getGameByUUID(games, uuid);

        Assertions.assertEquals(1, updatedGame.getHomeTeamScore());
        Assertions.assertEquals(1, updatedGame.getAwayTeamScore());
    }

    @Test
    void updateScoreShouldNotUpdateScoreIfScoreIsNegativeOrNull() {
        var uuid = scoreboardService.startGame("POL", "GER");

        Assertions.assertThrows(IllegalArgumentException.class, () -> scoreboardService.updateScore(uuid, -1, 0),
                "Score can't be a negative number!");
        Assertions.assertThrows(IllegalArgumentException.class, () -> scoreboardService.updateScore(uuid, null, 0),
                "Score can't be a negative number!");

        var games = scoreboardService.getGames();
        var game = getGameByUUID(games, uuid);
        Assertions.assertEquals(0, game.getHomeTeamScore());
        Assertions.assertEquals(0, game.getAwayTeamScore());
    }

    @Test
    void getSummaryByTotalScoreShouldReturnListOfGames() {
        var uuid = scoreboardService.startGame("MEX", "CAN");
        scoreboardService.updateScore(uuid, 0, 5);
        var uuid2 = scoreboardService.startGame("SPA", "BRA");
        scoreboardService.updateScore(uuid2, 10, 2);
        var uuid3 = scoreboardService.startGame("GER", "FRA");
        scoreboardService.updateScore(uuid3, 2, 2);
        var uuid4 = scoreboardService.startGame("URU", "ITA");
        scoreboardService.updateScore(uuid4, 6, 6);
        var uuid5 = scoreboardService.startGame("ARG", "AUS");
        scoreboardService.updateScore(uuid5, 3, 1);

        var summary = scoreboardService.getSummaryByTotalScore();

        Assertions.assertNotNull(summary);
        assertThatGameHaveCorrectValues(summary.get(0), "URU", 6, "ITA", 6);
        assertThatGameHaveCorrectValues(summary.get(1), "SPA", 10, "BRA", 2);
        assertThatGameHaveCorrectValues(summary.get(2), "MEX", 0, "CAN", 5);
        assertThatGameHaveCorrectValues(summary.get(3), "ARG", 3, "AUS", 1);
        assertThatGameHaveCorrectValues(summary.get(4), "GER", 2, "FRA", 2);
    }

    @Test
    public void getSummaryByContinentShouldReturnContinentToScoreMapping() {
        var uuid = scoreboardService.startGame("POL", "GER");
        scoreboardService.updateScore(uuid, 2, 1);

        var uuid2 = scoreboardService.startGame("RPA", "USA");
        scoreboardService.updateScore(uuid2, 3, 2);

        var summary = scoreboardService.getSummaryByContinent();
        Assertions.assertNotNull(summary);
        Assertions.assertEquals(3, summary.get("EUR"));
        Assertions.assertEquals(3, summary.get("AFR"));
        Assertions.assertEquals(2, summary.get("NA"));
    }

    @Test
    void getSummaryByTotalScoreShouldReturnListOfGamesSortedByDateOfCreation() {
        var uuid = scoreboardService.startGame("MEX", "CAN");
        scoreboardService.updateScore(uuid, 7, 5);
        var uuid2 = scoreboardService.startGame("SPA", "BRA");
        scoreboardService.updateScore(uuid2, 10, 2);
        var uuid3 = scoreboardService.startGame("GER", "FRA");
        scoreboardService.updateScore(uuid3, 6, 6);

        var summary = scoreboardService.getSummaryByTotalScore();

        Assertions.assertNotNull(summary);
        assertThatGameHaveCorrectValues(summary.get(0), "GER", 6, "FRA", 6);
        assertThatGameHaveCorrectValues(summary.get(1), "SPA", 10, "BRA", 2);
        assertThatGameHaveCorrectValues(summary.get(2), "MEX", 7, "CAN", 5);
    }


}
