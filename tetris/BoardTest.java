package tetris;

import static org.junit.Assert.*;

import org.junit.*;

public class BoardTest {
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated;

	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	@Before
	public void setUp() throws Exception {
		b = new Board(3, 6);
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		
		b.place(pyr1, 0, 0);
	}
	
	// Check the basic width/height/max after the one placement
	@Test
	public void testSample1() {
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}
	
	// Place sRotated into the board, then check some measures
	@Test
	public void testSample2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());

		assertTrue(b.getGrid(1, 1));
		assertTrue(b.getGrid(-1, 1));
		assertFalse(b.getGrid(2, 5));

		assertEquals(1, b.clearRows());
		assertEquals(3, b.getMaxHeight());
	}

	@Test
	public void testClearRows(){
		assertEquals(1, b.clearRows());
		assertEquals(1, b.getMaxHeight());
		System.out.println(b.toString());
	}

	@Test
	public void testClearRows1(){
		b.commit();
		b.place(s, 0, 2);
		Piece s2 = new Piece(Piece.S2_STR);
		b.commit();
		b.place(s2.computeNextRotation(), 0, 3);
		assertEquals(2, b.clearRows());
		assertEquals(4, b.getMaxHeight());
		assertEquals(3, b.heights[0]);
		assertEquals(4, b.heights[1]);
		assertEquals(0, b.heights[2]);
		assertEquals(1, b.widths[0]);
		assertEquals(2, b.widths[1]);
		assertEquals(2, b.widths[2]);
		assertEquals(1, b.widths[3]);
		assertEquals(0, b.widths[4]);
		System.out.println(b.toString());
	}

	// Make  more tests, by putting together longer series of 
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.

	@Test
	public void testDropHeight(){
		b.commit();
		b.place(s, 0, 2);
		b.commit();
		Piece s2 = new Piece(Piece.S2_STR);
		b.commit();
		b.place(s2.computeNextRotation(), 0, 3);
		b.clearRows();
		assertEquals(4, b.dropHeight(s2, 0));
		b.commit();
		b.place(s2, 0, 4);
		assertEquals(6, b.heights[0]);
		System.out.println(b.toString());
	}

	@Test
	public void testUndo(){
		b.undo();
		assertEquals(0, b.getMaxHeight());
		b.place(s, 0, 0);
		b.commit();
		b.place(pyr4, 0, 1);
		b.clearRows();
		assertEquals(3, b.getMaxHeight());
		assertEquals(3, b.heights[0]);
		System.out.println(b.toString());
		b.undo();
		System.out.println(b.toString());
	}

	@Test(expected = RuntimeException.class)
	public void testSanityCheck(){
		b.heights[1] = 3;
		b.sanityCheck();
	}

	@Test(expected = RuntimeException.class)
	public void testSanityCheck1(){
		b.widths[1] = 2;
		b.sanityCheck();
	}

	@Test
	public void testDropHeightExtraCase(){
		b.clearRows();
		Piece stick = new Piece(Piece.STICK_STR);
		b.commit();
		b.place(stick, 0, 0);
		b.commit();
		Piece square = new Piece(Piece.SQUARE_STR);
		b.place(square, 0, 4);
		b.commit();
		System.out.println(b.toString());
	}
}
