package sportapp.services;

import sportapp.data.Game;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ScoreboardOperations {

    UUID startGame(String homeTeamName, String awayTeamName);

    boolean finishGame(UUID uuid);

    void updateScore(UUID gameUUID, Integer homeTeamScore, Integer awayTeamScore);

    List<Game> getSummaryByTotalScore();

    Map<String, Integer> getSummaryByContinent();
}
