package pl.pracinho.panguingue.model.mapper;

import pl.pracinho.panguingue.model.dto.PlayerDto;
import pl.pracinho.panguingue.model.entity.Player;

public class PlayerDtoMapper {
    public static PlayerDto parse(Player player) {
        return new PlayerDto(
                player.getName()
        );
    }
}