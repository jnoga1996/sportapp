package sportapp.validator;

import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;
import sportapp.data.Game;

import java.util.List;
import java.util.Objects;

@Log
@UtilityClass
public class GameValidator {

    public void validate(List<Game> games, Game game) {
        String homeTeamName = game.getHomeTeamName();
        String awayTeamName = game.getAwayTeamName();
        if (gameExistsInScoreboard(games, game)) {
            String errorMessage = "Game [%s] already exists in scoreboard!".formatted(game);
            log.warning(errorMessage);
            throw new IllegalStateException(errorMessage);
        } else if (isTheSameTeam(homeTeamName, awayTeamName)) {
            String errorMessage = "Game can't contain match with the same home team name and away team name: [%s]!".formatted(homeTeamName);
            log.warning(errorMessage);
            throw new IllegalStateException(errorMessage);
        } else if (isTeamAlreadyUsed(games, homeTeamName)) {
            String errorMessage = "Team %s is already participating in another match!".formatted(homeTeamName);
            log.warning(errorMessage);
            throw new IllegalStateException(errorMessage);
        } else if (isTeamAlreadyUsed(games, awayTeamName)) {
            String errorMessage = "Team %s is already participating in another match!".formatted(homeTeamName);
            log.warning(errorMessage);
            throw new IllegalStateException(errorMessage);
        }
    }

    private boolean gameExistsInScoreboard(List<Game> games, Game game) {
        return isTeamNameDuplicated(games, game.getHomeTeamName(), game.getAwayTeamName()) || isTeamNameDuplicated(games, game.getAwayTeamName(), game.getHomeTeamName());
    }

    private boolean isTheSameTeam(String homeTeamName, String awayTeamName) {
        return homeTeamName.equals(awayTeamName);
    }

    private boolean isTeamAlreadyUsed(List<Game> games, String teamName) {
        return games.stream().anyMatch(g -> g.getHomeTeamName().equals(teamName) || g.getAwayTeamName().equals(teamName));
    }

    private boolean isTeamNameDuplicated(List<Game> games, String homeTeamName, String awayTeamName) {
        return games.stream().anyMatch(g ->
                Objects.equals(g.getHomeTeamName(), homeTeamName) && Objects.equals(g.getAwayTeamName(), awayTeamName));
    }

}
