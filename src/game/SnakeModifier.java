package game;

/**
 * This class implements the {@link Snake} interface and holds all additional
 * modifying values (like temporary/permanent increases etc.)
 */
public class SnakeModifier implements Snake {

	private SnakeImpl snek;
	private int sizeIncrease, sizeDecrease, healthIncrease, healthDecrease, speedIncrease, speedDecrease;
	private boolean blockControl, reverseControl, invulnerability;

	public SnakeModifier(SnakeImpl snek) {
		this.snek = snek;
	}

	@Override
	public void move() {
		this.snek.move();
	}

	@Override
	public char getDirection() {
		return this.snek.getDirection();
	}

	@Override
	public Point[] getBody() {
		return this.snek.getBody();
	}

	@Override
	public int getSize() {
		return this.snek.getSize() + this.sizeIncrease - this.sizeDecrease;
	}

	@Override
	public void initPosition(int x, int y) {
		this.snek.initPosition(x, y);
	}

	public int getSizeIncrease() {
		return sizeIncrease;
	}

	public void setSizeIncrease(int sizeIncrease) {
		this.sizeIncrease = sizeIncrease;
	}

	public int getSizeDecrease() {
		return sizeDecrease;
	}

	public void setSizeDecrease(int sizeDecrease) {
		this.sizeDecrease = sizeDecrease;
	}

	public SnakeImpl getSnake() {
		return this.snek;
	}

	@Override
	public boolean isAlive() {
		return this.snek.isAlive();
	}

	@Override
	public void setAlive(boolean alive) {
		this.snek.setAlive(alive);
	}

	@Override
	public Point getLastTailPosition() {
		return this.snek.getLastTailPosition();
	}

	@Override
	public void setDirection(char d) {
		this.snek.setDirection(d);

	}

	@Override
	public String getName() {
		return this.snek.getName();
	}

	public SnakeImpl getSnek() {
		return snek;
	}

	public void setSnek(SnakeImpl snek) {
		this.snek = snek;
	}

	public int getHealthIncrease() {
		return healthIncrease;
	}

	public void setHealthIncrease(int healthIncrease) {
		this.healthIncrease = healthIncrease;
	}

	public int getHealthDecrease() {
		return healthDecrease;
	}

	public void setHealthDecrease(int healthDecrease) {
		this.healthDecrease = healthDecrease;
	}

	public int getSpeedIncrease() {
		return speedIncrease;
	}

	public void setSpeedIncrease(int speedIncrease) {
		this.speedIncrease = speedIncrease;
	}

	public int getSpeedDecrease() {
		return speedDecrease;
	}

	public void setSpeedDecrease(int speedDecrease) {
		this.speedDecrease = speedDecrease;
	}

	public boolean isBlockControl() {
		return blockControl;
	}

	public void setBlockControl(boolean blockControl) {
		this.blockControl = blockControl;
	}

	public boolean isReverseControl() {
		return reverseControl;
	}

	public void setReverseControl(boolean reverseControl) {
		this.reverseControl = reverseControl;
	}

	public boolean isInvulnerability() {
		return invulnerability;
	}

	public void setInvulnerability(boolean invulnerability) {
		this.invulnerability = invulnerability;
	}

	@Override
	public int getHealth() {
		return this.snek.getHealth() + this.healthIncrease - this.healthDecrease;
	}

	@Override
	public double getSpeed() {
		return this.snek.getSpeed() + this.speedIncrease - this.speedDecrease;
	}
}
