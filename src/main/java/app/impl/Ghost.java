package app.impl;

import java.util.LinkedList;
import java.util.Queue;

import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import util.BoardCoordinate;
import util.Constants;
import util.Direction;

/* This is the superclass for the Ghosts in the game. It contains the basic
 * methods used by all the ghosts, and all the individual ghosts extend from
 * this class. This class implements the collideable interface, and also contains the search algorith
*/

public class Ghost implements Collideable {

	private Rectangle _ghost;

	private Pacman _pacman;
	private SmartSquare[][] _pacmanArray;
	private GhostPen _ghostPen;
	private Game _game;
	private Label _score;
	private Label _lives;
	private Direction _moveDirection;

	/*
	 * In the constructor, the ghost takes in a pacman, an array, the ghostpen, the
	 * game, the scorelabel, and the lives label It sets the initial move direction
	 * of the first ghost to RIGHT, and calls a method to setUpGhost when the ghost
	 * is first created
	 */

	public Ghost(Pacman pacman, SmartSquare[][] array, GhostPen ghostpen, Game game, Label scoreLabel, Label lives) {

		_ghost = new Rectangle();
		_pacman = pacman;
		_pacmanArray = array;
		_ghostPen = ghostpen;
		_game = game;
		_score = scoreLabel;
		_lives = lives;

		this.setUpGhost();
		_moveDirection = Direction.RIGHT;

	}

	/*
	 * This method returns the y coordinate of the ghost
	 */

	public int getYGhost() {
		return (int) (this.getGhost().getY() / Constants.WALL_SIZE);
	}

	/*
	 * This method returns the x coordinate of the ghost
	 */

	public int getXGhost() {
		return (int) (this.getGhost().getX() / Constants.WALL_SIZE);
	}

	/*
	 * This method moves the ghost to the right, removes it from its current spot on
	 * the _pacmanArray, and adds it to the spot to the right. It contains an if
	 * statement to handle cases when the ghost is wrapping over to the other side
	 * of the board
	 */

	private void moveRight() {

		if (this.getXGhost() + 1 > Constants.ARRAY_SIZE - 1) {
			_pacmanArray[this.getYGhost()][this.getXGhost()].getArrayList().remove(this);
			this.getGhost().setX(Constants.PACMAN_LEFT_EXIT_COLUMN * Constants.WALL_SIZE);
			_pacmanArray[this.getYGhost()][Constants.PACMAN_LEFT_EXIT_COLUMN].getArrayList().add(this);

		} else {

			_pacmanArray[this.getYGhost()][this.getXGhost()].getArrayList().remove(this);
			this.getGhost().setX(this.getGhost().getX() + Constants.WALL_SIZE);
			_pacmanArray[this.getYGhost()][this.getXGhost()].getArrayList().add(this);

		}
	}

	/*
	 * This method moves the ghost to the left, removes it from its current spot on
	 * the _pacmanArray, and adds it to the spot on the left. I also created an if
	 * statement to allow for wrapping across the board
	 */

	private void moveLeft() {

		if (this.getXGhost() < 1) {
			_pacmanArray[this.getYGhost()][this.getXGhost()].getArrayList().remove(this);
			this.getGhost().setX(Constants.PACMAN_RIGHT_EXIT_COLUMN * Constants.WALL_SIZE);
			_pacmanArray[this.getYGhost()][Constants.PACMAN_RIGHT_EXIT_COLUMN].getArrayList().add(this);
		} else {

			_pacmanArray[this.getYGhost()][this.getXGhost()].getArrayList().remove(this);
			this.getGhost().setX(this.getGhost().getX() - Constants.WALL_SIZE);
			_pacmanArray[this.getYGhost()][this.getXGhost()].getArrayList().add(this);

		}
	}

	/*
	 * This method moves the ghost up, removes it from its current spot on the
	 * _pacmanArray, and adds it to the spot above it
	 */

	private void moveUp() {

		_pacmanArray[this.getYGhost()][this.getXGhost()].getArrayList().remove(this);
		this.getGhost().setY(this.getGhost().getY() - Constants.WALL_SIZE);
		_pacmanArray[this.getYGhost()][this.getXGhost()].getArrayList().add(this);

	}

	/*
	 * This method moves the ghost down, removes it from its current spot on the
	 * _pacmanArray, and adds it to the spot below it
	 */

