package pl.pracinho.panguingue.model.dto;

import pl.pracinho.panguingue.model.enums.CardRank;
import pl.pracinho.panguingue.model.enums.CardSuit;

public record CardDto(CardSuit suit, CardRank rank) {
}
