package pl.pracinho.panguingue.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;
import pl.pracinho.panguingue.exception.GameNotFoundException;
import pl.pracinho.panguingue.model.dto.CardDto;
import pl.pracinho.panguingue.model.entity.Game;
import pl.pracinho.panguingue.model.enums.CardRank;
import pl.pracinho.panguingue.model.enums.CardSuit;
import pl.pracinho.panguingue.repository.GameRepository;

import java.util.*;

@RequiredArgsConstructor
@Service
public class GameLogicService {

    private final GameRepository gameRepository;
    private final List<CardDto> CARDS_DECK = initCards();

    public Game findById(String gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId))
                ;
    }

    private List<CardDto> initCards() {
        return Arrays.stream(CardSuit.values())
                .flatMap(cs -> Arrays.stream(CardRank.values())
                        .map(cr -> new CardDto(cs, cr)))
                .toList();
    }

    public void dealTheCards(Game game) {
        List<CardDto> cards = new ArrayList<>(CARDS_DECK);
        Collections.shuffle(cards);

        Stack<List<CardDto>> cardParts = partitionCards(cards, game.getPlayersCount());
        game.getPlayers()
                .forEach(p -> p.setCards(cardParts.pop()));
    }

    private Stack<List<CardDto>> partitionCards(List<CardDto> cards, int size) {
        List<List<CardDto>> partition = ListUtils.partition(cards, (cards.size() / size));
        Stack<List<CardDto>> stack = new Stack<>();
        stack.addAll(partition);
        return stack;
    }
}
