package messagehandler.message;

import java.util.List;

public class InfoMessage extends Message {

	private List<PlayerInfo> infos;
	private List<ArtifactInfo> artifacts;
	private int remainingTime;

	public InfoMessage(MessageType type, List<PlayerInfo> infos, List<ArtifactInfo> artifacts, int remainingTime) {
		super(type);

		this.infos = infos;
		this.artifacts = artifacts;
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

	public List<ArtifactInfo> getArtifacts() {
		return artifacts;
	}

	public void setArtifacts(List<ArtifactInfo> artifacts) {
		this.artifacts = artifacts;
	}

}
