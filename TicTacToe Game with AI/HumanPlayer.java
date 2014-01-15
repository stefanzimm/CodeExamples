/**
 * Stefan Zimmerman
 * TicTacToe Game
 */
package game;

import java.util.Scanner;

public class HumanPlayer extends Player{

	private int selRow;
	private int selCol;
	
	public HumanPlayer(String n, char p){
		super(n, p);
	}
	
	@SuppressWarnings("resource")
	@Override
	public Move getNextMove(State s){
		Scanner scan = new Scanner(System.in);
		Move m = null;
		do{
			System.out.println("Choose your move");
			try{
				selRow = Integer.parseInt(scan.next());
				selCol = Integer.parseInt(scan.next());
			}catch(Exception ex){
			}
			if(selRow >= s.getBoard().length || selCol >= s.getBoard().length || selRow < 0 || selCol < 0){
				System.out.println("Out of bounds!");
			}else if(s.getBoard()[selRow][selCol] != ' '){
				System.out.println("Not a valid move");
			}else{
				m = new Move(this, selRow, selCol);
			}
		}while(m == null);
		return m;
	}
}
