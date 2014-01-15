/**
 * Stefan Zimmerman
 * TicTacToe Game
 */
package game;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {
	
	private State curState;
	private Player winner;
	private ArrayList<Player> players = new ArrayList<Player>();
	
	public static final char[][] START_BOARD = {{' ',' ',' '},
											 	{' ',' ',' '},
											 	{' ',' ',' '}};
	
	private Random rand = new Random();
	
	public Game(Player p1, Player p2){
		curState = new State(START_BOARD, p1, p2).copy();
		if(rand.nextInt(2) == 0){
			curState.changeTurn();
		}
		players.add(p1);
		players.add(p2);
	}
	
	public Player getCurPlayer(){
		return curState.getTurn();
	}
	
	public State getCurState(){
		return curState;
	}
	/**
	 * returns true if the game is over
	 */
	public boolean isEnd(){
		return curState.isEnd() || getWinner() != null;
	}
	
	/**
	 * returns the winner if there is one
	 */
	public Player getWinner(){
		if(winner == null){
			char[][] board = curState.getBoard();
			if(getMaxLine(board, curState.getTurn()) == board.length){
				winner = curState.getTurn();
				return winner;
			}else if(getMaxLine(board, curState.getNotTurn()) == board.length){
				winner = curState.getNotTurn();
			}
		}
		return winner;
	}
	
	/**
	 * updates the current state of the game
	 */
	public boolean update(){
		boolean moveMade = false;
		if(!isEnd() && getWinner() == null){
			Player p = curState.getTurn();
			Move m = p.getNextMove(curState);
			if(m != null && m.isValid(curState)){
				m.make(curState);
				moveMade = true;
			}
		}
		return moveMade;
	}
	
	/**
	 * return the max line found in any row, col, diagonal for the player
	 */
	public static int getMaxLine(char[][] b, Player p){
		boolean[][] tFBoard = getTFBoard(b, p);
		boolean[][] rTFBoard = getTFBoard(rotate(b), p);
		int maxLine = 0;
		maxLine = Math.max(maxLine, getMaxRow(tFBoard));
		maxLine = Math.max(maxLine, getMaxRow(rTFBoard));
		maxLine = Math.max(maxLine, getMaxDiag(tFBoard));
		maxLine = Math.max(maxLine, getMaxDiag(rTFBoard));
		return maxLine;
	}
	
	/**
	 * returns a board where every space that belongs to the player is true
	 * and all other spaces are false
	 */
	public static boolean[][] getTFBoard(char[][] b, Player p){
		boolean[][] board = new boolean[b.length][b[0].length];
		for(int row = 0; row < b.length; row++){
			for(int col = 0; col < b[row].length; col++){
				board[row][col] = b[row][col] == p.getPiece();
			}
		}
		return board;
	}
	
	/**
	 * rotates a board by 90 degrees
	 */
	public static char[][] rotate(char[][] board){
		char[][] result = new char[board.length][board[0].length];
		for(int row = 0; row < board.length; row++){
			for(int col = 0; col < board[row].length; col++){
				result[col][board.length - row - 1] = board[row][col];
			}
		}
		return result;
	}
	
	/**
	 * returns the max row
	 */
	public static int getMaxRow(boolean[][] board){
		int max = 0;
		for(boolean[] row : board){
			int cur = 0;
			for(boolean b : row){
				if(b){
					cur++;
				}
			}
			max = Math.max(max, cur);
		}
		return max;
	}
	
	/**
	 * returns the max diagonal
	 */
	public static int getMaxDiag(boolean[][] board){
		int cur = 0;
		for(int row = 0, col = 0; row < board.length && col < board.length; row++, col++){
			if(board[row][col]){
				cur++;
			}
		}
		return cur;
	}
	
	@SuppressWarnings("resource")
	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		String prompt = "Choose Player1 and Player2:\n   0)HumanPlayer\n   1)AIV1\n   2)AIV2\n   3)AIV3\n   4)AIV4";
		int p1 = -1;
		int p2 = -1;
		Player player1 = null;
		Player player2 = null;
		do{
			System.out.println(prompt);
			try{
				p1 = Integer.parseInt(scan.next());
				p2 = Integer.parseInt(scan.next());
			}catch(Exception ex){
			}
			if(p1 < 0 || p1 > 4 || p2 < 0 || p2 > 4){
				System.out.println("Not valid players");
			}else{
				switch(p1){
				case 1: player1 = new AIV1("1Player1", 'x');
						break;
				case 2: player1 = new AIV2("2Player1", 'x');
						break;
				case 3: player1 = new AIV3("3Player1", 'x');
						break;
				case 4: player1 = new AIV4("4Player1", 'x');
						break;
				default: player1 = new HumanPlayer("Player1", 'x');
						break;
				}
				switch(p2){
				case 1: player2 = new AIV1("1Player2", 'o');
						break;
				case 2: player2 = new AIV2("2Player2", 'o');
						break;
				case 3: player2 = new AIV3("3Player2", 'o');
						break;
				case 4: player2 = new AIV4("4Player2", 'o');
						break;
				default: player2 = new HumanPlayer("Player2", 'o');
						break;
				}
			}
		}while(player1 == null && player2 == null);
		Game game = new Game(player1, player2);
		game.getCurState().print();
		while(!game.isEnd()){
			game.update();
			game.getCurState().print();
		}
		if(game.getWinner() != null){
			System.out.println("Winner is " + game.getWinner() + "!");
		}else{
			System.out.println("No winner");
		}
	}
}
