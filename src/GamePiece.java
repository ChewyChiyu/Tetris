import java.awt.Point;

public class GamePiece{

	GamePieceType type;
	Point anchor;
	int rotationIndex = 0;
	boolean isCurre = false;
	
	
	//map of piece
	Point[] map;
	
	public GamePiece(GamePieceType type, Point anchor){
		this.type = type;
		this.anchor = anchor;
		map = type.map;
	}	
	
	//checking to see if point on grid is self
	boolean isSelf(Point pos){
		for(int mapCount = 0; mapCount < map.length; mapCount++){
			int row =  this.anchor.y  + ( this.map[mapCount].y );
			int col =  this.anchor.x  + ( this.map[mapCount].x );
			if(row == pos.x && col == pos.y){
				return true;
			}
		}
		return false;
	}
	
}
