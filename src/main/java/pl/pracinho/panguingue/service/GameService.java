package pl.pracinho.panguingue.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pracinho.panguingue.model.dto.GameDto;
import pl.pracinho.panguingue.model.entity.Game;
import pl.pracinho.panguingue.model.enums.GameStatus;
import pl.pracinho.panguingue.model.mapper.GameDtoMapper;
import pl.pracinho.panguingue.repository.GameRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GameService {

    private static final int MAX_PLAYERS = 2;
    private final GameRepository gameRepository;
    private final GameLogicService gameLogicService;

    public List<GameDto> getAvailableGames() {
        return gameRepository.getAvailableGames();
    }

    public String newGame(String name) {
        List<GameDto> activeGames = getAvailableGames();
        if (activeGames.size() >= 10)
            throw new IllegalStateException("Cannot create new Game! Active game count : " + activeGames.size());
        return gameRepository.newGame(name);
    }

    public GameDto findDtoById(String gameId) {
        return GameDtoMapper.parse(gameLogicService.findById(gameId));
    }


    public void joinGame(String name, String gameId) throws IllegalStateException {
        Game game = gameRepository.joinGame(name, gameId);
        if (game.getPlayers().size() == MAX_PLAYERS) game.setStatus(GameStatus.IN_PROGRESS);
    }

    public boolean checkStartGame(String gameId) {
        return findDtoById(gameId).getPlayers().size() == MAX_PLAYERS;
    }

    public boolean canJoin(GameDto game, String name) {
        return game.getPlayers().size() < MAX_PLAYERS && game.getPlayers().stream().noneMatch(p -> p.equals(name));
    }

    public boolean checkPlayGame(String name, GameDto game) {
        return game.getPlayers()
                .stream()
                .anyMatch(p -> p.equals(name));
    }

    public void checkGamePage(GameDto game, String name) {
        if (game.getStatus() == GameStatus.FINISHED)
            throw new IllegalStateException("Game " + game.getId() + " finished!");

        if (game.getStatus() != GameStatus.IN_PROGRESS)
            throw new IllegalStateException("Game " + game.getId() + " not started!");

        if (!checkPlayGame(name, game))
            throw new IllegalStateException("Game " + game.getId() + " in progress! You can't open game page!");
    }
}