package pl.pracinho.panguingue.model.entity;

import lombok.Getter;
import lombok.Setter;
import pl.pracinho.panguingue.model.dto.CardDto;

import java.util.Collections;
import java.util.List;

@Getter
public class Player {
    private final String name;
    @Setter
    private List<CardDto> cards;
    private int index;

    public Player(String name, int index) {
        this.name = name;
        this.index = index;
        this.cards = Collections.emptyList();
    }


}