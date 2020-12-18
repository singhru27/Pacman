package app.impl;

import javafx.scene.layout.BorderPane;

/*
 * In the constructor, I create a new BorderPane called _root. I then create an instance of the Game class, which takes the 
 * root as a passed in parameter
 */

public class PaneOrganizer {
	
	private BorderPane _root;
	
	public PaneOrganizer() {
		
		_root = new BorderPane ();
		Game game = new Game (_root);
	}
	
	/*
	 * This method returns the BorderPane _root that was instantiated in the
	 * Constructor
	 */

	public BorderPane getRoot() {

		return _root;
	}

}
