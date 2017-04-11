package game;

import game.CollisionTarget.CollisionTypes;

public interface CollisionListener {

	void collisionDetected(CollisionTypes coll, Snake snek, Point point, int gridID);
}
