package pl.pracinho.panguingue.model.mapper;

import pl.pracinho.panguingue.model.dto.CardDto;
import pl.pracinho.panguingue.model.dto.GameDto;
import pl.pracinho.panguingue.model.dto.PlayerInfo;
import pl.pracinho.panguingue.model.dto.ResultDto;
import pl.pracinho.panguingue.model.entity.Game;
import pl.pracinho.panguingue.model.entity.Player;
import pl.pracinho.panguingue.model.enums.Place;

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
                .playersInfo(
                        getPlayersInfo(game.getPlayers(), game.getResults())
                )
                .playerIndex(
                        getPlayerIndex(game.getPlayers(), name)
                )
                .status(game.getStatus())
                .playersCount(game.getPlayersCount())
                .stack(game.getStack())
                .actualPlayer(game.getActualPlayer())
                .results(game.getResults())
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

    private static Map<Integer, PlayerInfo> getPlayersInfo(LinkedList<Player> players, List<ResultDto> results) {
        return players.stream()
                .collect(Collectors.toMap(
                        Player::getIndex,
                        p -> new PlayerInfo(p.getName(), p.getCards().size(), getPlace(results, p.getIndex()))
                ));
    }

    private static Place getPlace(List<ResultDto> results, Integer playerIndex) {
        ResultDto result = results.stream()
                .filter(r -> r.playerIndex() == playerIndex)
                .findFirst()
                .orElse(null);

        return result != null
                ? result.place()
                : null;
    }

    private static List<CardDto> getCards(LinkedList<Player> players, String name) {
        if (name == null) return Collections.emptyList();

        Optional<Player> playerOptional = players.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst();

        if (playerOptional.isPresent()) {
            List<CardDto> cards = playerOptional.get().getCards();
            cards.sort(Comparator.comparing(c -> c.getRank().getValue()));
            return cards;
        }
        return Collections.emptyList();
    }
}