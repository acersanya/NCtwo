package test;
import static org.junit.Assert.*;
import java.util.Date;
import org.junit.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.ArrayJournal;
import model.Record;

/**
 * 
 * @author acersanya
 * Testing some methods
 * Not full code coverage
 */
public class ArrayJournalTest {
protected Record a1= new Record(new Date(1994,4,12),"stumbONE",Record.ImportanceLevel.LEVEL_FOUR,"stumb");
protected Record a2= new Record(new Date(1994,4,12),"stumbONE",Record.ImportanceLevel.LEVEL_FOUR,"stumb");
protected ArrayJournal test = new ArrayJournal();





	@Before
	public void fill(){
		test.add(a1);
		test.add(a2);
	}


	@Test()
	public void testAddingToCollection() {
		assertTrue(test.size()== 2);
		fail();
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testException(){
		test.get(111);
	}
	
	
	@After
	public void remove(){
		test.removeAll();
	}

}
