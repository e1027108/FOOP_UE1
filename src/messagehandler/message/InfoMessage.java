package messagehandler.message;

import java.util.List;

public class InfoMessage extends Message {

	private List<PlayerInfo> infos;
	private int remainingTime;

	public InfoMessage(MessageType type, List<PlayerInfo> infos, int remainingTime) {
		super(type);

		this.infos = (infos);
		this.setRemainingTime(remainingTime);
	}

	public List<PlayerInfo> getInfos() {
		return this.infos;
	}

	public int getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
	}

}
