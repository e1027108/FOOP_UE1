package messagehandler;

import messagehandler.message.Message;

public abstract class MessageHandler {
	
	//general server messages
	protected static final String UPDATE = "UPD";
	protected static final String BASE_INFO = "BAI";
	
	//game status server messages
	protected static final String GAME_START = "STR";
	protected static final String GAME_END = "END";
	
	//player specific server messages
	protected static final String TEXT_MESSAGE = "TXT";
	protected static final String YOU_LOSE = "SAD";
	protected static final String PLAYER_LEFT = "PLL";

	//client messages
	protected static final String PLAYER_READY = "PLR";
	protected static final String DIRECTION_CHANGE = "DIC";
	protected static final String DISCONNECT = "DIS";
	
	public abstract Message decode(String input);
	
	public abstract String encode(Message input);
	
}
