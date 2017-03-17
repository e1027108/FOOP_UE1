package game;

public class SnakeModifier implements Snake {

	private SnakeImpl snek;
	private int sizeIncrease;
	private int sizeDecrease;

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
}
