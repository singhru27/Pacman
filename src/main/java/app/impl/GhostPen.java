package app.impl;

import java.util.LinkedList;
import java.util.Queue;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import util.Constants;

/*
 * This class models the pen that the ghosts are held in. It is responsible for initially setting up the ghosts and adding 
 * them to their respective locations. It is also responsible for adding the ghosts back into the pen at the center of the game,
 * and for removing them periodically according to a specifid time increment
 */

public class GhostPen {

	private SmartSquare[][] _pacmanArray;
	private Pane _pacmanPane;
	private Queue<Ghost> _ghostPenQueue;
	private Ghost _ghost1;
	private Ghost _ghost2;
	private Ghost _ghost3;
	private Ghost _ghost4;
	private Ghost[] _ghostPenLocationTracker;
	private Timeline timeline;

	/*
	 * In the constructor, I take in an array and a pane as passed in parameters. I
	 * then initialize the rest of the instance variables within this class, and
	 * call a method to set up the timeline for this class. I create a
	 * ghostPenLocationTracker to keep track of which location the queued ghosts are
	 * located at. Finally, I also call a method to set up the TimeHandler that
	 * dequeues ghosts
	 */

	public GhostPen(SmartSquare[][] array, Pane pane) {

		_pacmanArray = array;
		_pacmanPane = pane;
		_ghostPenQueue = new LinkedList<>();
		_ghostPenLocationTracker = new Ghost[3];
		this.setUpTimeline();

	}

	/*
	 * Since both the ghosts and the ghostpen need to know about each other, they
	 * both cannot take in the other object as parameters (since one object must
	 * necessarily be created first. To solve this, I created a new method that
	 * directly creates references to the ghosts by taking in the ghosts as
	 * parameters and directly creating references to these ghosts
	 */

	public void createGhostReferences(Ghost ghost1, Ghost ghost2, Ghost ghost3, Ghost ghost4) {
		_ghost1 = ghost1;
		_ghost2 = ghost2;
		_ghost3 = ghost3;
		_ghost4 = ghost4;
	}

	/*
	 * This method is called when the ghosts are first being created. It adds the
	 * ghosts to the SmartSquare in the respective pacman array, sets the coordinate
	 * of the ghost, and adds the ghost to the pane
	 */

	public void setUpGhost(Ghost ghost) {
		_pacmanPane.getChildren().add(ghost.getGhost());
		this.queueGhost(ghost);

	}

	/*
	 * This method is used to queue up all the ghosts to their original positions
	 * (used when Pacman has been eaten). It removes all the ghosts from their
	 * respective SmartSquares, and then resets their positions. It also empties the
	 * queue to fully reset the game, and clears out the array that keeps track of
	 * ghost locations
	 * 
	 */

	public void resetGhosts() {

		this.clearQueue();
		int i = 0;
		while (i < _ghostPenLocationTracker.length) {

			_ghostPenLocationTracker[i] = null;
			i++;

		}

		this.queueGhost(_ghost1);
		this.queueGhost(_ghost2);
		this.queueGhost(_ghost3);
		this.queueGhost(_ghost4);
	}

	/*
	 * This method is called when a ghost is to be queued into the pen. It adds the
	 * ghost to the appropriate location, and adds it to the queue if the ghostpen
	 * is not already full. I also use a ghostPenTracker to determine where in the
	 * pen newly queued ghosts should go
	 */

