package pl.pracinho.panguingue.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pracinho.panguingue.model.enums.CardRank;
import pl.pracinho.panguingue.model.enums.CardSuit;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CardDto {

    private CardSuit suit;
    private CardRank rank;


}