package pl.pracinho.panguingue.repository;

import org.springframework.stereotype.Repository;
import pl.pracinho.panguingue.exception.GameNotFoundException;
import pl.pracinho.panguingue.model.dto.GameDto;
import pl.pracinho.panguingue.model.entity.Game;
import pl.pracinho.panguingue.model.entity.Player;
import pl.pracinho.panguingue.model.enums.GameStatus;
import pl.pracinho.panguingue.model.mapper.GameDtoMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class GameRepository {
    private Map<String, Game> gameMap;
    private final int MAX_PLAYERS_COUNT = 2;

    public GameRepository() {
        gameMap = new HashMap<>();
    }

    public String newGame(String playerName, int playersCount) {
        Game game = new Game(playerName, playersCount);
        gameMap.put(game.getId(), game);
        return game.getId();
    }

    public List<GameDto> getAvailableGames() {
        return gameMap.values()
                .stream()
                .filter(game -> game.getStatus() != GameStatus.FINISHED)
                .map(g -> GameDtoMapper.parse(g, null))
                .toList();
    }

    public Optional<Game> findById(String gameId) {
        return Optional.ofNullable(gameMap.get(gameId));
    }

    public Game joinGame(String name, String gameId) throws IllegalStateException {
        Game game = gameMap.get(gameId);
        if (game == null)
            throw new GameNotFoundException(gameId);

        if (game.getStatus() != GameStatus.NEW)
            throw new IllegalStateException("Cannot join to " + gameId + ". Game status : " + game.getStatus());

        if (game.getPlayers().get(0).getName().equals(name))
            throw new IllegalStateException("Game " + gameId + " was created by you!");

        game.getPlayers().add(new Player(name, game.getPlayers().size() + 1));
        return game;
    }

}