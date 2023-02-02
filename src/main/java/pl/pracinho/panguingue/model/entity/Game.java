package pl.pracinho.panguingue.model.entity;

import lombok.Getter;
import lombok.Setter;
import pl.pracinho.panguingue.exception.PlayerNotFoundException;
import pl.pracinho.panguingue.model.dto.CardDto;
import pl.pracinho.panguingue.model.enums.GameStatus;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;
import java.util.stream.IntStream;

@Getter
public class Game {

    private String id;
    @Setter
    private GameStatus status;
    private Stack<CardDto> stack;

    private LinkedList<Player> players;
    private LocalDateTime startTime;
    private int playersCount;

    @Setter
    private int actualPlayer;

    public Game(String player1, int playersCount) {
        this.playersCount = playersCount;
        players = new LinkedList<>();
        players.add(new Player(player1, 1));
        this.id = UUID.randomUUID().toString();
        this.status = GameStatus.NEW;
        this.startTime = LocalDateTime.now();
        this.stack = new Stack<>();
    }

    public Player getPlayer(String playerName) {
        return players.stream()
                .filter(p -> p.getName().equals(playerName))
                .findFirst()
                .orElseThrow(() -> new PlayerNotFoundException(playerName));
    }

    public void addCardToStack(CardDto cardDto) {
        this.stack.push(cardDto);
    }

    public List<CardDto> getFromStack() {
        int count = Math.min(stack.size(), 3);
        return IntStream.range(0, count)
                .boxed()
                .map(i -> stack.pop())
                .toList();
    }

    public void nextPlayer() {
        if (actualPlayer == playersCount) this.actualPlayer = 1;
        else actualPlayer++;
    }
}