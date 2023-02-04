package pl.pracinho.panguingue.utils;

import pl.pracinho.panguingue.model.dto.CardDto;
import pl.pracinho.panguingue.model.dto.ResultDto;
import pl.pracinho.panguingue.model.entity.Game;
import pl.pracinho.panguingue.model.enums.GameStatus;
import pl.pracinho.panguingue.model.enums.Place;

import java.util.List;
import java.util.stream.IntStream;

public class GameUtils {

    public static List<CardDto> getFromStack(Game game) {
        int count = game.getStack().size() > 3 ? 3 : game.getStack().size() - 1;
        return IntStream.range(0, count).boxed().map(i -> game.getStack().pop()).toList();
    }

    public static void nextPlayer(Game game) {
        if (game.getActualPlayer() == 99) return;

        if (game.getActualPlayer() == game.getPlayersCount())
            game.setActualPlayer(1);
        else
            game.setActualPlayer(game.getActualPlayer() + 1);

        if (isNextPlayerFinished(game))
            nextPlayer(game);
    }

    private static boolean isNextPlayerFinished(Game game) {
        return game.getPlayers().stream()
                .filter(p -> p.getIndex() == game.getActualPlayer())
                .findFirst().get()
                .getCards()
                .isEmpty();
    }

    public static void finishPlayer(Game game, String playerName) {
        game.getResults().add(new ResultDto(Place.findByNumber(game.getResults().size() + 1), getPlayerIdx(game, playerName)));
        if (game.getResults().size() + 1 == game.getPlayersCount()) {
            game.getResults().add(new ResultDto(Place.findByNumber(game.getResults().size() + 1),
                    getLastPlayerIdx(game)));
            game.setActualPlayer(99);
            game.setStatus(GameStatus.FINISHED);
        }
    }

    private static int getPlayerIdx(Game game, String playerName) {
        return game.getPlayers()
                .stream()
                .filter(p -> p.getName().equals(playerName))
                .findFirst()
                .get()
                .getIndex();
    }

    private static int getLastPlayerIdx(Game game) {
        return game.getPlayers().stream()
                .filter(p -> !p.getCards().isEmpty())
                .findFirst()
                .get()
                .getIndex();
    }
}