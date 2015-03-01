//This class is the GUI representation of the board
//Allowing the player to see the board
//And make moves on the board
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

public class CheckersGUI extends JFrame implements ActionListener
{
	private JButton[][] buttons;
	private int prevClicked, currClick; //This will be useful in the ActionListener, when you need to remember what was previously clicked.
	//prevClicked stores data in the form: first digit = row, second digit = column
	private CheckersOp data;
	private boolean isTwoHumans; //This decides whether it's a human v. human or human v. computer match. (Could've made two classes, but this was simpler).
	private SwingWorker<Void,CheckersOp> playWorker;
	
	public CheckersGUI(String title, boolean hasTwoHumans)
	{
		super(title);

		setSize(800,800);
		setLayout(new GridLayout(8,8));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		prevClicked = 0;
		
		buttons = new JButton[8][8];
		for(int r=0; r<8; r++)
		{
			for(int c=0; c<8; c++)
			{
				buttons[r][c] = new JButton("O");
				add(buttons[r][c]);
				buttons[r][c].addActionListener(this);
				buttons[r][c].setActionCommand(r+""+c);
				buttons[r][c].setVisible(true);
				buttons[r][c].setFont(new Font("Arial", Font.BOLD, 60));
			}
		}
		
		
		data = new CheckersOp();
		data.resetBoard();
		isTwoHumans = hasTwoHumans;
		drawBoard(data);

	}
	
	public void drawBoard(CheckersOp data) //Takes the board info and redraws the board; will run after every move
	{
		int[][] board = data.getBoard();
		for(int row=0; row<8; row++)
		{
			for(int column=0; column<8; column++)
			{
				if(board[row][column]==0)
				{
					buttons[row][column].setText("");
				}
				if(board[row][column]==1)
				{
					buttons[row][column].setForeground(Color.RED);
					buttons[row][column].setText("O");
				}
				if(board[row][column]==2)
				{
					buttons[row][column].setForeground(Color.BLACK);
					buttons[row][column].setText("O");
				}
				if(board[row][column]==3)
				{
					buttons[row][column].setForeground(Color.RED);
					buttons[row][column].setText("K");
				}
				if(board[row][column]==4)
				{
					buttons[row][column].setForeground(Color.BLACK);
					buttons[row][column].setText("K");
				}
			}
		}
		repaint();
	}
	
	public void actionPerformed(ActionEvent evt) //Interprets button presses
	{
		synchronized (playWorker) {
			currClick = Integer.parseInt(evt.getActionCommand());
			if (data.checkValidMove(prevClicked, currClick) != 0) {
				playWorker.notify(); // unblocks the SwingWorker playWorker thread
			} else {
			prevClicked = currClick; //You'll notice that the old player's last square will get stored as the next player's prevClicked, but
			//but this is okay, because there will never be a legal move whose prevClick is a square with the opponent's piece
			}
			this.setTitle(returnTitleString(data.turnCount));
		}
	}
	
	private String returnTitleString(int turnCount)
	{
		String titleString = "";
		if(isTwoHumans) titleString += "Human vs. Human";
		else titleString += "Human vs. Computer";
		titleString += ", ";
		if(data.isRedTurn()) titleString += "Red's Turn";
		else titleString += "Black's Turn";
		titleString += ", Turn Count: " + turnCount;
		return titleString;
	}
	
	public void play() {
		playWorker = new SwingWorker<Void,CheckersOp>() {

			@Override
			protected Void doInBackground() throws Exception {
				synchronized (this)
				{
					while (data.checkWinner() == 0) {
						wait();
						data.makeMove(prevClicked, currClick);
						playWorker.notify();
						System.out.println("Human just moved, I think.");
						publish(data);
						System.out.println(!data.isRedTurn()&&!isTwoHumans);
						if(data.isRedTurn()&&!isTwoHumans)
						{
							System.out.println("thinking...");
							//Here we feed our wonderful AI the board, and ask it to make as many moves as needed
							//Then we'll have a while loop in here, too: while(data.currJumper!=null), keep playing
							//And finally at the end of the jump chain or at the end of the single move, the turns will have flipped within data
							//, the actionListener will be done
							//And control will pass back to the player.
												
							
							//long startMillis = System.currentTimeMillis(); -- can use this later to program in a time delay
							CheckersAITree thinker = new CheckersAITree(data); //QUESTION: does this change the board? Do I need to clone stuff?
							thinker.buildTree();
							String bestMoves = thinker.findBestMove(); //We ask our AI Tree what the best move is
							//We are apparently getting a null pointer exception on this next line!
							String[] arrMoves = bestMoves.split(","); //Divides up the string of moves by comma
							System.out.println("The best move is: " + bestMoves);
							for(int i=0; i<arrMoves.length-1; i++)
							{
								System.out.println("AI moving once.");
								data.makeMove(Integer.parseInt(arrMoves[i]),Integer.parseInt(arrMoves[i+1]));
								publish(data);
								Thread.sleep(500);
							}
							publish(data);
						}
						//Possible issue: if the computer has no moves, what happens? Probably nothing wrong.
					}
				}
				return null;
			}
			
			protected void done() {
				int winner = data.checkWinner();
				if(winner==1)
				{
					System.out.println("The Computer has Triumphed. RIP.");
				}
				else if(winner==2)
				{
					System.out.println("You beat the computer! Congratulations!");
				}
			}
			
			protected void process(List<CheckersOp> chunks) {
				CheckersOp state = chunks.get(chunks.size()-1);
				drawBoard(state);
			}
			
		};
		playWorker.execute();
	}
	
	public static void main(String[] args)
	{
		CheckersGUI game = new CheckersGUI("First Game!", false);
		game.play();
	}

}

