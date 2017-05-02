package messagehandler.message;

import java.util.ArrayList;

public class BaseMessage extends Message {

	private ArrayList<PlayerInfo> info;
	private int numberOfPlayers;
	
	public BaseMessage(MessageType type, PlayerInfo... ps) {
		super(type);
		
		info = new ArrayList<PlayerInfo>();
		
		for(PlayerInfo p : ps){
			this.info.add(p);
		}
		
		this.setNumberOfPlayers(info.size());
	}
	
	public PlayerInfo getInfo(int player){
		return this.info.get(player);
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

}
