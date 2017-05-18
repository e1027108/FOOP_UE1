package messagehandler;

import messagehandler.message.Message;

public abstract class MessageHandler {
	
	//general server messages
	public static final String UPDATE = "UPD";
	public static final String BASE_INFO = "BAI";
	
	//game status server messages
	public static final String GAME_START = "STR";
	public static final String GAME_END = "END";
	
	//player specific server messages
	public static final String TEXT_MESSAGE = "TXT";
	public static final String YOU_LOSE = "SAD";
	public static final String PLAYER_LEFT = "PLL";

	//client messages
	public static final String PLAYER_READY = "PLR";
	public static final String DIRECTION_CHANGE = "DIC";
	public static final String DISCONNECT = "DIS";
	public static final String INITIALIZATION = "INI";

	protected static final int POINT_CODE_LENGTH = 4;
	
	public abstract Message decode(String input);
	
	public abstract String encode(Message input);
	
}
