package sportapp.component;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sportapp.services.ScoreboardService;

import static sportapp.util.TestUtils.assertThatGameHaveCorrectValues;

public class ScoreboardComponentTest {

    private final ScoreboardService scoreboardService;

    public ScoreboardComponentTest() {
        this.scoreboardService = new ScoreboardService();
    }

    @Test
    public void testScoreboardFlow() {
        // Two games started at the same time
        var polVsUsaUUID = scoreboardService.startGame("POL", "USA");
        var gerVsFraUUID = scoreboardService.startGame("GER", "FRA");

        var firstSummary = scoreboardService.getSummaryByTotalScore();
        assertThatGameHaveCorrectValues(firstSummary.get(0), "GER", 0, "FRA", 0);
        assertThatGameHaveCorrectValues(firstSummary.get(1), "POL", 0, "USA", 0);

        // POL team scores and there are no changes in other game, meanwhile new game starts
        scoreboardService.updateScore(polVsUsaUUID, 1, 0);
        var uruVsItaUUID = scoreboardService.startGame("URU", "ITA");
        var secondSummary = scoreboardService.getSummaryByTotalScore();

        assertThatGameHaveCorrectValues(secondSummary.get(0), "POL", 1, "USA", 0);
        assertThatGameHaveCorrectValues(secondSummary.get(1), "URU", 0, "ITA", 0);
        assertThatGameHaveCorrectValues(secondSummary.get(2), "GER", 0, "FRA", 0);

        // POL vs USA and GER vs FRA games end, meanwhile ITA scores 2 goals in a short time
        scoreboardService.finishGame(polVsUsaUUID);
        scoreboardService.finishGame(gerVsFraUUID);
        scoreboardService.updateScore(uruVsItaUUID, 0, 1);
        scoreboardService.updateScore(uruVsItaUUID, 0, 2);
        var thirdSummary = scoreboardService.getSummaryByTotalScore();

        assertThatGameHaveCorrectValues(thirdSummary.get(0), "URU", 0, "ITA", 2);

        // URU vs ITA ends
        scoreboardService.finishGame(uruVsItaUUID);
        var fourthSummary = scoreboardService.getSummaryByTotalScore();
        Assertions.assertTrue(fourthSummary.isEmpty());
    }
}
