package util;

/*
 * This class contains constants for the pane size, the sizes of the wall, the dimensions of the array, the radius of pacman,
 * the initial values of the lives and the score, the radius of the dot
 */
public class Constants {
	public static final double PANE_SIZE_X = 920;
	public static final double PANE_SIZE_Y = 920;
	public static final double WALL_SIZE = 40;
	public static final int ARRAY_SIZE = 23;
	public static final double PACMAN_RADIUS = 20;
	public static final double DOT_RADIUS = 4;
	public static final double ENERGIZER_RADIUS = 8;

	/*
	 * These variables define the Durations of the various timelines used in the
	 * game
	 */

	public static final double PACMAN_DURATION = .25;
	public static final double GHOST_PEN_DURATION = 6;
	public static final int FRIGHTENED_DURATION = 50;
	public static final int SCATTER_DURATION =10;
	public static final int NORMAL_DURATION = 50;

	/*
	 * These constants define the basic counter and tracker variables used in the
	 * game
	 */
	
	public static final int NUMBER_OF_OBJECTS = 186;
	public static final int FIRST_ELEMENT = 0;
	public static final int INITIAL_LIVES = 3;
	public static final int INITIAL_SCORE = 0;
	public static final int INITIAL_ENERGY = 1;
	public static final int FRIGHTENED_ENERGY_STATE = 3;

	/*
	 * These constants are used to determine scoring for the game
	 */
	public static final int DOT_SCORE = 10;
	public static final int GHOST_SCORE = 200;
	public static final int ENERGIZER_SCORE = 100;

	/*
	 * These constants defined the exit rows and columns for the pacman, and are
	 * used for wrapping funcitonality COLUMN = 11
	 */
	
	public static final int PACMAN_INITIAL_COLUMN = 11;
	public static final int PACMAN_INITIAL_ROW = 17;
	public static final int PACMAN_EXIT_ROW = 11;
	public static final int PACMAN_RIGHT_EXIT_COLUMN = 22;
	public static final int PACMAN_LEFT_EXIT_COLUMN = 0;

	/*
	 * These constants determine the locations of the ghosts when they get sent to
	 * the pen
	 */

	public static final int GHOST_PEN_GHOST_ROW = 10;
	public static final int GHOST_PEN_OUTER_GHOST_ROW = 8;
	public static final int GHOST_PEN_LEFT_GHOST_COLUMN = 10;
	public static final int GHOST_PEN_CENTER_GHOST_COLUMN = 11;
	public static final int GHOST_PEN_RIGHT_GHOST_COLUMN = 12;


	/*
	 * The follow constants are used to defined the locations of the four corners of
	 * the board for scatter mode
	 */
	
	public static final int LEFT_COLUMN_BOARD = 1;
	public static final int RIGHT_COLUMN_BOARD = 21;
	public static final int BOTTOM_ROW = 21;
	public static final int TOP_ROW = 1;

}
