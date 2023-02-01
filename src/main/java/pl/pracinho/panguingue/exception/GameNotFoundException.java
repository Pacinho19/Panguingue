package pl.pracinho.panguingue.exception;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(String gameId) {
        super("Game " + gameId + " not found!");
    }
}