package controller;

import model.IllegalMoveException;
import model.LegalMoves;
import model.ReversiModel;
import model.ReversiMove;

/**
 * Main controller for the Reversi game. Contains all the logic for
 * finding legal moves, determining the computer's best move, and flipping
 * the correct tiles when a move is made.
 * 
 * @author Wes Rodgers
 *
 */
public class ReversiController {
	
	private ReversiModel model;
	private LegalMoves humanLegal;
	private LegalMoves computerLegal;
	
	/**
	 * regular constructor for the controller
	 */
	public ReversiController() {
		this.model = new ReversiModel();
		this.humanLegal = new LegalMoves();
		this.computerLegal = new LegalMoves();
	}
	
	/**
	 * controller that takes a custom ReversiModel instead of the default
	 * useful for testing
	 * 
	 * @param model a pre-made ReversiModel
	 */
	public ReversiController(ReversiModel model) {
		this.model = model;
		this.humanLegal = new LegalMoves();
		this.computerLegal = new LegalMoves();
	}
	
	/**
	 * adds a specific move to the computer's legal list, mainly for testing.
	 * 
	 * @param x
	 * @param y
	 */
	public void addComputerMove(int x, int y) {
		computerLegal.addMove(x, y);
	}
	
	/**
	 * makes the human move
	 * 
	 * @param x the x coordinate we are placing a piece at
	 * @param y the y coordinate we are placing a piece at
	 * @throws IllegalMoveException when the move wouldn't flip any pieces
	 */
	public void humanTurn(int x, int y) throws IllegalMoveException{
		if(!humanLegal.contains(x, y)) {
			throw new IllegalMoveException("This is an illegal move. Must place piece so your colors book-end a line of your opponents.");
		}	
		
		model.setColorAt(x, y, 'W');
		flipColors(x, y, 'W');
	}
	
	/**
	 * flips the colors for a specific move. The logic is simple, just moves
	 * from the target location in each of the 8 possible directions until it
	 * finds either an empty space or another spot that matches the initial color.
	 * Then either does nothing if the space is empty, or flips every color inbetween
	 * the matching color and the target location
	 * 
	 * @param x x coordinate of the spot we're flipping from
	 * @param y y coordinate of the spot we're flipping from
	 * @param color the color we're flipping to
	 */
	public void flipColors(int x, int y, char color) {
		
		//flips appropriate colors to the right of the move
		for(int i=x+1; i<8; i++) {
			if(model.getColorAt(i, y) == '_') {
				break;
			}
			if(model.getColorAt(i, y) == color) {
				for(int j=i-1; j>x; j--) {
					model.setColorAt(j, y, color);
				}
				break;
			}
		}
		
		//flips appropriate colors to the left of the move
		for(int i=x-1; i>=0; i--) {
			if(model.getColorAt(i, y) == '_') {
				break;
			}
			if(model.getColorAt(i, y) == color) {
				for(int j=i+1; j<x; j++) {
					model.setColorAt(j, y, color);
				}
				break;
			}
		}
		
		//flips appropriate colors below the move
		for(int i=y+1; i<8; i++) {
			if(model.getColorAt(x, i) == '_') {
				break;
			}
			if(model.getColorAt(x, i) == color) {
				for(int j=i-1; j>y; j--) {
					model.setColorAt(x, j, color);
				}
				break;
			}
		}
		
		//flips appropriate colors above the move
		for(int i=y-1; i>=0; i--) {
			if(model.getColorAt(x, i) == '_') {
				break;
			}
			if(model.getColorAt(x, i) == color) {
				for(int j=i+1; j<y; j++) {
					model.setColorAt(x, j, color);
				}
				break;
			}
		}
		
		//flips lower right diagonal pieces
		for(int i=x+1, j=y+1; i<8 && j<8; i++, j++) {
			if(model.getColorAt(i, j) == '_') {
				break;
			}
			if(model.getColorAt(i, j) == color) {
				for(int k=i-1, l=j-1; k>x; k--, l--) {
					model.setColorAt(k, l, color);
				}
				break;
			}
		}
		
		// flips upper left diagonal pieces
		for(int i=x-1, j=y-1; i>=0 && j>=0; i--, j--) {
			if(model.getColorAt(i, j) == '_') {
				break;
			}
			if(model.getColorAt(i, j) == color) {
				for(int k=i+1, l=j+1; k<x; k++, l++) {
					model.setColorAt(k, l, color);
				}
				break;
			}
		}
		
		// flips upper right diagonal pieces
		for(int i=x+1, j=y-1; i<8 && j>=0; i++, j--) {
			if(model.getColorAt(i, j) == '_') {
				break;
			}
			if(model.getColorAt(i, j) == color) {
				for(int k=i-1, l=j+1; k>x; k--, l++) {
					model.setColorAt(k, l, color);
				}
				break;
			}
		}
		
		// flips lower left diagonal pieces
		for(int i=x-1, j=y+1; i>=0 && j<8; i--, j++) {
			if(model.getColorAt(i, j) == '_') {
				break;
			}
			if(model.getColorAt(i, j) == color) {
				for(int k=i+1, l=j-1; k<x; k++, l--) {
					model.setColorAt(k, l, color);
				}
				break;
			}
		}
		
	}

