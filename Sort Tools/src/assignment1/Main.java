/*
 * This file is how you might test out your code.  Don't submit this, and don't
 * have a main method in SortTools.java.
 */

package assignment1;


public class Main {
    public static void main(String [] args) {
        // call your test methods here
        // SortTools.isSorted() etc.
    	//int[] x = new int[]{-5, -4, -3, -2, -1, 0};
    	//int n = x.length;
    	//SortTools.isSorted(x, n);
    	//int y = SortTools.find(x, n, -6);
    	//int[] y = SortTools.copyAndInsert(x, n, 1);
    	//int y = SortTools.insertInPlace(x, n, 1);
    	//int z = SortTools.insertInPlace(x, n, -6);
//    	//int[] y = new int[]{7, 3, 1, 3};
//    	//int m = 1;
//    	//SortTools.insertSort(y, m);
    	int[] x = new int[]{-5, -4, -3, -2, -1, 0};
        int[] original = x.clone();
        int n = x.length;

        SortTools.find(x, n, -3); // Should be 2
        // x should = original

    }
}
