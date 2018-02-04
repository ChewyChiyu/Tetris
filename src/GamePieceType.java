import java.awt.Color;
import java.awt.Point;

public enum GamePieceType {
	
	SQUARE(1,-1,Color.YELLOW,false, new Point[]{new Point(0,0),new Point(0,-1),new Point(-1,0),new Point(-1,-1) }), 
	LINE(2,-2,Color.BLUE,true, new Point[]{new Point(0,-1),new Point(0,0),new Point(0,-2),new Point(0,-3) }), 
	T(3,-3,Color.ORANGE,true, new Point[]{new Point(-1,0),new Point(0,0),new Point(1,0),new Point(0,-1) }), 
	L(4,-4,Color.RED,true, new Point[]{new Point(-1,0),new Point(-1,-1),new Point(0,0),new Point(1,0) }), 
	REVERSE_L(5,-5,Color.GREEN,true, new Point[]{new Point(-1,0),new Point(1,-1),new Point(0,0),new Point(1,0) }), 
	SQUIGLY(6,-6,Color.MAGENTA,true, new Point[]{new Point(0,0),new Point(-1,0),new Point(0,-1),new Point(1,-1) }), 
	REVERSE_SQUIGLY(7,-7,Color.GRAY,true, new Point[]{new Point(0,0),new Point(1,0),new Point(0,-1),new Point(-1,-1) });
	
	
	
	Color c;
	int id;
	int anchorId;
	boolean canRotate;
	Point[] map;
	private GamePieceType(int id, int anchorId, Color c, boolean canRotate, Point[] map){
		this.c = c;
		this.id = id;
		this.anchorId = anchorId;
		this.canRotate = canRotate;
		this.map = map;
	}
	
}
