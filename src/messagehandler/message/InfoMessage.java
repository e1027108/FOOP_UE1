package messagehandler.message;

public class InfoMessage extends Message {

	private PlayerInfo info;
	private int remainingTime;

	public InfoMessage(MessageType type, PlayerInfo info, int remainingTime) {
		super(type);

		this.info = (info);
		this.setRemainingTime(remainingTime);
	}

	public PlayerInfo getInfo(){
		return this.info;
	}

	public int getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
	}

}
