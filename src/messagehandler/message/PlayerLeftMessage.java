package messagehandler.message;

public class PlayerLeftMessage extends Message {

	private int playerNumber;
	
	public PlayerLeftMessage(MessageType type, int playerNumber) {
		super(type);
		this.setPlayerNumber(playerNumber);
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}
	
}
