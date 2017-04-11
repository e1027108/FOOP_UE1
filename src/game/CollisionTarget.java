package game;

import java.util.ArrayList;
import java.util.List;

public abstract class CollisionTarget {

	public enum CollisionTypes {
		OWN_BODY, OTHER_SNAKE, BORDER, ARTIFACT
	}

	protected List<CollisionListener> collisionListeners;

	protected CollisionTarget() {
		this.collisionListeners = new ArrayList<CollisionListener>();
	}

	public void addCollisionListener(CollisionListener listener) {
		this.collisionListeners.add(listener);
	}

	protected void fireCollisionDetection(CollisionTypes coll, Snake snek, Point point, int gridID) {
		for (CollisionListener cl : this.collisionListeners) {
			cl.collisionDetected(coll, snek, point, gridID);
		}
	}
}
