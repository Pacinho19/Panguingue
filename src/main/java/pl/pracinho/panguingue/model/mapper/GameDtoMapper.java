package pl.pracinho.panguingue.model.mapper;

import pl.pracinho.panguingue.model.GameDto;
import pl.pracinho.panguingue.model.entity.Game;
import pl.pracinho.panguingue.model.entity.Player;

public class GameDtoMapper {

    public static GameDto parse(Game game) {
        return GameDto.builder()
                .id(game.getId())
                .startTime(game.getStartTime())
                .players(game.getPlayers().stream().map(Player::getName).toList())
                .status(game.getStatus())
                .build();
    }
}