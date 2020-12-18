package app.impl;

/*
 * This interface is implemented by all classes of objects that are held within the SmartSquares in the game
 */

public interface Collideable {

	public boolean checkMoveValidity();

	public void collide();
}
