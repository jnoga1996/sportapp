package sportapp.services;

import lombok.Getter;
import lombok.extern.java.Log;
import sportapp.data.Game;
import sportapp.validator.GameValidator;

import java.util.*;

@Log
@Getter
public class ScoreboardService implements ScoreboardOperations {

    private final List<Game> games;
    private final ContinentMapperService continentMapperService;

    public ScoreboardService() {
        games = new ArrayList<>();
        continentMapperService = new ContinentMapperService();
    }

    @Override
    public UUID startGame(String homeTeamName, String awayTeamName) {
        Game game = new Game(homeTeamName, awayTeamName);
        GameValidator.validate(games, game);
        games.add(game);
        return game.getUuid();
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

    @Override
    public Map<String, Integer> getSummaryByContinent() {
        Map<String, Integer> continentToScoreMap = new HashMap<>();
        games.forEach(game -> {
                    putIntoMap(continentToScoreMap, game.getHomeTeamName(), game.getHomeTeamScore());
                    putIntoMap(continentToScoreMap, game.getAwayTeamName(), game.getAwayTeamScore());
                }
        );
        return continentToScoreMap;
    }

    private void putIntoMap(Map<String, Integer> continentToScoreMap, String team, Integer score) {
        String continentByCountry = continentMapperService.getContinentByCountry(team);
            continentToScoreMap.compute(continentByCountry,
                    (k, currentScore) -> score + Optional.ofNullable(currentScore).orElse(0));
    }

}
