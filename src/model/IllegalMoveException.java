package model;

/**
 * Exception for when the user's move isn't legal
 * 
 * @author Wes Rodgers
 *
 */
public class IllegalMoveException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public IllegalMoveException(String errorMessage) {
		super(errorMessage);
	}

}
