package model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;


/**
 * @author acersanya
 * ArrayJournal class which implemented Journal interface
 *  architecture of Journal is based on simple arrays
 */
public class ArrayJournal implements Journal {

	private static final int DEFAULT_SIZE = 10;
	private static final Record[] EMPTY_ELEMENT_DATA = {};
	private Record[] recordArray;
	private int size;
	private Record[] temp;
	static private Comparator<Record> dateCompartor;

	/**
	 * Default Constructor Create array with default size
	 */
	public ArrayJournal() {
		recordArray = new Record[DEFAULT_SIZE];
		size = 0;
	}

	
	/**
	 * Getter
	 * @return record
	 */
	public Record[] getRecrods(){
		return recordArray;
	}
	
	
	/**
	 * Not default constructor with arguments
	 * 
	 * @param capacity
	 *            ArrayJournal size
	 */
	public ArrayJournal(int capacity) {
		size = 0;
		if (capacity > 0) {
			recordArray = new Record[capacity];
		}
		if (capacity == 0) {
			recordArray = EMPTY_ELEMENT_DATA;
		} else {
			throw new IllegalArgumentException("Illegal Capacity: " + capacity);
		}
	}

	/**
	 * Checks if array size is enough for holding Records if not array size will
	 * be multiplied by two
	 */
	private void ensureCapacity() {
		if (size == recordArray.length || size == recordArray.length - 1) {
			temp = new Record[recordArray.length * 2];
			System.arraycopy(recordArray, 0, temp, 0, recordArray.length);
			recordArray = temp;
		}
	}

	/**
	 * Checks if array size is enough for holding records from current and
	 * another journal
	 * 
	 * @param len
	 *            journal length
	 */
	private void ensureCapacityJournal(int len) {
		int journalLen = len + size;
		if (journalLen > recordArray.length) {
			temp = new Record[journalLen];
			System.arraycopy(recordArray, 0, temp, 0, size);
			recordArray = temp;
		}
	}

	/**
	 * Add Record to ArrayJournal Ensures Capacity if needed
	 */
	@Override
	public void add(Record r) {
		ensureCapacity();
		recordArray[size++] = r;
	}

	/**
	 * Copy one Journal into another one
	 * 
	 */
	@Override
	public void add(Journal j) {
		if (j.size() != 0) {
			temp = new Record[j.size()];
			for (int i = 0; i < j.size(); i++) {
				temp[i] = j.get(i);
			}
			ensureCapacityJournal(j.size());
			int counter = 0;
			for (int i = size; i < recordArray.length; i++) {
				recordArray[i] = temp[counter++];
				size++;
			}
		} else {
			throw new IllegalArgumentException("Illegal Capacity of journal size:" + j.size());
		}
	}

	/**
	 * Removes the first occurrence of the element in the Journal if it's
	 * present If not message will be printed
	 */
	@Override
	public void remove(Record r) {
		for (int i = 0; i < recordArray.length; i++) {
			if (recordArray[i].equals(r)) {
				delByIndex(i);
			}
		}

	}

	/**
	 * Private method for deleting element by index;
	 * 
	 * @param i
	 */
	private void delByIndex(int i) {
		int num = size - i - 1;
		System.arraycopy(recordArray, i + 1, recordArray, i, num);
		recordArray[--size] = null;
	}

	/**
	 * Return Record by index in record array;
	 */
	@Override
	public Record get(int index) {
		if(index > recordArray.length){
			throw new IllegalArgumentException("Out of bound");
		}
		return recordArray[index];
	}

	/**
	 * Substitute the element by index If index is greater than recordArray
	 * size, can not be accessed
	 * 
	 * @throws IllegalArgumentException
	 */
	@Override
	public void set(int index, Record record) {
		if (index > recordArray.length) {
			throw new IllegalArgumentException("Out of bound!!! Check Index");
		} else {
			recordArray[index] = record;
		}
	}

	/**
	 * Insert specified element on the index position and shifts right elements
	 * one position to the right
	 */
	@Override
	public void insert(int index, Record record) {
		ensureCapacity();
		temp = new Record[recordArray.length];
		System.arraycopy(recordArray, 0, temp, 0, index - 1);
		temp[index] = record;
		System.arraycopy(recordArray, index, temp, index + 1, size - index);
		recordArray = temp;

	}

	@Override
	public void remove(int index) {
		// if delete make length of new array - 1
		temp = new Record[recordArray.length - 1];
		System.arraycopy(recordArray, 0, temp, 0, index - 1);
		System.arraycopy(recordArray, index + 1, temp, index, size - index);
		recordArray = temp;
	}



	@Override
	/**
	 * Remove Record from index to index
	 */
	public void remove(int fromIndex, int toIndex) {
		temp = new Record[recordArray.length];
		System.out.println(temp.length);
		System.arraycopy(recordArray, 0, temp, 0, fromIndex - 1);
		System.arraycopy(recordArray, toIndex + 1, temp, fromIndex, size - toIndex - 1);
		recordArray = temp;
	}

	/**
	 * Remove all records
	 */
	@Override
	public void removeAll() {
		for(Record i: recordArray){
			//GCC will work much faster
			i = null;
		}
		recordArray = null;
	}

	/**
	 * Get size
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Filter by expression 
	 */
	@Override
	public Journal filter(String s) {
		Journal journal = new CollectionJournal();
		for (Record i : recordArray) {
			if (containsIgnoreCase(i.toString(), s)) {
				journal.add(i);
			}
		}
		return journal;
	}

	/**
	 * filter for Journal
	 * filters are records from Journal from Date range
	 */
	
	@Override
	public Journal filter(Date fromDate, Date toDate) {
		Journal journal = new ArrayJournal();
		for(Record i: recordArray){
			if(i.getDate().after(fromDate) && i.getDate().before(toDate)){
				journal.add(i);
			}
		}
		return journal;
	}

/**
 * Inner classes implements compare method
 */
	static {
		dateCompartor = new Comparator<Record>() {
			@Override
			public int compare(Record o1, Record o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		};
	}

	/**
	 * Sort by date()
	 * Trim free space
	 */
	@Override
	public void sortByDate() {
		trim();
		Arrays.sort(recordArray, Comparator.comparing(Record::getDate));
	}

	/**
	 * Trim array to necessary size (without null reference) Comparator can't
	 * compare null references
	 */
	public void trim() {
		temp = new Record[size];
		System.arraycopy(recordArray, 0, temp, 0, size);
		recordArray = temp;
	}

	/**
	 * Compare By Importance First compare by Importance level then by Date if
	 * they have equal importance Used Java 8
	 */
	@Override
	public void sortByImportanceDate() {
		trim();
		Arrays.sort(recordArray, Comparator.comparing(Record::getImportLevel).thenComparing(Record::getDate));
	}

	/**
	 * Multiple comparing :)
	 */
	@Override
	public void sortByImportanceSortDate() {
		trim();
		Arrays.sort(recordArray, Comparator.comparing(Record::getImportLevel).thenComparing(Record::getDate)
				.thenComparing(Record::getMessageSource));

	}

	/**
	 * Multiple comparing 
	 * Sort by message source then sort by date
	 */
	@Override
	public void sortBySourceDate() {
		trim();
		Arrays.sort(recordArray, Comparator.comparing(Record::getMessageSource).thenComparing(Record::getDate));
	}

	/**
	 * Print all records in Journal
	 */
	@Override
	public void printRecords() {
		for (Record i : temp) {
			System.out.println(i);
		}

	}

	
	/**
	 * Not implemented yet
	 */
	@Override
	public ArrayList<Record> getRecords() {
		return null;
	}

}
