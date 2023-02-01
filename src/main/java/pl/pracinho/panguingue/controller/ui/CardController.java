package pl.pracinho.panguingue.controller.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.pracinho.panguingue.config.UIConfig;
import pl.pracinho.panguingue.model.dto.CardDto;
import pl.pracinho.panguingue.model.enums.CardRank;
import pl.pracinho.panguingue.model.enums.CardSuit;

import javax.websocket.server.PathParam;

@Controller
@RequiredArgsConstructor
public class CardController {

    @GetMapping(UIConfig.CARD_TEST)
    public String cardTest(@PathVariable("suit") CardSuit cardSuit,
                           @PathVariable("rank") CardRank cardRank,
                           Model model) {
        model.addAttribute("cardDto", new CardDto(cardSuit, cardRank));
        return "card-test";
    }
}
