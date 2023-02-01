package pl.pracinho.panguingue.model.entity;

import lombok.Getter;
import lombok.Setter;
import pl.pracinho.panguingue.exception.PlayerNotFoundException;
import pl.pracinho.panguingue.model.enums.GameStatus;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.UUID;

@Getter
public class Game {

    private String id;
    @Setter
    private GameStatus status;
    private LinkedList<Player> players;
    private LocalDateTime startTime;

    @Setter
    private String winnerInfo;

    private int playersCount;

    public Game(String player1, int playersCount) {
        this.playersCount = playersCount;
        players = new LinkedList<>();
        players.add(new Player(player1, 1));
        this.id = UUID.randomUUID().toString();
        this.status = GameStatus.NEW;
        this.startTime = LocalDateTime.now();
    }

    public Player getPlayer(String playerName) {
        return players.stream()
                .filter(p -> p.getName().equals(playerName))
                .findFirst()
                .orElseThrow(() -> new PlayerNotFoundException(playerName));
    }

}