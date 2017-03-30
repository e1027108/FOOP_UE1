package game;

public class Point {

	private int x = 0;
	private int y = 0;

	public Point(int u, int v) {
		setX(u);
		setY(v);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + this.x + "," + this.y + ")";
	}
}
