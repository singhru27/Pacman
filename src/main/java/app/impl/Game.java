package app.impl;

import cs015.fnl.PacmanSupport.BoardLocation;
import cs015.fnl.PacmanSupport.SupportMap;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import util.Constants;
import util.Direction;

/*
 * This class contains all the basic logic for the game. It sets up the initial board, and creates the labels reflecting the
 * lives/score of the game. It creates the system of counters used throughout the game, and also creates the timehandler
 * and keyhandlers that handle the key input and object movement throughout the game. This class also contains a multitude
 * of getters and setters that return and set the values of the various counters used throughout the game
 */

public class Game {

	private BorderPane _root;
	private SmartSquare[][] _pacmanArray;
	private Pane _pacmanPane;
	private Pacman _pacman;
	private Label _scoreBoard;
	private Label _lives;
	private Timeline timeline;

	private GhostPen _ghostPen;
	private Ghost1 _ghost1;
	private Ghost2 _ghost2;
	private Ghost3 _ghost3;
	private Ghost4 _ghost4;

	private int _gameCounter;
	private int _livesCounter;
	private int _score;
	private int _energyState;
	private int _energyStateCounter;
	private int _normalStateCounter;
	private int _scatterStateCounter;
	private Direction _direction;

	/*
	 * In the constructor, I add the Pane to the center of the BorderPane and I
	 * initialize the _pacmanArray. I initialize the labels and the score/life
	 * counters, and create the new pacman. I initialize the GhostPen, and call a
	 * method to create references to the ghosts within the GhostPen class. I also
	 * call the command to set up the initial board and set up the user input (with
	 * the labels)
	 */

	public Game(BorderPane border) {

		_root = border;
		_lives = new Label();
		_scoreBoard = new Label();
		_lives = new Label();
		this.setUpCounters();

		_pacmanArray = new SmartSquare[Constants.ARRAY_SIZE][Constants.ARRAY_SIZE];
		_pacman = new Pacman(_pacmanArray);
		_pacmanPane = new Pane();
		_pacmanPane.setPrefSize(Constants.PANE_SIZE_X, Constants.PANE_SIZE_Y);
		_root.setCenter(_pacmanPane);

		_ghostPen = new GhostPen(_pacmanArray, _pacmanPane);
		_ghost1 = new Ghost1(_pacman, _pacmanArray, _ghostPen, this, _scoreBoard, _lives);
		_ghost2 = new Ghost2(_pacman, _pacmanArray, _ghostPen, this, _scoreBoard, _lives);
		_ghost3 = new Ghost3(_pacman, _pacmanArray, _ghostPen, this, _scoreBoard, _lives);
		_ghost4 = new Ghost4(_pacman, _pacmanArray, _ghostPen, this, _scoreBoard, _lives);
		_ghostPen.createGhostReferences(_ghost1, _ghost2, _ghost3, _ghost4);

		this.setUpInitialBoard();
		this.setUpGameBasics();
		this.setUpKeyHandler();
		this.setUpTimeline();

	}

	/*
	 * This helper method sets up the key input response system in the game
	 */

	private void setUpKeyHandler() {

		_pacmanPane.setOnKeyPressed(new KeyHandler());
		_pacmanPane.requestFocus();
		_pacmanPane.setFocusTraversable(true);

	}

	/*
	 * This private inner class is responsible for setting the directional enum to
	 * UP, RIGHT, LEFT, DOWN to the right, left, up, and down.
	 */

	private class KeyHandler implements EventHandler<KeyEvent> {

		/*
		 * I use math to create integers that represent the equivalent index position of
		 * pacman within the array of SmartSquares. I then use a switch statement within
		 * each type of key press. If the adjacent square is empty, that automatically
		 * means the pacman can move into that square and so I set the Direction enum to
		 * the direction of the key press. If the adjacent square is not empty, I then
		 * run through the first item (which will always be a wall) located in the
		 * adjacent SmartSquare and check for move validity. If the movement is valid, I
		 * set the direction enum to the value indicated by the corresponding key press
		 */

