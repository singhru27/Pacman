package app.impl;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import util.Constants;

public class Pacman {

	private Circle _pacman;

	/*
	 * In the constructor, I create the circle to represent pacman, I also set its
	 * color and its size
	 */

	public Pacman(SmartSquare[][] array) {

		_pacman = new Circle();
		_pacman.setRadius(Constants.PACMAN_RADIUS);
		_pacman.setFill(Color.YELLOW);

	}

	/*
	 * This method returns the row associated with pacman
	 */

	public int getRow() {
		return (int) ((int) (this.getYLocation() - Constants.PACMAN_RADIUS) / (Constants.WALL_SIZE));
	}

	/*
	 * This method returns the column associated with pacman
	 */

	public int getColumn() {

		return (int) ((int) (this.getXLocation() - Constants.PACMAN_RADIUS) / (Constants.WALL_SIZE));

	}

	/*
	 * This method returns the current X location of the _pacman Circle
	 */

	public double getXLocation() {

		return _pacman.getCenterX();
	}

	/*
	 * This method returns the current y location of the _pacman circle
	 */

	public double getYLocation() {

		return _pacman.getCenterY();
	}

	/*
	 * This method makes the pacman move right by one square
	 */

	public void moveRight() {

		_pacman.setCenterX(_pacman.getCenterX() + Constants.WALL_SIZE);
	}

	/*
	 * This method makes the pacman move left by one square
	 */

	public void moveLeft() {

		_pacman.setCenterX(_pacman.getCenterX() - Constants.WALL_SIZE);
	}

	/*
	 * This method makes the pacman move Up by one square
	 */

	public void moveUp() {

		_pacman.setCenterY(_pacman.getCenterY() - Constants.WALL_SIZE);
	}

	/*
	 * This method makes the pacman move Down by one square
	 */

	public void moveDown() {

		_pacman.setCenterY(_pacman.getCenterY() + Constants.WALL_SIZE);
	}
	/*
	 * This method sets the current X location of the _pacman circle
	 */

	public void setXLocation(double x) {

		_pacman.setCenterX(x);
	}

	/*
	 * This method sets the current y location of the _pacman circle
	 */

	public void setYlocation(double y) {

		_pacman.setCenterY(y);
	}

	/*
	 * This method returns the _pacman Circle
	 */

	public Circle getPacman() {
		return _pacman;
	}
}
