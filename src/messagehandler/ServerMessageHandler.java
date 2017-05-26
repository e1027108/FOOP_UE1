package messagehandler;

import java.util.ArrayList;
import java.util.List;

import artifacts.Artifacts;
import game.Point;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import messagehandler.message.AckMessage;
import messagehandler.message.ArtifactInfo;
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
			decoded = decodeInformation(MessageType.UPD, input);
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
		case ACKNOWLEDGED:
			decoded = decodeAcknowledgement(input);
			break;
		default:
			throw new IllegalArgumentException("Invalid message type code: " + head);
		}

		return decoded;
	}

	private Message decodeAcknowledgement(String input) {
		int number = Integer.parseInt(input.substring(3));
		return new AckMessage(MessageType.ACK, number);
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

	private InfoMessage decodeInformation(MessageType type, String input) {
		String payload = input.substring(3,input.length());
		ArrayList<PlayerInfo> pis = new ArrayList<PlayerInfo>();
		ArrayList<ArtifactInfo> ais = new ArrayList<ArtifactInfo>();
		int remainingTime = 0;

		//split string into player's information, artifact positions and time left (at end of message)
		String data[] = payload.split("(?=[PTA])");

		for(String datapoint: data){
			//players
			if(datapoint.charAt(0) == 'P'){
				//split the strings into substrings starting with upper case letter
				String info[] = datapoint.split("(?=[A-Z])");
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
					default:
						throw new IllegalArgumentException("Invalid information code: " + s.charAt(0));
					}
				}
				pis.add(playerInf);
			}
			else if (datapoint.charAt(0) == 'T') {
				remainingTime = Integer.parseInt(datapoint.substring(1));
			}
			else if (datapoint.charAt(0) == 'A') {
				ArtifactInfo artifact = new ArtifactInfo();
				String info[] = datapoint.split("(?=[A-Z])");
				for(String s: info){
					switch(s.charAt(0)){
					case 'A':
						artifact.setType(parseArtifactType(s.substring(1,3)));
						break;
					case 'X': //position, like an x on a pirate's map
						artifact.setPosition(computePosition(s.substring(1)).get(0)); //re-use of method, should only ever find one point
						break;
					default:
						throw new IllegalArgumentException("Invalid information code: " + s.charAt(0));
					}
				}
				ais.add(artifact);
			}
		}

		return new InfoMessage(type,pis,ais,remainingTime);
	}

	//codes are lower case, because upper case stands for deliminators
	private Artifacts parseArtifactType(String code) {
		switch(code){
		case "hi":
			return Artifacts.HEALTH_INCREASE;
		case "hd":
			return Artifacts.HEALTH_DECREASE;
		case "si":
			return Artifacts.SIZE_INCREASE;
		case "sd":
			return Artifacts.SIZE_DECREASE;
		case "pi":
			return Artifacts.SPEED_INCREASE;
		case "pd":
			return Artifacts.SPEED_DECREASE;
		case "bc":
			return Artifacts.BLOCK_CONTROL;
		case "rc":
			return Artifacts.REVERSE_CONTROL;
		case "in":
			return Artifacts.INVULNERABILITY;
		default:
			throw new IllegalArgumentException("Illegal artifact code: " + code);
		}
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
			encoded = UPDATE + encodeInformation((InfoMessage) input);
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
		case ACK:
			encoded = ACKNOWLEDGED + ((AckMessage) input).getNumber();
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

	private String encodeInformation(InfoMessage input) {
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

		for(ArtifactInfo i: input.getArtifacts()){
			encoded += "A";
			
			switch(i.getType()){
			case HEALTH_INCREASE:
				encoded += "hi";
				break;
			case HEALTH_DECREASE:
				encoded += "hd";
				break;
			case SIZE_INCREASE:
				encoded += "si";
				break;
			case SIZE_DECREASE:
				encoded += "sd";
				break;
			case SPEED_INCREASE:
				encoded += "pi";
				break;
			case SPEED_DECREASE:
				encoded += "pd";
				break;
			case BLOCK_CONTROL:
				encoded += "bc";
				break;
			case INVULNERABILITY:
				encoded += "in";
				break;
			case REVERSE_CONTROL:
				encoded += "rc";
				break;
			default:
				throw new IllegalArgumentException("Invalid artifact type: " + i.getType());
			}
			
			encoded += "X" + encodePoint(i.getPosition());
			
		}
		
		encoded += "T" + input.getRemainingTime();
		
		return encoded;
	}

	private String encodeBody(ArrayList<Point> body) {
		String bodyCode = "";

		//assertion x,y never over 99, never negative ( (0,37) currently standard?)
		for(Point p: body){
			//fills string with leading zeros if necessary
			bodyCode += encodePoint(p);
		}

		return bodyCode;
	}
	
	private String encodePoint(Point point){
		return String.format("%02d",point.getX()) + String.format("%02d",point.getY());
	}

}