		@Override
		public void handle(KeyEvent e) {

			KeyCode keyPressed = e.getCode();
			int row = (int) ((int) (_pacman.getYLocation() - Constants.PACMAN_RADIUS) / (Constants.WALL_SIZE));
			int column = (int) ((int) (_pacman.getXLocation() - Constants.PACMAN_RADIUS) / (Constants.WALL_SIZE));
			switch (keyPressed) {

			case RIGHT:

				// The if statement is embedded to prevent array out of bounds indexing issues

				if (column + 1 > Constants.ARRAY_SIZE - 1) {

				} else {

					SmartSquare square = _pacmanArray[row][column + 1];

					if (square.getArrayList().isEmpty()
							|| square.getArrayList().get(Constants.FIRST_ELEMENT).checkMoveValidity()) {
						_direction = Direction.RIGHT;
					}
				}

				break;

			case LEFT:

				// This if statement is embedded to prevent array out of bounds indexing issues

				if (column - 1 < 0) {

				} else {

					SmartSquare square2 = _pacmanArray[row][column - 1];
					if (square2.getArrayList().isEmpty()
							|| square2.getArrayList().get(Constants.FIRST_ELEMENT).checkMoveValidity()) {
						_direction = Direction.LEFT;
					}
				}
				break;

			case UP:
				SmartSquare square3 = _pacmanArray[row - 1][column];

				if (square3.getArrayList().isEmpty()
						|| square3.getArrayList().get(Constants.FIRST_ELEMENT).checkMoveValidity()) {
					_direction = Direction.UP;
				}

				break;

			case DOWN:
				SmartSquare square4 = _pacmanArray[row + 1][column];

				if (square4.getArrayList().isEmpty()
						|| square4.getArrayList().get(Constants.FIRST_ELEMENT).checkMoveValidity()) {
					_direction = Direction.DOWN;
				}

			default:
				break;

			}
			e.consume();
		}
	}

	/*
	 * This method is used to end the game. It removes key functionality, creates an
	 * endGame label, and removes all the timelines from the game
	 */

	private void endGame() {
		_pacmanPane.setOnKeyPressed(null);
		timeline.stop();
		Label endLabel = new Label("Game Over");
		_root.setCenter(endLabel);
		_ghostPen.endTimeline();
	}

	/*
	 * This method sets up the timeline which controls the animation for the pacman
	 * game
	 */

	private void setUpTimeline() {

		KeyFrame kf = new KeyFrame(Duration.seconds(Constants.PACMAN_DURATION), new TimeHandler());
		timeline = new Timeline(kf);
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	/*
	 * This private inner Timehandler class sets in motion the basic mechanics of
	 * the game. It moves the pacman in the direction of the DIRECTION enum if there
	 * is no wall in that direction. It then collides with every object located in
	 * that SmartSquare. It also contains the command for executing the search
	 * method for each of the ghosts
	 */

	private class TimeHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			this.checkForGameEnd();
			this.updateEnergyStates();
			this.moveObjects();

		}

		/*
		 * This method ends the game if lives = 0 or if there are no energizers or dots
		 * left
		 */

		private void checkForGameEnd() {

			if (_livesCounter == 0 || _gameCounter == 0) {
				Game.this.endGame();
			}
		}

		/*
		 * This method sets the energy states for the game (chase, frightened, or
		 * scatter)
		 */

		private void updateEnergyStates() {

			/*
			 * The following two if statement is used to switch between the two
			 * non-frightened energy states
			 */

			if (_energyState == 1) {

				if (_normalStateCounter < Constants.NORMAL_DURATION) {
					_normalStateCounter++;
				}

				else {
					_normalStateCounter = 0;
					_energyState = 2;
				}

			}

			if (_energyState == 2) {

				if (_scatterStateCounter < Constants.SCATTER_DURATION) {
					_scatterStateCounter++;
				}

				else {
					_scatterStateCounter = 0;
					_energyState = 1;
				}

			}

			/*
			 * This statement is used to reset the energy state of the ghost from frightened
			 * to normal after a certain amount of time. This is implemented by using an
			 * energyStateCounter variable
			 */

			if (_energyState == 3) {

				if (_energyStateCounter < Constants.FRIGHTENED_DURATION) {
					_energyStateCounter++;
				} else {

					_energyStateCounter = 0;
					_energyState = Constants.INITIAL_ENERGY;
					_ghost1.setColor();
					_ghost2.setColor();
					_ghost3.setColor();
					_ghost4.setColor();
				}
			}

		}

		/*
		 * This method is used to move the objects in the game. The pacman is moved into
		 * the direction of the indicated Direction enum, and is moved into the
		 * appropriate indexed location on the _pacmanArray.
		 */

