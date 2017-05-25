package messagehandler.message;

import artifacts.ArtifactConstants;
import artifacts.Artifacts;
import game.Point;

public class ArtifactInfo {

	private Artifacts type;
	private Point position;

	public ArtifactInfo(){}

	public ArtifactInfo(Artifacts type, Point position){
		this.setType(type);
		this.setPosition(position);
	}

	public Artifacts getType() {
		return type;
	}

	public void setType(Artifacts type) {
		this.type = type;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public String getImage(){
		switch(type){
		case HEALTH_INCREASE:
			return ArtifactConstants.HEALTH_INCREASE_IMAGE;
		case HEALTH_DECREASE:
			return ArtifactConstants.HEALTH_DECREASE_IMAGE;
		case SIZE_INCREASE:
			return ArtifactConstants.SIZE_INCREASE_IMAGE;
		case SIZE_DECREASE:
			return ArtifactConstants.SIZE_DECREASE_IMAGE;
		case BLOCK_CONTROL:
			return ArtifactConstants.BLOCK_CONTROL_IMAGE;
		case REVERSE_CONTROL:
			return ArtifactConstants.REVERSE_CONTROL_IMAGE;
		case INVULNERABILITY:
			return ArtifactConstants.INVULNERABILITY_IMAGE;
		case SPEED_INCREASE:
			return ArtifactConstants.SPEED_INCREASE_IMAGE;
		case SPEED_DECREASE:
			return ArtifactConstants.SPEED_DECREASE_IMAGE;
		default: 
			return "";
		}
	}
}
