/**
 * Stefan Zimmerman
 * TicTacToe Game
 */
package game;

public abstract class Player {

	private String name;
	private char piece;
	
	public Player(String n, char p){
		this.name = n;
		this.piece = p;
	}
	
	public char getPiece(){
		return piece;
	}
	
	public String toString(){
		return name + "(" + Character.toString(piece) + ")";
	}
	
	public abstract Move getNextMove(State state);
}