import java.awt.Dimension;
import java.awt.Font;
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

	int score;
	
	

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
		getInputMap().put(KeyStroke.getKeyStroke("W"), "W");
		
		getActionMap().put("W", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				gameBoard.rotate();
			}

		});
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
				worldTick = 0; //reseting tick
			    gameBoard.shiftDown();
			     
			}
		}
		repaint();
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setFont(new Font("Aerial",Font.BOLD,20));
		g.drawString("Score: " + score, screenDim.width / 4, screenDim.height / 20);
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
		gameBoard = new Grid(10,24,20,new Point((int)screenDim.getWidth() / 6 , (int)screenDim.getHeight() / 10), this);
		nodes.add(gameBoard);
		gameBoard.summonPiece();
	}


}
