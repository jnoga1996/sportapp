package sportapp.services;

import sportapp.data.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScoreboardService implements ScoreboardOperations {

    private final List<Game> games;

    public ScoreboardService() {
        games = new ArrayList<>();
    }

    @Override
    public UUID startGame(String homeTeamName, String awayTeamName) {
        Game game = new Game(homeTeamName, awayTeamName);
        Game swappedGame = new Game(awayTeamName, homeTeamName);
        if (games.contains(game) || games.contains(swappedGame)) {
            throw new IllegalStateException("Game already exists in scoreboard!");
        }
        games.add(game);
        return game.getUuid();
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

    public List<Game> getGames() {
        return games;
    }
}
