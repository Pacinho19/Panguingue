package pl.pracinho.panguingue.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.pracinho.panguingue.model.dto.CardDto;
import pl.pracinho.panguingue.model.dto.GameDto;
import pl.pracinho.panguingue.model.dto.ThrowCardDto;
import pl.pracinho.panguingue.model.entity.Game;
import pl.pracinho.panguingue.model.enums.GameStatus;
import pl.pracinho.panguingue.model.mapper.GameDtoMapper;
import pl.pracinho.panguingue.repository.GameRepository;
import pl.pracinho.panguingue.utils.GameUtils;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final GameLogicService gameLogicService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public List<GameDto> getAvailableGames() {
        return gameRepository.getAvailableGames();
    }

    public String newGame(String name, int playersCount) {
        List<GameDto> activeGames = getAvailableGames();
        if (activeGames.size() >= 10)
            throw new IllegalStateException("Cannot create new Game! Active game count : " + activeGames.size());
        return gameRepository.newGame(name,playersCount);
    }

    public GameDto findDtoById(String gameId, String name) {
        return GameDtoMapper.parse(gameLogicService.findById(gameId), name);
    }


    public void joinGame(String name, String gameId) throws IllegalStateException {
        Game game = gameRepository.joinGame(name, gameId);
        if (game.getPlayers().size() == game.getPlayersCount()) game.setStatus(GameStatus.IN_PROGRESS);
    }

    public boolean checkStartGame(String gameId) {
        Game game = gameLogicService.findById(gameId);
        boolean startGame = game.getPlayers().size() == game.getPlayersCount();
        if (startGame) gameLogicService.dealTheCards(game);
        return startGame;
    }

    public boolean canJoin(GameDto game, String name) {
        return game.getPlayers().size() < game.getPlayersCount() && game.getPlayers().stream().noneMatch(p -> p.equals(name));
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

    public void move(String gameId, String name, LinkedList<CardDto> cardsToThrow) {
        Game game = gameLogicService.findById(gameId);
        List<CardDto> cards = game.getPlayers()
                .stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .get()
                .getCards();

        cardsToThrow
                .forEach(cardDto -> {
                    CardDto playerCard = findPlayerCard(cards, cardDto);
                    cards.remove(playerCard);
                    game.addCardToStack(playerCard);
                });

        if (cards.isEmpty())
            GameUtils.finishPlayer(game, name);
        finishRound(game);
    }

    private List<CardDto> getCardsToThrow(List<CardDto> playerCards, ThrowCardDto throwCardDto) {
        return !throwCardDto.all()
                ? List.of(throwCardDto.card())
                : playerCards.stream().filter(c -> c.getRank() == throwCardDto.card().getRank()).toList();
    }

    public void takeCards(String name, String gameId) {
        Game game = gameLogicService.findById(gameId);
        List<CardDto> cards = game.getPlayers()
                .stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .get()
                .getCards();

        cards.addAll(GameUtils.getFromStack(game));
        finishRound(game);
    }

    private void finishRound(Game game) {
        GameUtils.nextPlayer(game);
        simpMessagingTemplate.convertAndSend("/reload-board/" + game.getId(), true);
    }

    private CardDto findPlayerCard(List<CardDto> cards, CardDto cardDto) {
        return cards.stream()
                .filter(f -> f.getRank() == cardDto.getRank()
                             && f.getSuit() == cardDto.getSuit())
                .findFirst()
                .get();

    }


}