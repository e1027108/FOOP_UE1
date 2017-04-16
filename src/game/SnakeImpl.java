package game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the {@link Snake} interface and holds the start/default
 * values of the players snake.
 */
public class SnakeImpl implements Snake {

	private String name;
	private int gridID;

	protected Directions direction;
	protected static final int DEFAULT_HEALTH = 5;
	protected static final int DEFAULT_MAX_HEALTH = 5;
	protected static final double DEFAULT_SPEED = 1.0;
	protected static final int DEFAULT_SIZE = 4;
	protected static final int DEFAULT_TIME_TO_REACT = 1;
	private int sizeModifier, healthModifier, speedIncrease, speedDecrease;
	private boolean blockControl, reverseControl, invulnerability;

	private boolean alive;

	private Point lastTailPosition;

	protected ArrayDeque<Point> position;

	public SnakeImpl(String n, int gridID, Directions dir) {
		name = n;
		this.gridID = gridID;
		direction = dir;
		sizeModifier = 0;
		healthModifier = 0;
		speedIncrease = 0;
		speedDecrease = 0;

		setAlive(true);

		position = new ArrayDeque<Point>();
	}

	@Override
	public void initPosition(int x, int y) {
		// must be changed too
		for (int i = 0; i < DEFAULT_SIZE; i++) {
			if (direction == Directions.N) {
				position.add(new Point(x + i, y));
			}
			if (direction == Directions.E) {
				position.add(new Point(x, y - i));
			}
			if (direction == Directions.S) {
				position.add(new Point(x - i, y));
			}
			if (direction == Directions.W) {
				position.add(new Point(x, y + i));
			}
		}
	}

	@Override
	public void move() {
		Point head = position.peekFirst();
		if (direction == Directions.N) {
			position.addFirst(new Point(head.getX() - 1, head.getY()));
		}
		if (direction == Directions.S) {
			position.addFirst(new Point(head.getX() + 1, head.getY()));
		}
		if (direction == Directions.E) {
			position.addFirst(new Point(head.getX(), head.getY() + 1));
		}
		if (direction == Directions.W) {
			position.addFirst(new Point(head.getX(), head.getY() - 1));
		}
		lastTailPosition = position.pollLast();

	}

	@Override
	public Directions getDirection() {
		return direction;
	}

	@Override
	public Point[] getBody() {
		ArrayDeque<Point> pos = position.clone();
		Point[] body = new Point[DEFAULT_SIZE];
		for (int i = 0; i < DEFAULT_SIZE; i++) {
			body[i] = pos.poll();
		}
		return body;
	}

	@Override
	public List<Point> getBodyList() {
		List<Point> bodyList = new ArrayList<Point>();
		Point[] body = getBody();
		for (int i = 0; i < body.length; i++) {
			bodyList.add(body[i]);
		}

		return bodyList;
	}

	@Override
	public boolean isAlive() {
		return alive;
	}

	@Override
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	@Override
	public Point getLastTailPosition() {
		return lastTailPosition;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void changeDirection(Directions d) {
		if ((d == Directions.N && direction != Directions.S) || (d == Directions.S && direction != Directions.N)
				|| (d == Directions.E && direction != Directions.W)
				|| (d == Directions.W && direction != Directions.E)) {
			direction = d;
		}
	}

	@Override
	public int getHealth() {
		return DEFAULT_HEALTH + healthModifier;
	}

	@Override
	public double getSpeed() {
		return DEFAULT_SPEED + speedIncrease - speedDecrease;
	}

	@Override
	public int getSize() {
		return DEFAULT_SIZE + sizeModifier;
	}
	
	@Override
	public int getMaxHealth(){
		return DEFAULT_MAX_HEALTH + sizeModifier;
	}

	public int getGridID() {
		return gridID;
	}

	/**
	 * changes the current size modifier by the input
	 * @param change a positive or negative value
	 */
	public void changeSizeModifier(int change) {
		this.sizeModifier += change;
	}

	/**
	 * changes the current health modifier by the input
	 * @param change a positive or negative value
	 */
	public void changeHealthModifier(int change) {
		//can't be healed above max
		if(this.healthModifier + change + DEFAULT_HEALTH > DEFAULT_MAX_HEALTH + sizeModifier){
			this.healthModifier = DEFAULT_MAX_HEALTH + sizeModifier - DEFAULT_HEALTH;
		}
		else{
			this.healthModifier += change;
		}
	}

	
	//these need two methods, since speed changes are temporary
	
	public void changeSpeedIncrease(int change) {
		//weird cases that change increase into negative
		if(this.speedIncrease + change < 0){
			this.speedIncrease = 0;
		}
		else{
			this.speedIncrease += change;
		}
	}

	public void changeSpeedDecrease(int change) {
		//weird cases that change decrease into negative
		if(this.speedDecrease + change < 0){
			this.speedDecrease = 0;
		}
		else{
			this.speedDecrease += change;
		}
	}

	public boolean hasBlockControl() {
		return blockControl;
	}

	public void setBlockControl(boolean blockControl) {
		this.blockControl = blockControl;
	}

	public boolean hasReverseControl() {
		return reverseControl;
	}

	public void setReverseControl(boolean reverseControl) {
		this.reverseControl = reverseControl;
	}

	public boolean isInvulnerable() {
		return invulnerability;
	}

	public void setInvulnerability(boolean invulnerability) {
		this.invulnerability = invulnerability;
	}

}
