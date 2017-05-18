package messagehandler;

import java.util.ArrayList;
import java.util.List;

import game.Point;
import javafx.scene.paint.Color;
import messagehandler.message.InfoMessage;
import messagehandler.message.Message;
import messagehandler.message.Message.MessageType;
import messagehandler.message.PlayerInfo;
import messagehandler.message.PlayerLeftMessage;
import messagehandler.message.TextMessage;

public class ServerMessageHandler extends MessageHandler {

	@Override
	public Message decode(String input) {
		Message decoded = null;
		String head = input.substring(0,3);

		switch(head){
		case UPDATE:
			decoded = decodePlayerInfo(MessageType.UPD, input);
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
			throw new IllegalArgumentException("Invalid message type code: " + head);
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

	/**
	 * TODO: irgendwie broken, alles == null!
	 */
	private InfoMessage decodePlayerInfo(MessageType type, String input) {
		String payload = input.substring(3,input.length());
		ArrayList<PlayerInfo> pis = new ArrayList<PlayerInfo>();
		int remainingTime = 0;

		//split string into player's information and time left (at end of message)
		String players[] = payload.split("(?=[PT])");

		for(String p: players){
			//split the strings into substrings starting with upper case letter
			String info[] = p.split("(?=[A-Z])");
			PlayerInfo playerInf = new PlayerInfo();
			for(String s: info){
				switch(s.charAt(0)){
				case 'P': //playerNumber
					playerInf.setNumber(Integer.parseInt(s.substring(1,2)));
					break;
				case 'N': //name
					playerInf.setName(s.substring(1));
					break;
				case 'C': //color
					playerInf.setColor(Color.web(s.substring(1),1));
					break;
				case 'M': //max health
					playerInf.setMaxHealth(Integer.parseInt(s.substring(1)));
					break;
				case 'H': //current health
					playerInf.setHealth(Integer.parseInt(s.substring(1)));
					break;
				case 'B': //body positions
					playerInf.setBody(computePosition(s.substring(1)));
					break;
				case 'L': //bLocked
					playerInf.setBlocked(true);
					break;
				case 'I': //invincible
					playerInf.setInvincible(true);
					break;
				case 'R': //reverse control
					playerInf.setReversed(true);
					break;
				case 'T':
					remainingTime = Integer.parseInt(s.substring(1));
					break;
				default:
					throw new IllegalArgumentException("Invalid information code: " + s.charAt(0));
				}
			}
			if (p.charAt(0) != 'T') {
				pis.add(playerInf);
			}
		}

		return new InfoMessage(type,pis,remainingTime);
	}

	private ArrayList<Point> computePosition(String coded) {
		ArrayList<Point> points = new ArrayList<Point>();

		if(coded.length()%POINT_CODE_LENGTH != 0){
			throw new IllegalArgumentException("Invalid position code!");
		}

		for(int i = 0; i < (coded.length()/POINT_CODE_LENGTH); i++){
			String point = coded.substring(i*POINT_CODE_LENGTH, i*POINT_CODE_LENGTH + POINT_CODE_LENGTH);

			points.add(new Point(Integer.parseInt(point.substring(0, POINT_CODE_LENGTH/2)),
					Integer.parseInt(point.substring(POINT_CODE_LENGTH/2, POINT_CODE_LENGTH))));
		}

		return points;
	}

	@Override
	public String encode(Message input) {
		String encoded = "";

		switch(input.getType()){
		case UPD:
			encoded = UPDATE + encodePlayerInfo((InfoMessage) input);
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
			throw new IllegalArgumentException("Invalid message type: " + input.getType());
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

	private String encodePlayerInfo(InfoMessage input) {
		String encoded = "";
		List<PlayerInfo> infos = input.getInfos();

		for(PlayerInfo i: infos){
			if(i.getNumber() != null){
				encoded += "P" + i.getNumber();
			}
			if(i.getName() != null){
				encoded += "N" + i.getName();
			}
			if(i.getColor() != null){
				encoded += "C" + String.valueOf(i.getColor()).substring(2,8);
			}
			if(i.getMaxHealth() != null){
				encoded += "M" + i.getMaxHealth();
			}
			if(i.getHealth() != null){
				encoded += "H" + i.getHealth();
			}
			if(i.getBody() != null){
				encoded += "B" + encodeBody(i.getBody());
			}
			if(i.isBlocked()){
				encoded += "L";
			}
			if(i.isInvincible()){
				encoded += "I";
			}
			if(i.isReversed()){
				encoded += "R";
			}
		}

		encoded += "T" + input.getRemainingTime();

		System.out.println(encoded);

		return encoded;
	}

	private String encodeBody(ArrayList<Point> body) {
		String bodyCode = "";

		//assertion x,y never over 99, never negative ( (0,37) currently standard?)
		for(Point p: body){
			//fills string with leading zeros if necessary
			bodyCode += String.format("%02d",p.getX()) + String.format("%02d",p.getY());
		}

		return bodyCode;
	}

}
