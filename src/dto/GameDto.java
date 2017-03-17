package dto;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javafx.scene.paint.Color;

public class GameDto {

	private String name;
	private Color color;
	private InetAddress ip;
	private Integer players;
	private boolean host;

	public GameDto(String name, Color color, InetAddress ip) {
		this.setName(name);
		this.setColor(color);
		this.setIp(ip);
		this.setHost(false);
		this.setPlayers(null); //TODO handle not knowing this ... client should just not use this until server provides info in game view?
	}

	public GameDto(String name, Color color, Integer players) throws UnknownHostException {
		this.setName(name);
		this.setColor(color);
		this.setIp(InetAddress.getLocalHost()); //TODO replace with server specific address?
		this.setHost(true);
		this.setPlayers(players);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public InetAddress getIp() {
		return ip;
	}

	public void setIp(InetAddress ip) {
		this.ip = ip;
	}

	public Integer getPlayers() {
		return players;
	}

	public void setPlayers(Integer players) {
		this.players = players;
	}

	public boolean isHost() {
		return host;
	}

	public void setHost(boolean host) {
		this.host = host;
	}

}
