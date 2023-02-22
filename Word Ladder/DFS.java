package assignment3;

import java.util.*;

public class DFS {

    static Set<String> dict;

    DFS (Set<String> dict) {
        DFS.dict = dict;
    }

    /**
     * Sorts the words in an arrayList from the word with the least number of differences
     * to the word with the most number of differences to the given word.
     * @param words list of words to be sorted
     * @param end word to be compared to
     * @return returns sorted array of words
     */
    public static ArrayList<Vertex> sortWords(ArrayList<Vertex> words, Vertex end) {
        ArrayList<Vertex> sorted = new ArrayList<>();
        for (Vertex word : words) {
            int index = 0;
            for (Vertex sortedWord : sorted) {
                if (word.getDistance(end) < sortedWord.getDistance(end)) {
                    break;
                }
                index++;
            }
            sorted.add(index, word);
        }
        return sorted;
    }

    /**
     * Helper function for depth first search
     * @param start starting word
     * @param end ending word
     * @param path current path to word
     * @param visited set containing visited words
     * @return returns true if ladder successful, false otherwise
     */
    public static boolean DFSHelper(Vertex start, Vertex end, ArrayList<Vertex> path, ArrayList<Vertex> visited) {
        if (start == null)
            return false;
        path.add(start);
        visited.add(start);
        if (start.equals(end)) {
            return true;
        }else {
            ArrayList<Vertex> neighbors = sortWords(start.getNeighbors(dict), end);
            for (Vertex neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    if (DFSHelper(neighbor, end, path, visited)) {
                        return true;
                    } else {
                        path.remove(neighbor);
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<String> getDFSLadder(String start, String end) {
        // Returned list should be ordered start to end.  Include start and end.
        // If ladder is empty, return list with just start and end.
        // TODO some code
        ArrayList<Vertex> path = new ArrayList<>();
        ArrayList<Vertex> visited = new ArrayList<>();
        Vertex startVertex = new Vertex(start);
        Vertex endVertex = new Vertex(end);
        DFSHelper(startVertex, endVertex, path, visited);

        // convert visited to ArrayList<String>
        ArrayList<String> result = new ArrayList<>();
        for (Vertex v : path) {
            result.add(v.word);
        }

        //No ladder found. Create list with start and end word only
        if (!result.get(result.size() - 1).equals(end)) {
            result.clear();
            result.add(start);
            result.add(end);
        }

        return result;
    }
}
