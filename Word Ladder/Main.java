/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * Hamza Mujtaba
 * hm25936
 * 17620
 * Julian Wearden
 * jfw864
 * 17615
 * Slip days used: <0>
 * Git URL: https://github.com/EE422C/fa-22-assignment-3-fa22-pair-30.git
 * Fall 2022
 */


package assignment3;

import java.util.*;
import java.io.*;

public class Main {

    // static variables and constants only here.
    static Set<String> dict;

    public static void main(String[] args) throws Exception {

        Scanner kb;    // input Scanner for commands
        PrintStream ps;    // output file, for student testing and grading only
        // If arguments are specified, read/write from/to files instead of Std IO.
        if (args.length != 0) {
            kb = new Scanner(new File(args[0]));
            ps = new PrintStream(new File(args[1]));
            System.setOut(ps);            // redirect output to ps
        } else {
            kb = new Scanner(System.in);// default input from Stdin
            ps = System.out;            // default output to Stdout
        }
        initialize();
        
        //Read in words
        ArrayList<String> input = parse(kb);
        while(input != null) {
//        	System.out.println(input);
        	
        	//Do DFS
            ArrayList<String> result = getWordLadderDFS(input.get(0), input.get(1));
            printLadder(result);
            
            //Do BFS
            ArrayList<String> resultBFS = getWordLadderBFS(input.get(0), input.get(1));
            printLadder(resultBFS);

            input = parse(kb);
        }
    }

    /**
     * Initializes static variables/constants
     * Created dict as a HashSet using makeDictionary
     */
    public static void initialize() {
        // initialize your static variables or constants here.
        // We will call this method before running our JUNIT tests.  So call it
        // only once at the start of main.
    	dict = new HashSet<>(makeDictionary());
    }

    /**
     * @param keyboard Scanner connected to System.in
     * @return ArrayList of Strings containing start word and end word.
     * If command is /quit, return empty ArrayList.
     */
    public static ArrayList<String> parse(Scanner keyboard) {
        ArrayList<String> words = new ArrayList<>();
        String word1 = keyboard.next();
        if (word1.equals("/quit"))
            return null;
        String word2 = keyboard.next();
        words.add(word1);
        words.add(word2);
        return words;
    }

    /**
     * Takes start and end words in lowercase, outputs ArrayList 
     * words in lowercase. Finds ladder using DFS. If there is no 
     * ladder, returns ArrayList with only start and end words.
     * @param start starting word
     * @param end ending word
     * @return returns ladder array list
     */
    public static ArrayList<String> getWordLadderDFS(String start, String end) {
        DFS myDFS = new DFS(dict);
        return myDFS.getDFSLadder(start, end);
    }

    /**
     * Takes start and end words in lowercase, outputs ArrayList 
     * words in lowercase. Finds ladder using BFS. If there is no 
     * ladder, returns ArrayList with only start and end words.
     * @param start starting word
     * @param end ending word
     * @return returns ladder array list
     */
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
    	BFS myBFS = new BFS();
        return myBFS.getLadderBFS(start, end, dict);
    }


    /**
     * Prints given ArrayList or no word ladder found if ladder = [startWord, endWord]
     * @param ladder ladder to be printed
     */
    public static void printLadder(ArrayList<String> ladder) {
        if (ladder.size() == 2) {
        	System.out.println("no word ladder can be found between " + ladder.get(0) + " and " + ladder.get(1) + ".");
        }
        else {
        	System.out.println("a " + (ladder.size() - 2) + "-rung word ladder exists between " + ladder.get(0) + " and " + ladder.get(ladder.size() - 1));
            // print each word in the ladder
            for (String word : ladder) {
                System.out.println(word);
            }
        }
    	
    }
    
    
    /**
     * Creates dictionary out of words in a text file
     * @return set of words in text file
     */
    /* Do not modify makeDictionary */
    public static Set<String> makeDictionary() {
        Set<String> words = new HashSet<String>();
        Scanner infile = null;
        try {
            infile = new Scanner(new File("five_letter_words.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("Dictionary File not Found!");
            e.printStackTrace();
            System.exit(1);
        }
        while (infile.hasNext()) {
            words.add(infile.next().toUpperCase());
        }
        return words;
    }
}
