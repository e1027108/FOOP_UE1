package MessageHandler;

import game.Directions;

public class DirectionChangeMessage extends Message {

	private Directions direction;
	
	public DirectionChangeMessage(MessageType type, Directions direction) {
		super(type);
		this.setDirection(direction);
	}

	public Directions getDirection() {
		return direction;
	}

	public void setDirection(Directions direction) {
		this.direction = direction;
	}

}
