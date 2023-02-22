package assignment3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class BFS {
	
	class Node {
		public String word;		// Word to be stored
		public Node parent;		// Node that called this node
		public boolean visited;	// Has node been searched yet
		
		/**
		 * Node Constructor for use when parent node is unknown
		 * @param word string word to be stored in node
		 */
		public Node(String word) {
			this.word = word;
			this.parent = null;
			this.visited = false;
		}
		
		/**
		 * Node Constructor for use when parent node is known
		 * @param word string word to be stored in node
		 * @param parent node's parent
		 */
		public Node(String word, Node parent) {
			this.word = word;
			this.parent = parent;
			this.visited = false;
		}
		
		public void setParent(Node parent) {
			this.parent = parent;
		}
		
		public Node getParent() {
			return this.parent;
		}
		
		public String getWord() {
			return this.word;
		}
		
		public void setWord(String word) {
			this.word = word;
		}
		
		public void visit() {
			visitedSet.add(this.word);
		}
		
		public boolean visited() {
			return visitedSet.contains(this.word);
		}
		
	}
	
	public Queue<Node> frontierQueue;	//Stores next nodes to be traversed
	ArrayList<String> orderedLadder;	//Stores final ordered ladder
	Set<String> visitedSet = new HashSet<String>(); // Stores visited words
	
	
	public BFS() {
		this.frontierQueue = new LinkedList<Node>();
		this.orderedLadder = new ArrayList<String>();
	}
	
	/** 
	 * Returns ladder as ArrayList (startWord -> endWord) using BFS
	 * or empty ArrayList if no ladder found.
	 * @param startWord the starting word for the ladder
	 * @param endWord the ending word for the ladder
	 * @param dict the dictionary of available words for the ladder
	 * @return ladder as ArrayList(startWord -> endWord)
	**/ 
	public ArrayList<String> getLadderBFS(String startWord, String endWord, Set<String> dict) {
		
		Stack<String> ladder = new Stack<String>();	// FILO to help undo ladder
		
		// Add starting node to queue and mark as visited
		Node wordNode = new Node(startWord);
		this.frontierQueue.add(wordNode);
		wordNode.visit();
		
		// While there are still nodes to be checked in queue
		while (!this.frontierQueue.isEmpty()) {
			// Get next node in queue
			Node currentV = this.frontierQueue.remove();
			// Get nodes neighbors
			ArrayList<Node> neighborsList = getNeighbors(currentV, dict);
			// Traverse through neighbors
			for (Node adjV:neighborsList) {
				// Add to queue if node hasn't been visited (Account for start having been visited)
				if (!adjV.visited() && !adjV.getWord().equals(startWord)) {
					this.frontierQueue.add(adjV);
					adjV.visit();
				}
				
				//If last word found
				if (adjV.getWord().equals(endWord)) {
					// Trace back lineage through parent nodes (endWord -> parent -> parent -> ... -> startWord)
					while(adjV.getParent() != null) {
						ladder.push(adjV.parent.getWord());
						adjV = adjV.parent;
					}
					
					// Store ladder in startWord -> endWord order as ArrrayList
					while(!ladder.isEmpty()) {
						this.orderedLadder.add(ladder.pop());
					}
					// Add end word to end of ladder
					orderedLadder.add(endWord);
					return this.orderedLadder;
				}
			}
		}
		
		// No ladder found, return start and end word only
		this.orderedLadder.add(startWord);
		this.orderedLadder.add(endWord);
		return this.orderedLadder;
	}

	
	/**
	 * Returns ArrayList containing all the neighbors of a node.
	 * Neighbors are those words which only change 1 char from
	 * given word.
	 * @param wordNode node whose neighbors are to be found
	 * @param dict the dictionary of available words
	 * @return all neighboring nodes in ArrayList
	**/
	 public ArrayList<Node> getNeighbors(Node wordNode, Set<String> dict) {
		 	char[] alpha = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	        ArrayList<Node> neighbors = new ArrayList<>();
	        for (int i = 0; i < wordNode.getWord().length(); i++) {
			    for (char c : alpha) {
			        String newWord = wordNode.getWord().substring(0, i) + c + wordNode.getWord().substring(i + 1);
			        if (!wordNode.getWord().equals(newWord) && dict.contains(newWord.toUpperCase())) {
			        	neighbors.add(new Node(newWord, wordNode));
			        }
			    }
			}
	        return neighbors;
	    }
}


