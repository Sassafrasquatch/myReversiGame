package model;

/**
 * provides a move class for LegalMoves, has hashCode() and equals() overrides
 * to make sure HashMap can properly check whether it contains them.
 * 
 * @author Wes Rodgers
 *
 */
public class ReversiMove {

	private int x, y;
	
	public ReversiMove(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Overrides hashCode just in case it tries to hash based on something other than
	 * the move's x/y values
	 */
	@Override
	public int hashCode() {
		return Integer.hashCode(this.x + this.y);
	}
	
	/**
	 * Overrides equals to return two moves as equal when they have the same coordinates
	 */
	@Override
	public boolean equals(Object o) {
		if(o==null || o.getClass() != getClass()) {
			return false;
		}
		return this.getX() == ((ReversiMove) o).getX() && this.getY() == ((ReversiMove) o).getY();
	}

	/**
	 * getter for y coordinate
	 * 
	 * @return y coordinate value
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * getter for x coordinate
	 * 
	 * @return x coordinate value
	 */
	public int getX() {
		return this.x;
	}
}
