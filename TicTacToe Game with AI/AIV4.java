/**
 * Stefan Zimmerman
 * TicTacToe Game
 */
package game;

import java.util.List;

public class AIV4 extends AIV3{
	
	public boolean isFirstMove = true;
	
	public AIV4(String n, char p, boolean s){
		super(n, p, s);
	}
	
	public AIV4(String n, char p){
		super(n, p, false);
	}
	
	@Override
	public Move getNextMove(State s){
		List<Move> validMoves = getValidMoves(s);
		if(isFirstMove){
			isFirstMove = false;
			return getFirstMove(s);
		}
		if(show){
			if(canWin(s)){
				System.out.println("*Winning*");  //======
			}else if(shouldBlock(s)){
				System.out.println("*Blocking*"); //======
			}else{
				System.out.println("*Trapping*"); //======
			}
		}
		return max(s, validMoves);
	}
	
	/**
	 *  (Offense)
	 * This AI computes its heuristic based on
	 * trying to win as first priority and trap
	 * as third priority.
	 * 
	 *  (Defense)
	 * This AI computes its heuristic based on
	 * stopping his opponent from winning as
	 * second priority.
	 */
	@Override
	public int getHeurScore(State s, Move m){
		if(canWin(s)){            // Win
			return offenseHeur(s, m, this.getPiece());
		}else if(shouldBlock(s)){ // Block
			return defenseHeur(s, m, s.getNotTurn().getPiece());
		}else{                    // Try Trap
			return trapHeur(s, m, this.getPiece());
		}
	}
	
	public int trapHeur(State s, Move m, char c){
		if(isTrap(s, m, c)){ // Try Trap
			return s.getBoard().length + 2;
		}else{               // Try Bait
			return checkBait(s, m, c);
		}
	}
	
	/**
	 * Checks to see if the given move is a trap.
	 */
	public boolean isTrap(State state, Move m, char c){
		State s = state.copy();
		m.make(s);
		int winable = 0;
		char[][] r = Game.rotate(s.getBoard());
		winable += (getMaxRow1(s.getBoard(), m.getRow(), c) == s.getBoard().length - 1) ? 1 : 0;
		winable += (getMaxRow1(r, m.getCol(), c) == s.getBoard().length - 1) ? 1 : 0;
		if(m.getRow() == m.getCol()){
			winable += (getMaxDiag1(s.getBoard(), c) == s.getBoard().length - 1) ? 1 : 0;
		}
		if(s.getBoard().length - m.getRow() - 1 == m.getCol()){
			winable += (getMaxDiag1(r, c) == s.getBoard().length - 1) ? 1 : 0;
		}
		return winable > 1;
	}
	
	/**
	 * Checks to see if the given move is bait
	 * for a trap.
	 */
	public int checkBait(State state, Move m, char c){
		State s = state.copy();
		m.make(s);
		int rc = findFill(s, m, c);
		if(rc == -1){
			return checkSet(state, m, c);
		}
		int col = rc % 100;
		int row = (rc - col) / 100;
		Move m1 = new Move(s.getTurn(), row, col);
		if(isTrap(s, m1, s.getTurn().getPiece())){
			return -1;
		}else{
			int em = checkBait(s, m1, s.getTurn().getPiece());
			if(em == s.getBoard().length + 1){
				return -1;
			}else if(em == -1){
				return s.getBoard().length + 1;
			}
		}
		m1.make(s);
		if(!shouldBlock(s)){
			List<Move> validMoves = getValidMoves(s);
			for(Move move : validMoves){
				if(isTrap(s, move, s.getTurn().getPiece())){
					return s.getBoard().length + 1;
				}
			}
		}
		return offenseHeur(state, m, c);
	}
	
	/**
	 * Checks to see if the given move can set
	 * up a trap without being bait. And returns
	 * a heuristic depending on the chances of
	 * it causing a trap.
	 */
	public int checkSet(State state, Move move, char c){
		State s = state.copy();
		move.make(s);
		State s1;
		List<Move> moves = getValidMoves(s);
		List<Move> moves1;
		int trap = 0;
		int total = moves.size();
		for(Move m : moves){
			s1 = s.copy();
			m.setPlayer(s1.getTurn());
			m.make(s1);
			if(shouldBlock(s1)){
				int rc = findFill(s1, m, m.getPlayer().getPiece());
				Move m2 = new Move(s1.getTurn(), rc / 100, rc % 100);
				if(isTrap(s1, m2, s1.getTurn().getPiece())){
					trap++;
				}
			}else{
				moves1 = getValidMoves(s1);
				for(Move m1 : moves1){
					if(isTrap(s1, m1, s1.getTurn().getPiece())){
						trap++;
						break;
					}
				}
			}
		}
		float t1 = trap;
		float t2 = total;
		float r = 0;
		r = t1 / t2;
		return (r > 0.3) ? state.getBoard().length : offenseHeur(state, move, c);
	}
	
	/**
	 * Finds the coordinates of a block move.
	 */
	public int findFill(State state, Move m, char c){
		State s = state.copy();
		int row = m.getRow();
		int col = m.getCol();
		char[][] b = s.getBoard();
		char[][] r = Game.rotate(b);
		if((getMaxRow1(b, row, c) == s.getBoard().length - 1)){
			for(int i = 0; i < b.length; i++){
				if(b[row][i] == ' '){
					return (row * 100 + i);
				}
			}
		}else if((getMaxRow1(r, col, c) == s.getBoard().length - 1)){
			for(int i = 0; i < b.length; i++){
				if(b[i][col] == ' '){
					return (i * 100 + col);
				}
			}
		}else if((getMaxDiag1(b, c) == s.getBoard().length - 1)){
			for(int i = 0; i < b.length; i++){
				if(b[i][i] == ' '){
					return (i * 100 + i);
				}
			}
		}else if((getMaxDiag1(r, c) == s.getBoard().length - 1)){
			for(int i = 0; i < b.length; i++){
				if(b[b.length - i - 1][i] == ' '){
					return ((b.length - i - 1) * 100 + i);
				}
			}
		}
		return -1;
	}
	
	/**
	 * gets the first move for the AI
	 */
	public Move getFirstMove(State s){
		char c = ' ';
		Move m = null;
		for(int row = 0; row < s.getBoard().length; row++){
			for(int col = 0; col < s.getBoard()[row].length; col++){
				c = s.getBoard()[row][col];
				if(c != ' '){
					m = new Move(s.getNotTurn(), row, col);
					break;
				}
			}
		}
		if(m != null){
			if(m.isCorner(s) || m.isSide(s)){
				return new Move(this, (s.getBoard().length - 1) / 2, (s.getBoard().length - 1) / 2); //If they take side or corner take middle
			}else{
				return new Move(this, 0, 0);
			}
		}else{
			return new Move(this, 0, 0);//If they havent gone take corner
		}
	}
}