package pl.pracinho.panguingue.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pracinho.panguingue.exception.GameNotFoundException;
import pl.pracinho.panguingue.model.entity.Game;
import pl.pracinho.panguingue.repository.GameRepository;

@RequiredArgsConstructor
@Service
public class GameLogicService {

    private final GameRepository gameRepository;
    public Game findById(String gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId))
                ;
    }

}
