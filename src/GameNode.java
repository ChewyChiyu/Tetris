import java.awt.Graphics;
import java.awt.Point;

public abstract class GameNode{
	
	Point location;
	abstract void draw(Graphics g);
	
	public GameNode(Point location){
		this.location = location;
	}
	
}
