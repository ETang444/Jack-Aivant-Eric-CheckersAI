import java.util.*;

//It is, indeed, a tree within a treeeeeeeee
//This tree organizes a bunch of JumpNodes
//To form the AI's processing of all possible jump chains
public class JumpTree 
{
	private JumpNode root;
	private ArrayList<String> allJumpChains; //our methods will fill these in
	
	public JumpTree(CheckersOp inputBoard, int inputPiece, String inputHowWeGotHere)
	{
		//So the tree takes three inputs:
		//The current board, the number of the piece we're trying to move, and the first jump (String HowWeGotHere))
		CheckersOp tempBoard = new CheckersOp(inputBoard); //We need to clone inputBoard here
		root = new JumpNode(tempBoard, inputPiece, inputHowWeGotHere);
		allJumpChains = new ArrayList<String>();
	}
	
	private void fillInKids(JumpNode rt)
	{
		//In theory, we could optimize this to only check backwards and forwards jumps for the correct colors
		if(rt==null)
			return;
		String howWeGotHere = rt.howWeGotHere;
		int currPiece = rt.currPiece;
		//This next part basically scrolls through all possible jumps for that piece
		//And checks to see if they're valid
		//If any given move is valid, it creates a temporary board, moves on that board, then
		//Then assigns the root's child to a new JumpNode using that board, the new piece, and the appended series of moves
		if(rt.currBoard.checkValidMove(currPiece, currPiece+22)==2)
		{
			CheckersOp tempBoard = new CheckersOp(rt.currBoard);
			tempBoard.makeMove(currPiece, currPiece+22);
			rt.downRight = new JumpNode(tempBoard, currPiece+22, howWeGotHere+","+(currPiece+22));
			fillInKids(rt.downRight);
		}
		if(rt.currBoard.checkValidMove(currPiece, currPiece+18)==2)
		{
			CheckersOp tempBoard = new CheckersOp(rt.currBoard);
			tempBoard.makeMove(currPiece, currPiece+18);
			rt.downLeft = new JumpNode(tempBoard, currPiece+18, howWeGotHere+","+(currPiece+18));
			fillInKids(rt.downLeft);
		}
		if(rt.currBoard.checkValidMove(currPiece, currPiece-18)==2)
		{
			CheckersOp tempBoard = new CheckersOp(rt.currBoard); 
			tempBoard.makeMove(currPiece, currPiece-18);
			rt.upRight = new JumpNode(tempBoard, currPiece-18, howWeGotHere+","+(currPiece-18));
			fillInKids(rt.upRight);

		}
		if(rt.currBoard.checkValidMove(currPiece, currPiece-22)==2)
		{
			CheckersOp tempBoard = new CheckersOp(rt.currBoard);
			tempBoard.makeMove(currPiece, currPiece-22);
			rt.upLeft = new JumpNode(tempBoard, currPiece-22, howWeGotHere+","+(currPiece-22));
			fillInKids(rt.upLeft);
		}
		//Should we add commas between move chains? Yep.
	}
	
	
	private void getAllJumpChainsHelp(JumpNode rt)
	{
		if(rt==null)
			return;
		if(rt.downRight==null&&rt.downLeft==null&&rt.upRight==null&&rt.upLeft==null)
		{
			allJumpChains.add(rt.howWeGotHere);
		}
		else
		{
			getAllJumpChainsHelp(rt.downRight);
			getAllJumpChainsHelp(rt.downLeft);
			getAllJumpChainsHelp(rt.upRight);
			getAllJumpChainsHelp(rt.upLeft);
		}
	}
	
	public ArrayList<String> getAllJumpChains()
	{
		fillInKids(root);
		getAllJumpChainsHelp(root);
		return allJumpChains;
	}
	
}
