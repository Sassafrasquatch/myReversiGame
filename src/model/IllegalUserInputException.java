package model;

/**
 * Exception for when user input doesn't fit the standards 
 * 
 * @author Wes Rodgers
 *
 */
public class IllegalUserInputException extends Exception{

	private static final long serialVersionUID = 1L;

	public IllegalUserInputException(String errorMessage) {
		super(errorMessage);
	}
}
