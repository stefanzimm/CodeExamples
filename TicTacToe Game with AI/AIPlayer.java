/**
 * Stefan Zimmerman
 * TicTacToe Game
 */
package game;

import java.util.ArrayList;
import java.util.List;

public abstract class AIPlayer extends Player{
	
	public boolean show = false;

	public AIPlayer(String n, char p, boolean s){
		super(n, p);
		this.show = s;
	}
	
	/**
	 * gets all valid moves
	 */
	public List<Move> getValidMoves(State s){
		List<Move> validMoves = new ArrayList<Move>();
		for(int i = 0; i < s.getBoard().length; i++){
			for(int j = 0; j < s.getBoard()[i].length; j++){
				Move m = new Move(this, i, j);
				if(m.isValid(s)){
					validMoves.add(m);
				}
			}
		}
		return validMoves;
	}
	
	/**
	 * Returns a random min heuristic move.
	 */
	public Move min(State s, List<Move> moves){
		List<Move> goodMoves = new ArrayList<Move>();
		int min = Integer.MAX_VALUE;
		int score = 0;
		for(Move m : moves){
			score = getHeurScore(s, m);
			if(show){
				System.out.println("Heur = " + score);   //======
				System.out.println(m.toString() + "\n"); //======
			}
			if(score < min){
				min = score;
				goodMoves.clear();
			}
			if(score == min){
				goodMoves.add(m);
			}
		}
		if(goodMoves.isEmpty()){
			return null;
		}else{
			return goodMoves.get((int)(Math.random() * goodMoves.size()));
		}
	}
	
	/**
	 * Returns a random max heuristic move.
	 */
	public Move max(State s, List<Move> moves){
		List<Move> goodMoves = new ArrayList<Move>();
		int max = Integer.MIN_VALUE;
		int score = 0;
		for(Move m : moves){
			score = getHeurScore(s, m);
			if(show){
				System.out.println("Heur = " + score);   //======
				System.out.println(m.toString() + "\n"); //======
			}
			if(score > max){
				max = score;
				goodMoves.clear();
			}
			if(score == max){
				goodMoves.add(m);
			}
		}
		if(goodMoves.isEmpty()){
			return null;
		}else{
			return goodMoves.get((int)(Math.random() * goodMoves.size()));
		}
	}
	
	/**
	 * gets the next move
	 */
	public abstract Move getNextMove(State s);
	
	/**
	 * computes the Heuristic score for a state
	 */
	public abstract int getHeurScore(State s, Move m);
}
