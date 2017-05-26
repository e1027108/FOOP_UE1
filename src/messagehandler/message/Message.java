package messagehandler.message;

public class Message {
	
	public static enum MessageType {
		UPD, STR, END, TXT, SAD, PLL, PLR, DIC, DIS, INI, ACK
	}

	private MessageType type;
	
	public Message(MessageType type){
		this.setType(type);
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

}
