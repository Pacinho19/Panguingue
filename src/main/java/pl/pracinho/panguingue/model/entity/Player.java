package pl.pracinho.panguingue.model.entity;

import lombok.Getter;
import lombok.Setter;
import pl.pracinho.panguingue.model.dto.CardDto;

import java.util.List;

@Getter
public class Player {
    private final String name;
    @Setter
    private List<CardDto> cards;

    public Player(String name) {
        this.name = name;
    }


}