/**
 * Stefan Zimmerman
 * TicTacToe Game
 */
package game;

import java.util.List;

public class AIV3 extends AIPlayer{

	public AIV3(String n, char p, boolean s){
		super(n, p, s);
	}
	
	public AIV3(String n, char p){
		super(n, p, false);
	}
	
	/**
	 * AI V3
	 * This AI only looks at its current possible
	 * moves and chooses when to be offensive or
	 * defensive. (Offense/Defense)
	 */
	@Override
	public Move getNextMove(State s){
		List<Move> validMoves = getValidMoves(s);
		return max(s, validMoves);
	}
	
	/**
	 * Decides if the AI should block
	 * its opponent.
	 */
	public boolean shouldBlock(State s){
		int i = getMaxLine(s, s.getNotTurn());
		return (i == s.getBoard().length - 1 && !canWin(s));
	}
	
	/**
	 * Decides if the AI can win
	 * in one move.
	 */
	public boolean canWin(State s){
		int i = getMaxLine(s, s.getTurn());
		return i == s.getBoard().length - 1;
	}
	
	/**
	 * Returns the max uninterrupted line of a player
	 */
	public int getMaxLine(State s, Player p){
		int max = 0;
		char[][] b = s.getBoard();
		char[][] r = Game.rotate(b);
		for(int row = 0; row < b.length; row++){
			max = Math.max(max, getMaxRow1(b, row, p.getPiece()));
			max = Math.max(max, getMaxRow1(r, row, p.getPiece()));
		}
		max = Math.max(max, getMaxDiag1(b, p.getPiece()));
		max = Math.max(max, getMaxDiag1(r, p.getPiece()));
		return max;
	}
	
	/**
	 *  (Offense)
	 * This AI computes its heuristic based on
	 * the maximum number of pieces it can have
	 * in a row.
	 * 
	 *  (Defense)
	 * This AI computes its heuristic based on
	 * stopping his opponent from winning.
	 */
	@Override
	public int getHeurScore(State s, Move m){
		if(shouldBlock(s)){ // Defense
			return defenseHeur(s, m, s.getNotTurn().getPiece());
		}else{              // Offense
			return offenseHeur(s, m, this.getPiece());
		}
	}
	
	/**
	 * Offense
	 */
	public int offenseHeur(State state, Move m, char c){
		State s = state.copy();
		m.make(s);
		int max = 0;
		int row = m.getRow();
		int col = m.getCol();
		char[][] b = s.getBoard();
		char[][] r = Game.rotate(b);
		max = Math.max(max, getMaxRow1(b, row, c));
		max = Math.max(max, getMaxRow1(r, col, c));
		if(row == col){
			max = Math.max(max, getMaxDiag1(b, c));
		}
		if(b.length - row - 1 == col){
			max = Math.max(max, getMaxDiag1(r, c));
		}
		return max;
	}
	
	/**
	 * Offense
	 */
	public int getMaxRow1(char[][] board, int row, char p){
		int cur = 0;
		for(int col = 0; col < board.length; col++){
			if(board[row][col] == p){
				cur++;
			}else if(board[row][col] != ' '){
				cur = 0;
				break;
			}
		}
		return cur;
	}
	
	/**
	 * Offense
	 */
	public int getMaxDiag1(char[][] board, char p){
		int cur = 0;
		for(int i = 0; i < board.length; i++){
			if(board[i][i] == p){
				cur++;
			}else if(board[i][i] != ' '){
				cur = 0;
				break;
			}
		}
		return cur;
	}
	
	/**
	 * Defense
	 */
	public int defenseHeur(State state, Move m, char c){
		State s = state.copy();
		m.make(s);
		int max = 0;
		int row = m.getRow();
		int col = m.getCol();
		char[][] b = s.getBoard();
		char[][] r = Game.rotate(b);
		max = Math.max(max, getMaxRow2(b, row, c));
		max = Math.max(max, getMaxRow2(r, col, c));
		if(row == col){
			max = Math.max(max, getMaxDiag2(b, c));
		}
		if(b.length - row - 1 == col){
			max = Math.max(max, getMaxDiag2(r, c));
		}
		return max;
	}
	
	/**
	 * Defense
	 */
	public int getMaxRow2(char[][] board, int row, char p){
		int cur = 0;
		for(int col = 0; col < board.length; col++){
			if(board[row][col] == p){
				cur++;
			}
		}
		return cur;
	}
	
	/**
	 * Defense
	 */
	public int getMaxDiag2(char[][] board, char p){
		int cur = 0;
		for(int i = 0; i < board.length; i++){
			if(board[i][i] == p){
				cur++;
			}
		}
		return cur;
	}
}