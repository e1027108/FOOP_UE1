package messagehandler;

import messagehandler.message.BaseMessage;
import messagehandler.message.Message;
import messagehandler.message.PlayerLeftMessage;
import messagehandler.message.TextMessage;
import messagehandler.message.UpdateMessage;
import messagehandler.message.Message.MessageType;

public class ServerMessageHandler extends MessageHandler{

	@Override
	public Message decode(String input) {
		Message decoded = null;
		String head = input.substring(0,3);
		
		switch(head){
		case UPDATE:
			decoded = decodeUpdate(input);
			break;
		case BASE_INFO:
			decoded = decodeBaseInfo(input);
			break;
		case GAME_START:
			decoded = decodeGameStart();
			break;
		case GAME_END:
			decoded = decodeGameEnd();
			break;
		case TEXT_MESSAGE:
			decoded = decodeTextMessage(input);
			break;
		case YOU_LOSE:
			decoded = decodeYouLose();
			break;
		case PLAYER_LEFT:
			decoded = decodePlayerLeft(input);
			break;
		default:
			break;
		}
		
		return decoded;
	}

	private PlayerLeftMessage decodePlayerLeft(String input) {
		String payload = input.substring(3,input.length());

		if(payload.length() > 1){
			throw new IllegalArgumentException("Directions should be only one character long. Actual direction string is: " + payload);
		}
		else{
			return new PlayerLeftMessage(MessageType.PLL,Integer.parseInt(payload));
		}
	}

	private Message decodeYouLose() {
		return new Message(MessageType.SAD);
	}

	private TextMessage decodeTextMessage(String input) {
		String payload = input.substring(3,input.length());
		
		return new TextMessage(MessageType.TXT, payload);
	}

	private Message decodeGameEnd() {
		return new Message(MessageType.END);
	}

	private Message decodeGameStart() {
		return new Message(MessageType.STR);
	}

	private BaseMessage decodeBaseInfo(String input) {
		String payload = input.substring(3,input.length());
		
		//TODO implement code for all player's informations
		
		return null;
	}

	private UpdateMessage decodeUpdate(String input) {
		String payload = input.substring(3,input.length());
		
		//TODO implement code for all player's position and status information
		
		return null;
	}

	@Override
	public String encode(Message input) {
		String encoded = "";
		
		switch(input.getType()){
		case UPD:
			encoded = encodeUpdate((UpdateMessage) input);
			break;
		case BAI:
			encoded = encodeBaseInfo((BaseMessage) input);
			break;
		case STR:
			encoded = encodeGameStart();
			break;
		case END:
			encoded = encodeGameEnd();
			break;
		case TXT:
			encoded = encodeTextMessage((TextMessage) input);
			break;
		case SAD:
			encoded = encodeYouLose();
			break;
		case PLL:
			encoded = encodePlayerLeft((PlayerLeftMessage) input);
			break;
		default:
			break;
		}
		
		return encoded;
	}

	private String encodePlayerLeft(PlayerLeftMessage input) {
		return PLAYER_LEFT + input.getPlayerNumber();
	}

	private String encodeYouLose() {
		return YOU_LOSE;
	}

	private String encodeTextMessage(TextMessage input) {
		return input.getMessage();
	}

	private String encodeGameEnd() {
		return GAME_END;
	}

	private String encodeGameStart() {
		return GAME_START;
	}

	private String encodeBaseInfo(BaseMessage input) {
		//TODO read from then implemented BaseMessage
		
		return null;
	}

	private String encodeUpdate(UpdateMessage input) {
		//TODO read from then implemented UpdateMessage

		return null;
	}

}
