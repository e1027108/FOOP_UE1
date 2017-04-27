package MessageHandler;

public class ServerMessageHandler extends MessageHandler{

	@Override
	public Message decode(String input) {
		Message decoded = null;
		String head = input.substring(0,2);
		
		switch(head){
		case UPDATE:
			decoded = decodeUpdate(input);
			break;
		case BASE_INFO:
			decoded = decodeBaseInfo(input);
			break;
		case GAME_START:
			decoded = decodeGameStart(input);
			break;
		case GAME_END:
			decoded = decodeGameEnd(input);
			break;
		case TEXT_MESSAGE:
			decoded = decodeTextMessage(input);
			break;
		case YOU_LOSE:
			decoded = decodeYouLose(input);
			break;
		case PLAYER_LEFT:
			decoded = decodePlayerLeft(input);
			break;
		default:
			break;
		}
		
		return decoded;
	}

	private Message decodePlayerLeft(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	private Message decodeYouLose(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	private Message decodeTextMessage(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	private Message decodeGameEnd(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	private Message decodeGameStart(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	private Message decodeBaseInfo(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	private Message decodeUpdate(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encode(Message input) {
		String encoded = "";
		
		switch(input.getType()){
		case UPD:
			encoded = encodeUpdate(input);
			break;
		case BAI:
			encoded = encodeBaseInfo(input);
			break;
		case STR:
			encoded = encodeGameStart(input);
			break;
		case END:
			encoded = encodeGameEnd(input);
			break;
		case TXT:
			encoded = encodeTextMessage(input);
			break;
		case SAD:
			encoded = encodeYouLose(input);
			break;
		case PLL:
			encoded = encodePlayerLeft(input);
			break;
		default:
			break;
		}
		
		return encoded;
	}

	private String encodePlayerLeft(Message input) {
		// TODO Auto-generated method stub
		return null;
	}

	private String encodeYouLose(Message input) {
		// TODO Auto-generated method stub
		return null;
	}

	private String encodeTextMessage(Message input) {
		// TODO Auto-generated method stub
		return null;
	}

	private String encodeGameEnd(Message input) {
		// TODO Auto-generated method stub
		return null;
	}

	private String encodeGameStart(Message input) {
		// TODO Auto-generated method stub
		return null;
	}

	private String encodeBaseInfo(Message input) {
		// TODO Auto-generated method stub
		return null;
	}

	private String encodeUpdate(Message input) {
		// TODO Auto-generated method stub
		return null;
	}

}
