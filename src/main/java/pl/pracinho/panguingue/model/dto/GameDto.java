package pl.pracinho.panguingue.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import pl.pracinho.panguingue.model.enums.GameStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Stack;

@Getter
@Builder
@AllArgsConstructor
public class GameDto {

    private String id;
    private GameStatus status;
    private List<String> players;
    private List<CardDto> cards;
    private Stack<CardDto> stack;
    private Map<Integer, PlayerInfo> playersInfo;
    private LocalDateTime startTime;
    private int playersCount;
    private Integer playerIndex;
    private Integer actualPlayer;

    private List<ResultDto> results;

    public int getNextPlayer(int offset) {
        int idx = playerIndex + offset;
        if (idx > playersCount) return idx - playersCount;
        return idx;
    }

    public CardDto getTopOfStack() {
        return stack.peek();
    }
}