	private void moveDown() {

		_pacmanArray[this.getYGhost()][this.getXGhost()].getArrayList().remove(this);
		this.getGhost().setY(this.getGhost().getY() + Constants.WALL_SIZE);
		_pacmanArray[this.getYGhost()][this.getXGhost()].getArrayList().add(this);

	}

	/*
	 * This method moves the ghost in a certain direction, depending on the value of
	 * the passed in enum. If the ghost is currently queued in the ghost pen, the
	 * ghost will not move
	 */

	public void move(Direction direction) {

		if (_ghostPen.getQueue().contains(this) == false) {
			switch (direction) {

			case RIGHT:
				this.moveRight();
				break;

			case LEFT:
				this.moveLeft();
				break;

			case UP:
				this.moveUp();
				break;

			case DOWN:
				this.moveDown();
				break;

			}
		}
	}

	/*
	 * This method sets the initial size of the _ghost square
	 */

	private void setUpGhost() {

		_ghost.setWidth(Constants.WALL_SIZE);
		_ghost.setHeight(Constants.WALL_SIZE);

	}

	/*
	 * This method returns the _ghost Rectangle within this wrapper class
	 */

	public Rectangle getGhost() {

		return _ghost;
	}

	/*
	 * This method sets the x location of the ghost
	 */

	public void setXLocation(double x) {

		_ghost.setX(x);
	}

	/*
	 * this method sets the y location of the ghost
	 */

	public void setYLocation(double y) {
		_ghost.setY(y);
	}

	/*
	 * This method is used to determine whether it is valid for pacman to move into
	 * a square with this object It returns true, since it is valid
	 */

	public boolean checkMoveValidity() {
		return true;
	}

	/*
	 * This method is called whenever the pacman collides with a ghost. If the
	 * pacman is in the normal energy state or in the scatter state, it resets the
	 * location of the pacman to its original location, resets the move direction
	 * and drops the lives. The clear queue method is used to allow the Game to
	 * break out of the for loop that calls the collide method, to prevent infinite
	 * looping (since the ArrayList in the SmartSquare will keep growing and the
	 * game will never exit the loop. If an energizer has been eaten, the ghost in
	 * question will be put back into the Ghostpen and the score will be
	 * incremented. There is an additional else if command within this method, that
	 * deals with the edge case of when the ghost is energized and pacman eats it at
	 * this location. Since this will result in an infinite loop (ghosts are being
	 * continuously added to the arraylist that the pacman is located in, and the
	 * pacman is executing a while loop to collide with all ghosts in this location,
	 * the else if statement moves pacman one square to the right. This should not
	 * cause a change in gameplay, while preventing this error
	 */

	public void collide() {

		if (_game.getEnergyState() == 1 || _game.getEnergyState() == 2) {
			_pacman.setXLocation(Constants.PACMAN_INITIAL_COLUMN * Constants.WALL_SIZE + Constants.PACMAN_RADIUS);
			_pacman.setYlocation(Constants.PACMAN_INITIAL_ROW * Constants.WALL_SIZE + Constants.PACMAN_RADIUS);
			_ghostPen.resetGhosts();
			_game.setLives(_game.getLives() - 1);
			_game.resetDirection();
			_lives.setText(" Lives " + Integer.toString(_game.getLives()));
			this.ghostBFS(0, 0);
		} else if (_pacman.getRow() == Constants.GHOST_PEN_OUTER_GHOST_ROW
				&& _pacman.getColumn() == Constants.GHOST_PEN_CENTER_GHOST_COLUMN
				&& _game.getDirection()==Direction.RIGHT) {
			_pacmanArray[this.getYGhost()][this.getXGhost()].getArrayList().remove(this);
			_pacman.moveRight();
			_ghostPen.queueGhost(this);
			_game.setScore(_game.getScore() + Constants.GHOST_SCORE);
			_score.setText(" Score " + Integer.toString(_game.getScore()));
		} else if (_pacman.getRow() == Constants.GHOST_PEN_OUTER_GHOST_ROW
				&& _pacman.getColumn() == Constants.GHOST_PEN_CENTER_GHOST_COLUMN
				&& _game.getDirection()==Direction.LEFT) {
			_pacmanArray[this.getYGhost()][this.getXGhost()].getArrayList().remove(this);
			_pacman.moveLeft();
			_ghostPen.queueGhost(this);
			_game.setScore(_game.getScore() + Constants.GHOST_SCORE);
			_score.setText(" Score " + Integer.toString(_game.getScore()));
		} else {
			_ghostPen.queueGhost(this);
			_game.setScore(_game.getScore() + Constants.GHOST_SCORE);
			_score.setText(" Score " + Integer.toString(_game.getScore()));
		}

	}

