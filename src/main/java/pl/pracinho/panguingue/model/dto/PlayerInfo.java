package pl.pracinho.panguingue.model.dto;

import pl.pracinho.panguingue.model.enums.Place;

public record PlayerInfo(String name, Integer cardsCount, Place place) {
}