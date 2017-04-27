package MessageHandler;

public class Message {
	
	protected static enum MessageType {
		UPD, BAI, STR, END, TXT, SAD, PLL, PLR, DIC, DIS
	}

	private MessageType type;
	
	//TODO create loads of variables that are ignored/are null, if set type doesn't concern them
	
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
