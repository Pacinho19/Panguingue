package pl.pracinho.panguingue.config;

public class UIConfig {
    public static final String HOME = "/panguingue";
    public static final String GAMES = HOME + "/games";
    public static final String NEW_GAME = GAMES + "/new";
    public static final String GAME_PAGE = GAMES + "/{gameId}";
    public static final String GAME_ROOM = GAME_PAGE + "/room";
    public static final String PLAYERS = GAME_ROOM + "/players";
}