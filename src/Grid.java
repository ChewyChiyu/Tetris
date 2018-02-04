import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

public class Grid extends GameNode{

	int[][] gameBoard;

	final int SPACER;
	//the zone where pieces spawn
	final int SPAWN_BUFFER = 10;
	final Point spawnPoint;
	GamePiece currentPiece;


	public Grid(int col, int row, int spacer, Point location){
		super(location);
		gameBoard = new int[row + SPAWN_BUFFER][col];
		SPACER = spacer;
		spawnPoint = new Point(row / 5, 5);
	}


	//check if can rotate, if can then do it
	void rotate(){		
		//Mock rotation for bounds test
		cleanPrev(); //clear previous first
		for(int mapCount = 0; mapCount < currentPiece.map.length; mapCount++){
			Point test = new Point(-currentPiece.map[mapCount].y,currentPiece.map[mapCount].x);
			int rowTest =  currentPiece.anchor.y  + ( test.y );
			int colTest =  currentPiece.anchor.x  + ( test.x );
			if(colTest < 0 || colTest > gameBoard[0].length-1 || rowTest > gameBoard.length-1){ //testing bounds x and y
				updateBoard();
				return;
			}
			if(gameBoard[rowTest][colTest] != 0){ //not empty space
				if(!currentPiece.isSelf(test)){
					updateBoard();
					return;
				}
			}
		}		

		//if reaches this far . . . rotation is a go
		currentPiece.rotate();	
		updateBoard();
	}

	//basic game logic, shifting the pieces down by one 
	void shiftDown(){
		cleanPrev(); //cleaning previous move
		currentPiece.anchor.y++; //move currentPiece down
		updateBoard();
		checkForCompletion(); //checking for completion
	}

	void shiftRight(int index){
		checkForCompletion(); //checking for completion
		cleanPrev(); //cleaning previous move
		for(int mapCount = 0; mapCount < currentPiece.map.length; mapCount++){
			int col =  currentPiece.anchor.x  + ( currentPiece.map[mapCount].x );
			int row =  currentPiece.anchor.y  + ( currentPiece.map[mapCount].y );
			if(col + index < 0 || col + index > gameBoard[0].length-1){ //out of bounds
				updateBoard();
				return;
			}
			if(gameBoard[row][col + index] != 0){ //into another block
				updateBoard();
				return;
			}

		} //if it made it this far then move the block to index
		currentPiece.anchor.x+=index;
		updateBoard();
	}

	//checking if pieces or game is at completion
	void checkForCompletion(){
		//checking if piece is at completion, not gonna move
		//block guard
		for(int mapCount = 0; mapCount < currentPiece.map.length; mapCount++){
			int row =  currentPiece.anchor.y  + ( currentPiece.map[mapCount].y );
			int col =  currentPiece.anchor.x  + ( currentPiece.map[mapCount].x );
			//next board slot is another block
			if(row + 1 == gameBoard.length){ // there is another block already there
				summonPiece();
				return;
			}
			if((gameBoard[row+1][col] != 0 && !currentPiece.isSelf(new Point(row+1,col)))){
				summonPiece();
				return;
			}
		}


	}

	//check if there is a row made 
	int checkForRow(){ //if the row is all != 0 then there is a row
		boolean[] completedRows = new boolean[gameBoard.length];
		int scoreIndex = 0;
		for(int row = 0; row < gameBoard.length; row++){
			completedRows[row] = true;
			for(int col = 0; col < gameBoard[0].length; col++){
				if(gameBoard[row][col] == 0) {  completedRows[row]  = false; } // no row completed	
			}
		}
		//removing rows completed
		for(int row = 0; row < gameBoard.length; row++){
			if(completedRows[row]){
				for(int col = 0; col < gameBoard[0].length; col++){
					gameBoard[row][col] = 0;
				}
			}
		}
		//shifting down all rows
		for(int completedRowCount = 0; completedRowCount < completedRows.length; completedRowCount++){
			if(completedRows[completedRowCount]){ // move down one index for every row removed
				
				scoreIndex++; //increasing the score index
				
				for(int row = completedRowCount; row > 0; row--){
					for(int col = 0; col < gameBoard[0].length; col++){
						if(!currentPiece.isSelf(new Point(row,col))){ //moving everything but current piece
							gameBoard[row][col] = gameBoard[row-1][col];
						}
					}
				}
			}
		}
		updateBoard(); //updating board
		return scoreIndex; //returning score
	
	}

