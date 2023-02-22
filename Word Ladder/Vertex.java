package assignment3;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class Vertex {
    String word;
    static char[] alpha = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    Vertex(String word) {
        this.word = word;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Vertex other = (Vertex) obj;
        return this.word.equals(other.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }

    /**
     * Counts number of differences between two words of the same length
     * @param other vertex to be compared to
     * @return returns number of characters different between word1 & word2
     */
    public int getDistance(Vertex other) {
        int distance = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != other.word.charAt(i)) {
                distance++;
            }
        }
        return distance;
    }

    public ArrayList<Vertex> getNeighbors(Set<String> dict) {
        ArrayList<Vertex> neighbors = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
		    for (char c : alpha) {
		        String newWord = word.substring(0, i) + c + word.substring(i + 1);
		        if (!word.equals(newWord)) {
		            if (dict.contains(newWord.toUpperCase())) {
		                neighbors.add(new Vertex(newWord));
		            }
		        }
		    }
		}
        return neighbors;
    }
}