	/**
	 * determines the computer's best move, returns the move 
	 * it actually made
	 * 
	 * @return the move the computer made
	 */
	public int[] computerTurn() {
		ReversiMove best = computerLegal.bestMove();
		int x = best.getX();
		int y = best.getY();
		model.setColorAt(x, y, 'B');
		flipColors(x, y, 'B');
		
		return new int[] {x, y};
	}

	/**
	 * calculates all legal moves for both sides. The logic is pretty simple,
	 * it looks at each spot on the board. When it finds a non-empty spot, it
	 * travels outwards in each of the 8 possible directions. If the immediate 
	 * neighboring piece in a direction is the opposite color it continues until
	 * it finds an empty space, or another piece that matches the initial color.
	 * If it found an empty space, it adds that space's location to the appropriate
	 * legal move list.
	 */
	public void calculateLegal() {
		
		this.humanLegal = new LegalMoves();
		this.computerLegal = new LegalMoves();
		int indexOne, indexTwo, pointCount;
		
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				if(model.getColorAt(i, j) == '_') {
					continue;
				}
				else {
					char currColor = model.getColorAt(i, j);
					
					//checks legal to the right horizontally
					indexOne = i;
					pointCount = 0;
					for(int k=i+1; k<8 && model.getColorAt(k, j) != '_' && model.getColorAt(k, j) != currColor; k++) {
						indexOne = k+1;
						pointCount++;
					}
					if(indexOne < 8) {
						if(model.getColorAt(indexOne, j) == '_') {
							if(currColor == 'W') {
								this.humanLegal.addMove(indexOne, j);
							}
							else {
								this.computerLegal.addMove(indexOne, j, pointCount);
							}
						}
					}
					
					//checks legal to the left horizontally
					indexOne = i;
					pointCount = 0;
 					for(int k=i-1; k >= 0 && model.getColorAt(k, j)!= '_' && model.getColorAt(k, j) != currColor; k--) {
						indexOne = k-1;
						pointCount++;
					}
 					if(indexOne >= 0) {
						if(model.getColorAt(indexOne, j) == '_') {
							if(currColor == 'W') {
								this.humanLegal.addMove(indexOne, j);
							}
							else {
								this.computerLegal.addMove(indexOne, j, pointCount);
							}
						}
 					}
					
					//checks legal to the top
					indexOne = j;
					pointCount = 0;
					for(int k=j-1; k>=0 && model.getColorAt(i, k)!= '_' && model.getColorAt(i, k) != currColor; k--) {
						indexOne = k-1;
						pointCount++;
					}
 					if(indexOne >= 0) {
						if(model.getColorAt(i, indexOne) == '_') {
							if(currColor == 'W') {
								this.humanLegal.addMove(i, indexOne);
							}
							else {
								this.computerLegal.addMove(i, indexOne, pointCount);
							}
						}
 					}
					
					//checks legal to the bottom
					indexOne = j;
					pointCount = 0;
					for(int k=j+1; k < 8 && model.getColorAt(i, k)!= '_' && model.getColorAt(i, k) != currColor; k++) {
						indexOne = k+1;
						pointCount++;
					}
 					if(indexOne < 8) {
						if(model.getColorAt(i, indexOne) == '_') {
							if(currColor == 'W') {
								this.humanLegal.addMove(i, indexOne);
							}
							else {
								this.computerLegal.addMove(i, indexOne, pointCount);
							}
						}
 					}
					
					//check legal to top right
					indexOne = i;
					indexTwo = j;
					pointCount = 0;
					for(int k=i+1, l=j-1; k<8 && l>=0 && model.getColorAt(k,l)!= '_' && model.getColorAt(k, l) != currColor; k++, l--) {
						indexOne = k+1;
						indexTwo = l-1;
						pointCount++;
					}
					if(indexOne < 8) {
						if(indexTwo >= 0) {
							if(model.getColorAt(indexOne, indexTwo) == '_') {
								if(currColor == 'W') {
									this.humanLegal.addMove(indexOne,  indexTwo);;
								}
								else {
									this.computerLegal.addMove(indexOne, indexTwo, pointCount);
								}
							}
						}
					}
					
					//check legal to bottom right
					indexOne = i;
					indexTwo = j;
					pointCount = 0;
					for(int k=i+1, l=j+1; k<8 && l<8 && model.getColorAt(k,l)!= '_' && model.getColorAt(k, l) != currColor; k++, l++) {
						indexOne = k+1;
						indexTwo = l+1;
						pointCount++;
					}
					if(indexOne < 8) {
						if(indexTwo < 8) {
							if(model.getColorAt(indexOne, indexTwo) == '_') {
								if(currColor == 'W') {
									this.humanLegal.addMove(indexOne,  indexTwo);;
								}
								else {
									this.computerLegal.addMove(indexOne, indexTwo, pointCount);
								}
							}
						}
					}
					
					//check legal to bottom left
					indexOne = i;
					indexTwo = j;
					pointCount = 0;
					for(int k=i-1, l=j+1; k>=0 && l<8 && model.getColorAt(k,l)!= '_' && model.getColorAt(k, l) != currColor; k--, l++) {
						indexOne = k-1;
						indexTwo = l+1;
						pointCount++;
					}
					if(indexOne >= 0) {
						if(indexTwo < 8) {
							if(model.getColorAt(indexOne, indexTwo) == '_') {
								if(currColor == 'W') {
									this.humanLegal.addMove(indexOne,  indexTwo);;
								}
								else {
									this.computerLegal.addMove(indexOne, indexTwo, pointCount);
								}
							}
						}
					}
					
					//checks top left
					indexOne = i;
					indexTwo = j;
					pointCount = 0;
					for(int k=i-1, l=j-1; k>=0 && l>=0 && model.getColorAt(k,l)!= '_' && model.getColorAt(k, l) != currColor; k--, l--) {
						indexOne = k-1;
						indexTwo = l-1;
						pointCount++;
					}
					if(indexOne >= 0) {
						if(indexTwo >= 0) {
							if(model.getColorAt(indexOne, indexTwo) == '_') {
								if(currColor == 'W') {
									this.humanLegal.addMove(indexOne,  indexTwo);;
								}
								else {
									this.computerLegal.addMove(indexOne, indexTwo, pointCount);
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * returns the piece at (i,j)
	 * 
	 * @param i x coordinate
	 * @param j y coordinate
	 * @return the color of the piece at (i,j)
	 */
	public String getPieceAt(int i, int j) {
		return "" + model.getColorAt(i, j);
	}

	/**
	 * returns true if a move is in that color's legal move list
	 * false otherwise.
	 * 
	 * @param x x coordinate of the move
	 * @param y y coordinate of the move
	 * @param color color of the move
	 * @return true if legal, false otherwise
	 */
	public boolean isLegal(int x, int y, char color) {
		if(model.getColorAt(x, y) != '_') {
			return false;
		}
		if(color == 'W' && humanLegal.contains(x, y)) {
			return true;
		}
		if(color == 'B' && computerLegal.contains(x, y)) {
			return true;
		}
		return false;
	}

	/**
	 * returns true if the human's score is higher than the computer
	 * false if a tie or the computer is higher
	 * 
	 * @return true if human score > computer
	 */
	public boolean didHumanWin() {
		return getHumanScore() > getComputerScore();
	}

	/**
	 * returns the humans score.
	 * 
	 * @return the human's score
	 */
	public int getHumanScore() {
		return model.getHumanCount();
	}

	/**
	 * returns the computer's score
	 * 
	 * @return the computer's score
	 */
	public int getComputerScore() {
		return model.getComputerCount();
	}

	/**
	 * returns true if the user has a legal move, false otherwise
	 * 
	 * @param human boolean set to true if the user is human, false otherwise
	 * @return
	 */
	public boolean hasLegal(boolean human) {
		if(human) {
			return humanLegal.length() > 0;
		}
		return computerLegal.length() > 0;
	}

	/**
	 * adds a move at (i,j) to the human's legal list
	 * helpful for testing.
	 * 
	 * @param i x coordinate
	 * @param j y coordinate
	 */
	public void addHumanMove(int i, int j) {
		humanLegal.addMove(i, j);
	}
}
