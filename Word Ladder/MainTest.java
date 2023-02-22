package assignment3;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainTest {

	private static Set<String> dict;
	private static ByteArrayOutputStream outContent;

	private static final int SHORT_TIMEOUT = 300; // ms
	private static final int SEARCH_TIMEOUT = 30000; // ms

	private boolean verifyLadder(ArrayList<String> ladder, String start, String end) {
		String prev = null;
		if (ladder == null)
			return true;
		for (String word : ladder) {
			if (!dict.contains(word.toUpperCase()) && !dict.contains(word.toLowerCase())) {
				return false;
			}
			if (prev != null && !differByOne(prev, word))
				return false;
			prev = word;
		}
		return ladder.size() > 0
				&& ladder.get(0).toLowerCase().equals(start)
				&& ladder.get(ladder.size() - 1).toLowerCase().equals(end);
	}

	private static boolean differByOne(String s1, String s2) {
		if (s1.length() != s2.length())
			return false;

		int diff = 0;
		for (int i = 0; i < s1.length(); i++) {
			if (s1.charAt(i) != s2.charAt(i) && diff++ > 1) {
				return false;
			}
		}

		return true;
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		Main.initialize();
		dict = Main.makeDictionary();
		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
	}

	@After
	public void tearDown() throws Exception {
	}

	// test a very long word ladder over 500 words and check for duplicates and runtime
	@Test(timeout = SHORT_TIMEOUT)
	public void testLongDFSLadder() {
		ArrayList<String> res = Main.getWordLadderDFS("iller", "nylon");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(verifyLadder(res, "iller", "nylon"));
		assertFalse(res == null || res.size() == 0 || res.size() == 2);
	}

	// test a short word ladder (4 words maximum) to check efficiency of optimization algorithm for dfs
	@Test(timeout = SHORT_TIMEOUT)
	public void testShortDFSLadder() {
		ArrayList<String> res = Main.getWordLadderDFS("elves", "piled");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(verifyLadder(res, "elves", "piled"));
		assertFalse(res == null || res.size() == 0 || res.size() == 2);
		assertTrue(res.size() <= 6);
	}

	// test a pair of words that don't have a ladder between them with DFS
	@Test(timeout = SEARCH_TIMEOUT)
	public void testNoDFSLadder() {
		ArrayList<String> res = Main.getWordLadderDFS("books", "zebra");
		outContent.reset();
		Main.printLadder(res);
		String str = outContent.toString().replace("\n", "").replace(".", "").trim();
		assertEquals("no word ladder can be found between books and zebra", str);
	}

	// test a ladder that maximizes runtime of getNeighbors by going from a to z using DFS
	@Test(timeout = SHORT_TIMEOUT)
	public void testAZDFSLadder() {
		ArrayList<String> res = Main.getWordLadderDFS("aahed", "zymes");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(verifyLadder(res, "aahed", "zymes"));
		assertFalse(res == null || res.size() == 0 || res.size() == 2);
	}

	// test a ladder using a pair of words that are vry similar to each other using DFS
	@Test(timeout = SHORT_TIMEOUT)
	public void testSimilarDFSLadder() {
		ArrayList<String> res = Main.getWordLadderDFS("worth", "wroth");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(verifyLadder(res, "worth", "wroth"));
		assertFalse(res == null || res.size() == 0 || res.size() == 2);
	}

	// test the longest possible word ladder and check for duplicates and runtime. Also, it should be at most 29 rungs long.
	@Test(timeout = SHORT_TIMEOUT)
	public void testLongBFSLadder() {
		ArrayList<String> res = Main.getWordLadderBFS("iller", "nylon");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(verifyLadder(res, "iller", "nylon"));
		assertFalse(res == null || res.size() == 0 || res.size() == 2);
		assertTrue(res.size() <= 31);
	}

	// test a short word ladder (4 words maximum) to check that bfs is finding the shortest path
	@Test(timeout = SHORT_TIMEOUT)
	public void testShortBFSLadder() {
		ArrayList<String> res = Main.getWordLadderBFS("elves", "piled");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(verifyLadder(res, "elves", "piled"));
		assertFalse(res == null || res.size() == 0 || res.size() == 2);
		assertTrue(res.size() <= 6);
	}

	// test a pair of words that don't have a ladder between them with BFS
	@Test(timeout = SHORT_TIMEOUT)
	public void testNoBFSLadder() {
		ArrayList<String> res = Main.getWordLadderBFS("books", "zebra");
		outContent.reset();
		Main.printLadder(res);
		String str = outContent.toString().replace("\n", "").replace(".", "").trim();
		assertEquals("no word ladder can be found between books and zebra", str);
	}

	// test a ladder that maximizes runtime of getNeighbors by going from a to z using BFS
	@Test(timeout = SHORT_TIMEOUT)
	public void testAZBFSLadder() {
		ArrayList<String> res = Main.getWordLadderBFS("aahed", "zymes");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(verifyLadder(res, "aahed", "zymes"));
		assertFalse(res == null || res.size() == 0 || res.size() == 2);
	}

	// test a ladder using a pair of words that are vry similar to each other using BFS
	@Test(timeout = SHORT_TIMEOUT)
	public void testSimilarBFSLadder() {
		ArrayList<String> res = Main.getWordLadderBFS("worth", "wroth");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(verifyLadder(res, "worth", "wroth"));
		assertFalse(res == null || res.size() == 0 || res.size() == 2);
	}
}