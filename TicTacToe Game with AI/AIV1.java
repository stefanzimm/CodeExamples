/**
 * Stefan Zimmerman
 * TicTacToe Game
 */
package game;

import java.util.List;

public class AIV1 extends AIPlayer{

	public AIV1(String n, char p, boolean s){
		super(n, p, s);
	}
	
	public AIV1(String n, char p){
		super(n, p, false);
	}
	
	/**
	 * AI V1
	 * This AI only looks at its current possible
	 * moves and tries to win as fast as possible.
	 */
	@Override
	public Move getNextMove(State s){
		List<Move> validMoves = getValidMoves(s);
		return max(s, validMoves);
	}
	
	
	/**
	 * AI V1
	 * This AI computes its heuristic based on
	 * the maximum number of pieces it can have
	 * in a row. (Offense)
	 */
	@Override
	public int getHeurScore(State state, Move m){
		State s = state.copy();
		m.make(s);
		int max = 0;
		int row = m.getRow();
		int col = m.getCol();
		char[][] b = s.getBoard();
		char[][] r = Game.rotate(b);
		max = Math.max(max, getMaxRow(b, row, this.getPiece()));
		max = Math.max(max, getMaxRow(r, col, this.getPiece()));
		if(row == col){
			max = Math.max(max, getMaxDiag(b, this.getPiece()));
		}
		if(b.length - row - 1 == col){
			max = Math.max(max, getMaxDiag(r, this.getPiece()));
		}
		return max;
	}
	
	private int getMaxRow(char[][] board, int row, char p){
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
	
	private int getMaxDiag(char[][] board, char p){
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
}
