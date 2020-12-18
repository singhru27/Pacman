package app.impl;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import util.Constants;

/*
 * This class represents the Walls within the game. It also implements the collideable interface
 */

public class Wall implements Collideable {

	private Rectangle _rect;
	private int _row;
	private int _column;
	private Pane _pacmanPane;

	/*
	 * In the constructor of this class, I call the setUpWall method to create the
	 * square associated with the wall. I take in integers to set the initial
	 * location of the Wall, and a pane to add the wall visually.
	 */

	public Wall(int y, int x, Pane pane) {

		_row = y;
		_column = x;
		_pacmanPane = pane;
		_rect = new Rectangle();
		this.setUpWall();

	}

	/*
	 * This method sets the color of the wall, sets the size of the wall, sets the
	 * location of the wall using the passed in values of the row and the column,
	 * and adds the wall to the pane
	 */

	private void setUpWall() {

		_rect.setFill(Color.LIGHTSKYBLUE);
		_rect.setWidth(Constants.WALL_SIZE);
		_rect.setHeight(Constants.WALL_SIZE);
		_rect.setX(_column * Constants.WALL_SIZE);
		_rect.setY(_row * Constants.WALL_SIZE);
		_pacmanPane.getChildren().add(_rect);

	}

	/*
	 * This method is used to determine whether it is valid for pacman to move into
	 * a square with this object It returns true, since it is valid
	 */

	public boolean checkMoveValidity() {
		return false;
	}

	/*
	 * This is an empty method to allow for polymorphic collision detection
	 */

	public void collide() {

	}

}
