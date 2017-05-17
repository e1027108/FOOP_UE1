package messagehandler.message;

import java.util.ArrayList;

public class InfoMessage extends Message {

	private ArrayList<PlayerInfo> infos;
	private int remainingTime;

	public InfoMessage(MessageType type, ArrayList<PlayerInfo> infos, int remainingTime) {
		super(type);

		this.infos = (infos);
		this.setRemainingTime(remainingTime);
	}

	public ArrayList<PlayerInfo> getInfos(){
		return this.infos;
	}

	public int getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
	}

}
