package sportapp.services;

import java.util.List;
import java.util.UUID;

public interface ScoreboardOperations {

    UUID startGame(String homeTeamName, String awayTeamName);

    boolean finishGame(UUID uuid);

    void updateScore(UUID gameUUID, Integer homeTeamScore, Integer awayTeamScore);

    // TODO method should return collection of Games, will change definition after data structure is ready
    List<Object> getSummaryByTotalScore();
}