	public void queueGhost(Ghost ghost) {

		// Case for when the ghostPenQueue is empty

		if (_ghostPenQueue.isEmpty()) {
			_ghostPenQueue.add(ghost);
			ghost.setXLocation(Constants.GHOST_PEN_LEFT_GHOST_COLUMN * Constants.WALL_SIZE);
			ghost.setYLocation(Constants.GHOST_PEN_GHOST_ROW * Constants.WALL_SIZE);

			_ghostPenLocationTracker[0] = ghost;

			return;
		}

		// Case for when the ghostPenQueue has one ghost

		if (_ghostPenQueue.size() == 1) {

			_ghostPenQueue.add(ghost);

			if (_ghostPenLocationTracker[0] == null) {

				ghost.setXLocation(Constants.GHOST_PEN_LEFT_GHOST_COLUMN * Constants.WALL_SIZE);
				ghost.setYLocation(Constants.GHOST_PEN_GHOST_ROW * Constants.WALL_SIZE);

				_ghostPenLocationTracker[0] = ghost;

			} else if (_ghostPenLocationTracker[1] == null) {

				ghost.setXLocation(Constants.GHOST_PEN_CENTER_GHOST_COLUMN * Constants.WALL_SIZE);
				ghost.setYLocation(Constants.GHOST_PEN_GHOST_ROW * Constants.WALL_SIZE);
				_ghostPenLocationTracker[1] = ghost;

			} else if (_ghostPenLocationTracker[2] == null) {

				ghost.setXLocation(Constants.GHOST_PEN_RIGHT_GHOST_COLUMN * Constants.WALL_SIZE);
				ghost.setYLocation(Constants.GHOST_PEN_GHOST_ROW * Constants.WALL_SIZE);
				_ghostPenLocationTracker[2] = ghost;

			}

			return;
		}

		// Case for when the ghostPenQueue has two ghosts

		if (_ghostPenQueue.size() == 2) {

			_ghostPenQueue.add(ghost);

			if (_ghostPenLocationTracker[0] == null) {

				ghost.setXLocation(Constants.GHOST_PEN_LEFT_GHOST_COLUMN * Constants.WALL_SIZE);
				ghost.setYLocation(Constants.GHOST_PEN_GHOST_ROW * Constants.WALL_SIZE);

				_ghostPenLocationTracker[0] = ghost;

			} else if (_ghostPenLocationTracker[1] == null) {

				ghost.setXLocation(Constants.GHOST_PEN_CENTER_GHOST_COLUMN * Constants.WALL_SIZE);
				ghost.setYLocation(Constants.GHOST_PEN_GHOST_ROW * Constants.WALL_SIZE);
				_ghostPenLocationTracker[1] = ghost;

			} else if (_ghostPenLocationTracker[2] == null) {

				ghost.setXLocation(Constants.GHOST_PEN_RIGHT_GHOST_COLUMN * Constants.WALL_SIZE);
				ghost.setYLocation(Constants.GHOST_PEN_GHOST_ROW * Constants.WALL_SIZE);
				_ghostPenLocationTracker[2] = ghost;

			}

			return;
		}

		// Case for when the ghostPenQueue has three ghosts

		if (_ghostPenQueue.size() == 3) {

			ghost.setXLocation(Constants.GHOST_PEN_CENTER_GHOST_COLUMN * Constants.WALL_SIZE);
			ghost.setYLocation(Constants.GHOST_PEN_OUTER_GHOST_ROW * Constants.WALL_SIZE);
			_pacmanArray[Constants.GHOST_PEN_OUTER_GHOST_ROW][Constants.GHOST_PEN_CENTER_GHOST_COLUMN].addItem(ghost);
			return;
		}

	}

	/*
	 * This method is used to dequeue a ghost. It removes the ghost from the queue,
	 * sets it outside of the ghostpen, and adds it to the _pacmanArray. I also
	 * remove the ghost from the ghostPenLocationTracker by using a linear search to
	 * find the index within the array, and removing that value
	 */

	private void deQueueGhost(Ghost ghost) {
		_ghostPenQueue.remove(ghost);

		int i = 0;
		while (i < _ghostPenLocationTracker.length) {

			if (_ghostPenLocationTracker[i] == ghost) {
				_ghostPenLocationTracker[i] = null;
				break;
			} else {
				i++;
			}
		}

		ghost.setXLocation(Constants.GHOST_PEN_CENTER_GHOST_COLUMN * Constants.WALL_SIZE);
		ghost.setYLocation(Constants.GHOST_PEN_OUTER_GHOST_ROW * Constants.WALL_SIZE);
		_pacmanArray[Constants.GHOST_PEN_OUTER_GHOST_ROW][Constants.GHOST_PEN_CENTER_GHOST_COLUMN].addItem(ghost);
	}

	/*
	 * This method is used to clear the queue and to remove all objects in the
	 * square outside the ghostPen where the fourth ghost appears
	 */

	private void clearQueue() {
		_pacmanArray[(int) (_ghost1.getGhost().getY() / Constants.WALL_SIZE)][(int) (_ghost1.getGhost().getX()
				/ Constants.WALL_SIZE)].getArrayList().remove(_ghost1);
		_pacmanArray[(int) (_ghost2.getGhost().getY() / Constants.WALL_SIZE)][(int) (_ghost2.getGhost().getX()
				/ Constants.WALL_SIZE)].getArrayList().remove(_ghost2);
		_pacmanArray[(int) (_ghost3.getGhost().getY() / Constants.WALL_SIZE)][(int) (_ghost3.getGhost().getX()
				/ Constants.WALL_SIZE)].getArrayList().remove(_ghost3);
		_pacmanArray[(int) (_ghost4.getGhost().getY() / Constants.WALL_SIZE)][(int) (_ghost4.getGhost().getX()
				/ Constants.WALL_SIZE)].getArrayList().remove(_ghost4);

		_ghostPenQueue.clear();
	}

	/*
	 * This method returns the ghostPenqueue when called
	 */

	public Queue<Ghost> getQueue() {
		return _ghostPenQueue;
	}

	/*
	 * This method is used to set up the timeline. The timeline is used to
	 * automatically dequeue ghosts every 7 seconds
	 */

	private void setUpTimeline() {

		KeyFrame kf = new KeyFrame(Duration.seconds(Constants.GHOST_PEN_DURATION), new TimeHandler());
		timeline = new Timeline(kf);
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();

	}

	/*
	 * This timehandler inner class dequeues a ghost every time it is called
	 */

	private class TimeHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {

			if (_ghostPenQueue.isEmpty() == false) {
				GhostPen.this.deQueueGhost(_ghostPenQueue.element());
			}

		}
	}

	/*
	 * This method ends the GhostPenQueue timeline
	 */

	public void endTimeline() {
		timeline.stop();
	}

}
