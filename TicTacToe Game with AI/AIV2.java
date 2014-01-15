/**
 * Stefan Zimmerman
 * TicTacToe Game
 */
package game;

import java.util.List;

public class AIV2 extends AIPlayer{

	public AIV2(String n, char p, boolean s){
		super(n, p, s);
	}
	
	public AIV2(String n, char p){
		super(n, p, false);
	}
	
	/**
	 * AI V2
	 * This AI only looks at its current possible
	 * moves and tries to block the opponent as
	 * much as possible.
	 */
	@Override
	public Move getNextMove(State s){
		List<Move> validMoves = getValidMoves(s);
		return min(s, validMoves);
	}
	
	
	/**
	 * AI V2
	 * This AI computes its heuristic based on
	 * stopping its opponent from having too
	 * many pieces in a row. (Defense)
	 */
	@Override
	public int getHeurScore(State state, Move m){
		State s = state.copy();
		m.make(s);
		int max = 0;
		char[][] b = s.getBoard();
		char[][] r = Game.rotate(b);
		max = Math.max(max, getMaxRow(b, s.getTurn().getPiece()));
		max = Math.max(max, getMaxRow(r, s.getTurn().getPiece()));
		max = Math.max(max, getMaxDiag(b, s.getTurn().getPiece()));
		max = Math.max(max, getMaxDiag(r, s.getTurn().getPiece()));
		return max;
	}
	
	private int getMaxRow(char[][] board, char p){
		int max = 0;
		int cur = 0;
		for(char[] row : board){
			cur = 0;
			for(char c : row){
				if(c == p){
					cur++;
				}else if(c != ' '){
					cur = 0;
					break;
				}
			}
			max = Math.max(max, cur);
		}
		return max;
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
