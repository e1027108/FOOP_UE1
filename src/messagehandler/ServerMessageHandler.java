package messagehandler;

import java.util.ArrayList;

import game.Point;
import javafx.scene.paint.Color;
import messagehandler.message.InfoMessage;
import messagehandler.message.Message;
import messagehandler.message.PlayerLeftMessage;
import messagehandler.message.TextMessage;
import messagehandler.message.Message.MessageType;
import messagehandler.message.PlayerInfo;

public class ServerMessageHandler extends MessageHandler{

	@Override
	public Message decode(String input) {
		Message decoded = null;
		String head = input.substring(0,3);

		switch(head){
		case UPDATE:
			decoded = decodePlayerInfo(MessageType.UPD, input);
			break;
		case BASE_INFO:
			decoded = decodePlayerInfo(MessageType.BAI, input);
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

	private InfoMessage decodePlayerInfo(MessageType type, String input) {
		String payload = input.substring(3,input.length());
		//split the strings into substrings starting with upper case letter
		String info[] = payload.split("(?=[A-Z])");
		PlayerInfo pi = new PlayerInfo();
		int remainingTime = 0;

		for(String s: info){
			switch(s.charAt(0)){
			case 'P': //playerNumber
				pi.setNumber(Integer.parseInt(s.substring(1,2)));
				break;
			case 'N': //name
				pi.setName(s.substring(1));
				break;
			case 'C': //color
				pi.setColor(Color.web(s.substring(1),1));
				break;
			case 'M': //max health
				pi.setMaxHealth(Integer.parseInt(s.substring(1)));
				break;
			case 'H': //current health
				pi.setHealth(Integer.parseInt(s.substring(1)));
				break;
			case 'B': //body positions
				pi.setBody(computePosition(s.substring(1)));
				break;
			case 'L': //bLocked
				pi.setBlocked(true);
				break;
			case 'I': //invincible
				pi.setInvincible(true);
				break;
			case 'R': //reverse control
				pi.setReversed(true);
				break;
			case 'T':
				remainingTime = Integer.parseInt(s.substring(1));
				break;
			default:
				throw new IllegalArgumentException("Invalid information code: " + s.charAt(0));
			}
		}

		return new InfoMessage(type,pi,remainingTime);
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
		case BAI:
			encoded = BASE_INFO + encodePlayerInfo((InfoMessage) input);
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
		MessageType type = input.getType();
		PlayerInfo info = input.getInfo();

		/*if(type == MessageType.BAI){
			encoded += BASE_INFO;
		}
		else if(type == MessageType.UPD){
			encoded += UPDATE;
		}*/

		//TODO code constants?
		if(info.getNumber() != null){
			encoded += "P" + info.getNumber();
		}
		if(info.getName() != null){
			encoded += "N" + info.getName();
		}
		if(info.getColor() != null){
			encoded += "C" + String.valueOf(info.getColor()).substring(2,8);
		}
		if(info.getMaxHealth() != null){
			encoded += "M" + info.getMaxHealth();
		}
		if(info.getHealth() != null){
			encoded += "H" + info.getHealth();
		}
		if(info.getBody() != null){
			encoded += "B" + encodeBody(info.getBody());
		}
		if(info.isBlocked()){
			encoded += "L";
		}
		if(info.isInvincible()){
			encoded += "I";
		}
		if(info.isReversed()){
			encoded += "R";
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
