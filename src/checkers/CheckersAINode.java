import java.util.*;

public class CheckersAINode 
{
	private CheckersOp board; //The current board position
	public ArrayList<CheckersAINode> children; //The next possible boards
	boolean isLeaf; //Stores if this node is a leaf (changes the value function)
	private double value;
	private String moves;
	public String bestNextMove;
	
	//IDEA: instead of keeping track of whether it follows the root, and howWeGotHere
	//We loop through the root's children, until we find the best value, and store this
	//Then we loop through the root's possible moves, counting up every time, until we reach the stored value
	//(This will thus be the best position to go to next, since they're added in the same order they're counted in)
	//Then we take that given move, and return it (or the given jump chain)
	//^^All these above ideas will be implemented in the tree class?
	
	public CheckersAINode(CheckersOp inputBoard, boolean inputIsLeaf, String moveSequence)
	{
		board = new CheckersOp(inputBoard);
		children = new ArrayList<CheckersAINode>();
		isLeaf = inputIsLeaf;
		moves=moveSequence;
		bestNextMove = null;
	}
	
	public CheckersOp getBoard()
	{
		return board;
	}
	
	public ArrayList<CheckersAINode> getChildren()
	{
		return children;
	}
	public String getMoves()
	{
		return moves;
	}
	
	public void buildTree(int currHeight)
	{
		//Now we build the subtree; this is a recursive function
		//For each of these, we need to run through every possible move and jump chain
		//Then we add all those possible following boards to children

		if(isLeaf)
		{
			return; //If it's the leaf, stop building
		}
		
		int currTurn = board.turn;
		System.out.println(currTurn);
		boolean nextIsLeaf = false;
		//Here's the code that changes the height of the tree! If a given node has less than 8 pieces,
		//The computer looks 6 moves deep instead of 5.
		if(board.rCount+board.bCount<8)
		{
			if(currHeight==5)
				nextIsLeaf = true;
		}
		else
		{
			if(currHeight==4) //Later we can change the height of this tree! For now it's 5 moves deep.
				nextIsLeaf = true;
		}
		
		//To change! 0 is black and 1 is red
		if(currTurn==1) //If it's the red pieces
		{
			for(int row=0; row<8; row++)
			{
				for(int col=0; col<8; col++)
				{
					addValidSquareMovesForRed(row,col,nextIsLeaf);
				}
			}
		}
		else if(currTurn==0)
		{
			for(int row=0; row<8; row++)
			{
				for(int col=0; col<8; col++)
				{
					addValidSquareMovesForBlack(row,col,nextIsLeaf);
				}
			}
		}
		for(CheckersAINode kid:children)
		{
			kid.buildTree(currHeight+1);
		}
	}
	
