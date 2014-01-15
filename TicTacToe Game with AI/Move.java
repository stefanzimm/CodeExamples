/**
 * Stefan Zimmerman
 * TicTacToe Game
 */
package game;

public class Move {
	
	private Player player;
	private int row;
	private int col;
	
	public Move(Player p, int r, int c){
		this.player = p;
		this.row = r;
		this.col = c;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public void setPlayer(Player p){
		this.player = p;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getCol(){
		return col;
	}
	
	public boolean isValid(State s){
		return s.getBoard()[row][col] == ' ';
	}
	
	public void make(State s){
		s.getBoard()[row][col] = s.getTurn().getPiece();
		s.changeTurn();
	}
	
	public boolean isCorner(State s){
		return ((row == 0 && col == 0) ||
				(row == 0 && col == s.getBoard().length - 1) ||
				(row == s.getBoard().length - 1 && col == 0) ||
				(row == s.getBoard().length - 1 && col == s.getBoard().length - 1));
	}
	
	public boolean isSide(State s){
		return ((row == 0 && col > 0 && col < s.getBoard().length - 1) ||
				(row == s.getBoard().length - 1 && col > 0 && col < s.getBoard().length - 1) ||
				(row > 0 && row < s.getBoard().length - 1 && col == 0) ||
				(row > 0 && row < s.getBoard().length - 1 && col == s.getBoard().length - 1));
	}
	
	public String toString(){
		return "Move:" + row + "," + col;
	}
}
