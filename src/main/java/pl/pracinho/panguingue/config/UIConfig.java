package pl.pracinho.panguingue.config;

public class UIConfig {
    public static final String PREFIX = "/panguingue";
    public static final String GAMES = PREFIX + "/games";
    public static final String NEW_GAME = GAMES + "/new";
    public static final String GAME_PAGE = GAMES + "/{gameId}";
    public static final String GAME_ROOM = GAME_PAGE + "/room";
    public static final String PLAYERS = GAME_ROOM + "/players";
    public static final String CARD = PREFIX + "/card";
    public static final String CARD_TEST = CARD + "/{suit}/{rank}";
    public static final String GAME_MOVE = GAME_PAGE + "/move";
}