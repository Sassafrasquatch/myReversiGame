package model;

import java.io.Serializable;

/**
 * Reversi Board, object to be passed around by observable/observers holding
 * the current state of the reversi board.
 * 
 * @author Wes Rodgers
 *
 */
public class ReversiBoard implements Serializable{
	private static final long serialVersionUID = 1L;
	char[][] board;
	int humanCount;
	int computerCount;
	int[] move;
	char movecolor;
	public boolean humanTurn;
	
	/**
	 * Constructor, just saves the passed in parameters into the appropriate fields
	 * 
	 * @param board
	 * @param humanCount
	 * @param computerCount
	 * @param movecolor 
	 * @param move 
	 */
	public ReversiBoard(char[][] board, int humanCount, int computerCount, int[] move, char movecolor) {
		this.board = board;
		this.humanCount = humanCount;
		this.computerCount = computerCount;
		this.move = move;
		this.movecolor = movecolor;
	}
	
	/**
	 * getter for board field
	 * 
	 * @return board field
	 */
	public char[][] getBoard(){
		return this.board;
	}
	
	/**
	 * getter for move
	 * 
	 * @return coordinate of the move
	 */
	public int[] getMove() {
		return this.move;
	}
	
	/**
	 * getter for movecolor
	 * @return color of the move
	 */
	public char getMoveColor() {
		return this.movecolor;
	}

	/**
	 * getter for human score
	 * 
	 * @return human score
	 */
	public int getHumanCount() {
		return this.humanCount;
	}
	
	/**
	 * getter for computer score
	 * 
	 * @return the computer score
	 */
	public int getComputerCount() {
		return this.computerCount;
	}
	
	/**
	 * getter method for ReversiBoard class, returns the color character at position x,y
	 * 
	 * @param x the x axis coordinate for the character array representing our board
	 * @param y the y axis coordinate for the character array representing our board
	 * @return the color character at position x,y on the board
	 */
	public char getColorAt(int x, int y) {
		return this.board[x][y];
	}
}
