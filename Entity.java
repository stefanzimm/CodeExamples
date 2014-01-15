package theLostElement.entity;

import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
import theLostElement.map.Map;

public abstract class Entity{

	public boolean isInAir;
	public boolean jumping;
	public boolean dashing;
	
	public int hp = 0;
	public int maxHp = 0;
	
	public Map map;

	protected FloatBuffer vertBuf;
	protected float[] vert;
	
	public float x;
	public float y;
	
	public float sx = 0.0f; //size of entity
	public float sy = 0.0f; //size of entity
	
	public float pdx = 0.0f; //past dx
	public float ldx = 0.1f; //last dx (to handle facing direction when dx == 0)
	public float dx = 0.0f;
	public float dy = 0.0f;
	
	public Entity(float x, float y, Map m){
		this.x = x;
		this.y = y;
		this.map = m;
	}
	
	public void draw(GL10 gl){}
	
	/**
	 * Changes the velocity of the entity by its surroundings.
	 * Handles all acceleration and collisions with walls.
	 */
	public void updateVelocity(){
		if(dashing){ //Dashing is an ability that causes the entity to dash to a side.
			dx -= Math.signum(dx) * 0.005f;
			if(Math.abs(dx) < 0.15f){
				dashing = false;
				dx = 0;
			}
		}else{
			dx = pdx;
		}
		
		//Check Left wall collision
		boolean stop = false;
		for(int by = (int)(-(y + sy)) / 2; by <= (int)(-((y - sy) + 0.00001f)) / 2; by++){
			if(dashing){
				stop = stop || !map.map[by][(int)((x - sx) + dx) / 2].canPass; //The map contains blocks that have and attibute canPass
			}else{
				stop = stop || !map.map[by][(int)((x - sx) + (Math.signum(dx) * 0.150f)) / 2].canPass;
			}
		}
		if(stop){
			x -= (((x - sx) % 1.0000f) - 0.00001f);
			dx = 0;
		}
		
		//Check Right wall collision
		stop = false;
		for(int by = (int)(-(y + sy)) / 2; by <= (int)(-((y - sy) + 0.00001f)) / 2; by++){
			if(dashing){
				stop = stop || !map.map[by][(int)((x + sx) + dx) / 2].canPass;
			}else{
				stop = stop || !map.map[by][(int)((x + sx) + (Math.signum(dx) * 0.150f)) / 2].canPass;
			}
		}
		if(stop){
			x += (0.99999f - ((x + sx) % 1.0000f));
			dx = 0;
		}
		x += dx;
		if(dx != 0)
			ldx = dx;
		if(isInAir){
			if(!dashing){
				if(dy > -0.6f){
					dy -= 0.012f;
				}
			}else{
				if(dy > -0.6f){
					dy -= 0.006f;
				}
			}
			
			//Check roof collision
			stop = false;
			for(int bx = (int)(x - sx) / 2; bx <= (int)(x + sx) / 2; bx++){
				stop = stop || !map.map[(int)(-(y + dy + sy)) / 2][bx].canPass;
			}
			if(stop){
				y -= (((y + sy) % 1.0000f) + 0.00001f);
				dy = 0;
			}
			
			//Check floor collision
			stop = false;
			for(int bx = (int)(x - sx) / 2; bx <= (int)(x + sx) / 2; bx++){
				stop = stop || !map.map[(int)(-((y + dy) - sy)) / 2][bx].canPass;
			}
			if(stop){
				isInAir = false;
				y -= (0.99999f + ((y - sy) % 1.0000f));
				dy = 0;
			}
		}else{
			
			//Check if walked off edge
			stop = false;
			for(int bx = (int)(x - sx) / 2; bx <= (int)(x + sx) / 2; bx++){
				stop = stop || !map.map[(int)(-((y + dy) - (sy + 0.005f))) / 2][bx].canPass;
			}
			if(!stop){
				isInAir = true;
			}
		}
		y += dy;
	}
}