		private void moveObjects() {

			int row = (int) ((int) (_pacman.getYLocation() - Constants.PACMAN_RADIUS) / (Constants.WALL_SIZE));
			int column = (int) ((int) (_pacman.getXLocation() - Constants.PACMAN_RADIUS) / (Constants.WALL_SIZE));

			/*
			 * An if statement that executes if the _direction enum is not null is executed
			 * to prevent thrown errors in the beginning of the game when the pacman has not
			 * yet begun moving
			 */
			

			if (_direction != null) {
				switch (_direction) {

				/*
				 * If the pacman is on the left or right side of the board, it will wrap around
				 * to the opposite side. If it is not, it will continue moving as normal
				 */

				case RIGHT:

					/*
					 * This statement is executed if the Pacman is exiting the right side of the
					 * board. It make the pacman collide with all objects on the exit space of the
					 * board, and then clears the array
					 */

					if (column + 1 > Constants.ARRAY_SIZE - 1) {

						_pacman.setXLocation(
								Constants.WALL_SIZE * Constants.PACMAN_LEFT_EXIT_COLUMN + Constants.PACMAN_RADIUS);
						_pacman.setYlocation(Constants.WALL_SIZE * Constants.PACMAN_EXIT_ROW + Constants.PACMAN_RADIUS);

						for (int i = 0; i < _pacmanArray[_pacman.getRow()][_pacman.getColumn()].getArrayList()
								.size(); i++) {
							_pacmanArray[_pacman.getRow()][_pacman.getColumn()].getArrayList().get(i).collide();
						}

						_pacmanArray[_pacman.getRow()][_pacman.getColumn()].getArrayList().clear();

					}

					/*
					 * This statement is executed if it is valid to move into the cell to the right
					 * of pacman. If it is valid, it moves the pacman into that cell then collides
					 * with all objects (if there are any) then moves the ghost, then collides
					 * again. The
					 */

					else {

						SmartSquare square1 = _pacmanArray[row][column + 1];
						if (square1.getArrayList().isEmpty()
								|| square1.getArrayList().get(Constants.FIRST_ELEMENT).checkMoveValidity()) {

							_pacman.moveRight();
							for (int i = 0; i < _pacmanArray[_pacman.getRow()][_pacman.getColumn()].getArrayList()
									.size(); i++) {
								_pacmanArray[_pacman.getRow()][_pacman.getColumn()].getArrayList().get(i).collide();
							}

							_pacmanArray[_pacman.getRow()][_pacman.getColumn()].getArrayList().clear();

						}
					}

					break;

				case LEFT:

					/*
					 * This statement is executed if the Pacman is exiting the right side of the
					 * board. It make the pacman collide with all objects on the exit space of the
					 * board
					 */

					if (column < 1) {

						_pacman.setXLocation(
								Constants.WALL_SIZE * Constants.PACMAN_RIGHT_EXIT_COLUMN + Constants.PACMAN_RADIUS);
						_pacman.setYlocation(Constants.WALL_SIZE * Constants.PACMAN_EXIT_ROW + Constants.PACMAN_RADIUS);

						for (int i = 0; i < _pacmanArray[_pacman.getRow()][_pacman.getColumn()].getArrayList()
								.size(); i++) {
							_pacmanArray[_pacman.getRow()][_pacman.getColumn()].getArrayList().get(i).collide();
						}

						_pacmanArray[_pacman.getRow()][_pacman.getColumn()].getArrayList().clear();

					}

					/*
					 * This statement is executed if it is valid to move into the cell to the left
					 * of pacman. If it is valid, it moves the pacman into that cell then collides
					 * with all objects (if there are any) then moves the ghost, then collides again
					 */

					else {

						SmartSquare square2 = _pacmanArray[row][column - 1];

						if (square2.getArrayList().isEmpty()
								|| square2.getArrayList().get(Constants.FIRST_ELEMENT).checkMoveValidity()) {

							_pacman.moveLeft();
							for (int i = 0; i < _pacmanArray[_pacman.getRow()][_pacman.getColumn()].getArrayList()
									.size(); i++) {
								_pacmanArray[_pacman.getRow()][_pacman.getColumn()].getArrayList().get(i).collide();
							}

							_pacmanArray[_pacman.getRow()][_pacman.getColumn()].getArrayList().clear();

						}
					}

					break;

				/*
				 * This statement is executed if it is valid to move into the cell above that of
				 * of pacman. If it is valid, it moves the pacman into that cell then collides
				 * with all objects (if there are any) then moves the ghost, then collides again
				 */

				case UP:
					SmartSquare square3 = _pacmanArray[row - 1][column];

					if (square3.getArrayList().isEmpty()
							|| square3.getArrayList().get(Constants.FIRST_ELEMENT).checkMoveValidity()) {

						_pacman.moveUp();

						for (int i = 0; i < _pacmanArray[_pacman.getRow()][_pacman.getColumn()].getArrayList()
								.size(); i++) {
							_pacmanArray[_pacman.getRow()][_pacman.getColumn()].getArrayList().get(i).collide();
						}

						_pacmanArray[_pacman.getRow()][_pacman.getColumn()].getArrayList().clear();

					}
					break;

				/*
				 * This statement is executed if it is valid to move into the cell below that of
				 * of pacman. If it is valid, it moves the pacman into that cell then collides
				 * with all objects (if there are any) then moves the ghost, then collides again
				 */

				case DOWN:
					SmartSquare square4 = _pacmanArray[row + 1][column];

					if (square4.getArrayList().isEmpty()
							|| square4.getArrayList().get(Constants.FIRST_ELEMENT).checkMoveValidity()) {

						_pacman.moveDown();
						for (int i = 0; i < _pacmanArray[_pacman.getRow()][_pacman.getColumn()].getArrayList()
								.size(); i++) {
							_pacmanArray[_pacman.getRow()][_pacman.getColumn()].getArrayList().get(i).collide();
						}

						_pacmanArray[_pacman.getRow()][_pacman.getColumn()].getArrayList().clear();

					}

					break;

				default:
					break;
				}
			}

			/*
			 * This switch statement is used to move the ghost and collide with objects in
			 * pacman's square, after the pacman has already been moved.
			 */

			switch (_energyState) {

			case 1:
				_ghost1.move(_ghost1.ghostBFS(row, column - 2));
				_ghost2.move(_ghost2.ghostBFS(row - 2, column + 2));
				_ghost3.move(_ghost3.ghostBFS(row - 2, column));
				_ghost4.move(_ghost4.ghostBFS(row, column));
				break;

			case 2:
				_ghost1.move(_ghost1.ghostBFS(Constants.TOP_ROW, Constants.LEFT_COLUMN_BOARD));
				_ghost2.move(_ghost2.ghostBFS(Constants.TOP_ROW, Constants.RIGHT_COLUMN_BOARD));
				_ghost3.move(_ghost3.ghostBFS(Constants.BOTTOM_ROW, Constants.LEFT_COLUMN_BOARD));
				_ghost4.move(_ghost4.ghostBFS(Constants.BOTTOM_ROW, Constants.RIGHT_COLUMN_BOARD));
				break;

			case 3:
				_ghost1.move(_ghost1.ghostBFS((int) (Math.random() * 23), (int) (Math.random() * 23)));
				_ghost2.move(_ghost2.ghostBFS((int) (Math.random() * 23), (int) (Math.random() * 23)));
				_ghost3.move(_ghost3.ghostBFS((int) (Math.random() * 23), (int) (Math.random() * 23)));
				_ghost4.move(_ghost4.ghostBFS((int) (Math.random() * 23), (int) (Math.random() * 23)));
				break;

	}

			for (int i = 0; i < _pacmanArray[_pacman.getRow()][_pacman.getColumn()].getArrayList().size(); i++) {
				_pacmanArray[_pacman.getRow()][_pacman.getColumn()].getArrayList().get(i).collide();
			}

			_pacmanArray[_pacman.getRow()][_pacman.getColumn()].getArrayList().clear();

		}

	}

	/*
	 * This helper method is used to set up the initial counters (lives, score,
	 * energy state), as well as the energy state counter (which is used to reset
	 * the ghosts back from the frightened state
	 */

	private void setUpCounters() {

		_gameCounter = Constants.NUMBER_OF_OBJECTS;
		_livesCounter = Constants.INITIAL_LIVES;
		_score = Constants.INITIAL_SCORE;
		_energyState = Constants.INITIAL_ENERGY;
		_energyStateCounter = Constants.INITIAL_ENERGY;
		_scatterStateCounter = Constants.INITIAL_ENERGY;
		_normalStateCounter = Constants.INITIAL_ENERGY;

	}

	/*
	 * This method is used to set up the labels that keep track of how many lives
	 * are left in the game, and create the button for quitting the game
	 */

	private void setUpGameBasics() {

		HBox _userInputBox = new HBox();
		Button bt1 = new Button("Quit");
		_lives.setText(" Lives " + Integer.toString(_livesCounter));
		_scoreBoard.setText(" Score " + Integer.toString(_score));
		_userInputBox.getChildren().addAll(bt1, _lives, _scoreBoard);
		_root.setBottom(_userInputBox);
		bt1.setOnAction(new QuitHandler());
		bt1.setFocusTraversable(false);
		_lives.setFocusTraversable(false);
		_scoreBoard.setFocusTraversable(false);

	}

	/*
	 * This class contains the logic for quitting the game when the button is
	 * pressed
	 */

	private class QuitHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {

			System.exit(0);
		}

	}

	/*
	 * This method is used to set up the initial board. It scans over the entire
	 * boardArray, and adds the appropriate object to the SmartSquare indexed into
	 * the location indicated by the BoardArray
	 */

	private void setUpInitialBoard() {

		BoardLocation[][] boardArray = SupportMap.getMap();

		// Adding a SmartSquare to each location on the _pacmanArray

		for (int i = 0; i < boardArray.length; i++) {

			for (int j = 0; j < boardArray.length; j++) {

				SmartSquare square = new SmartSquare();

				_pacmanArray[i][j] = square;

			}

		}

		for (int i = 0; i < boardArray.length; i++) {

			for (int j = 0; j < boardArray.length; j++) {

				switch (boardArray[i][j]) {

				// Used if there is nothing contained within this square on the array

				case FREE:
					break;

				// Used to add Dots to the board

				case DOT:
					_pacmanArray[i][j].addItem(new Dot(i, j, _pacmanPane, this, _scoreBoard));
					break;

				// Used to add walls to the board

				case WALL:
					_pacmanArray[i][j].addItem(new Wall(i, j, _pacmanPane));
					break;

				// Used to add the Pacman to the board

				case PACMAN_START_LOCATION:
					_pacman.setXLocation(j * Constants.WALL_SIZE + Constants.PACMAN_RADIUS);
					_pacman.setYlocation(i * Constants.WALL_SIZE + Constants.PACMAN_RADIUS);
					_pacmanPane.getChildren().add(_pacman.getPacman());
					break;

				// Used to add energizers to the board

				case ENERGIZER:
					_pacmanArray[i][j].addItem(
							new Energizer(i, j, this, _pacmanPane, _ghost1, _ghost2, _ghost3, _ghost4, _scoreBoard));
					break;

				// Used to add Ghosts. All the ghosts are positioned according to the blue ghost

				case GHOST_START_LOCATION:
					_ghostPen.setUpGhost(_ghost1);
					_ghostPen.setUpGhost(_ghost2);
					_ghostPen.setUpGhost(_ghost3);
					_ghostPen.setUpGhost(_ghost4);
					break;

				default:
					break;

				}

			}

		}

	}

	/*
	 * This method returns the _score
	 */

	public int getScore() {
		return _score;
	}

	/*
	 * This method sets the score
	 */

	public void setScore(int x) {
		_score = x;
	}

	/*
	 * this method returns the current _energy state
	 */

	public int getEnergyState() {
		return _energyState;
	}

	/*
	 * This method sets the energy state
	 */

	public void setEnergyState(int x) {
		_energyState = x;
	}

	/*
	 * This method returns the value of the _lives variable
	 */

	public int getLives() {
		return _livesCounter;
	}

	/*
	 * This method sets the value of the _lives variable
	 */

	public void setLives(int x) {
		_livesCounter = x;
	}

	/*
	 * This method sets the value of the _direction variable
	 */

	public void resetDirection() {
		_direction = null;
	}

	/*
	 * This method decreases the game counter
	 */

	public void decrementGameCounter() {
		_gameCounter--;
	}
	
	public int getGameCounter () {
		return _gameCounter;
	}

	/*
	 * This method returns the direction that the pacman is moving
	 */

	public Direction getDirection() {
		return _direction;
	}

}
