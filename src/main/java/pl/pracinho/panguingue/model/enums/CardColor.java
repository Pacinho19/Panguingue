package pl.pracinho.panguingue.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.*;

@RequiredArgsConstructor
public enum CardColor {

    BLACK("#000000"),
    RED("#FF0000");

    @Getter
    private final String hex;

}