	void summonPiece(){
		//spawning in new piece
		currentPiece = makePiece();
	}

	//making random game piece
	GamePiece makePiece(){
		GamePieceType randPieceType = GamePieceType.L;
		switch((int)(Math.random()*7)){
		case 0:
			randPieceType = GamePieceType.L;
			break;
		case 1:
			randPieceType = GamePieceType.LINE;
			break;
		case 2:
			randPieceType = GamePieceType.REVERSE_L;
			break;
		case 3:
			randPieceType = GamePieceType.REVERSE_SQUIGLY;
			break;
		case 4:
			randPieceType = GamePieceType.SQUARE;
			break;
		case 5:
			randPieceType = GamePieceType.SQUIGLY;
			break;
		case 6:
			randPieceType = GamePieceType.T;
			break;
		}
		return new GamePiece(randPieceType,new Point(spawnPoint));

	}

	//Translating piece to gameBoard array
	void updateBoard(){

		for(int mapCount = 0; mapCount < currentPiece.map.length; mapCount++){

			//finding locaiton of piece
			int col =  currentPiece.anchor.x  + ( currentPiece.map[mapCount].x );
			int row =  currentPiece.anchor.y  + ( currentPiece.map[mapCount].y );
			gameBoard[row][col] = currentPiece.type.id;

			//master block decal
			if(currentPiece.map[mapCount].x == 0 && currentPiece.map[mapCount].y == 0){
				gameBoard[row][col] = currentPiece.type.anchorId;
			}

		}
	}

	//cleaning up path of previous move
	void cleanPrev(){
		for(int mapCount = 0; mapCount < currentPiece.map.length; mapCount++){
			//finding locaiton of piece
			int col =  currentPiece.anchor.x  + ( currentPiece.map[mapCount].x );
			int row =  currentPiece.anchor.y  + ( currentPiece.map[mapCount].y );
			gameBoard[row][col] = 0;
		}
	}

	//clearing the board
	void clearBoard(){
		for(int row = 0; row < gameBoard.length; row++){
			for(int col = 0; col < gameBoard[0].length; col++){
				gameBoard[row][col] = 0;
			}
		}
	}

	void draw(Graphics g){
		g.setFont(new Font("Aerial",Font.BOLD,11));
		//drawing empty grid 
		int xBuffer = location.x;
		int yBuffer = location.y;
		for(int row = SPAWN_BUFFER; row < gameBoard.length; row++){
			for(int col = 0;  col < gameBoard[0].length; col++){
				g.setColor(Color.BLACK);
				//outlining grids
				g.drawRect(xBuffer, yBuffer, SPACER, SPACER);

				g.setColor(Color.WHITE);
				//drawing grid pieces
				int gridId = Math.abs(gameBoard[row][col]);
				if(gridId == GamePieceType.L.id){
					g.setColor(GamePieceType.L.c);
				}else if(gridId == GamePieceType.LINE.id){
					g.setColor(GamePieceType.LINE.c);
				}else if(gridId == GamePieceType.REVERSE_L.id){
					g.setColor(GamePieceType.REVERSE_L.c);
				}else if(gridId == GamePieceType.REVERSE_SQUIGLY.id){
					g.setColor(GamePieceType.REVERSE_SQUIGLY.c);
				}else if(gridId == GamePieceType.SQUARE.id){
					g.setColor(GamePieceType.SQUARE.c);
				}else if(gridId == GamePieceType.SQUIGLY.id){
					g.setColor(GamePieceType.SQUIGLY.c);
				}else if(gridId == GamePieceType.T.id){
					g.setColor(GamePieceType.T.c);
				}

				//filling grid piece
				g.fillRect(xBuffer, yBuffer, SPACER, SPACER);

				g.setColor(Color.black);
				if(gameBoard[row][col] < 0){ // anchor piece
					g.drawString("M", xBuffer + SPACER / 4, yBuffer + SPACER /2);
				}



				//increasing xBuffer
				xBuffer += SPACER;


			}
			//reseting xBuffer
			xBuffer = location.x;
			//increasing yBuffer
			yBuffer += SPACER;
		}



	}

}
