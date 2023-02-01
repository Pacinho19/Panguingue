package pl.pracinho.panguingue.model.entity;

import lombok.Getter;

@Getter
public class Player {
    private final String name;
    public Player(String name) {
        this.name = name;
    }



}