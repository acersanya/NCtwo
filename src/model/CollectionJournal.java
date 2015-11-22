package model;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

/**
 * @author acersanya
 * CollectionJournal is Journal implementation, using
 * default collection ArrayList
 */
public class CollectionJournal implements Journal {

	private ArrayList<Record> recordList;

	public CollectionJournal() {
		this.recordList = new ArrayList<>();
	}


	/**
	 * Add record to Journal
	 */
	@Override
	public void add(Record r) {
		if (r != null) {
			recordList.add(r);
		}
	}

	@Override
	/**
	 * Add one Journal to another Journal
	 */
	public void add(Journal j) {
		recordList.addAll(j.getRecords());
	}

	/**
	 * Get list of all records from Journal
	 */
	public ArrayList<Record> getRecords() {
		return recordList;
	}
	
	

	/**
	 * Remove first Record from Journal
	 */
	@Override
	public void remove(Record r) {
		Iterator<Record> iter = recordList.iterator();
		while (iter.hasNext()) {
			if (iter.equals(r)) {
				iter.remove();
			}
			iter.next();
		}
	}

	/**
	 * Get Record by index
	 */
	@Override
	public Record get(int index) {
		return recordList.get(index);
	}

	/**
	 * Replace Record in index position
	 */
	@Override
	public void set(int index, Record record) {
		recordList.set(index, record);
	}

	/**
	 * insert Record and shift other records to the right 
	 * for one position
	 */
	@Override
	public void insert(int index, Record record) {
		recordList.add(index, record);
	}

	/**
	 * Remove Record by index
	 */
	@Override
	public void remove(int index) {
		recordList.remove(index);
	}

	/**
	 * Remove record from index to index
	 */
	@Override
	public void remove(int fromIndex, int toIndex) {
		for (int i = fromIndex; i <= toIndex; i++) {
			recordList.remove(i);
		}
	}

	@Override
	/**
	 * Remove all records from Journal
	 */
	public void removeAll() {
		recordList.clear();
	}

	/**
	 * get Journal size
	 */
	@Override
	public int size() {
		return recordList.size();
	}
	

	/**
	 * filter Journal by some expression
	 */
	@Override
	public Journal filter(String s) {
		Journal journal = new CollectionJournal();
		for (Record i : recordList) {
			if (containsIgnoreCase(i.toString(), s)) {
				journal.add(i);
			}
		}
		return journal;
	}


	
	/**
	 * Filter records by date
	 */
	@Override
	public Journal filter(Date fromDate, Date toDate) {
		Journal journal = new CollectionJournal();
		for (Record i : recordList) {
			if (i.getDate().after(fromDate) && i.getDate().before(toDate)) {
				journal.add(i);
			}
		}
		return journal;
	}

	
	/**
	 * Anonymous class for Comparator
	 */
	public static final Comparator<Record> COMPARE_DATE = new Comparator<Record>() {
		
		@Override
		public int compare(Record o1, Record o2) {
			return o1.getDate().compareTo(o2.getDate());
		}
	};
	
	/**
	 * Sort records by date
	 */
	@Override
	public void sortByDate() {
	 Collections.sort(this.recordList,COMPARE_DATE);
	}

	/**
	 * Sort by Importance Level, then sort by Date
	 */
	@Override
	public void sortByImportanceDate() {
		Collections.sort(recordList, Comparator.comparing(Record::getImportLevel).thenComparing(Record::getDate));
	}

	/**
	 * Sort by Importance level then sort by message sort after that sort by date
	 */
	@Override
	public void sortByImportanceSortDate() {
		Collections.sort(recordList,Comparator.comparing(Record::getImportLevel).thenComparing(Record::getMessageSource).thenComparing(Record::getDate));
	}

	/**
	 * Sort by source message, then sort by date
	 */
	@Override
	public void sortBySourceDate() {
		Collections.sort(recordList,Comparator.comparing(Record::getMessageSource).thenComparing(Record::getDate));
	}
	
	/**
	 * Print all records
	 */
	@Override
	public void printRecords() {
		for(Record i: recordList){
			System.out.println(i);
		}

	}

}
