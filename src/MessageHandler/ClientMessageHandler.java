package MessageHandler;

import MessageHandler.Message.MessageType;
import game.Directions;

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
		return new Message(MessageType.DIS);
	}

	private Message decodeReady(String input) {
		return new Message(MessageType.PLR);
	}

	private Message decodeDirectionChange(String input) {
		String payload = input.substring(3,input.length());

		if(payload.length() > 1){
			throw new IllegalArgumentException("Directions should be only one character long. Actual direction string is: " + payload);
		}
		else{
			switch(payload.charAt(0)){
			case 'W':
				return new DirectionChangeMessage(MessageType.DIC,Directions.W);
			case 'N':
				return new DirectionChangeMessage(MessageType.DIC,Directions.N);
			case 'E':
				return new DirectionChangeMessage(MessageType.DIC,Directions.E);
			case 'S':
				return new DirectionChangeMessage(MessageType.DIC,Directions.S);
			default:
				throw new IllegalArgumentException("Direction '" + payload.charAt(0) + "' is not defined!");
			}
		}
	}

	@Override
	public String encode(Message input) {
		String encoded = "";

		switch(input.getType()){
		case DIC:
			encoded = encodeDirectionChange((DirectionChangeMessage) input);
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
		return DISCONNECT;
	}

	private String encodeReady(Message input) {
		return PLAYER_READY;
	}

	private String encodeDirectionChange(DirectionChangeMessage input) {
		return DIRECTION_CHANGE + input.getDirection();
	}	

}
