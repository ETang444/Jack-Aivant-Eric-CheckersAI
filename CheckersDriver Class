//This is the starting screen, where you choose 1-player or two-player game
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
public class CheckersDriver extends JFrame implements ActionListener
{
	public CheckersDriver(String title)
	{
		super(title);
		setSize(400,200);
		setLayout(new GridLayout(2,1));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JButton onePlayer = new JButton("One Player Game");
		add(onePlayer);
		onePlayer.addActionListener(this);
		onePlayer.setVisible(true);
		onePlayer.setActionCommand("One Player Game");
		
		JButton twoPlayer = new JButton("Two Player Game");
		add(twoPlayer);
		twoPlayer.addActionListener(this);
		twoPlayer.setVisible(true);
		twoPlayer.setActionCommand("Two Player Game");
	}
	
	public void actionPerformed(ActionEvent evt)
	{
		if(evt.getActionCommand().equals("One Player Game"))
		{
			CheckersGUIPruning game = new CheckersGUIPruning("Human vs. Computer", false);
			game.setVisible(true);
		}
		else if(evt.getActionCommand().equals("Two Player Game"))
		{
			CheckersGUIPruning game = new CheckersGUIPruning("Human vs. Human", true);
			game.setVisible(true);
		}
		this.setVisible(false);
		this.dispose();
	}
	
	public static void main(String[] args)
	{
		CheckersDriver hello = new CheckersDriver("Welcome! One or Two Player Game?");
		hello.setVisible(true);
	}
}
