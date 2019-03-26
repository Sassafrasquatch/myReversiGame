package model;

import java.util.Observable;

/**
 * Main model class for the Reversi game. Sets up the board and has methods to
 * deal with altering it.
 * 
 * @author Wes Rodgers
 *
 */
@SuppressWarnings("deprecation")
public class ReversiModel extends Observable{
	
	private char[][] board;
	private int humanCount;
	private int computerCount;
	private ReversiBoard observableBoard;
	
	
	/**
	 * Constructor for a brand new board. Sets up initial pieces and 2-2 score
	 * and updates any listeners.
	 */
	public ReversiModel() {
		this.board = new char[8][8];
		
		//sets all initial positions on the board to an underscore to represent empty.
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				this.board[i][j] = '_';
			}
		}
		
		//sets up the initial 4 pieces
		this.board[3][3] = 'W';
		this.board[4][4] = 'W';
		this.board[3][4] = 'B';
		this.board[4][3] = 'B';		
		
		this.humanCount = 2;
		this.computerCount = 2;
		
		observableBoard = new ReversiBoard(board, humanCount, computerCount, null, ' ');
		setChanged();
		notifyObservers();
	}
	
	
	
	/**
	 * Secondary constructor for loading existing boards
	 * 
	 * @param 
	 */
	public ReversiModel(ReversiBoard observableBoard) {
		this.board = observableBoard.getBoard();
		this.computerCount = observableBoard.getComputerCount();
		this.humanCount = observableBoard.getHumanCount();
		this.observableBoard = observableBoard;
	}
	
	
	
	public ReversiBoard getBoard() {
		return this.observableBoard;
	}
	
	
	
	/**
	 * getter method for ReversiModel class, returns the color character at position x,y
	 * 
	 * @param x the x axis coordinate for the character array representing our board
	 * @param y the y axis coordinate for the character array representing our board
	 * @return the color character at position x,y on the board
	 */
	public char getColorAt(int x, int y) {
		return this.board[x][y];
	}
	
	
	/**
	 * setter method for ReversiModel class, sets the character at position x,y as color
	 * 
	 * @param x the x axis coordinate for the character array representing our board
	 * @param y the y axis coordinate for the character array representing our board
	 * @param color
	 */
	public void setColorAt(int x, int y, char color) {
		
		if(this.board[x][y] == color) {
			return;
		}
		
		if(this.board[x][y] == '_') {
			if(color == 'W') {
				this.humanCount++;
			}
			else {
				this.computerCount++;
			}
		}
		
		else {
			if(color == 'W') {
				this.humanCount++;
				this.computerCount--;
			}
			else {
				this.humanCount--;
				this.computerCount++;
			}
		}
		
		this.board[x][y] = color;
		
		int[] move = new int[2];
		move[0] = x;
		move[1] = y;
		char movecolor = color;
		observableBoard = new ReversiBoard(this.board, humanCount, computerCount, move, movecolor);
		setChanged();
		notifyObservers(observableBoard);
	}

	/**
	 * returns the human score
	 * 
	 * @return the human score
	 */
	public int getHumanCount() {
		return this.humanCount;
	}

	/**
	 * setter for human score
	 * 
	 * @param i number to set humanCount to
	 */
	public void setHumanCount(int i) {
		humanCount = i;
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
	 * setter for computer score
	 * 
	 * @param i value to set computerCount to
	 */
	public void setComputerCount(int i) {
		computerCount = i;
	}
	
}
