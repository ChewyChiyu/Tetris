import java.awt.Dimension;

public class TetrisLauncher {
	
	public static void main(String[] args){
		new TetrisLauncher();
	}
	
	TetrisLauncher(){
		new GameWindow(new Dimension(320,650));
	}
	
}
