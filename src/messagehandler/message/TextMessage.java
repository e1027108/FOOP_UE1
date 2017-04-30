package messagehandler.message;

public class TextMessage extends Message {

	private String message;
	
	public TextMessage(MessageType type, String message) {
		super(type);
		this.setMessage(message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
