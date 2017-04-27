package MessageHandler;

/**
 * this class implements message handling for all messages coming from the client
 * the server needs the decode, the client the encode message
 * @author patrick.bellositz
 */
public class ClientMessageHandler extends MessageHandler {

	@Override
	public Message decode(String input){
		Message decoded = null;
		String head = input.substring(0,2);
		
		switch(head){		
		case DIRECTION_CHANGE:
			decoded = decodeDirectionChange(input);
			break;
		case PLAYER_READY:
			decoded = decodeReady(input);
			break;
		case DISCONNECT:
			decoded = decodeDisconnect(input);
			break;
		default:
			//TODO this is not a message OR this is a server message
			break;
		}
		
		return decoded;
	}

	private Message decodeDisconnect(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	private Message decodeReady(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	private Message decodeDirectionChange(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encode(Message input) {
		String encoded = "";
		
		switch(input.getType()){
		case DIC:
			encoded = encodeDirectionChange(input);
			break;
		case PLR:
			encoded = encodeReady(input);
			break;
		case DIS:
			encoded = encodeDisconnect(input);
			break;
		default:
			break;
		}
		
		return encoded;
	}

	private String encodeDisconnect(Message input) {
		// TODO Auto-generated method stub
		return null;
	}

	private String encodeReady(Message input) {
		// TODO Auto-generated method stub
		return null;
	}

	private String encodeDirectionChange(Message input) {
		// TODO Auto-generated method stub
		return null;
	}	

}
