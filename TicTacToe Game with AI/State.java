/**
 * Stefan Zimmerman
 * TicTacToe Game
 */
package game;

public class State{
	
	private Player turn;
	private Player notTurn;
	
	private char[][] board;
	
	public State(char[][] b, Player t, Player n){
		this.board = b;
		this.turn = t;
		this.notTurn = n;
	}
	
	public char[][] getBoard(){
		return board;
	}
	
	public Player getTurn(){
		return turn;
	}
	
	public Player getNotTurn(){
		return notTurn;
	}
	
	public void changeTurn(){
		Player temp = turn;
		turn = notTurn;
		notTurn = temp;
	}
	
	/**
	 * determines if the game has ended
	 */
	public boolean isEnd(){
		boolean end = true;
		for(char[] row : board){
			for(char p : row){
				if(p == ' '){
					end = false;
				}
			}
		}
		return end;
	}
	
	/**
	 * prints a visual representation of the board
	 */
	public void print(){
		System.out.println(turn + "'s turn");
		String r = "";
		for(char[] row : board){
			r = "|";
			for(char p : row){
				r += p + "|";
			}
			System.out.println(r);
		}
	}
	
	/**
	 * creates a copy of the state
	 */
	public State copy(){
		char[][] newBoard = new char[board.length][board[0].length];
		for(int row = 0; row < board.length; row++){
			for(int col = 0; col < board[row].length; col++){
				newBoard[row][col] = board[row][col];
			}
		}
		return new State(newBoard, turn, notTurn);
	}
}