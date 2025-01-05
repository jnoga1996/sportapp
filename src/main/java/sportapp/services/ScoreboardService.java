package sportapp.services;

import lombok.Getter;
import lombok.extern.java.Log;
import sportapp.data.Game;

import java.util.*;

@Log
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
            String errorMessage = "Game [%s] already exists in scoreboard!".formatted(game);
            log.warning(errorMessage);
            throw new IllegalStateException(errorMessage);
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
        gameToRemove.ifPresent(game -> {
            games.remove(game);
            log.info("Game [%s] has been removed from scoreboard!".formatted(game));
        });
        return gameToRemove.isPresent();
    }

    @Override
    public void updateScore(UUID gameUUID, Integer homeTeamScore, Integer awayTeamScore) {
        var gameToUpdate = games.stream()
                .filter(game -> game.getUuid().equals(gameUUID))
                .findFirst();
        gameToUpdate.ifPresent(game -> {
            log.info("Going to update score for game: [%s]".formatted(game));
            game.updateScore(homeTeamScore, awayTeamScore);
            log.info("Score updated for game: [%s]".formatted(game));
        });
    }

    @Override
    public List<Game> getSummaryByTotalScore() {
        return games.stream()
                .sorted(Comparator.comparingInt(g -> g.getAwayTeamScore() + g.getHomeTeamScore()))
                .toList()
                .reversed();
    }

}
