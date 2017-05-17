package messagehandler.message;

import java.util.ArrayList;

import game.Point;
import javafx.scene.paint.Color;

public class PlayerInfo {

	//info for initializing
	private Integer playerNumber; //0-3
	private String name;
	private Color color;
	
	//info for updates
	private Integer maxHealth;
	private Integer health;
	private ArrayList<Point> body;
	//don't really need speed info?
	private boolean blocked; //just to display info icon
	private boolean reversed; //just to display info icon
	private boolean invincible; //color effect?
	
	//TODO check for missing information
	
	public PlayerInfo(){
		//standard is false, otw manually set
		blocked = false;
		reversed = false;
		invincible = false;
	}
	
	public PlayerInfo(int playerNum, String name, Color color) {
		this.playerNumber = playerNum;
		this.name = name;
		this.color = color;		
	}

	public Integer getNumber() {
		return playerNumber;
	}
	
	public void setNumber(int number) {
		this.playerNumber = number;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String string) {
		this.name = string;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Integer getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public Integer getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public ArrayList<Point> getBody() {
		return body;
	}

	public void setBody(ArrayList<Point> body) {
		this.body = body;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public boolean isReversed() {
		return reversed;
	}

	public void setReversed(boolean reversed) {
		this.reversed = reversed;
	}

	public boolean isInvincible() {
		return invincible;
	}

	public void setInvincible(boolean invincible) {
		this.invincible = invincible;
	}
	
	public String toString() {
		if(playerNumber != null && name != null && color != null){
			return "{ playerNumber: " + String.valueOf(playerNumber) + " name: " + name + " color: " + color.toString() + " }";
		}
		else{
			return "player info incomplete!";
		}
	}

}
