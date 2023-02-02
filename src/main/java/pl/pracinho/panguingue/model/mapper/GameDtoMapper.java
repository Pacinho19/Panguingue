package pl.pracinho.panguingue.model.mapper;

import pl.pracinho.panguingue.exception.PlayerNotFoundException;
import pl.pracinho.panguingue.model.dto.CardDto;
import pl.pracinho.panguingue.model.dto.GameDto;
import pl.pracinho.panguingue.model.entity.Game;
import pl.pracinho.panguingue.model.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class GameDtoMapper {

    public static GameDto parse(Game game, String name) {
        return GameDto.builder()
                .id(game.getId())
                .startTime(game.getStartTime())
                .players(game.getPlayers().stream().map(Player::getName).toList())
                .cards(
                        getCards(game.getPlayers(), name)
                )
                .opponentsCardsCount(
                        getOpponentsCardsCount(game.getPlayers(), name)
                )
                .playerIndex(
                        getPlayerIndex(game.getPlayers(), name)
                )
                .status(game.getStatus())
                .playersCount(game.getPlayersCount())
                .stack(new Stack<>())
                .build();
    }

    private static Integer getPlayerIndex(LinkedList<Player> players, String name) {
        Optional<Player> playerOpt = players.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst();

        if (playerOpt.isEmpty()) return null;
        return playerOpt.get()
                .getIndex();
    }

    private static Map<Integer, Integer> getOpponentsCardsCount(LinkedList<Player> players, String name) {
        if (name == null) return Collections.emptyMap();

        return players.stream()
                .filter(p -> !p.getName().equals(name))
                .map(p -> Map.entry(p.getIndex(), p.getCards().size()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static List<CardDto> getCards(LinkedList<Player> players, String name) {
        if (name == null) return Collections.emptyList();

        Optional<Player> playerOptional = players.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst();

        if (playerOptional.isPresent()) {
            List<CardDto> cards = playerOptional.get().getCards();
            cards.sort(Comparator.comparing(c -> c.rank().getValue()));
            return cards;
        }
        return Collections.emptyList();
    }
}