	public void addValidSquareMovesForRed(int row, int col, boolean nextIsLeaf)
	{
		int index = 10*row+col;
		
		//These are helper methods: find the possible moves for any given square
		if(board.getBoard()[row][col]==1)//If we found a normal red piece
		{
			//Later, we may optimize the searching order by putting the jumps first
			if(board.checkValidMove(10*row+col,10*(row+1)+col-1)==1) //Moving down-left
			{
				CheckersOp tempBoard = new CheckersOp(board); //Does this work? Will my changes in tempBoard affect board?
				tempBoard.makeMove(10*row+col,10*(row+1)+col-1);
				String lastMove=(10*row+col) + "," + (10*(row+1)+col-1);
				children.add(new CheckersAINode(tempBoard, nextIsLeaf, lastMove));
			}
			//And if moveDownLeft==0, then the move is invalid and we don't add anything
			else if(board.checkValidMove(10*row+col,10*(row+2)+col-2)==2) //Jumping down left (this is only possible when moving down-left is impossible)
			{
				//Do we need to make a TREE of all possible jumps?
				//Then we find all leaf nodes, and those are all the possible jump chains!
				//Working out jump chains (using the JumpTree):
				addJumperChildren(index,2,-2,nextIsLeaf);
			}
			
			if(board.checkValidMove(10*row+col,10*(row+1)+col+1)==1) //moving down-right
			{
				CheckersOp tempBoard = new CheckersOp(board); //QUESTION: will my changes to tempBoard affect board? -- Now they won't!
				tempBoard.makeMove(10*row+col,10*(row+1)+col+1);
				String lastMove=(10*row+col) + "," + (10*(row+1)+col+1);
				children.add(new CheckersAINode(tempBoard, nextIsLeaf, lastMove));
			}
			//Then, we test for jumping down-right (only possible if moving down-right is impossible)
			else if(board.checkValidMove(10*row+col,10*(row+2)+col+2)==2) //Jumping down-right
			{
				addJumperChildren(index,2,2,nextIsLeaf);
			}
		}
		else if(board.getBoard()[row][col]==3) //This means we found a red king!
		{
			if(board.checkValidMove(10*row+col,10*(row+1)+col-1)==1) //Moving down-left
			{
				CheckersOp tempBoard = new CheckersOp(board); //Does this work? Will my changes in tempBoard affect board?
				tempBoard.makeMove(10*row+col,10*(row+1)+col-1);
				String lastMove=(10*row+col) + "," + (10*(row+1)+col-1);
				children.add(new CheckersAINode(tempBoard, nextIsLeaf, lastMove));
			}
			else if(board.checkValidMove(10*row+col,10*(row+2)+col-2)==2) //Jumping down left
			{
				//Now we have to work out jump chains: just use a while loop to keep checking for chains (and/or DFS))
				//Once we've reached the end of a chain, we stop and add it to children
				addJumperChildren(index,2,-2,nextIsLeaf);

			}
			
			if(board.checkValidMove(10*row+col,10*(row+1)+col+1)==1) //moving down-right
			{
				CheckersOp tempBoard = new CheckersOp(board); //QUESTION: will my changes to tempBoard affect board?
				tempBoard.makeMove(10*row+col,10*(row+1)+col+1);
				String lastMove=(10*row+col) + "," + (10*(row+1)+col+1);
				children.add(new CheckersAINode(tempBoard, nextIsLeaf, lastMove));
			}
			else if(board.checkValidMove(10*row+col,10*(row+2)+col+2)==2) //Jumping down-right
			{
				addJumperChildren(index,2,-2,nextIsLeaf);

			}
			
			if(board.checkValidMove(10*row+col,10*(row-1)+col-1)==1) //Moving up-left
			{
				CheckersOp tempBoard = new CheckersOp(board); //Does this work? Will my changes in tempBoard affect board?
				tempBoard.makeMove(10*row+col,10*(row-1)+col-1);
				String lastMove=(10*row+col) + "," + (10*(row-1)+col-1);
				children.add(new CheckersAINode(tempBoard, nextIsLeaf, lastMove));
			}
			else if(board.checkValidMove(10*row+col,10*(row-2)+col-2)==2) //Jumping up-left
			{
				addJumperChildren(index,-2,-2,nextIsLeaf);
			}
			
			if(board.checkValidMove(10*row+col,10*(row-1)+col+1)==1) //moving up-right
			{
				CheckersOp tempBoard = new CheckersOp(board); //QUESTION: will my changes to tempBoard affect board?
				tempBoard.makeMove(10*row+col,10*(row-1)+col+1);
				String lastMove=(10*row+col) + "," + (10*(row-1)+col+1);
				children.add(new CheckersAINode(tempBoard, nextIsLeaf, lastMove));
			}
			else if(board.checkValidMove(10*row+col,10*(row-2)+col+2)==2) //Jumping up-right
			{
				addJumperChildren(index,-2,+2,nextIsLeaf);
			}
		}
	}
	
	public void addValidSquareMovesForBlack(int row, int col, boolean nextIsLeaf)
	{
		int index = 10*row + col;
		if(board.getBoard()[row][col]==2)//If we found a normal black piece
		{
			//Could optimize this by shuffling the if and else checks
			if(board.checkValidMove(10*row+col,10*(row-1)+col-1)==1) //Moving up-left
			{
				CheckersOp tempBoard = new CheckersOp(board); //Does this work? Will my changes in tempBoard affect board?
				tempBoard.makeMove(10*row+col,10*(row-1)+col-1);
				String lastMove=(10*row+col) + "," + (10*(row-1)+col-1);
				children.add(new CheckersAINode(tempBoard, nextIsLeaf, lastMove));
			}
			else if(board.checkValidMove(10*row+col,10*(row-2)+col-2)==2) //Jumping  up-left
			{
				//Now we have to work out jump chains: just use a while loop to keep checking for chains (and/or DFS))
				//Once we've reached the end of a chain, we stop and add it to children
				addJumperChildren(index,-2,-2,nextIsLeaf);
			}
			
			if(board.checkValidMove(10*row+col,10*(row-1)+col+1)==1) //moving up-right
			{
				CheckersOp tempBoard = new CheckersOp(board); //QUESTION: will my changes to tempBoard affect board?
				tempBoard.makeMove(10*row+col,10*(row-1)+col+1);
				String lastMove=(10*row+col) + "," + (10*(row-1)+col-1);
				children.add(new CheckersAINode(tempBoard, nextIsLeaf, lastMove));
			}
			else if(board.checkValidMove(10*row+col,10*(row-2)+col+2)==2) //Jumping up-right
			{
				addJumperChildren(index,-2,2,nextIsLeaf);
			}
		}
		
		else if(board.getBoard()[row][col]==4) //This means we found a red king!
		{
			if(board.checkValidMove(10*row+col,10*(row+1)+col-1)==1) //Moving down-left
			{
				CheckersOp tempBoard = new CheckersOp(board); //Does this work? Will my changes in tempBoard affect board?
				tempBoard.makeMove(10*row+col,10*(row+1)+col-1);
				String lastMove=(10*row+col) + "," + (10*(row+1)+col-1);
				children.add(new CheckersAINode(tempBoard, nextIsLeaf, lastMove));
			}
			else if(board.checkValidMove(10*row+col,10*(row+2)+col-2)==2) //Jumping down left
			{
				//Now we have to work out jump chains: just use a while loop to keep checking for chains (and/or DFS))
				//Once we've reached the end of a chain, we stop and add it to children
				addJumperChildren(index,2,-2,nextIsLeaf);

			}
			
			if(board.checkValidMove(10*row+col,10*(row+1)+col+1)==1) //moving down-right
			{
				CheckersOp tempBoard = new CheckersOp(board); //QUESTION: will my changes to tempBoard affect board?
				tempBoard.makeMove(10*row+col,10*(row+1)+col+1);
				String lastMove=(10*row+col) + "," + (10*(row+1)+col+1);
				children.add(new CheckersAINode(tempBoard, nextIsLeaf, lastMove));
			}
			else if(board.checkValidMove(10*row+col,10*(row+2)+col+2)==2) //Jumping down-right
			{
				addJumperChildren(index,2,2,nextIsLeaf);
			}
			
			if(board.checkValidMove(10*row+col,10*(row-1)+col-1)==1) //Moving up-left
			{
				CheckersOp tempBoard = new CheckersOp(board); //Does this work? Will my changes in tempBoard affect board?
				tempBoard.makeMove(10*row+col,10*(row-1)+col-1);
				String lastMove=(10*row+col) + "," + (10*(row-1)+col-1);
				children.add(new CheckersAINode(tempBoard, nextIsLeaf, lastMove));
			}	
			else if(board.checkValidMove(10*row+col,10*(row-2)+col-2)==2) //Jumping up-left
			{
				addJumperChildren(index,-2,-2,nextIsLeaf);
			}
			
			if(board.checkValidMove(10*row+col,10*(row-1)+col+1)==1) //moving up-right
			{
				CheckersOp tempBoard = new CheckersOp(board); //QUESTION: will my changes to tempBoard affect board?
				tempBoard.makeMove(10*row+col,10*(row-1)+col+1);
				String lastMove=(10*row+col) + "," + (10*(row-1)+col+1);
				children.add(new CheckersAINode(tempBoard, nextIsLeaf, lastMove));
			}
			else if(board.checkValidMove(10*row+col,10*(row-2)+col+2)==2) //Jumping up-right
			{
				//And once again, we have to work out jump chains
				addJumperChildren(index,-2,2,nextIsLeaf);

			}
		}
	}
	
