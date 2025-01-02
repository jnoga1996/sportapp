package sportapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScoreboardService implements ScoreboardOperations {

    private final List<Object> games;

    public ScoreboardService() {
        games = new ArrayList<>();
    }

    @Override
    public UUID startGame(String homeTeamName, String awayTeamName) {
        return null;
    }

    @Override
    public boolean finishGame(UUID uuid) {
        return false;
    }

    @Override
    public void updateScore(UUID gameUUID, Integer homeTeamScore, Integer awayTeamScore) {
        var game = games.stream()
                .findFirst()
                .orElseThrow();
        //game.updateScore(homeTeamScore, awayTeamScore);
    }

    @Override
    public List<Object> getSummaryByTotalScore() {
        return null;
    }
}
