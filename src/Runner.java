import java.awt.Toolkit;
import java.util.Date;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import gameoflife.ConwaysGameOfLife;
import model.ArrayJournal;
import model.CollectionJournal;
import model.Journal;
import model.Record;
import gameoflife.*;

public class Runner {
	
	/**
	 * Game of life initializer 
	 */
	public static void gameOfLife() {
		ConwaysGameOfLife game = new ConwaysGameOfLife();
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setTitle("Conway's Game of Life");

		game.setSize(ConwaysGameOfLife.DEFAULT_WINDOW_SIZE);
		game.setMinimumSize(ConwaysGameOfLife.MINIMUM_WINDOW_SIZE);
		game.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - game.getWidth()) / 2,
				(Toolkit.getDefaultToolkit().getScreenSize().height - game.getHeight()) / 2);
		game.setVisible(true);

	}

	public static void main(String[] args) {
		Journal journal = new CollectionJournal();


		Record a1 = new Record("2005-12-12 12:12:12 level_one test test");
		Record a2 = new Record("1994-10-12 12:12:12 level_one test test");
		Record a3 = new Record("2006-1-12 12:12:12 level_one hello test");
		Record a4 = new Record("1992-5-12 12:12:12 level_one hello test");
		journal.add(a1);
		journal.add(a2);
		journal.add(a3);
		journal.add(a4);
		
		journal.sortByDate();
		
	
		
		
		for(Record i: journal.getRecords()){
			System.out.println(i);
		}

		
		gameOfLife();
		

	}



}
