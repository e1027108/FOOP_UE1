package gui;

import javafx.scene.control.TextArea;

public class MessageEngine {

	private TextArea area;
	//TODO use queues to save all errors/messages
	
	public MessageEngine(TextArea msgArea) {
		this.area = msgArea;
	}

	public void setAreaEmpty() {
		area.setText("");
	}
	
	public void writeError(String msg){
		area.setStyle("-fx-text-fill: red;");
		area.setText(msg + "\n");
	}
	
	//TODO have a minimum delay of .25 s between messages being written, use queue
	public void writeMessage(String msg) {
		area.setStyle("-fx-text-fill: black;");
		area.setScrollTop(Double.MAX_VALUE); // scrolls down
		area.appendText(msg + "\n");
	}

}
