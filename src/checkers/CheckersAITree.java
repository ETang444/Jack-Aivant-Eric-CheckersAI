import java.util.*;

public class CheckersAITree 
{
	private CheckersAINode root;
	
	public CheckersAITree(CheckersOp inputBoard)
	{
		root = new CheckersAINode(inputBoard, false, null);
	}
	
	public void buildTree()
	{
		root.buildTree(0);
	}
	
	public String findBestMove() //Returns the sequence of best moves
	{
		root.buildTree(0);
		root.value();
		return root.bestNextMove;
	}
}
