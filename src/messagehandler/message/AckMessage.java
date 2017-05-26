package messagehandler.message;

public class AckMessage extends Message {
	
	private int number;

	public AckMessage(MessageType type, int number){
		super(type);
		this.setNumber(number);
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
}
