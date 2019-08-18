package chess;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import sun.applet.Main;
import sun.audio.*;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class V8 extends JPanel {
	private static Piece[][] pieces;
	private static Board board;
	private static Board aBoard;
	private final static int MOVE_LENGTH = 400;
	private final static int FPS = 30;
	private static int turn, movingX, movingY, kingX, kingY, animatingX, animatingY, konami, demote;
	private static int[][] captures = new int[2][6];
	private static boolean chosen, moving, check, showMove, gameOn = true;
	private static List<Board> boards = new ArrayList<Board>();
	private static List<Board> aBoards = new ArrayList<Board>(); //hey i made this
	private static int display;
	private static int AIColor = 1;
	private static boolean AIEnabled = false;
	private static int globalDepth = 4;
	private static String theme;
	private static V8 panel;
	private static JButton left, right, skipLeft, skipRight, undo;
	private static JLabel space;
	private static BufferedImage[][] pieceImages = new BufferedImage[3][6];
	private static BufferedImage[][] capturedPieces = new BufferedImage[2][6];
	private static BufferedImage whitePawn, whiteRook, whiteKnight, whiteBishop, whiteQueen, whiteKing;
	private static BufferedImage blackPawn, blackRook, blackKnight, blackBishop, blackQueen, blackKing;
	private static BufferedImage highlight, attacking, clicked, passant, checker;
	private static int moves = 0;

	//my main man 
	public static void main(String[] args){
		pieces = new Piece[10][10];
		turn = 0;
		newGame(board);

		//choose theme
		theme = "normal";
		JPanel pane = new JPanel();
		JRadioButton normal = new JRadioButton("Normal");
		JRadioButton sky = new JRadioButton("Sky");
		JRadioButton red = new JRadioButton("Red");
		normal.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(normal.isSelected()){
					theme = "normal";
					sky.setSelected(false);
					red.setSelected(false);
				}
				else{
					theme = "normal";
				}
			}
		});
		sky.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(sky.isSelected()){
					theme = "sky";
					normal.setSelected(false);
					red.setSelected(false);
				}
				else{
					theme = "normal";
				}

			}
		});
		red.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(red.isSelected()){
					theme = "red";
					normal.setSelected(false);
					sky.setSelected(false);
				}
				else{
					theme = "normal";
				}
			}
		});
		pane.add(normal);
		pane.add(sky);
		pane.add(red);
		JOptionPane.showMessageDialog(null, pane, "  Choose a Theme", JOptionPane.PLAIN_MESSAGE);
		JPanel pane1 = new JPanel();
		JRadioButton onePlayer = new JRadioButton ("1 Player");
		JRadioButton twoPlayer = new JRadioButton ("2 Players");
		onePlayer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(onePlayer.isSelected()){
					AIEnabled = true;
					twoPlayer.setSelected(false);
				}
				else{
					AIEnabled = false;
				}
			}
		});
		twoPlayer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(twoPlayer.isSelected()){
					AIEnabled = false;
					onePlayer.setSelected(false);
				}
				else{
					AIEnabled = false;
				}
			}
		});
		pane1.setLayout(new BoxLayout(pane1, BoxLayout.Y_AXIS));
		pane1.add(onePlayer);
		pane1.add(twoPlayer);
		JOptionPane.showMessageDialog(null, pane1, " How Many Players?", JOptionPane.PLAIN_MESSAGE);
		if(AIEnabled){
			JPanel pane2 = new JPanel();
			JRadioButton white = new JRadioButton("White");
			JRadioButton black = new JRadioButton("Black");
			pane2.setLayout(new BoxLayout(pane2, BoxLayout.Y_AXIS));
			white.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(white.isSelected()){
						AIColor = 1;
						black.setSelected(false);
					}
					else{
						AIColor = 1;
					}
				}
			});
			black.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(black.isSelected()){
						AIColor = 0;
						white.setSelected(false);
					}
					else{
						AIColor = 1;
					}
				}
			});
			pane2.add(white);
			pane2.add(black);
			JOptionPane.showMessageDialog(null, pane2, " Play as what color?", JOptionPane.PLAIN_MESSAGE);
		}

		//load my pictures and put them in an array
		try {
			whitePawn = ImageIO.read(Main.class.getResource("/"+theme+"/whitePawn.png"));
			pieceImages[0][0] = whitePawn;
			capturedPieces[0][0] = whitePawn;
			whiteRook = ImageIO.read(Main.class.getResource("/"+theme+"/whiteRook.png"));
			pieceImages[0][1] = whiteRook;
			capturedPieces[0][1] = whiteRook;
			whiteKnight = ImageIO.read(Main.class.getResource("/"+theme+"/whiteKnight.png"));
			pieceImages[0][2] = whiteKnight; 
			capturedPieces[0][2] = whiteKnight;
			whiteBishop = ImageIO.read(Main.class.getResource("/"+theme+"/whiteBishop.png"));
			pieceImages[0][3] = whiteBishop;
			capturedPieces[0][3] = whiteBishop;
			whiteQueen = ImageIO.read(Main.class.getResource("/"+theme+"/whiteQueen.png"));
			pieceImages[0][4] = whiteQueen;
			capturedPieces[0][4] = whiteQueen;
			whiteKing = ImageIO.read(Main.class.getResource("/"+theme+"/whiteKing.png"));
			pieceImages[0][5] = whiteKing;
			capturedPieces[0][5] = whiteKing;
			blackPawn = ImageIO.read(Main.class.getResource("/"+theme+"/blackPawn.png"));
			pieceImages[1][0] = blackPawn;
			capturedPieces[1][0] = blackPawn;
			blackRook = ImageIO.read(Main.class.getResource("/"+theme+"/blackRook.png"));
			pieceImages[1][1] = blackRook;
			capturedPieces[1][1] = blackRook;
			blackKnight = ImageIO.read(Main.class.getResource("/"+theme+"/blackKnight.png"));
			pieceImages[1][2] = blackKnight;
			capturedPieces[1][2] = blackKnight;
			blackBishop = ImageIO.read(Main.class.getResource("/"+theme+"/blackBishop.png"));
			pieceImages[1][3] = blackBishop;
			capturedPieces[1][3] = blackBishop;
			blackQueen = ImageIO.read(Main.class.getResource("/"+theme+"/blackQueen.png"));
			pieceImages[1][4] = blackQueen;
			capturedPieces[1][4] = blackQueen;
			blackKing = ImageIO.read(Main.class.getResource("/"+theme+"/blackKing.png"));
			pieceImages[1][5] = blackKing;
			capturedPieces[1][5] = blackKing;
			highlight = ImageIO.read(Main.class.getResource("/resources/circle.png"));
			attacking = ImageIO.read(Main.class.getResource("/resources/attackCircle.png"));
			clicked = ImageIO.read(Main.class.getResource("/resources/clicked.png"));
			passant = ImageIO.read(Main.class.getResource("/resources/redCircle.png"));
			checker = ImageIO.read(Main.class.getResource("/resources/checker.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		panel = new V8();
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 810));
		JFrame frame = new JFrame();
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame.setVisible(true);
				left = new JButton("<");
				right = new JButton(">");
				skipLeft = new JButton("|<");
				skipRight = new JButton(">|");
				undo = new JButton("Undo");
				space = new JLabel("   ");
				left.setFont(new Font("Calibri", Font.BOLD, 18));
				right.setFont(new Font("Calibri", Font.BOLD, 18));
				skipLeft.setFont(new Font("Calibri", Font.BOLD, 18));
				skipRight.setFont(new Font("Calibri", Font.BOLD, 18));
				undo.setFont(new Font("Calibri", Font.BOLD, 18));
				space.setFont(new Font("ARIAL", Font.BOLD, 18));
				left.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						showMove = true;
						if(display > 0){
							display--;
						}
						deselect(board);
						findChecker(board);
						panel.repaint();
					}
				});
				right.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						showMove = true;
						if(display < boards.size()-1){
							display++;
						}
						deselect(board);
						findChecker(board);
						panel.repaint();
					}
				});
				skipLeft.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						showMove = true;
						display = 0;
						deselect(board);
						findChecker(board);
						panel.repaint();
					}
				});
				skipRight.addActionListener(new ActionListener(){ 
					public void actionPerformed(ActionEvent e){
						showMove = true;
						display = boards.size()-1;
						deselect(board);
						findChecker(board);
						panel.repaint();
					}
				});
				undo.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						showMove = true;
						if(display != 0 && display == boards.size()-1){
							if(!AIEnabled){
								boards.remove(display);
								display--;
								board = duplicate(boards.get(display));
								int[] i = new int[] {1, 0};
								turn = i[turn];
								panel.repaint();
							}
							else{
								boards.remove(display);
								boards.remove(display-1);
								display -= 2;
								board = duplicate(boards.get(display));
								panel.repaint();
							}
						}
					}
				});
				panel.add(undo);
				panel.add(skipLeft);
				panel.add(left);
				panel.add(right);
				panel.add(skipRight);
				panel.add(space);
				frame.add(panel);
			}
		});
		frame.setDefaultCloseOperation(3);
		frame.setBounds(50,100, 1300,927);
		frame.setResizable(true);
		frame.setTitle("Chess Version 8.0");
		if(AIColor == 0){
			aBoard = duplicate(board);
			makeMove(board, alphaBeta(board, "", globalDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, AIColor), AIColor, true);
			changeTurn();
		}
		/*int r = 0;
		makeMove(board, "7252  F ", 0, false);
		makeMove(board, "2838 TF ", 1, false);
		makeMove(board, "5242    ", 0, false);
		String list = possibleMoves(board, 1);
		System.out.println(list.length()/8);
		for(int x = 0; x < list.length(); x+=8){
			String move = list.substring(x,x+8);
			makeMove(board,move,1,false);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			panel.repaint();
			System.out.println(move);
			String s = possibleMoves(board, 0);
			System.out.println(s);
			for(int y = 0; y < s.length(); y+=8){
				String f = s.substring(y,y+8);
				makeMove(board, f, 0, false);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				panel.repaint();
				undoMove(board, f, 0);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				panel.repaint();
			}

			undoMove(board,move,1);
		}
		System.out.println(r);
		//alphaBeta(board,"",2,-10000000,1000000,0);
		System.out.println(moves);*/

		/*alphaBeta(board, "", 4, -10000000,1000000, 0);
		System.out.println(moves);*/

		/*board.getPieces()[4][4].setPiece(board.getPieces()[7][1]);
		board.getPieces()[4][3].setPiece(board.getPieces()[2][3]);
		board.getPieces()[4][3].setTwoMove(true);
		board.setEnX(4);
		board.setEnY(3);
		String move = "4434 TF ";
		makeMove(board, move,0,false);
		undoMove(board, move, 0);
		String move1 = "7611RTFS";
		makeMove(board, move1, 0, false);
		undoMove(board, move1, 0);
		System.out.println(possibleMoves(board, 1));
		System.out.println(board.getEnX() + "," + board.getEnY());
		panel.repaint();*/
		//clicky click
		panel.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				if(!AIEnabled || turn != AIColor){
					showMove = false;
					int[] convert = new int[] {420,8,7,6,5,4,3,2,1};
					int x = (e.getY()+60)/100;
					int y = (e.getX()+60)/100;
					if(turn == 1 && x >= 1 && x <= 8 && y >= 1 && y <= 8){
						x = convert[x];
						y = convert[y];
					}
					if((x < 1 || x > 8) || (y < 1 || y > 8)){
						deselect(board);
					}
					else{
						if(display != boards.size()-1){
							display = boards.size()-1;
							panel.repaint();
						}
						if(!chosen){
							if(board.getPieces()[x][y].getColor() == turn){
								select(board, x, y, turn);
							}
						}
						else{
							if(board.getPieces()[x][y].isHighlighted()){
								board.getPieces()[board.getSelectedX()][board.getSelectedY()].setAnimating(true);
								move(board, board.getSelectedX(), board.getSelectedY(), x, y, turn, true, false);
								Timer change = new Timer(MOVE_LENGTH+500, new ActionListener(){
									public void actionPerformed(ActionEvent e){
										changeTurn();
										panel.repaint();
										if(AIEnabled){
											aBoard = duplicate(board);
											makeMove(board, alphaBeta(board, "", globalDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, AIColor), AIColor, true);
											changeTurn();
											panel.repaint();
										}
									}
								});
								change.setRepeats(false);
								change.start();
							}
							else if(board.getPieces()[x][y].getColor() == turn){
								deselect(board);
								select(board, x, y, turn);
							}
							else{
								deselect(board);
							}
						}
					}
					panel.repaint();
				}
			}
			public void mouseReleased(MouseEvent e){
				if(!AIEnabled || turn != AIColor){
					int[] convert = new int[] {420,8,7,6,5,4,3,2,1};
					int x = (e.getY()+60)/100;
					int y = (e.getX()+60)/100;
					if(turn == 1){
						if(x > 0 && x < 9){
							x = convert[x];
						}
						else{
							x = -69;
						}
						if(y > 0 && y < 9){
							y = convert[y];
						}
						else{
							y = -69;
						}
					}
					if((x < 1 || x > 8) || (y < 1 || y > 8)){
						deselect(board);
					}
					else{
						if(moving){
							if(board.getPieces()[board.getSelectedX()][board.getSelectedY()].isMoving()){
								if(board.getPieces()[x][y].isHighlighted()){
									move(board, board.getSelectedX(), board.getSelectedY(), x, y, turn, false, false);
									changeTurn();
									panel.repaint();
									if(AIEnabled){
										aBoard = duplicate(board);
										makeMove(board, alphaBeta(board, "", globalDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, AIColor), AIColor, true);
										changeTurn();
										panel.repaint();
									}
								}
								else if(board.getPieces()[x][y].getColor() != turn){
									deselect(board);
								}
							}
						}
					}
					moving = false;
					if(board.getSelectedX() >= 1 && board.getSelectedY() <= 8){
						board.getPieces()[board.getSelectedX()][board.getSelectedY()].setMoving(false);
					}
					panel.repaint();
				}
			}
		});
		panel.addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(MouseEvent e){
				if(!AIEnabled || turn != AIColor){
					int[] convert = new int[] {9,8,7,6,5,4,3,2,1,0,-69};
					int x = (e.getY()+60)/100;
					int y = (e.getX()+60)/100;
					if(turn == 1){
						if(x > -1 && x < 10){
							x = convert[x];
						}
						else{
							if(x < 0){
								x = 69;
							}
							else{
								x = -69;
							}
						}
						if(y > -1 && y < 10){
							y = convert[y];
						}
						else{
							if(y < 0){
								y = 69;
							}
							else{
								y = -69;
							}
						}
					}
					if((x > 0 && x < 9) && (y > 0 && y < 9)){
						if(!chosen && !moving){
							if(board.getPieces()[x][y].getColor() == turn){
								select(board, x, y, turn);
							}
						}

						if(board.getPieces()[x][y].getColor() == turn){
							moving = true;
							board.getPieces()[board.getSelectedX()][board.getSelectedY()].setMoving(true);
						}
						if(board.getSelectedX() >= 1 && board.getSelectedY() <= 8){
							if(board.getPieces()[board.getSelectedX()][board.getSelectedY()].isMoving()){
								if(e.getX() > 40 && e.getX() < 840){
									movingX = e.getX()-50;
								}
								if(e.getY() > 40 && e.getY() < 840){
									movingY = e.getY()-50;
								}
								panel.repaint();
							}
						}
					}
					else{
						if((x < 1 || x > 8) && (y > 0 && y < 9)){
							movingX = e.getX()-50;
							panel.repaint();
						}
						if((x > 0 && x < 9) && (y < 1 || y > 8)){
							movingY = e.getY()-50;
							panel.repaint();
						}
						if(x < 1 && turn == 0){
							movingY = -10;
						}
						else if(x < 1 && turn == 1){
							movingY = 790;
						}
						if(x > 8 && turn == 0){
							movingY = 790;
						}
						else if(x > 8 && turn == 1){
							movingY = -10;
						}
						if(y < 1 && turn == 0){
							movingX = -10;
						}
						else if(y < 1 && turn == 1){
							movingX = 790;
						}
						if(y > 8 && turn == 0){
							movingX = 790;
						}
						else if(y > 8 && turn == 1){
							movingX = -10;
						}
					}
				}
			}
		});
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "a");
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0), "b");
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0), "c");
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "d");
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "e");
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_K, 0), "k");
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
		panel.getActionMap().put("up", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(konami == 0 || konami == 1){
					konami++;
				}
				else{
					konami = 0;
				}
			}
		});
		panel.getActionMap().put("down", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(konami == 2 || konami == 3){
					konami++;
				}
				else{
					konami = 0;
				}
			}
		});
		panel.getActionMap().put("left", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(konami == 4 || konami == 6){
					konami++;
				}
				else{
					konami = 0;
				}
			}
		});
		panel.getActionMap().put("right", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(konami == 5 || konami == 7){
					konami++;
				}
				else{
					konami = 0;
				}
			}
		});
		panel.getActionMap().put("b", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(konami == 8){
					konami++;
				}
				else{
					konami = 0;
				}
			}
		});
		panel.getActionMap().put("a", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(konami == 9){
					konami++;
				}
				else{
					konami = 0;
				}
			}
		});
		panel.getActionMap().put("d", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(demote == 0 || demote == 5){
					demote++;
				}
				else{
					demote = 0;
				}
			}
		});
		panel.getActionMap().put("e", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(demote == 1 || demote == 4){
					demote++;
				}
				else{
					demote = 0;
				}
			}
		});
		panel.getActionMap().put("c", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(demote == 2){
					demote++;
				}
				else{
					demote = 0;
				}
			}
		});
		panel.getActionMap().put("k", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(demote == 3){
					demote++;
				}
				else{
					demote = 0;
				}
			}
		});
		panel.getActionMap().put("enter", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(konami == 10){
					JOptionPane.showMessageDialog(null, "Konami Code Activated");
					deselect(board);
					for(int x = 1; x < 9; x++){
						for(int y = 1; y < 9; y++){
							if(board.getPieces()[x][y].getColor() == turn && board.getPieces()[x][y].getValue() != 'K'){
								board.getPieces()[x][y].setPiece(new Queen(turn));
							}
						}
					}
					konami = 0;
					panel.repaint();
				}
				else{
					konami = 0;
				}
				if(demote == 6){
					int[] b = new int[]{1, 0};
					int t = b[turn];
					JOptionPane.showMessageDialog(null, "Get Decked");
					deselect(board);
					for(int x = 1; x < 9; x++){
						for(int y = 1; y < 9; y++){
							if(board.getPieces()[x][y].getColor() == t && board.getPieces()[x][y].getValue() != 'K'){
								board.getPieces()[x][y] = new Pawn(t);
							}
						}
					}
					demote = 0;
					panel.repaint();
				}
				else{
					demote = 0;
				}
			}
		});
	}
	//I think this is actually pointless 
	public V8(){
		super();
	}
	//van gogh this motha
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Board displayBoard = boards.get(display);
		int displayTurn = turn;
		int[] k = new int[]{1,0};
		if(display == boards.size()-1){
			displayBoard = board;
		}
		else{
			int t = k[displayTurn];
			checkAttacks(displayBoard, t);
			check = isCheck(displayBoard);
			if(check){
				findChecker(displayBoard);
			}
		}

		displayCaptures(g);

		boolean white = true;
		for(int x = 40; x < 840; x+=100){
			for(int y = 40; y < 840; y+=100){
				if(white){
					g.setColor(Color.WHITE);
				}
				else{
					if(theme.equals("sky")){
						g.setColor(new Color(200,255,255));
					}
					else if(theme.equals("red")){
						g.setColor(new Color(255, 100, 100));
					}
					else{
						g.setColor(Color.LIGHT_GRAY);
					}
				}
				g.fillRect(x, y, 100, 100);
				white = !white;
			}
			white = !white;
		}
		if(!AIEnabled){
			if(turn == 0){
				paintWhitePerspective(g, displayBoard);
			}
			else if(turn == 1){
				paintBlackPerspective(g, displayBoard);
			}
		}
		else if(AIColor == 1){
			paintWhitePerspective(g, displayBoard);
		}
		else{
			paintBlackPerspective(g, displayBoard);
		}
	}
	public static void paintWhitePerspective(Graphics g, Board displayBoard){
		int pieceValue;
		g.setColor(Color.BLACK);
		g.setFont(new Font("ARIAL", 1, 24));
		g.drawString("A            B            C            D            E            F            G            H", 80, 30);
		g.drawString("A            B            C            D            E            F            G            H", 80, 865);
		int i = 8;
		for(int x = 95; x < 895; x+=100){
			g.drawString(""+i, 15, x);
			g.drawString(""+i, 855, x);
			i--;
		}
		if(isCheck(displayBoard)){
			g.drawImage(checker, (displayBoard.getCheckingY()-1)*100+40, (displayBoard.getCheckingX() - 1)*100+40, null);
		}
		if(showMove){
			g.setColor(new Color(0, 0, 255, 50));
			g.fillRect((displayBoard.getNewPosY()-1)*100+40, (displayBoard.getNewPosX()-1)*100+40, 100, 100);
			g.fillRect((displayBoard.getOldPosY()-1)*100+40, (displayBoard.getOldPosX()-1)*100+40, 100, 100);
		}
		for(int x = 1; x < 9; x++){
			for(int y = 1; y < 9; y++){
				pieceValue = getPieceValue(displayBoard.getPieces()[x][y]);
				if(displayBoard.getPieces()[x][y].isClicked()){
					g.drawImage(clicked,(y-1)*100+40, (x-1)*100+40, null);
				}
				/*
				if(displayBoard.getPieces()[x][y].isAttacked()){
					g.setColor(Color.RED);
					g.fillRect((y-1)*100 + 70, (x-1)*100 + 70, 40, 40);
				}
				 */
				if(!displayBoard.getPieces()[x][y].isMoving() && !displayBoard.getPieces()[x][y].isAnimating()){
					g.drawImage(pieceImages[displayBoard.getPieces()[x][y].getColor()][pieceValue], (y-1)*100+40, (x-1)*100+40, null);
				}
				if(displayBoard.getPieces()[x][y].isHighlighted() && displayBoard.getPieces()[x][y].getColor() == 1){
					g.drawImage(attacking,(y-1)*100+57, (x-1)*100+55, null);
				}
				else if(displayBoard.getPieces()[x][y].isHighlighted() && displayBoard.getPieces()[x+1][y+1].getValue() == 'P' && displayBoard.getPieces()[x+1][y].getValue() == 'P' && displayBoard.getPieces()[x+1][y+1].getColor() != displayBoard.getPieces()[x+1][y].getColor() && displayBoard.getPieces()[x+1][y+1].getColor() == 0 && displayBoard.getPieces()[x+1][y].isTwoMove()){
					g.drawImage(passant, (y-1)*100+57, (x-1)*100+55, null);
				}
				else if(displayBoard.getPieces()[x][y].isHighlighted() && displayBoard.getPieces()[x+1][y-1].getValue() == 'P' && displayBoard.getPieces()[x+1][y].getValue() == 'P' && displayBoard.getPieces()[x+1][y-1].getColor() != displayBoard.getPieces()[x+1][y].getColor() && displayBoard.getPieces()[x+1][y-1].getColor() == 0 && displayBoard.getPieces()[x+1][y].isTwoMove()){
					g.drawImage(passant, (y-1)*100+57, (x-1)*100+55, null);
				}
				else if(displayBoard.getPieces()[x][y].isHighlighted()){
					g.drawImage(highlight,(y-1)*100+57, (x-1)*100+55, null);
				}
			}
		}
		for(int x = 1; x < 9; x++){
			for(int y = 1; y < 9; y++){
				pieceValue = getPieceValue(displayBoard.getPieces()[x][y]);
				if(displayBoard.getPieces()[x][y].isMoving()){
					g.drawImage(pieceImages[displayBoard.getPieces()[x][y].getColor()][pieceValue], movingX, movingY, null);
				}
				if(displayBoard.getPieces()[x][y].isAnimating()){
					g.drawImage(pieceImages[displayBoard.getPieces()[x][y].getColor()][pieceValue], animatingX, animatingY, null);
				}
			}
		}
	}
	public static void paintBlackPerspective(Graphics g, Board displayBoard){
		int pieceValue;
		g.setColor(Color.BLACK);
		g.setFont(new Font("ARIAL", 1, 24));
		g.drawString("H            G            F            E            D            C            B            A", 80, 30);
		g.drawString("H            G            F            E            D            C            B            A", 80, 865);
		int i = 1;
		for(int x = 95; x < 895; x+=100){
			g.drawString(""+i, 15, x);
			g.drawString(""+i, 855, x);
			i++;
		}
		if(isCheck(displayBoard)){
			int[] convert = new int[]{420, 8, 7, 6, 5, 4, 3, 2, 1};
			int checkX = -1;
			int checkY = -1;
			if(displayBoard.getCheckingX() >= 1 && displayBoard.getCheckingX() <= 8){
				checkX = convert[displayBoard.getCheckingX()];
				checkY = convert[displayBoard.getCheckingY()];
			}
			g.drawImage(checker, (checkY-1)*100+40, (checkX-1)*100+40, null);
		}
		if(showMove && displayBoard.getOldPosX() != -1){
			int[] convert = new int[]{420, 8, 7, 6, 5, 4, 3, 2, 1};
			int oldX = convert[displayBoard.getOldPosX()];
			int oldY = convert[displayBoard.getOldPosY()];
			int newX = convert[displayBoard.getNewPosX()];
			int newY = convert[displayBoard.getNewPosY()];
			g.setColor(new Color(0, 0, 255, 50));
			g.fillRect((newY-1)*100+40, (newX-1)*100+40, 100, 100);
			g.fillRect((oldY-1)*100+40, (oldX-1)*100+40, 100, 100);
		}
		int w = 1;
		for(int x = 8; x > 0; x--){
			int v = 1;
			for(int y = 8; y > 0; y--){
				pieceValue = getPieceValue(displayBoard.getPieces()[x][y]);
				if(displayBoard.getPieces()[x][y].isClicked()){
					g.drawImage(clicked,(v-1)*100+40, (w-1)*100+40, null);
				}
				/*
				if(displayBoard.getPieces()[x][y].isAttacked()){
					g.setColor(Color.GREEN);
					g.fillRect((v-1)*100+70, (w-1)*100+70, 40, 40);
				}
				 */
				if(!displayBoard.getPieces()[x][y].isMoving() && ! displayBoard.getPieces()[x][y].isAnimating()){
					g.drawImage(pieceImages[displayBoard.getPieces()[x][y].getColor()][pieceValue], (v-1)*100+40, (w-1)*100+40, null);
				}
				if(displayBoard.getPieces()[x][y].isHighlighted() && displayBoard.getPieces()[x][y].getColor() == 0){
					g.drawImage(attacking,(v-1)*100+57, (w-1)*100+55, null);
				}
				else if(displayBoard.getPieces()[x][y].isHighlighted() && displayBoard.getPieces()[x-1][y+1].getValue() == 'P' && displayBoard.getPieces()[x-1][y].getValue() == 'P' && displayBoard.getPieces()[x-1][y+1].getColor() != displayBoard.getPieces()[x-1][y].getColor() && displayBoard.getPieces()[x-1][y+1].getColor() == 1 && displayBoard.getPieces()[x-1][y].isTwoMove()){
					g.drawImage(passant, (v-1)*100+57, (w-1)*100+55, null);
				}
				else if(displayBoard.getPieces()[x][y].isHighlighted() && displayBoard.getPieces()[x-1][y-1].getValue() == 'P' && displayBoard.getPieces()[x-1][y].getValue() == 'P' && displayBoard.getPieces()[x-1][y-1].getColor() != displayBoard.getPieces()[x-1][y].getColor() && displayBoard.getPieces()[x-1][y-1].getColor() == 1 && displayBoard.getPieces()[x-1][y].isTwoMove()){
					g.drawImage(passant, (v-1)*100+57, (w-1)*100+55, null);
				}
				else if(displayBoard.getPieces()[x][y].isHighlighted()){
					g.drawImage(highlight,(v-1)*100+57, (w-1)*100+55, null);
				}
				v++;
			}
			w++;
		}
		for(int x = 8; x > 0; x--){
			for(int y = 8; y > 0; y--){
				pieceValue = getPieceValue(displayBoard.getPieces()[x][y]);
				if(displayBoard.getPieces()[x][y].isMoving()){
					g.drawImage(pieceImages[displayBoard.getPieces()[x][y].getColor()][pieceValue], movingX, movingY, null);
				}
				if(displayBoard.getPieces()[x][y].isAnimating()){
					g.drawImage(pieceImages[displayBoard.getPieces()[x][y].getColor()][pieceValue], animatingX, animatingY, null);
				}
			}
		}
	}
	//returns numerical representation of each piece
	public static int getPieceValue(Piece p){
		int pieceValue;
		if(p.getValue() == 'P'){
			pieceValue = 0;
		}
		else if(p.getValue() == 'R'){
			pieceValue = 1;
		}
		else if(p.getValue() == 'N'){
			pieceValue = 2;
		}
		else if(p.getValue() == 'B'){
			pieceValue = 3;
		}
		else if(p.getValue() == 'Q'){
			pieceValue = 4;
		}
		else if(p.getValue() == 'K'){
			pieceValue = 5;
		}
		else{
			pieceValue = 0;
		}
		return pieceValue;
	}
	//sound for normal move
	public static void moveSound(){
		try{ 
			AudioPlayer.player.start(new AudioStream(Main.class.getResourceAsStream("/resources/normalMove.wav")));
		}
		catch(Exception e){
			System.out.print(e.toString());
		}
	}
	//resets all the pieces
	public static void newGame(Board b){
		//basic setup things
		gameOn = true;
		chosen = false;
		moving = false;
		check = false;
		movingX = -1;
		movingY = -1;
		kingX = -1;
		kingY = -1;
		animatingX = -1;
		animatingY = -1;
		konami = 0;
		demote = 0;
		for(int x = 0; x < 2; x++){
			captures[x][0] = 8;
			for(int y = 1; y < 4; y++){
				captures[x][y] = 2;
			}
			captures[x][4] = 1;
			captures[x][5] = 1;
		}
		//set up border
		for(int x = 0; x < 10; x++){
			pieces[0][x] = new Piece(2);
			pieces[9][x] = new Piece(2);
		}
		for(int x = 1; x < 9; x++){
			pieces[x][0] = new Piece(2);
			pieces[x][9] = new Piece(2);
		}
		//set up blank spaces
		for(int x = 3; x < 7; x++){
			for(int y = 1; y < 9; y++){
				pieces[x][y] = new Piece(' ');
			}
		}
		//set up pieces
		for(int x = 1; x < 9; x++){
			pieces[2][x] = new Pawn(1);
			pieces[7][x] = new Pawn(0);
		}
		int i = 8;
		for(int x = 0; x < 2; x++){
			pieces[i][1] = new Rook(x);
			pieces[i][8] = new Rook(x);
			pieces[i][2] = new Knight(x);
			pieces[i][7] = new Knight(x);
			pieces[i][3] = new Bishop(x);
			pieces[i][6] = new Bishop(x);
			pieces[i][4] = new Queen(x);
			pieces[i][5] = new King(x);
			i=1;
		}
		board = new Board(pieces);
		aBoard = duplicate(board);
		deselect(board);
		deselect(aBoard);
		boards.clear();
		boards.add(duplicate(board));
		aBoards.clear();
		aBoards.add(duplicate(aBoard));
		display = 0;
	}
	//print board (for debugging purposes)
	public static void print(Piece[][] board, int t){
		if(t==0){
			System.out.println("    A    B    C    D    E    F    G    H");
			int i = 8;
			for(int x = 1; x < 9; x++){
				System.out.print(i + " ");
				for(int y = 1; y < 9; y++){
					System.out.print(board[x][y]);
				}
				System.out.print(" " + i);
				System.out.println();
				i--;
			}
			System.out.println("    A    B    C    D    E    F    G    H");
		}
		else if(t==1){
			System.out.println("    H    G    F    E    D    C    B    A");
			int i = 1;
			for(int x = 8; x > 0; x--){
				System.out.print(i + " ");
				for(int y = 8; y > 0; y--){
					System.out.print(board[x][y]);
				}
				System.out.print(" " + i);
				System.out.println();
				i++;
			}
			System.out.println("    H    G    F    E    D    C    B    A");
		}
	}
	//calculates captured pieces
	public static void captures(){
		Board temp = boards.get(display);
		for(int x = 0; x < 2; x++){
			for(int y = 0; y < 6; y++){
				captures[x][y] = 0;
			}
		}
		for(int t = 0; t < 2; t++){
			for(int x = 1; x < 9; x++){
				for(int y = 1; y < 9; y++){
					if(temp.getPieces()[x][y].getValue() != ' ' && temp.getPieces()[x][y].getColor() == t){
						captures[t][getPieceValue(temp.getPieces()[x][y])]++;
					}
				}
			}
		}
	}
	//display captured pieces
	public static void displayCaptures(Graphics g){
		captures();
		if(turn == 0){
			int wPawns = 8 - captures[0][0];
			for(int x = 0; x < wPawns; x++){
				g.drawImage(capturedPieces[0][0], 900 + (30*x), 110, 30, 30, null);
			}
			int wRooks = 2 - captures[0][1];
			for(int x = 0; x < wRooks; x++){
				g.drawImage(capturedPieces[0][1], 900 + (210*x), 140, 30, 30, null);
			}
			int wKnights = 2 - captures[0][2];
			for(int x = 0; x < wKnights; x++){
				g.drawImage(capturedPieces[0][2], 930 + (150*x), 140, 30, 30, null);
			}
			int wBishops = 2 - captures[0][3];
			for(int x = 0; x < wBishops; x++){
				g.drawImage(capturedPieces[0][3], 960 + (90*x), 140, 30, 30, null);
			}
			int wQueens = 1 - captures[0][4];
			if(wQueens == 1){
				g.drawImage(capturedPieces[0][4], 990, 140, 30, 30, null);
			}
			int wKings = 1 - captures[0][5];
			if(wKings == 1){
				g.drawImage(capturedPieces[0][5], 1020, 140, 30, 30, null);
			}

			int bPawns = 8 - captures[1][0];
			for(int x = 0; x < bPawns; x++){
				g.drawImage(capturedPieces[1][0], 900 + (30*x), 710, 30, 30, null);
			}
			int bRooks = 2 - captures[1][1];
			for(int x = 0; x < bRooks; x++){
				g.drawImage(capturedPieces[1][1], 900 + (210*x), 740, 30, 30, null);
			}
			int bKnights = 2 - captures[1][2];
			for(int x = 0; x < bKnights; x++){
				g.drawImage(capturedPieces[1][2], 930 + (150*x), 740, 30, 30, null);
			}
			int bBishops = 2 - captures[1][3];
			for(int x = 0; x < bBishops; x++){
				g.drawImage(capturedPieces[1][3], 960 + (90*x), 740, 30, 30, null);
			}
			int bQueens = 1 - captures[1][4];
			if(bQueens == 1){
				g.drawImage(capturedPieces[1][4], 990, 740, 30, 30, null);
			}
			int bKings = 1 - captures[1][5];
			if(bKings == 1){
				g.drawImage(capturedPieces[1][5], 1020, 740, 30, 30, null);
			}
			int wPoints = 0;
			wPoints += wPawns;
			wPoints += wRooks * 5;
			wPoints += wKnights * 3;
			wPoints += wBishops * 3;
			wPoints += wQueens * 9;

			int bPoints = 0;
			bPoints += bPawns;
			bPoints += bRooks * 5;
			bPoints += bKnights * 3;
			bPoints += bBishops * 3;
			bPoints += bQueens * 9;

			g.setFont(new Font("ARIAL", 1, 20));
			g.setColor(Color.BLACK);

			if(wPoints > bPoints){
				g.drawString("+" + Integer.toString(wPoints-bPoints), 1140, 148);
			}
			else if(bPoints > wPoints){
				g.drawString("+" + Integer.toString(bPoints-wPoints), 1140, 748);
			}
		}
		else if(turn == 1){
			int wPawns = 8 - captures[0][0];
			for(int x = 0; x < wPawns; x++){
				g.drawImage(capturedPieces[0][0], 900 + (30*x), 710, 30, 30, null);
			}
			int wRooks = 2 - captures[0][1];
			for(int x = 0; x < wRooks; x++){
				g.drawImage(capturedPieces[0][1], 900 + (210*x), 740, 30, 30, null);
			}
			int wKnights = 2 - captures[0][2];
			for(int x = 0; x < wKnights; x++){
				g.drawImage(capturedPieces[0][2], 930 + (150*x), 740, 30, 30, null);
			}
			int wBishops = 2 - captures[0][3];
			for(int x = 0; x < wBishops; x++){
				g.drawImage(capturedPieces[0][3], 960 + (90*x), 740, 30, 30, null);
			}
			int wQueens = 1 - captures[0][4];
			if(wQueens == 1){
				g.drawImage(capturedPieces[0][4], 990, 740, 30, 30, null);
			}
			int wKings = 1 - captures[0][5];
			if(wKings == 1){
				g.drawImage(capturedPieces[0][5], 1020, 740, 30, 30, null);
			}

			int bPawns = 8 - captures[1][0];
			for(int x = 0; x < bPawns; x++){
				g.drawImage(capturedPieces[1][0], 900 + (30*x), 110, 30, 30, null);
			}
			int bRooks = 2 - captures[1][1];
			for(int x = 0; x < bRooks; x++){
				g.drawImage(capturedPieces[1][1], 900 + (210*x), 140, 30, 30, null);
			}
			int bKnights = 2 - captures[1][2];
			for(int x = 0; x < bKnights; x++){
				g.drawImage(capturedPieces[1][2], 930 + (150*x), 140, 30, 30, null);
			}
			int bBishops = 2 - captures[1][3];
			for(int x = 0; x < bBishops; x++){
				g.drawImage(capturedPieces[1][3], 960 + (90*x), 140, 30, 30, null);
			}
			int bQueens = 1 - captures[1][4];
			if(bQueens == 1){
				g.drawImage(capturedPieces[1][4], 990, 140, 30, 30, null);
			}
			int bKings = 1 - captures[1][5];
			if(bKings == 1){
				g.drawImage(capturedPieces[1][5], 1020, 140, 30, 30, null);
			}

			int wPoints = 0;
			wPoints += wPawns;
			wPoints += wRooks * 5;
			wPoints += wKnights * 3;
			wPoints += wBishops * 3;
			wPoints += wQueens * 9;

			int bPoints = 0;
			bPoints += bPawns;
			bPoints += bRooks * 5;
			bPoints += bKnights * 3;
			bPoints += bBishops * 3;
			bPoints += bQueens * 9;

			g.setFont(new Font("ARIAL", 1, 20));
			g.setColor(Color.BLACK);

			if(wPoints > bPoints){
				g.drawString("+" + Integer.toString(wPoints-bPoints), 1140, 748);
			}
			else if(bPoints > wPoints){
				g.drawString("+" + Integer.toString(bPoints-wPoints), 1140, 148);
			}
		}

	}
	//check if check
	public static boolean isCheck(Board b){
		for(int x = 1; x < 9; x++){
			for(int y = 1; y < 9; y++){
				if(b.getPieces()[x][y].getValue() == 'K' && b.getPieces()[x][y].isAttacked()){
					return true;
				}
			}
		}

		return false;
	}
	//checks if check is checkmate
	//pass it a turn of t, checks if t is the person in checkmate
	public static boolean isMate(Board b, int t, boolean ai){
		Board temp = duplicate(b);
		deselect(temp);
		for(int x = 1; x < 9; x++){
			for(int y = 1; y < 9; y++){
				if(temp.getPieces()[x][y].getColor()==t){
					select(temp, x, y, t);
					for(int i = 1; i < 9; i++){
						for(int j = 1; j < 9; j++){
							if(temp.getPieces()[i][j].isHighlighted()){
								deselect(temp);
								return false;
							}
						}
					}
					deselect(temp);
				}
			}
		}
		if(!ai){
			gameOn = false;
		}
		return true;
	}
	//find the lil dood who's attacking my king
	public static void findChecker(Board b1){
		boolean found = false;
		Board b = duplicate(b1);
		int[] c = new int[] {1, 0};
		int t = c[turn];
		for(int x = 1; x < 9; x++){
			for(int y = 1; y < 9; y++){
				select(b, x, y, turn);
				for(int i = 1; i < 9; i++){
					for(int j = 1; j < 9; j++){
						if(b.getPieces()[i][j].isHighlighted() && b.getPieces()[i][j].getValue() == 'K' && b.getPieces()[i][j].getColor() == t){
							found = true;
							b1.setCheckingX(x);
							b1.setCheckingY(y);
						}
					}
				}
				deselect(b);
			}
		}
		for(int x = 1; x < 9; x++){
			for(int y = 1; y < 9; y++){
				select(b, x, y, t);
				for(int i = 1; i < 9; i++){
					for(int j = 1; j < 9; j++){
						if(b.getPieces()[i][j].isHighlighted() && b.getPieces()[i][j].getValue() == 'K' && b.getPieces()[i][j].getColor() == turn){
							found = true;
							b1.setCheckingX(x);
							b1.setCheckingY(y);
						}
					}
				}
				deselect(b);
			}
		}
		if(!found){
			b1.setCheckingX(-1);
			b1.setCheckingY(-1);
		}
	}
	//runs the check method specially so it only says a square is attacked, not highlighted as a possible destination
	//CHECKS YOUR OPPONENTS ATTACKS
	public static void checkAttacks(Board b, int t){
		if(t==1){
			t = 0;
		}
		else{
			t = 1;
		}
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				b.getPieces()[i][j].setAttacked(false);
			}
		}
		//needs to be in separate for loops or it won't function properly
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				b.getPieces()[i][j].check(b.getPieces(), i, j, t, 1);
			}
		}

	}
	//stop highlighting all pieces
	public static void deselect(Board b){
		chosen = false;
		for(int x = 0; x < 10; x++){
			for(int y = 0; y < 10; y++){
				b.getPieces()[x][y].setHighlighted(false);
				b.getPieces()[x][y].setClicked(false);
			}
		}
	}
	//changes whose turn it is, also stores board state into arraylist
	public static void changeTurn(){
		int[] turns = new int[] {1, 0};
		turn = turns[turn];
		boards.add(duplicate(board));
		display++;
		showMove = true;
		//System.out.println(rating(board, 2, 2));
		System.out.println(possibleMoves(board, turn));
	}
	//returns a String with all the possible moves for whoever's turn it is. x1, y1, x2, y2, captured p, T for twomove, F if first move
	public static String possibleMoves(Board b1, int t){
		String list = "";
		Board b = duplicate(b1);
		for(int x = 1; x < 9; x++){
			for(int y = 1; y < 9; y++){
				if(b.getPieces()[x][y].getColor() == t){
					select(b, x, y, t);
					for(int i = 1; i < 9; i++){
						for(int j = 1; j < 9; j++){
							if(b.getPieces()[i][j].isHighlighted()){
								//promotions: y1,x2,y2,capture,'Y'
								if((i == 8 || i == 1) && b.getPieces()[x][y].getValue()=='P'){
									list += "" + y + i + j + b.getPieces()[i][j].getValue() + 'Y';
								}
								else{
									//normal: x1,y1,x2,y2,capture,T,F
									list += "" + x + y + i + j;

									//adds 'c' for kingside and 'C' for queenside castle
									//adds 'e' for white passant, 'E' for black passant
									//adds 'T' if this move is after a twomove, so undoing knows to reenable the twomove this move is undoing 
									//'F' for firstmove (hasMoved==false before this)
									//'S' if captured piece hadn't moved
									if((i == 1 || i == 8) && j == 7 && !b.getPieces()[x][y].hasMoved() && !b.getPieces()[i][8].hasMoved() && b.getPieces()[x][y].getValue() == 'K'){
										list += "c";
									}
									else if((i == 1 || i == 8) && j == 3 && !b.getPieces()[x][y].hasMoved() && !b.getPieces()[i][1].hasMoved() && b.getPieces()[x][y].getValue() == 'K'){
										list += "C";
									}
									else if(b.getPieces()[x][y].getValue() == 'P' && b.getPieces()[x][y].getColor() == 0 && b.getPieces()[i+1][j].getValue() == 'P' && b.getPieces()[i+1][j].getColor() == 1 && b.getPieces()[i+1][j].isTwoMove()){
										list += "e";
									}
									else if(b.getPieces()[x][y].getValue() == 'P' && b.getPieces()[x][y].getColor() == 1 && b.getPieces()[i-1][j].getValue() == 'P' && b.getPieces()[i-1][j].getColor() == 0 && b.getPieces()[i-1][j].isTwoMove()){
										list += "E";
									}
									else{
										list += b.getPieces()[i][j].getValue();
									}
								}
								if(b.getPieces()[b.getEnX()][b.getEnY()].isTwoMove()){
									list += 'T';
								}
								else{
									list += ' ';
								}
								if(!b.getPieces()[x][y].hasMoved()){
									list += 'F';
								}
								else{
									list += ' ';
								}
								if(!b.getPieces()[i][j].hasMoved() && b.getPieces()[i][j].getValue() != ' '){
									list += 'S';
								}
								else{
									list += ' ';
								}
							}
						}
					}
					deselect(b);
				}
			}
		}
		return list;
	}
	public static String sort(Board b, String list, int t){
		int[] score = new int[list.length()/8];
		for(int x = 0; x < list.length(); x+=8){
			makeMove(b, list.substring(x,x+8), t, false);
			score[x/8] = -rating(b, -1, 0, t);
			undoMove(b, list.substring(x,x+8), t);
		}
		String newListA = "";
		String newListB = list;
		for(int x = 0; x < Math.min(6, list.length()/8); x++){
			int max = -1000000;
			int maxLocation = 0;
			for(int y = 0; y < list.length()/8; y++){
				if(score[y] > max){
					max = score[y];
					maxLocation = y;
				}
			}
			score[maxLocation] = -1000000;
			newListA += list.substring(maxLocation*8, maxLocation*8+8);
			newListB = newListB.replace(list.substring(maxLocation*8, maxLocation*8+8), "");
		}
		return newListA + newListB;
	}
	public static int rating(Board b, int list, int depth, int t){
		//ratings + for white winning, - for black, but returns a - in the end so in total - white winning + black
		int counter = 0;
		int material = rateMaterial(b);
		counter += rateAttacks(b);
		counter += material;
		counter += rateMoveability(b, depth, material, 0);
		counter -= rateMoveability(b, depth, material, 1);
		counter += ratePositional(b, 0);
		if(AIColor == 0){
			counter *= -1;
		}
		return -(counter+depth*50);
	}
	public static int rateAttacks(Board b){
		int counter = 0;
		checkAttacks(b, 0);
		for(int x = 1; x < 9; x++){
			for(int y = 1; y < 9; y++){
				if(b.getPieces()[x][y].getColor()==0 && b.getPieces()[x][y].isAttacked){
					switch(b.getPieces()[x][y].getValue()){
					case 'P':
						counter -= 64;
						break;
					case 'R':
						counter -= 500;
						break;
					case 'N':
						counter -= 300;
						break;
					case 'B':
						counter -= 300;
						break;
					case 'Q':
						counter -= 900;
						break;
					case 'K':
						counter -= 200;
						break;
					}
				}
			}
		}
		checkAttacks(b, 1);
		for(int x = 1; x < 9; x++){
			for(int y = 1; y < 9; y++){
				if(b.getPieces()[x][y].getColor()==1 && b.getPieces()[x][y].isAttacked){
					switch(b.getPieces()[x][y].getValue()){
					case 'P':
						counter += 64;
						break;
					case 'R':
						counter += 500;
						break;
					case 'N':
						counter += 300;
						break;
					case 'B':
						counter += 300;
						break;
					case 'Q':
						counter += 900;
						break;
					case 'K':
						counter += 200;
						break;
					}
				}
			}
		}
		return counter/2;
	}
	public static int rateMaterial(Board b){
		int counter = 0;
		int wBishops = 0;
		int bBishops = 0;
		for(int x = 1; x < 9; x++){
			for(int y = 1; y < 9; y++){
				if(b.getPieces()[x][y].getColor() == 0){
					switch (b.getPieces()[x][y].getValue()){
					case 'P':
						counter += 100;
						break;
					case 'R':
						counter += 500;
						break;
					case 'N':
						counter += 300;
						break;
					case 'B':
						wBishops++;
						break;
					case 'Q':
						counter += 900;
						break;
					}
				}
				else if(b.getPieces()[x][y].getColor() == 1){
					switch (b.getPieces()[x][y].getValue()){
					case 'P':
						counter -= 100;
						break;
					case 'R':
						counter -= 500;
						break;
					case 'N':
						counter -= 300;
						break;
					case 'B':
						bBishops++;
						break;
					case 'Q':
						counter -= 900;
						break;
					}
				}
			}
		}
		if(wBishops >= 2){
			counter += 300 * wBishops;
		}
		else if(wBishops == 1){
			counter += 250;
		}

		if(bBishops >= 2){
			counter -= 300 * bBishops;
		}
		else if(bBishops == 1){
			counter -= 250;
		}

		return counter;
	}
	public static int rateMoveability(Board b, int depth, int material, int t){
		int counter = 0;
		String l = possibleMoves(b, t);
		int len = l.length();
		counter += len;//8 points per valid move
		if(len == 0){
			if(isMate(b, t, true)){
				counter -= 20000000*depth;
			}
			else{
				counter -= 150000*depth;
			}
		}
		return counter;
	}
	public static int ratePositional(Board b, int t){
		int counter = 0;
		for(int x = 1; x < 9; x++){
			for(int y = 1; y < 9; y++){
				if(b.getPieces()[x][y].getColor() == 0){
					if(b.getPieces()[x][y].getValue() == 'P' && b.getPieces()[x+1][y].getColor() == 0 && b.getPieces()[x+1][y].getValue() == 'P'){
						counter -= 36;	
					}
					if(b.getPieces()[x][y].getValue() == 'N'){
						if((b.getPieces()[x+1][y+1].getColor() == 0 && b.getPieces()[x+1][y+1].getValue() == 'P') || (b.getPieces()[x+1][y-1].getColor() == 0 && b.getPieces()[x+1][y-1].getValue() == 'P')){
							counter += 50;
						}
						if(x == 1 || x == 8 || y == 1 || y == 8){
							counter -= 30;
						}
					}
				}
				else{
					if(b.getPieces()[x][y].getValue() == 'P' && b.getPieces()[x+1][y].getColor() == 1 && b.getPieces()[x+1][y].getValue() == 'P'){
						counter += 36;
					}
					if(b.getPieces()[x][y].getValue() == 'N'){
						if((b.getPieces()[x-1][y+1].getColor() == 1 && b.getPieces()[x-1][y+1].getValue() == 'P') || (b.getPieces()[x-1][y-1].getColor() == 1 && b.getPieces()[x-1][y-1].getValue() == 'P')){
							counter -= 50;
						}
						if(x == 1 || x == 8 || y == 1 || y == 8){
							counter += 30;
						}
					}
				}


				
				
			}
		}
		return counter;
	}
	public static String alphaBeta(Board b, String move, int depth, int alpha, int beta, int t){
		String list = possibleMoves(b, t);
		if(depth == 0 || list.length() == 0 || !gameOn){
			moves++;
			return move + rating(b, list.length(), depth, t);
		}
	//	list = sort(b, list, t);
		if(t == 1){ //computer is playing black
			for(int x = 0; x < list.length(); x+=8){
				makeMove(b, list.substring(x, x+8), t, false);
				String returnString = alphaBeta(b, list.substring(x, x+8), depth-1, alpha, beta, 0);
				int value = Integer.parseInt(returnString.substring(8));
				undoMove(b, list.substring(x, x+8), t);
				if(value > alpha){
					alpha = value;
					if(depth == globalDepth){
						move = returnString.substring(0, 8);
					}
				}
				if(alpha >= beta){
					return move+alpha;
				}
			}
		}
		else if(t == 0){ //computer is playing white
			for(int x = 0; x < list.length(); x+=8){
				makeMove(b, list.substring(x, x+8), t, false);
				String returnString = alphaBeta(b, list.substring(x, x+8), depth-1, alpha, beta, 1);
				int value = Integer.valueOf(returnString.substring(8));
				undoMove(b, list.substring(x, x+8), t);
				if(value < beta){
					beta = value;
					if(depth == globalDepth){
						move = returnString.substring(0, 8);
					}
				}
				if(alpha >= beta){
					return move+beta;
				}
			}
		}
		if(t == 1){
			return move+alpha;
		}
		else{
			return move+beta;
		}
	}
	public static void makeMove(Board b, String move, int t, boolean animate){
		/*
		 * x1,y1,x2,y2,capture	normal
		 * x1,y1,x2,y2,c/C for kingside/queenside castle
		 * x1,y1,x2,y2,e/E for white/black en passant
		 * y1,x2,y2,capture, Y for promotion
		 * 01234,T for twoMove
		 * 01234, ,F for firstMove
		 * 01234, , ,S for captured piece hadn't moved
		 */
		if(gameOn){
			b.getPieces()[b.getEnX()][b.getEnY()].setTwoMove(false);
			/*move(b, Character.getNumericValue(move.charAt(0)), Character.getNumericValue(move.charAt(1)), Character.getNumericValue(move.charAt(2)), Character.getNumericValue(move.charAt(3)), t, animate, true);
			display++;
			boards.add(duplicate(b));*/
			if(move.charAt(4) == 'Y'){
				if(Character.getNumericValue(move.charAt(1)) == 1){
					b.getPieces()[Character.getNumericValue(move.charAt(1))][Character.getNumericValue(move.charAt(2))].setPiece(new Queen(0));
					b.getPieces()[2][Character.getNumericValue(move.charAt(0))].empty();
				}
				else{
					b.getPieces()[Character.getNumericValue(move.charAt(1))][Character.getNumericValue(move.charAt(2))].setPiece(new Queen(1));
					b.getPieces()[7][Character.getNumericValue(move.charAt(0))].empty();
				}
			}
			else{
				b.getPieces()[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))].setPiece(b.getPieces()[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]);
				if((Character.getNumericValue(move.charAt(0)) == 2 || Character.getNumericValue(move.charAt(0)) == 7) && (Character.getNumericValue(move.charAt(2)) == 4 || Character.getNumericValue(move.charAt(2)) == 5) && b.getPieces()[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))].getValue() == 'P'){
					b.setEnX(Character.getNumericValue(move.charAt(2)));
					b.setEnY(Character.getNumericValue(move.charAt(3)));
					b.getPieces()[b.getEnX()][b.getEnY()].setTwoMove(true);
				}
			}
			switch (move.charAt(4)){
			case 'c':
				b.getPieces()[Character.getNumericValue(move.charAt(0))][6].setPiece(b.getPieces()[Character.getNumericValue(move.charAt(0))][8]);
				break;
			case 'C':
				b.getPieces()[Character.getNumericValue(move.charAt(0))][4].setPiece(b.getPieces()[Character.getNumericValue(move.charAt(0))][1]);
				break;
			case 'e':
				b.getPieces()[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(3))].empty();
				break;
			case 'E':
				b.getPieces()[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(3))].empty();
				break;
			}
		}
	}
	public static void undoMove(Board b, String move, int t){
		//boards.remove(display);
		/*board = boards.get(0);
		display--;*/
		//board = duplicate(boards.get(display));
		/*
		 * x1,y1,x2,y2,capture	normal
		 * x1,y1,x2,y2,c/C for kingside/queenside castle
		 * x1,y1,x2,y2,e/E for white/black en passant
		 * y1,x2,y2,capture, Y for promotion
		 * 01234,T for twoMove
		 * 01234, ,F for firstMove
		 * 01234, , ,S for captured piece hadn't moved
		 */

		switch (move.charAt(4)){
		case 'Y':
			if(Character.getNumericValue(move.charAt(1)) == 1){
				b.getPieces()[2][Character.getNumericValue(move.charAt(0))].setPiece(new Pawn(0));
				b.getPieces()[Character.getNumericValue(move.charAt(1))][Character.getNumericValue(move.charAt(2))].setPiece(new Piece(1, move.charAt(3)));
			}
			else{
				b.getPieces()[7][Character.getNumericValue(move.charAt(0))].setPiece(new Pawn(1));
				b.getPieces()[Character.getNumericValue(move.charAt(1))][Character.getNumericValue(move.charAt(2))].setPiece(new Piece(0, move.charAt(3)));
			}
			break;
		case 'c':
			b.getPieces()[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))].setPiece(b.getPieces()[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]);
			b.getPieces()[Character.getNumericValue(move.charAt(0))][8].setPiece(b.getPieces()[Character.getNumericValue(move.charAt(0))][6]);
			b.getPieces()[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))].setMoved(false);
			b.getPieces()[Character.getNumericValue(move.charAt(0))][8].setMoved(false);
			break;
		case 'C':
			b.getPieces()[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))].setPiece(b.getPieces()[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]);
			b.getPieces()[Character.getNumericValue(move.charAt(0))][1].setPiece(b.getPieces()[Character.getNumericValue(move.charAt(0))][4]);
			b.getPieces()[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))].setMoved(false);
			b.getPieces()[Character.getNumericValue(move.charAt(0))][1].setMoved(false);
			break;
		case 'e':
			b.getPieces()[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))].setPiece(b.getPieces()[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]);
			b.getPieces()[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(3))].setPiece(new Pawn(1));
			b.getPieces()[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(3))].setTwoMove(true);
			break;
		case 'E':
			b.getPieces()[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))].setPiece(b.getPieces()[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]);
			b.getPieces()[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(3))].setPiece(new Pawn(0));
			b.getPieces()[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(3))].setTwoMove(true);
			break;
		default:
			b.getPieces()[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))].setPiece(b.getPieces()[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]);
			b.getPieces()[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))].setPiece(new Piece(1-t,move.charAt(4)));
			break;
		}
		if(move.charAt(5) == 'T'){
			b.getPieces()[b.getEnX()][b.getEnY()].setTwoMove(true);
		}
		if(move.charAt(6) == 'F'){
			b.getPieces()[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))].setMoved(false);
		}
		if(move.charAt(7) == 'S'){
			b.getPieces()[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))].setMoved(false);
		}
	}
	//sets kingX and kingY to coordinates of king for specified player
	public static void findKing(Board b, int t){
		for(int x = 1; x < 9; x++){
			for(int y = 1; y < 9; y++){
				if(b.getPieces()[x][y].getColor() == t && b.getPieces()[x][y].getValue() == 'K'){
					kingX = x;
					kingY = y;
				}
			}
		}
	}
	//returns a duplicate board to the current board state
	public static Board duplicate(Board b){
		Board copy = new Board(b);
		return copy;
	}
	//restrict moves that would result in the king being in check
	public static void restrict(Board b, int t){
		Board copy = duplicate(b);
		for(int x = 1; x < 9; x++){
			for(int y = 1; y < 9; y++){
				if(copy.getPieces()[x][y].isHighlighted()){
					copy.getPieces()[x][y].setPiece(copy.getPieces()[b.getSelectedX()][b.getSelectedY()]);
					checkAttacks(copy, t);
					findKing(copy, t);
					if(copy.getPieces()[kingX][kingY].isAttacked()){
						b.getPieces()[x][y].setHighlighted(false);
					}
					copy = duplicate(b);
				}
			}
		}
	}
	//highlights possible moves of selected piece
	public static void select(Board b, int x, int y, int t){
		checkAttacks(b, t);
		b.getPieces()[x][y].check(b.getPieces(), x, y, t, 0);
		b.getPieces()[x][y].setClicked(true);
		chosen = true;
		b.setSelectedX(x);
		b.setSelectedY(y);
		restrict(b, t);
	}
	//moves the piece if chosen location is valid
	public static void move(Board b, int x1, int y1, int x2, int y2, int t, boolean b1, boolean ai){
		b.setOldPosX(x1);
		b.setOldPosY(y1);
		b.setNewPosX(x2);
		b.setNewPosY(y2);
		Timer setPiece = new Timer(MOVE_LENGTH, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(b.getPieces()[x2][y2].getValue() != 'K'){
					b.getPieces()[x2][y2].setPiece(b.getPieces()[x1][y1]);
				}
				if((x2 == 8 || x2 == 1) && b.getPieces()[x2][y2].getValue()=='P'){
					if(!ai){
						b.getPieces()[x2][y2].promote();
					}
					else{
						b.getPieces()[x2][y2].setValue('Q');
					}
					panel.repaint();
				}
				int a = -1;
				if(turn == 1){
					a = 0;
				}
				else{
					a = 1;
				}
				if(!ai || b1){
					moveSound();
				}
				checkAttacks(b, a);
				check = isCheck(b);
				if(check){
					findChecker(b);
					panel.repaint();
					if(isMate(b, turn, false)){
						JOptionPane.showMessageDialog(null, "Checkmate!");
						int again = JOptionPane.showConfirmDialog(null, "Play Again?");
						if(again == JOptionPane.YES_OPTION){
							newGame(board);
							turn = 0;
							panel.repaint();
						}
						else if(again == JOptionPane.NO_OPTION){
							System.exit(0);
						}
					}
				}
			}
		});
		setPiece.setRepeats(false);
		Timer setKing = new Timer(MOVE_LENGTH/2, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				b.getPieces()[x2][y2].setPiece(b.getPieces()[x1][y1]);
			}
		});
		setKing.setRepeats(false);
		Timer setRook1 = new Timer(MOVE_LENGTH, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				b.getPieces()[x2][6].setPiece(b.getPieces()[x1][8]);
			}
		});
		setRook1.setRepeats(false);
		Timer animateRook1 = new Timer(MOVE_LENGTH/2, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				b.getPieces()[b.getSelectedX()][b.getSelectedY()].setAnimating(false);
				b.getPieces()[x1][8].setAnimating(true);
				animate(x1, 8, x2, 6, turn, 2);
			}
		});
		animateRook1.setRepeats(false);
		Timer setRook2 = new Timer(MOVE_LENGTH, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				b.getPieces()[x2][4].setPiece(b.getPieces()[x1][1]);
			}
		});
		setRook2.setRepeats(false);
		Timer animateRook2 = new Timer(MOVE_LENGTH/2, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				b.getPieces()[b.getSelectedX()][b.getSelectedY()].setAnimating(false);
				b.getPieces()[x1][1].setAnimating(true);
				animate(x1, 1, x2, 4, turn, 3);
			}
		});
		animateRook2.setRepeats(false);
		Timer passantTake1 = new Timer(MOVE_LENGTH, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				b.getPieces()[x2+1][y2].empty();
			}
		});
		passantTake1.setRepeats(false);
		Timer passantTake2 = new Timer(MOVE_LENGTH, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				b.getPieces()[x2-1][y2].empty();
			}
		});
		passantTake2.setRepeats(false);
		Timer twoTrue = new Timer(MOVE_LENGTH, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				b.getPieces()[x2][y2].setTwoMove(true);
			}
		});
		twoTrue.setRepeats(false);
		//castling is SO SO SO annoying WITH ANIMATIONS
		if((x2 == 1 || x2 == 8) && y2 == 7 && !b.getPieces()[x1][y1].hasMoved() && !b.getPieces()[x2][8].hasMoved() && b.getPieces()[x1][y1].getValue() == 'K'){
			b.getPieces()[b.getEnX()][b.getEnY()].setTwoMove(false);
			if(!b1){
				b.getPieces()[x2][y2].setPiece(b.getPieces()[x1][y1]);
				b.getPieces()[x2][6].setPiece(b.getPieces()[x1][8]);
				if(!ai || b1){
					moveSound();
				}
			}
			else{
				animate(x1, y1, x2, y2, t, 2);
				setKing.start();
				animateRook1.start();
				setRook1.start();
				setPiece.start();
			}
			deselect(b);
		}
		else if((x2 == 1 || x2 == 8) && y2 == 3 && !b.getPieces()[x1][y1].hasMoved() && !b.getPieces()[x2][1].hasMoved() && b.getPieces()[x1][y1].getValue() == 'K'){
			b.getPieces()[b.getEnX()][b.getEnY()].setTwoMove(false);
			if(!b1){
				b.getPieces()[x2][y2].setPiece(b.getPieces()[x1][y1]);
				b.getPieces()[x2][4].setPiece(b.getPieces()[x1][1]);
				if(!ai || b1){
					moveSound();
				}
			}
			else{
				animate(x1, y1, x2, y2, t, 2);
				setKing.start();
				animateRook2.start();
				setRook2.start();
				setPiece.start();
			}
			deselect(b);
		}
		//en passant is annoying too
		else if((x1 == 2 || x1 == 7) && (x2 == 4 || x2 == 5) && b.getPieces()[x1][y1].getValue() == 'P'){
			b.getPieces()[b.getEnX()][b.getEnY()].setTwoMove(false);
			b.setEnX(x2);
			b.setEnY(y2);
			if(!b1){
				b.getPieces()[x2][y2].setPiece(b.getPieces()[x1][y1]);
				if(!ai || b1){
					moveSound();
				}
			}
			else{
				animate(x1, y1, x2, y2, t, 1);
				setPiece.start();
				twoTrue.start();
			}
			b.getPieces()[x2][y2].setTwoMove(true);
			deselect(b);
		}
		else if(b.getPieces()[x1][y1].getValue() == 'P' && b.getPieces()[x1][y1].getColor() == 0 && b.getPieces()[x2+1][y2].getValue() == 'P' && b.getPieces()[x2+1][y2].getColor() == 1 && b.getPieces()[x2+1][y2].isTwoMove()){
			if(!b1){
				b.getPieces()[x2+1][y2].empty();
				b.getPieces()[x2][y2].setPiece(b.getPieces()[x1][y1]);
				if(!ai || b1){
					moveSound();
				}
			}
			else{
				animate(x1, y1, x2, y2, t, 1);
				setPiece.start();
				passantTake1.start();
			}
			deselect(b);
		}
		else if(b.getPieces()[x1][y1].getValue() == 'P' && b.getPieces()[x1][y1].getColor() == 1 && b.getPieces()[x2-1][y2].getValue() == 'P' && b.getPieces()[x2-1][y2].getColor() == 0 && b.getPieces()[x2-1][y2].isTwoMove()){
			if(!b1){
				b.getPieces()[x2-1][y2].empty();
				b.getPieces()[x2][y2].setPiece(b.getPieces()[x1][y1]);
				if(!ai || b1){
					moveSound();
				}
			}
			else{
				animate(x1, y1, x2, y2, t, 1);
				setPiece.start();
				passantTake2.start();
			}
			deselect(b);
		}
		//normal move
		else{
			b.getPieces()[b.getEnX()][b.getEnY()].setTwoMove(false);
			if(!b1){
				b.getPieces()[x2][y2].setPiece(b.getPieces()[x1][y1]);
				if(!ai || b1){
					moveSound();
				}
			}
			else{
				animate(x1, y1, x2, y2, t, 1);
				setPiece.start();
			}
			deselect(b);
		}
		if((x2 == 8 || x2 == 1) && b.getPieces()[x2][y2].getValue()=='P'){
			if(!ai){
				b.getPieces()[x2][y2].promote();
			}
			else{
				b.getPieces()[x2][y2].setValue('Q');
			}
			panel.repaint();
		}
		if(t == 1){
			t = 0;
		}
		else{
			t = 1;
		}
		checkAttacks(b, t);
		check = isCheck(b);
		if(check){
			findChecker(b);
			panel.repaint();
			if(isMate(b, t, false)){
				JOptionPane.showMessageDialog(null, "Checkmate!");
				int again = JOptionPane.showConfirmDialog(null, "Play Again?");
				if(again == JOptionPane.YES_OPTION){
					newGame(board);
					turn = 1;
					panel.repaint();
				}
				else if(again == JOptionPane.NO_OPTION){
					System.exit(0);
				}
			}
		}
		captures();
	}
	//slippy slide across the screen
	public static void animate(int x1, int y1, int x2, int y2, int t, int s){
		int[] convert = new int[] {420, 8, 7, 6, 5, 4, 3, 2, 1};
		if(t == 1){
			int x = convert[x1];
			int y = convert[y1];
			animatingX = (y-1)*100+40;
			animatingY = (x-1)*100+40;
		}
		else{
			animatingX = ((y1-1)*100)+40;
			animatingY = ((x1-1)*100)+40;
		}
		Timer animation = new Timer((MOVE_LENGTH/FPS)/s, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int[] conv = new int[841];
				conv[40] = 740;
				conv[140] = 640;
				conv[240] = 540;
				conv[340] = 440;
				conv[440] = 340;
				conv[540] = 240;
				conv[640] = 140;
				conv[740] = 40;
				int destX = (y2-1)*100+40;
				int destY = (x2-1)*100+40;
				int xVel = (((y2-y1)*100)/FPS);
				int yVel = (((x2-x1)*100)/FPS);
				if(s == 2 || s == 3){
					if(xVel < 0){
						xVel = -7;
					}
					else{
						xVel = 7;
					}
				}
				if(t == 1){
					xVel *= -1;
					yVel *= -1;
					destX = conv[destX];
					destY = conv[destY];
				}
				if(animatingX != destX || animatingY != destY){
					animatingX += xVel;
					animatingY += yVel;
					panel.repaint();
				}
			}
		});
		animation.setRepeats(true);
		int stopTime = 420;
		if(s == 1){
			stopTime = MOVE_LENGTH + 500;
		}
		else if(s == 2){
			stopTime = MOVE_LENGTH/2;
		}
		Timer stop = new Timer(stopTime, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				animation.stop();
			}
		});
		stop.setRepeats(false);

		animation.start();
		stop.start();
	}
}