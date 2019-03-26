package model;

import java.util.HashMap;

/**
 * Provides methods and a HashMap to keep track of legal moves and the points they're
 * worth
 * 
 * @author Wes Rodgers
 *
 */
public class LegalMoves {
	
	private HashMap<ReversiMove, Integer> moveList;
	
	public LegalMoves() {
		this.moveList = new HashMap<ReversiMove, Integer>();
	}
	
	/**
	 * returns the number of legal moves
	 * 
	 * @return the number of legal moves
	 */
	public int length() {
		return moveList.size();
	}
	
	/**
	 * adds a move at position (x,y) to the HashMap
	 * 
	 * @param x
	 * @param y
	 */
	public void addMove(int x, int y) {
		ReversiMove move = new ReversiMove(x, y);
		moveList.put(move, 0);
	}
	
	/**
	 * adds a move at position (x,y) the the HashMap,
	 * update the point count if it already exists
	 * 
	 * @param x
	 * @param y
	 * @param pointCount the number of points the move is worth
	 */
	public void addMove(int x, int y, int pointCount) {
		ReversiMove move = new ReversiMove(x, y);
		if(moveList.containsKey(move)) {
			moveList.put(move, moveList.get(move) + pointCount);
		}
		else {
			moveList.put(move, pointCount);
		}
	}
	
	/**
	 * returns true if a move at (x,y) is legal, false otherwise.
	 * 
	 * @param x
	 * @param y
	 * @return true if a move at (x,y) is in the HashMap, false otherwise
	 */
	public boolean contains(int x, int y) {
		return moveList.containsKey(new ReversiMove(x, y));
	}
	
	
	/**
	 * computer's "AI". Finds the move with the highest point count and returns it.
	 * 
	 * @return the move in the HashMap worth the most points
	 */
	public ReversiMove bestMove() {
		int count = -1;
		ReversiMove most = null;
		for(ReversiMove move : moveList.keySet()) {
			if(moveList.get(move) > count) {
				count = moveList.get(move);
				most = move;
			}
		}
		
		return most;
	}

	
}
