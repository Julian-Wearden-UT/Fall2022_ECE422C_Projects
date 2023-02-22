// SortTools.java

/*
 * EE422C Project 1 submission by
 * Julian Wearden
 * jfw864
 * 17615
 * Spring 2022
 * Slip days used: 0
 */

package assignment1;

public class SortTools {
    /**
      * Return whether the first n elements of x are sorted in non-decreasing
      * order.
      * @param x is the array
      * @param n is the size of the input to be checked
      * @return true if array is sorted
      */
    public static boolean isSorted(int[] x, int n) {
        for (int i = 1; i < n; i++) {
        	if (x[i] < x[i-1]) {		//if next is less than previous
        		return false;
        	}
        }
        return true;
    }

    /**
     * Return an index of value v within the first n elements of x.
     * @param x is the array
     * @param n is the size of the input to be checked
     * @param v is the value to be searched for
     * @return any index k such that k < n and x[k] == v, or -1 if no such k exists
     */
    public static int find(int[] x, int n, int v) {
    	int first = 0;
        int last = n - 1;
        int mid = (first+last)/2;
        while (first <= last) {
        	
        	if (v == x[mid]) {
        		return mid;
        	}
        	//In upper half
        	else if (v > x[mid]){
        		first = mid + 1;
        		mid = (first + last)/2;
        	}
        	else {
        		last = mid - 1;
        		mid = (first + last)/2;
        	}
        }
        return -1;
    }

    /**
     * Return a sorted, newly created array containing the first n elements of x
     * and ensuring that v is in the new array.
     * @param x is the array
     * @param n is the number of elements to be copied from x
     * @param v is the value to be added to the new array if necessary
     * @return a new array containing the first n elements of x as well as v
     */
    public static int[] copyAndInsert(int[] x, int n, int v) {
    	int[] y = new int[n+1];
    	boolean inserted = false;
    	for (int i = 0; i < n; i++) {
    		if (x[i] >= v && inserted != true) {
    			y[i] = v;
    			y[i + 1] = x[i];
    			inserted = true;
    		}
    		else if (inserted == true){
    			y[i+1] = x[i];
    		}
    		else {
    			y[i]=x[i];
    		}
    		
    	}
    	if (inserted != true) {
    		y[y.length - 1] = v;
    	}
        return y;
    }

    /**
     * Insert the value v in the first n elements of x if it is not already
     * there, ensuring those elements are still sorted.
     * @param x is the array
     * @param n is the number of elements in the array
     * @param v is the value to be added
     * @return n if v is already in x, otherwise returns n+1
     */
    public static int insertInPlace(int[] x, int n, int v) {
    	int[] y = new int[n+1];
    	boolean inserted = false;
    	boolean same = false;
    	for (int i = 0; i < n; i++) {
    		if (x[i] >= v && inserted != true) {
    			if (x[i] == v) {
    				same = true;
    			}
    			y[i] = v;
    			y[i + 1] = x[i];
    			inserted = true;
    		}
    		else if (inserted == true){
    			y[i+1] = x[i];
    		}
    		else {
    			y[i]=x[i];
    		}
    		
    	}
    	if (inserted != true) {
    		y[y.length - 1] = v;
    	}
        if (same == false) {
        	x = y;
        }
        
        return x.length;
    }

    /**
     * Sort the first n elements of x in-place in non-decreasing order using
     * insertion sort.
     * @param x is the array to be sorted
     * @param n is the number of elements of the array to be sorted
     */
    public static void insertSort(int[] x, int n) {
        for (int i = 1; i < n; i++) {
        	int swap = x[i];
        	
        	for (int j = i - 1; j >= 0; j--) {
        		if (x[j] > swap) {
        			x [j + 1] = x[j];
        			x[j] = swap;
        		}
        	}
        }
    }
}
