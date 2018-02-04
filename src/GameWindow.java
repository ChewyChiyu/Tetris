import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class GameWindow extends JPanel{

	Dimension screenDim;

	Thread gameEngine;
	Runnable gameLoop;
	boolean isRunning;

	boolean isPressingDown = false;

	final int WORLD_DELAY = 100;
	int worldTick = 0;

	Grid gameBoard;

	ArrayList<GameNode> nodes = new ArrayList<GameNode>();


	public GameWindow(Dimension d){
		screenDim = d;
		panel();
		start();
		keys();
	}

	void keys(){
		getInputMap().put(KeyStroke.getKeyStroke("D"), "D");
		getInputMap().put(KeyStroke.getKeyStroke("A"), "A");
		getInputMap().put(KeyStroke.getKeyStroke("S"), "S");
		getInputMap().put(KeyStroke.getKeyStroke("released S"), "rS");

		getActionMap().put("A", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				gameBoard.shiftRight(-1);
			}

		});
		getActionMap().put("D", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				gameBoard.shiftRight(1);
			}

		});
		getActionMap().put("S", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				isPressingDown = true;
				gameBoard.shiftDown();
			}

		});

		getActionMap().put("rS", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				isPressingDown = false;
			}

		});
	}

	synchronized void start(){
		gameLoop = () -> gameLoop();
		gameEngine = new Thread(gameLoop);
		isRunning = true;
		gameEngine.start();
	}


	void gameLoop(){
		while(isRunning){
			update();
			try{ Thread.sleep(WORLD_DELAY); } catch(Exception e) { e.printStackTrace(); }
		}
	}

	void update(){
		//shifting down gameBoard
		if(!isPressingDown){
			if(worldTick++ == 5){ // every half sec auto shift down
				gameBoard.shiftDown();
				gameBoard.checkForRow();
				worldTick = 0;
			}
		}
		gameBoard.updateBoard(); //updating pos of piece to array
		repaint();
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		for(int nodeCount = 0; nodeCount < nodes.size(); nodeCount++){
			GameNode node = nodes.get(nodeCount);
			node.draw(g);
		}
	}


	void panel(){
		JFrame frame = new JFrame();
		frame.add(this);
		frame.setPreferredSize(screenDim);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		screenElements();
	}

	// This is where the score and timer and all that stuff is
	void screenElements(){
		gameBoard = new Grid(10,24,20,new Point((int)screenDim.getWidth() / 6 , (int)screenDim.getHeight() / 10));
		nodes.add(gameBoard);
		gameBoard.summonPiece();
	}


}
