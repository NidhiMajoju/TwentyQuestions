import com.sun.org.apache.xpath.internal.operations.Gt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;

/**
 * A model for the game of 20 questions
 *
 * @author Rick Mercer
 */

public class GameTree
{
	private GTNode root;
	private GTNode current;
	private String fileName;
	private class GTNode
	{
		String val;
		GTNode left, right;
		public GTNode (String val)
		{
			this.val = val;
			left = right = null;
		}
	}
	/**
	 * Constructor needed to create the game.
	 *
	 * @param fileName
	 *          this is the name of the file we need to import the game questions
	 *          and answers from.
	 */

	public GameTree(String fileName)
	{
		//TODO
		try
		{
			Scanner scan = new Scanner(new File(fileName));
			this.fileName = fileName;
			root = new GTNode(scan.nextLine());
			root = loadTree(scan, root);
			current = root;
		}
		catch (FileNotFoundException e)
		{
			System.out.println ("File not Found");
		}
	}
	private GTNode loadTree (Scanner input, GTNode node) {
		if (!input.hasNextLine())
		{
			return node;
		}
		if (node == null)
		{
			node = new GTNode(input.nextLine()) ;
			if (!node.val.contains("?"))
			{
				return node;
			}
		}
		node.left = loadTree(input, node.left);
		node.right = loadTree(input, node.right);
		return node;
	}

	/*
	 * Add a new question and answer to the currentNode. If the current node has
	 * the answer chicken, theGame.add("Does it swim?", "goose"); should change
	 * that node like this:
	 */
	// -----------Feathers?-----------------Feathers?------
	// -------------/----\------------------/-------\------
	// ------- chicken  horse-----Does it swim?-----horse--
	// -----------------------------/------\---------------
	// --------------------------goose--chicken-----------
	/**
	 * @param newQ
	 *          The question to add where the old answer was.
	 * @param newA
	 *         -
	 */
	public void add(String newQ, String newA)
	{
		String oldA = current.val;
		current.val = newQ;
		current.left = new GTNode(newA);
		current.right = new GTNode(oldA);
	}

	/**
	 * True if getCurrent() returns an answer rather than a question.
	 *
	 * @return False if the current node is an internal node rather than an answer
	 *         at a leaf.
	 */
	public boolean foundAnswer()
	{
		return !(current.val.contains("?"));
	}

	/**
	 * Return the data for the current node, which could be a question or an
	 * answer.  Current will change based on the users progress through the game.
	 *
	 * @return The current question or answer.
	 */
	public String getCurrent()
	{
		return current.val; //replace
	}

	/**
	 * Ask the game to update the current node by going left for Choice.yes or
	 * right for Choice.no Example code: theGame.playerSelected(Choice.Yes);
	 *
	 * @param yesOrNo
	 */
	public void playerSelected(Choice yesOrNo)
	{
		//TODO
		if (yesOrNo == Choice.Yes)
		{
			current = current.left;
		}
		else
		{
			current = current.right;
		}
	}

	/**
	 * Begin a game at the root of the tree. getCurrent should return the question
	 * at the root of this GameTree.
	 */
	public void reStart()
	{
		current = this.root;
	}

	@Override
	public String toString()
	{
		return toStringHelper(root, 0);

	}
	private String toStringHelper (GTNode node, int depth)
	{
		if (node == null)
		{
			return "";
		}
		String str = "";
		str += toStringHelper(node.right, depth +1);
		int a = 0;
		while (a<depth)
		{
			str += "- ";
			a++;
		}
		str += node.val +"\n";
		str +=  toStringHelper(node.left, depth +1);
		return str;
	}

	/**
	 * Overwrite the old file for this gameTree with the current state that may
	 * have new questions added since the game started.
	 *
	 */
	public void saveGame()
	{
		String outputFileName = fileName;
		PrintWriter diskFile = null;
		try
		{
			diskFile = new PrintWriter(new File(outputFileName));
			saveGameHelper(diskFile, root);
		}
		catch (IOException io)
		{
			System.out.println("Could not create file: " + outputFileName);
		}
		//close the file
		diskFile.close();
	}
	private void saveGameHelper (PrintWriter diskFile, GTNode node)
	{
		if (node == null)
		{
			return;
		}
		diskFile.println(node.val);
		saveGameHelper(diskFile, node.left);
		saveGameHelper(diskFile, node.right);
		return;
	}
}
