package messagehandler.message;

public class InfoMessage extends Message {

	private PlayerInfo info;

	public InfoMessage(MessageType type, PlayerInfo info) {
		super(type);

		this.info = (info);
	}

	public PlayerInfo getInfo(){
		return this.info;
	}

}