	/*
	 * This method searches the entire board for the desired coordinate, and then
	 * returns the Direction enum that corresponds with the direction that the ghost
	 * needs to go to reach that coordinate. This method takes in a y and x integer
	 * value as parameters, which represent a specific point on the board. This
	 * method directly takes in an integer y and x to represent the location of the
	 * target (rather than a BoardCoordinate) to remove the possibility of potential
	 * bugs with the isFalse argument
	 */

	public Direction ghostBFS(int y, int x) {

		Queue<BoardCoordinate> boardCoordinatesQueue = new LinkedList<>();
		BoardCoordinate closestSquare = null;
		Double closestDistance = null;
		Direction turnDirection = null;
		Direction[][] directionalArray = new Direction[23][23];
		int xIndex = (int) (this.getGhost().getX() / Constants.WALL_SIZE);
		int yIndex = (int) (this.getGhost().getY() / Constants.WALL_SIZE);

		/*
		 * The following Switch statements are used to determine the valid neighbor
		 * squares for the ghost to first initially check If the ghost is initially
		 * moving right, it only checks the squares to the right, up, and down. This is
		 * to prevent 180 degree turns
		 */

		switch (_moveDirection) {

		case RIGHT:

			/*
			 * This top level if statement checks if the ghost is at the right edge of the
			 * board. If it is it adds the leftmost square ("wrapped") into the queue. If it
			 * isn't it executes the rest of the code
			 */

			if (xIndex + 1 > Constants.ARRAY_SIZE - 1) {
				boardCoordinatesQueue.add(new BoardCoordinate((int) Constants.PACMAN_EXIT_ROW,
						(int) Constants.PACMAN_LEFT_EXIT_COLUMN, false));
				directionalArray[(int) Constants.PACMAN_EXIT_ROW][(int) Constants.PACMAN_LEFT_EXIT_COLUMN] = Direction.RIGHT;
			} else {

				/*
				 * This statement checks if the square to the right of the ghost is a valid
				 * space to move into, If it is, it adds the corresponding coordinates into the
				 * Queue and adds the Right direction to the Directional Array, indexed by the
				 * coordinate of the square to the right of the ghost
				 */

				if (_pacmanArray[yIndex][xIndex + 1].getArrayList().isEmpty() || _pacmanArray[yIndex][xIndex + 1]
						.getArrayList().get(Constants.FIRST_ELEMENT).checkMoveValidity()) {

					boardCoordinatesQueue.add(new BoardCoordinate(yIndex, xIndex + 1, false));
					directionalArray[yIndex][xIndex + 1] = Direction.RIGHT;
				}

			}

			/*
			 * This statement checks if the square to above that of the ghost is is a valid
			 * space to move into, If it is, it adds the corresponding coordinates into the
			 * Queue and adds the UP direction to the Directional Array, indexed by the
			 * coordinate of the square above that of the ghost
			 */

			if (_pacmanArray[yIndex - 1][xIndex].getArrayList().isEmpty() || _pacmanArray[yIndex - 1][xIndex]
					.getArrayList().get(Constants.FIRST_ELEMENT).checkMoveValidity()) {

				boardCoordinatesQueue.add(new BoardCoordinate(yIndex - 1, xIndex, false));
				directionalArray[yIndex - 1][xIndex] = Direction.UP;
			}

			/*
			 * This statement checks if the square below that of the ghost is s a valid
			 * space to move into, If it is, it adds the corresponding coordinates into the
			 * Queue and adds the DOWN direction to the Directional Array, indexed by the
			 * coordinate of the square below that of the ghost
			 */

			if (_pacmanArray[yIndex + 1][xIndex].getArrayList().isEmpty() || _pacmanArray[yIndex + 1][xIndex]
					.getArrayList().get(Constants.FIRST_ELEMENT).checkMoveValidity()) {

				boardCoordinatesQueue.add(new BoardCoordinate(yIndex + 1, xIndex, false));
				directionalArray[yIndex + 1][xIndex] = Direction.DOWN;
			}

			break;

		case LEFT:

			/*
			 * This top level if statement checks if the ghost is at the left edge of the
			 * board. If it is it adds the rightmost square ("wrapped") into the queue. If
			 * it isn't it executes the rest of the code
			 */

			if (xIndex < 1) {
				boardCoordinatesQueue.add(new BoardCoordinate((int) Constants.PACMAN_EXIT_ROW,
						(int) Constants.PACMAN_RIGHT_EXIT_COLUMN, false));
				directionalArray[(int) Constants.PACMAN_EXIT_ROW][(int) Constants.PACMAN_RIGHT_EXIT_COLUMN] = Direction.LEFT;
			} else {

				/*
				 * This statement checks if the square to the left of the ghost is a valid space
				 * to move into, If it is, it adds the corresponding coordinates into the Queue
				 * and adds the LEFT direction to the Directional Array, indexed by the
				 * coordinate of the square to the left of the ghost
				 */

				if (_pacmanArray[yIndex][xIndex - 1].getArrayList().isEmpty() || _pacmanArray[yIndex][xIndex - 1]
						.getArrayList().get(Constants.FIRST_ELEMENT).checkMoveValidity()) {

					boardCoordinatesQueue.add(new BoardCoordinate(yIndex, xIndex - 1, false));
					directionalArray[yIndex][xIndex - 1] = Direction.LEFT;
				}

			}

			/*
			 * This statement checks if the square above that of the ghost is s a valid
			 * space to move into, If it is, it adds the corresponding coordinates into the
			 * Queue and adds the UP direction to the Directional Array, indexed by the
			 * coordinate of the square above that of the ghost
			 */

			if (_pacmanArray[yIndex - 1][xIndex].getArrayList().isEmpty() || _pacmanArray[yIndex - 1][xIndex]
					.getArrayList().get(Constants.FIRST_ELEMENT).checkMoveValidity()) {

				boardCoordinatesQueue.add(new BoardCoordinate(yIndex - 1, xIndex, false));
				directionalArray[yIndex - 1][xIndex] = Direction.UP;
			}

			/*
			 * This statement checks if the square below that of the ghost i a valid space
			 * to move into, If it is, it adds the corresponding coordinates into the Queue
			 * and adds the DOWN direction to the Directional Array, indexed by the
			 * coordinate of the square below that of the ghost
			 */

			if (_pacmanArray[yIndex + 1][xIndex].getArrayList().isEmpty() || _pacmanArray[yIndex + 1][xIndex]
					.getArrayList().get(Constants.FIRST_ELEMENT).checkMoveValidity()) {

				boardCoordinatesQueue.add(new BoardCoordinate(yIndex + 1, xIndex, false));
				directionalArray[yIndex + 1][xIndex] = Direction.DOWN;
			}

			break;

		case UP:

			/*
			 * This statement checks if the square to the left of the ghost is a valid space
			 * to move into, If it is, it adds the corresponding coordinates into the Queue
			 * and adds the LEFT direction to the Directional Array, indexed by the
			 * coordinate of the square to the left of the ghost
			 */

			if (_pacmanArray[yIndex][xIndex - 1].getArrayList().isEmpty() || _pacmanArray[yIndex][xIndex - 1]
					.getArrayList().get(Constants.FIRST_ELEMENT).checkMoveValidity()) {

				boardCoordinatesQueue.add(new BoardCoordinate(yIndex, xIndex - 1, false));
				directionalArray[yIndex][xIndex - 1] = Direction.LEFT;
			}

			/*
			 * This statement checks if the square to the right of the ghost is s a valid
			 * space to move into, If it is, it adds the corresponding coordinates into the
			 * Queue and adds the Right direction to the Directional Array, indexed by the
			 * coordinate of the square to the right of the ghost
			 */

			if (_pacmanArray[yIndex][xIndex + 1].getArrayList().isEmpty() || _pacmanArray[yIndex][xIndex + 1]
					.getArrayList().get(Constants.FIRST_ELEMENT).checkMoveValidity()) {

				boardCoordinatesQueue.add(new BoardCoordinate(yIndex, xIndex + 1, false));
				directionalArray[yIndex][xIndex + 1] = Direction.RIGHT;
			}

			/*
			 * This statement checks if the square to above that of the ghost is s a valid
			 * space to move into, If it is, it adds the corresponding coordinates into the
			 * Queue and adds the UP direction to the Directional Array, indexed by the
			 * coordinate of the square above that of the ghost
			 */

			if (_pacmanArray[yIndex - 1][xIndex].getArrayList().isEmpty() || _pacmanArray[yIndex - 1][xIndex]
					.getArrayList().get(Constants.FIRST_ELEMENT).checkMoveValidity()) {

				boardCoordinatesQueue.add(new BoardCoordinate(yIndex - 1, xIndex, false));
				directionalArray[yIndex - 1][xIndex] = Direction.UP;
			}

			break;

		case DOWN:

			/*
			 * This statement checks if the square to the left of the ghost is s a valid
			 * space to move into, If it is, it adds the corresponding coordinates into the
			 * Queue and adds the LEFT direction to the Directional Array, indexed by the
			 * coordinate of the square to the left of the ghost
			 */

			if (_pacmanArray[yIndex][xIndex - 1].getArrayList().isEmpty() || _pacmanArray[yIndex][xIndex - 1]
					.getArrayList().get(Constants.FIRST_ELEMENT).checkMoveValidity()) {

				boardCoordinatesQueue.add(new BoardCoordinate(yIndex, xIndex - 1, false));
				directionalArray[yIndex][xIndex - 1] = Direction.LEFT;
			}

			/*
			 * This statement checks if the square to the right of the ghost is s a valid
			 * space to move into, If it is, it adds the corresponding coordinates into the
			 * Queue and adds the Right direction to the Directional Array, indexed by the
			 * coordinate of the square to the right of the ghost
			 */

			if (_pacmanArray[yIndex][xIndex + 1].getArrayList().isEmpty() || _pacmanArray[yIndex][xIndex + 1]
					.getArrayList().get(Constants.FIRST_ELEMENT).checkMoveValidity()) {

				boardCoordinatesQueue.add(new BoardCoordinate(yIndex, xIndex + 1, false));
				directionalArray[yIndex][xIndex + 1] = Direction.RIGHT;
			}

			/*
			 * This statement checks if the square below that of the ghost is s a valid
			 * space to move into, If it is, it adds the corresponding coordinates into the
			 * Queue and adds the DOWN direction to the Directional Array, indexed by the
			 * coordinate of the square below that of the ghost
			 */

			if (_pacmanArray[yIndex + 1][xIndex].getArrayList().isEmpty() || _pacmanArray[yIndex + 1][xIndex]
					.getArrayList().get(Constants.FIRST_ELEMENT).checkMoveValidity()) {

				boardCoordinatesQueue.add(new BoardCoordinate(yIndex + 1, xIndex, false));
				directionalArray[yIndex + 1][xIndex] = Direction.DOWN;
			}

			break;

		}

		/*
		 * This while statement executes a breadth first traversal of the entire board
		 * to find the target.
		 */

		while (boardCoordinatesQueue.isEmpty() == false) {
			BoardCoordinate currentSquare = boardCoordinatesQueue.remove();
			int xIndex2 = currentSquare.getColumn();
			int yIndex2 = currentSquare.getRow();

			// This command marks the current location of the ghost in the DirectionalArray
			// so that it isn't revisited

			if (directionalArray[this.getYGhost()][this.getYGhost()] == null) {
				directionalArray[this.getYGhost()][this.getXGhost()] = Direction.RIGHT;
			}

			if (closestDistance == null || this.checkForDistance(y, x, currentSquare) < closestDistance) {
				closestSquare = currentSquare;
				closestDistance = this.checkForDistance(y, x, currentSquare);
				turnDirection = directionalArray[yIndex2][xIndex2];

			}

			/*
			 * This wrap statement checks if the left neighbor of the current square is off
			 * the grid. If so, it wraps around and checks for the rightmost wrapped square
			 */

			if (xIndex2 < 1) {
				if (directionalArray[Constants.PACMAN_EXIT_ROW][Constants.PACMAN_RIGHT_EXIT_COLUMN] == null
						&& (_pacmanArray[Constants.PACMAN_EXIT_ROW][Constants.PACMAN_RIGHT_EXIT_COLUMN].getArrayList()
								.isEmpty()
								|| _pacmanArray[Constants.PACMAN_EXIT_ROW][Constants.PACMAN_RIGHT_EXIT_COLUMN]
										.getArrayList().get(Constants.FIRST_ELEMENT).checkMoveValidity())) {

					boardCoordinatesQueue.add(
							new BoardCoordinate(Constants.PACMAN_EXIT_ROW, Constants.PACMAN_RIGHT_EXIT_COLUMN, false));

					directionalArray[Constants.PACMAN_EXIT_ROW][Constants.PACMAN_RIGHT_EXIT_COLUMN] = directionalArray[yIndex2][xIndex2];

				}
			} else {

				/*
				 * This if statement adds the left neighbor of the current square if it hasn't
				 * been reached before and if is a valid cell to move into
				 */

				if (directionalArray[yIndex2][xIndex2 - 1] == null
						&& (_pacmanArray[yIndex2][xIndex2 - 1].getArrayList().isEmpty()
								|| _pacmanArray[yIndex2][xIndex2 - 1].getArrayList().get(Constants.FIRST_ELEMENT)
										.checkMoveValidity())) {

					boardCoordinatesQueue.add(new BoardCoordinate(yIndex2, xIndex2 - 1, false));
					directionalArray[yIndex2][xIndex2 - 1] = directionalArray[yIndex2][xIndex2];

				}
			}

			/*
			 * This wrap statement checks if the right neighbor of the current square is off
			 * the grid. If so, it wraps around and checks for the leftmost wrapped square
			 */

			if (xIndex2 + 1 > Constants.ARRAY_SIZE - 1) {
				if (directionalArray[Constants.PACMAN_EXIT_ROW][Constants.PACMAN_LEFT_EXIT_COLUMN] == null
						&& (_pacmanArray[Constants.PACMAN_EXIT_ROW][Constants.PACMAN_LEFT_EXIT_COLUMN].getArrayList()
								.isEmpty()
								|| _pacmanArray[Constants.PACMAN_EXIT_ROW][Constants.PACMAN_LEFT_EXIT_COLUMN]
										.getArrayList().get(Constants.FIRST_ELEMENT).checkMoveValidity())) {

					boardCoordinatesQueue.add(
							new BoardCoordinate(Constants.PACMAN_EXIT_ROW, Constants.PACMAN_LEFT_EXIT_COLUMN, false));

					directionalArray[Constants.PACMAN_EXIT_ROW][Constants.PACMAN_LEFT_EXIT_COLUMN] = directionalArray[yIndex2][xIndex2];

				}

			} else {

				/*
				 * This if statement adds the right neighbor of the current square if it hasn't
				 * been reached before and if is a valid cell to move into
				 */

				if (directionalArray[yIndex2][xIndex2 + 1] == null
						&& (_pacmanArray[yIndex2][xIndex2 + 1].getArrayList().isEmpty()
								|| _pacmanArray[yIndex2][xIndex2 + 1].getArrayList().get(Constants.FIRST_ELEMENT)
										.checkMoveValidity())) {

					boardCoordinatesQueue.add(new BoardCoordinate(yIndex2, xIndex2 + 1, false));
					directionalArray[yIndex2][xIndex2 + 1] = directionalArray[yIndex2][xIndex2];

				}
			}

			/*
			 * This if statement adds the neighbor below the current square if it hasn't
			 * been reached before and if is a valid cell to move into
			 */

			if (directionalArray[yIndex2 + 1][xIndex2] == null
					&& (_pacmanArray[yIndex2 + 1][xIndex2].getArrayList().isEmpty()
							|| _pacmanArray[yIndex2 + 1][xIndex2].getArrayList().get(Constants.FIRST_ELEMENT)
									.checkMoveValidity())) {

				boardCoordinatesQueue.add(new BoardCoordinate(yIndex2 + 1, xIndex2, false));
				directionalArray[yIndex2 + 1][xIndex2] = directionalArray[yIndex2][xIndex2];

			}

			/*
			 * This if statement adds the neighbor above the current square if it hasn't
			 * been reached before and if is a valid cell to move into
			 */

			if (directionalArray[yIndex2 - 1][xIndex2] == null
					&& (_pacmanArray[yIndex2 - 1][xIndex2].getArrayList().isEmpty()
							|| _pacmanArray[yIndex2 - 1][xIndex2].getArrayList().get(Constants.FIRST_ELEMENT)
									.checkMoveValidity())) {

				boardCoordinatesQueue.add(new BoardCoordinate(yIndex2 - 1, xIndex2, false));
				directionalArray[yIndex2 - 1][xIndex2] = directionalArray[yIndex2][xIndex2];

			}

		}
		turnDirection = directionalArray[closestSquare.getRow()][closestSquare.getColumn()];
		_moveDirection = turnDirection;

		return turnDirection;
	}

	/*
	 * This method is used to check for the distance between a set of points and a
	 * Board Coordinate. It takes in a row, column, and a boardCoordinate as
	 * parameters and uses these variables to calculate the distance between the
	 * passed in row/column, and the boardCoordinate
	 */

	private double checkForDistance(int row, int column, BoardCoordinate boardCoordinate) {

		return Math
				.sqrt(Math.pow(boardCoordinate.getColumn() - column, 2) + Math.pow(boardCoordinate.getRow() - row, 2));

	}

}
