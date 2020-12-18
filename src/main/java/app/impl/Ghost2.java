package app.impl;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

/*
 * This is the subclass for the blue ghost
 */

public class Ghost2 extends Ghost {

	/*
	 * In the constructor, I initialize all the variables used by the superclass. I
	 * also call a method to set the color for this ghost
	 */

	public Ghost2(Pacman pacman, SmartSquare[][] array, GhostPen ghostpen, Game game, Label scoreLabel, Label lives) {

		super(pacman, array, ghostpen, game, scoreLabel, lives);
		this.setColor();

	}

	/*
	 * In this method, I set the color of this specific ghost
	 */

	public void setColor() {

		this.getGhost().setFill(Color.PINK);
	}

}
