package app.impl;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import util.Constants;

/*
 * This class represents the dots that pacman eats. It implements the Collideable interface
 */

public class Dot implements Collideable {

	private Circle _dot;
	private int _row;
	private int _column;
	private Pane _pacmanPane;
	private Label _scoreBoard;
	private Game _game;

	/*
	 * In the constructor, I call the command to set up each dot.I take in integers
	 * representing the location of the dot to set up the initial location, and take
	 * in a pane to add the dots visually. I take the _scoreBoard label as a passed
	 * in parameter to update the score as dots are eaten.
	 */

	public Dot(int y, int x, Pane pane, Game game, Label label) {

		_row = y;
		_column = x;
		_pacmanPane = pane;
		_scoreBoard = label;
		_game = game;
		_dot = new Circle();
		this.setUpDot();

	}

	/*
	 * This method is used to set up the dot. It sets the color of the dot, the size
	 * of the dot, the location of the dot and adds the dot to the pane
	 */

	private void setUpDot() {

		_dot.setFill(Color.BLACK);
		_dot.setRadius(Constants.DOT_RADIUS);
		_dot.setCenterX(_column * Constants.WALL_SIZE + Constants.PACMAN_RADIUS);
		_dot.setCenterY(_row * Constants.WALL_SIZE + Constants.PACMAN_RADIUS);
		_pacmanPane.getChildren().add(_dot);
	}

	/*
	 * This method is used to determine whether it is valid for pacman to move into
	 * a square with this object. It returns true, since it is valid
	 */

	public boolean checkMoveValidity() {
		return true;
	}

	/*
	 * This method removes the dot from the pane, and sets the scoreboard to the new
	 * score. It also decreases the gameCounter (which is a variable that keeps
	 * track of how many energizers and dots are left on the board, so that the game
	 * can be ended when all possible objects are eaten
	 */

	public void collide() {
		_game.decrementGameCounter();
		_pacmanPane.getChildren().remove(_dot);
		_game.setScore(_game.getScore() + Constants.DOT_SCORE);
		int score = _game.getScore();
		_scoreBoard.setText(" Score " + Integer.toString(score));

	}

}
