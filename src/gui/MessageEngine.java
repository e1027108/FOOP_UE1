package gui;

import java.util.ArrayDeque;
import java.util.Deque;

import javafx.scene.control.TextArea;
import javafx.util.Pair;

public class MessageEngine implements Runnable{

	private TextArea area;
	private Deque<Pair<String,Boolean>> messageQueue;
	private Pair<String,Boolean> previous;
	private boolean interrupted;
	
	private static final String RED = "-fx-text-fill: red;";
	private static final String BLACK = "-fx-text-fill: black;";
	private static final long WAIT_TIME = 150;
	
	public MessageEngine(TextArea msgArea) {
		this.area = msgArea;
		this.messageQueue = new ArrayDeque<Pair<String,Boolean>>();
		this.previous = null;
		this.interrupted = false;
	}
	
	@Override
	public void run() {
		while(!interrupted){
			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {
				// TODO no clue
			}
			
			//always prints messages automatically
			printNext();
		}
	}

	public void setAreaEmpty() {
		area.setText("");
	}

	public void addError(String err){
		messageQueue.push(new Pair<String,Boolean>(err,true));
	}

	public synchronized void addMessage(String msg){
		messageQueue.push(new Pair<String,Boolean>(msg,false));
	}

	private void printError(String msg){
		if(previous != null && !previous.getValue()){ //enables multiple errors to be shown
			area.clear();
		}
		
		area.setStyle(RED);
		area.setScrollTop(Double.MAX_VALUE); // scrolls down
		area.setText(msg + "\n");
	}
	
	private synchronized void printMessage(String msg) {
		if(previous != null && previous.getValue()){ //value is boolean (error y/n)
			area.clear();
		}
		
		area.setStyle(BLACK);
		area.setScrollTop(Double.MAX_VALUE); // scrolls down
		area.appendText(msg + "\n");
	}

	private synchronized void printNext(){
		Pair<String,Boolean> next;

		if(!messageQueue.isEmpty()){
			next = messageQueue.pop();

			if(next.getValue()){
				printError(next.getKey());
			}
			else{
				printMessage(next.getKey());
			}

			previous = next;
		}
	}
	
	public void interrupt(){
		interrupted = true;
	}

}
