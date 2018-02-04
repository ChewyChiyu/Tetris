import java.awt.Point;

public class GamePiece{

	GamePieceType type;
	Point anchor;
	int rotationIndex = 0;
	boolean isCompleted = false;
	
	//map of piece
	Point[] map;
	
	public GamePiece(GamePieceType type, Point anchor){
		this.type = type;
		this.anchor = anchor;
		map = type.map;
	}	
}
