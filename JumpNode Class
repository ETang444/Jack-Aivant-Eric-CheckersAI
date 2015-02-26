//This is a node of the JumpTree
//So after a piece can make a confirmed jump, we create a jump tree to see if it can jump any further
//Each fork in the tree corresponds to a new path the piece could take.
//And thus, by finding all the leaf nodes, we find all possible jump chains.
//Every chain is represented by a String: "Y1X1,Y2X2,Y3X3 . . ."

public class JumpNode 
{
	public CheckersOp currBoard;
	//I made these next ones public to save the trouble of 16 getter and setter methods.
	//Technically it's less secure, but . . .
	//No one's going to try and hack our CheckersAI for malevolent purposes
	public JumpNode upLeft;
	public JumpNode upRight;
	public JumpNode downLeft;
	public JumpNode downRight;
	public int currPiece;
	public String howWeGotHere;
	
	public JumpNode(CheckersOp inputBoard, int inputPiece, String inputHowWeGotHere)
	{
		currBoard = new CheckersOp(inputBoard);
		currPiece = inputPiece;
		upLeft = null;
		upRight = null;
		downLeft = null;
		downRight = null;
		howWeGotHere = inputHowWeGotHere;
	}
	
}
