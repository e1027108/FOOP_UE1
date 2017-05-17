package messagehandler;

import java.util.ArrayList;

import game.Directions;
import javafx.scene.paint.Color;
import messagehandler.message.DirectionChangeMessage;
import messagehandler.message.InfoMessage;
import messagehandler.message.Message;
import messagehandler.message.PlayerInfo;
import messagehandler.message.Message.MessageType;

/**
 * this class implements message handling for all messages coming from the client
 * the server needs the decode, the client the encode message
 * @author patrick.bellositz
 */
public class ClientMessageHandler extends MessageHandler {

	@Override
	public Message decode(String input){
		Message decoded = null;
		String head = input.substring(0,3);

		switch(head){		
		case DIRECTION_CHANGE:
			decoded = decodeDirectionChange(input);
			break;
		case PLAYER_READY:
			decoded = decodeReady();
			break;
		case DISCONNECT:
			decoded = decodeDisconnect();
			break;
		case INITIALIZATION:
			decoded = decodeInitialization(input);
			break;
		default:
			//TODO this is not a message OR this is a server message
			break;
		}

		return decoded;
	}

	private Message decodeDisconnect() {
		return new Message(MessageType.DIS);
	}

	private Message decodeReady() {
		return new Message(MessageType.PLR);
	}

	private DirectionChangeMessage decodeDirectionChange(String input) {
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
	
	private Message decodeInitialization(String input) {
		String payload = input.substring(3,input.length());
		String info[] = payload.split("(?=[A-Z])");
		ArrayList<PlayerInfo> infoContainer = new ArrayList<PlayerInfo>();
		PlayerInfo pi = new PlayerInfo();
		infoContainer.add(pi);
		int remainingTime = 0;
		for(String s: info){
			switch(s.charAt(0)){
			case 'N': //name
				pi.setName(s.substring(1));
				break;
			case 'C': //color
				pi.setColor(Color.web(s.substring(1),1));
				break;
			case 'T':
				remainingTime = Integer.parseInt(s.substring(1));
				break;
			default:
				throw new IllegalArgumentException("Invalid information code: " + s.charAt(0));
			}
		}
		
		return new InfoMessage(MessageType.INI,infoContainer,remainingTime);
	}

	@Override
	public String encode(Message input) {
		String encoded = "";

		switch(input.getType()){
		case DIC:
			encoded = encodeDirectionChange((DirectionChangeMessage) input);
			break;
		case PLR:
			encoded = encodeReady();
			break;
		case DIS:
			encoded = encodeDisconnect();
			break;
		case INI:
			encoded = encodeInitialization((InfoMessage) input);
		default:
			break;
		}

		return encoded;
	}

	private String encodeDisconnect() {
		return DISCONNECT;
	}

	private String encodeReady() {
		return PLAYER_READY;
	}

	private String encodeDirectionChange(DirectionChangeMessage input) {
		return DIRECTION_CHANGE + input.getDirection();
	}
	
	private String encodeInitialization(InfoMessage input) {
		return INITIALIZATION + "N" + input.getInfos().get(0).getName() + "C" + input.getInfos().get(0).getColor();
	}

}
