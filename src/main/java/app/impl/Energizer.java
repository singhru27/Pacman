package app.impl;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import util.Constants;

/*
 * This class represents the energizers in the game. It sets up the energizer's location, and also contains the logic
 * for when the pacman eats an energizer. This class implements the Collideable interface
 */

public class Energizer implements Collideable {

	private Circle _energizer;
	private Pane _pacmanPane;
	private Ghost _ghost1;
	private Ghost _ghost2;
	private Ghost _ghost3;
	private Ghost _ghost4;
	private int _row;
	private int _column;
	private Game _game;
	private Label _scoreBoard;

	/*
	 * In the constructor, I take in integers representing the row and columnn, as
	 * well as an array. This allows for the energizer to be placed in the
	 * appropriate location, using the row and column integers to set the location
	 * of the energizer. A pane is taken in to add the Energizer graphically. All
	 * the ghosts are taken in as parameters, so that they can be changed when the
	 * energizer is eaten. A method is then called to set up the energizer
	 */

	public Energizer(int row, int column, Game game, Pane pane, Ghost ghost1, Ghost ghost2, Ghost ghost3, Ghost ghost4,
			Label label) {

		_row = row;
		_column = column;
		_game = game;
		_scoreBoard = label;

		_energizer = new Circle();
		_pacmanPane = pane;
		_ghost1 = ghost1;
		_ghost2 = ghost2;
		_ghost3 = ghost3;
		_ghost4 = ghost4;

		this.setUpEnergizer();

	}

	/*
	 * This method is used to set up the energizer. It sets the color of the
	 * energizer, the radius of the energizer, the location of the energizer, and
	 * then adds it to the _pacmanPane
	 */

	private void setUpEnergizer() {
		_energizer.setFill(Color.GREEN);
		_energizer.setRadius(Constants.ENERGIZER_RADIUS);
		_energizer.setCenterX(_column * Constants.WALL_SIZE + Constants.PACMAN_RADIUS);
		_energizer.setCenterY(_row * Constants.WALL_SIZE + Constants.PACMAN_RADIUS);
		_pacmanPane.getChildren().add(_energizer);

	}

	/*
	 * This method is used to determine whether it is valid for pacman to move into
	 * a square with this object It returns true, since it is valid
	 */

	public boolean checkMoveValidity() {
		return true;
	}

	/*
	 * This method removes the energizer graphically and logically, and increments
	 * the score by 100 points. It also sets the color of all the ghosts to blue,
	 * and changes the energy state (which determines the ghost search modes) to 3.
	 * Finally, it decrements the gameCounter (whic is used to determine how many
	 * objects are left to be eaten on the board, so that the game can be ended
	 * appropriately when all objects are eaten.
	 */

	public void collide() {

		_pacmanPane.getChildren().remove(_energizer);

		_game.setScore(_game.getScore() + Constants.ENERGIZER_SCORE);
		int score = _game.getScore();
		_scoreBoard.setText(" Score " + Integer.toString(score));

		_game.setEnergyState(Constants.FRIGHTENED_ENERGY_STATE);
		_ghost1.getGhost().setFill(Color.BLUE);
		_ghost2.getGhost().setFill(Color.BLUE);
		_ghost3.getGhost().setFill(Color.BLUE);
		_ghost4.getGhost().setFill(Color.BLUE);

		_game.decrementGameCounter();

	}

}
