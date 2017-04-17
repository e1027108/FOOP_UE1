package game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import artifacts.ArtifactConstants;

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
	private List<Long> speedIncreaseTimer, speedDecreaseTimer;
	private long blockControlTimer, reverseControlTimer, invulnerabilityTimer;
	protected static List<Directions> vertical, horizontal;

	private boolean alive;
	private boolean respawn;

	private ArrayList<Point> deadParts;
	protected ArrayDeque<Point> position;

	public SnakeImpl(String n, int gridID, Directions dir) {
		name = n;
		this.gridID = gridID;
		direction = dir;
		sizeModifier = 0;
		healthModifier = 0;
		speedIncrease = 0;
		speedDecrease = 0;

		speedIncreaseTimer = new ArrayList<Long>();
		speedDecreaseTimer = new ArrayList<Long>();

		setAlive(true);
		respawn = false;
		
		position = new ArrayDeque<Point>();
		deadParts = new ArrayList<Point>();

		if(vertical == null){
			vertical = new ArrayList<Directions>();
			vertical.add(Directions.N);
			vertical.add(Directions.S);
		}
		if(horizontal == null){
			horizontal = new ArrayList<Directions>();
			horizontal.add(Directions.E);
			horizontal.add(Directions.W);
		}
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
		if (respawn) {
			if (position.size() == this.getSize()) {
				respawn = false;
			}
		}
		else {
			deadParts.add(position.pollLast());
		}

	}

	@Override
	public Directions getDirection() {
		return direction;
	}

	@Override
	public Point[] getBody() {
		ArrayDeque<Point> pos = position.clone();
		Point[] body = new Point[pos.size()];
		for (int i = 0; i < body.length; i++) {
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
	public String getName() {
		return this.name;
	}

	@Override
	public void changeDirection(Directions d) {
		if(!hasBlockControl()){
			if(!hasReverseControl()){
				if ((d == Directions.N && direction != Directions.S) || (d == Directions.S && direction != Directions.N)
						|| (d == Directions.E && direction != Directions.W) || (d == Directions.W && direction != Directions.E)) {
					direction = d;
				}
			}
			else{
				if ((d == Directions.N && direction != Directions.N) || (d == Directions.S && direction != Directions.S) 
						|| (d == Directions.E && direction != Directions.E) || (d == Directions.W && direction != Directions.W)) {
					direction = reverseDirection(d);
				}
			}
		}
	}

	private Directions reverseDirection(Directions d) {
		switch(d){
		case E:
			return Directions.W;
		case W:
			return Directions.E;
		case S:
			return Directions.N;
		case N:
			return Directions.S;
		default:
			return direction;
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

	@Override
	public int getGridID() {
		return gridID;
	}

	/**
	 * changes the current size modifier by the input
	 * @param change a positive or negative value
	 */
	@Override
	public void changeSizeModifier(int change) {
		this.sizeModifier += change;
		// TODO remove/add point to Back of position queue! (this influences
		// draw and update etc).
		if (change < 0) {
			changeHealthModifier(0);
			this.deadParts.add(position.pollLast());
			if (position.size() == 0) {
				this.setAlive(false);
			}
		}
	}

	/**
	 * changes the current health modifier by the input
	 * @param change a positive or negative value
	 */
	@Override
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
	@Override
	public void changeSpeedIncrease(int change) {
		//weird cases that change increase into negative
		if(this.speedIncrease + change < 0){
			this.speedIncrease = 0;
		}
		else{
			this.speedIncrease += change;
		}
		if (change > 0) {
			speedIncreaseTimer.add(System.currentTimeMillis());
		}
	}

	@Override
	public void changeSpeedDecrease(int change) {
		//weird cases that change decrease into negative
		if(this.speedDecrease + change < 0){
			this.speedDecrease = 0;
		}
		else{
			this.speedDecrease += change;
		}
		if (change > 0) {
			speedDecreaseTimer.add(System.currentTimeMillis());
		}
	}

	@Override
	public boolean hasBlockControl() {
		return blockControl;
	}

	@Override
	public void setBlockControl(boolean blockControl) {
		this.blockControl = blockControl;
		this.blockControlTimer = System.currentTimeMillis();
	}

	@Override
	public boolean hasReverseControl() {
		return reverseControl;
	}

	@Override
	public void setReverseControl(boolean reverseControl) {
		this.reverseControl = reverseControl;
		this.reverseControlTimer = System.currentTimeMillis();
	}

	@Override
	public boolean isInvulnerable() {
		return invulnerability;
	}

	@Override
	public void setInvulnerability(boolean invulnerability) {
		this.invulnerability = invulnerability;
		this.invulnerabilityTimer = System.currentTimeMillis();
	}

	@Override
	public void resetPosition(Point pos) {
		for(Point p : position) {
			deadParts.add(p);
		}
		position.clear();
		position.add(pos);
		direction = Directions.N;
		respawn = true;
	}
	
	@Override
	public ArrayList<Point> getDeadParts() {
		return this.deadParts;		
	}
	
	@Override
	public void clearDeadParts() {
		deadParts.clear();
	}

	@Override
	public void checkStatus() {
		/*
		 * check for reverseControl, blockControl, invulnerability,
		 * speedIncrease speedDecrease -> adapt if duration is over
		 */
		checkReverseControl();
		checkBlockControl();
		checkInvulnerability();
		checkSpeedIncrease();
		checkSpeedDecrease();
	}

	private void checkSpeedDecrease() {
		Iterator<Long> speedDecreaseIterator = speedDecreaseTimer.iterator();
		while (speedDecreaseIterator.hasNext()) {
			if ((speedDecreaseIterator.next() + 1000 * ArtifactConstants.SPEED_DECREASE_DURATION) <= System
					.currentTimeMillis()) {
				speedDecreaseIterator.remove();
				changeSpeedDecrease(-1 * ArtifactConstants.SPEED_DECREASE);
			}
		}
	}

	private void checkSpeedIncrease() {
		Iterator<Long> speedIncreaseIterator = speedIncreaseTimer.iterator();
		while (speedIncreaseIterator.hasNext()) {
			if ((speedIncreaseIterator.next() + 1000 * ArtifactConstants.SPEED_INCREASE_DURATION) <= System
					.currentTimeMillis()) {
				speedIncreaseIterator.remove();
				changeSpeedIncrease(-1 * ArtifactConstants.SPEED_INCREASE);
			}
		}
	}

	private void checkReverseControl() {
		if (hasReverseControl()
				&& ((this.reverseControlTimer + 1000 * ArtifactConstants.REVERSE_CONTROL_DURATION) <= System
						.currentTimeMillis())) {
			setReverseControl(false);
		}
	}

	private void checkBlockControl() {
		if (hasBlockControl() && ((this.blockControlTimer + 1000 * ArtifactConstants.BLOCK_CONTROL_DURATION) <= System
				.currentTimeMillis())) {
			setBlockControl(false);
		}
	}

	private void checkInvulnerability() {
		if (isInvulnerable()
				&& ((this.invulnerabilityTimer + 1000 * ArtifactConstants.INVULNERABILITY_DURATION) <= System
						.currentTimeMillis())) {
			setInvulnerability(false);
		}
	}

	@Override
	public void getStatus() {
		System.out.println("---- Snake " + getGridID() + " - Status:");
		System.out.println("Health        : " + getHealth());
		System.out.println("Size          : " + getSize());
		System.out.println("Speed         : " + getSpeed());
		System.out.println("Invulnerable  : " + isInvulnerable());
		System.out.println("BlockControl  : " + hasBlockControl());
		System.out.println("ReverseControl: " + hasReverseControl());
		System.out.println("------------------------");
	}
}