	private void addJumperChildren(int startIndex, int changeInY, int changeInX, boolean isNextLeaf)
	{
		//Note: only create a jump chain when one jump has already been executed (the changeInY and changeInX)
		//This avoids creating way too  many unnecessary jump trees
		//So the parameters are the start, the first jump's changeInY, the first jump's changeInX, and nextIsLeaf
		//This creates a new board with that first jump already made.
		System.out.println("Now we're making a JumpTree");
		CheckersOp inputBoard = new CheckersOp(board);
		inputBoard.makeMove(startIndex, startIndex + 10*changeInY + changeInX);
		JumpTree treeOfHops = new JumpTree(inputBoard,startIndex+10*changeInY + changeInX,startIndex + "," + (startIndex+10*changeInY + changeInX)); //Could definitely have a jumper problem
		ArrayList<String> hops = treeOfHops.getAllJumpChains();
		for(String oneChainz: hops)
		{
			CheckersOp tempBoard = new CheckersOp(board);
			String[] theChain = oneChainz.split(",");
			for(int i=0; i<theChain.length-1;i++)
			{
				tempBoard.makeMove(Integer.parseInt(theChain[i]), Integer.parseInt(theChain[i+1]));
			}
			children.add(new CheckersAINode(tempBoard, isNextLeaf, oneChainz));
		}
	}
	
	public double value() //We'll edit this for alpha-beta pruning soon!
	//When we use alpha-beta pruning, we'll probably just throw out all the current code and make it afresh
	{
		//Returns the maximum of the children values if it's computer's turn
		//Or returns the minimum of the children values if it's player turn
		//Or if it's the leaf node, return the current position
		if(isLeaf)
		{
			//Then we need to return the evaluation of any given position
			return board.evaluateBoard();
		}
		else
		{
			if(children.size()==0)
			{
				if(board.turn==1)
					return Integer.MIN_VALUE; //Worst possible outcome: no possible moves for the computer!
				else
					return Integer.MAX_VALUE; //This is the best possible outcome! No possible moves for the human!

			}
			else
			{ //Note that the computer is player 1, red!
				if(board.turn==1) //It's the computer's turn, return the max possible next position
				{
					double best = children.get(0).value();
					bestNextMove = children.get(0).moves;
					for(CheckersAINode possibleNext: children)
					{
						double nextStep = possibleNext.value();
						if(nextStep>best)
						{
							best = nextStep;
							bestNextMove = possibleNext.moves;
						}
					}
					return best;
				}
				else //otherwise, it's opponent's turn; return the worst (their best move)
				{
					double theirBest = children.get(0).value();
					bestNextMove = children.get(0).moves;
					for(CheckersAINode possibleNext: children)
					{
						double nextStep = possibleNext.value();
						if(nextStep<theirBest)
						{
							theirBest = nextStep;
							bestNextMove = possibleNext.moves;
						}
					}
					return theirBest;
				}
			}
		}
	}
}