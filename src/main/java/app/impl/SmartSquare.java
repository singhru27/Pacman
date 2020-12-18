package app.impl;

import java.util.ArrayList;

/*
 * This is the class for the smart squares that hold objects in my game. Each different "spot" on the board holds
 * a SmartSquare, and each SmartSquare contains objects via an ArrayList
 */

public class SmartSquare {
	
	private ArrayList<Collideable> _smartArrayList;
	
	/*
	 * In the constructor, I initialize the _smartArrayList
	 */
	
	public SmartSquare () {
		
		_smartArrayList = new ArrayList<Collideable> (); 
	}
	
	/*
	 * This method is used to add an item to the _smartArrayList within the SmartSquare
	 */
	
	public void addItem (Collideable o) {
		
		_smartArrayList.add(o);
	}
	
	/*
	 * This method is used to return the _smartArrayList within the SmartSquare
	 */
	
	public ArrayList<Collideable> getArrayList () {
		
		return _smartArrayList;
	}

}
