package sportapp.services;

import lombok.Getter;
import sportapp.data.Game;

import java.util.*;

@Getter
public class ScoreboardService implements ScoreboardOperations {

    private final List<Game> games;

    public ScoreboardService() {
        games = new ArrayList<>();
    }

    @Override
    public UUID startGame(String homeTeamName, String awayTeamName) {
        Game game = new Game(homeTeamName, awayTeamName);
        if (gameExistsInScoreboard(game)) {
            throw new IllegalStateException("Game already exists in scoreboard!");
        }
        games.add(game);
        return game.getUuid();
    }

    private boolean gameExistsInScoreboard(Game game) {
        return isTeamNameDuplicated(game.getHomeTeamName(), game.getAwayTeamName()) || isTeamNameDuplicated(game.getAwayTeamName(), game.getHomeTeamName());
    }

    private boolean isTeamNameDuplicated(String homeTeamName, String awayTeamName) {
        return games.stream().anyMatch(g ->
                Objects.equals(g.getHomeTeamName(), homeTeamName) && Objects.equals(g.getAwayTeamName(), awayTeamName));
    }

    @Override
    public boolean finishGame(UUID uuid) {
        var gameToRemove = games.stream().filter(game -> game.getUuid().equals(uuid)).findFirst();
        gameToRemove.ifPresent(games::remove);
        return gameToRemove.isPresent();
    }

    @Override
    public void updateScore(UUID gameUUID, Integer homeTeamScore, Integer awayTeamScore) {
        var gameToUpdate = games.stream()
                .filter(game -> game.getUuid().equals(gameUUID))
                .findFirst();
        gameToUpdate.ifPresent(game -> game.updateScore(homeTeamScore, awayTeamScore));
    }

    @Override
    public List<Game> getSummaryByTotalScore() {
        return games.stream()
                .sorted(Comparator.comparingInt(g -> g.getAwayTeamScore() + g.getHomeTeamScore()))
                .toList()
                .reversed();
    }

}
