package game;

import game.GameGrid.CollisionTypes;

public interface CollisionListener {

	void collisionDetected(CollisionTypes coll);
}
