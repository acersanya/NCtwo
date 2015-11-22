package model;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author acersanya
 * Journal interface
 */
public interface Journal {

	/**
	 * Add record to journal
	 * @param r  record
	 */
	void add(Record r);

	/**
	 * Add journal to existing journal
	 * @param j record
	 */
	void add(Journal j);

	/**
	 * Remove record from journal
	 * @param r record
	 */
	void remove(Record r);

	/**
	 * Get record by index
	 * @param index 
	 * @return record
	 */
	Record get(int index);

	/**
	 * Set Record by index
	 * @param index
	 * @param record
	 */
	void set(int index, Record record);

	/**
	 * Insert record into index position
	 * All records from the right side 
	 * will be shifted right for one position
	 * @param index
	 * @param record
	 */
	void insert(int index, Record record);

	/**
	 * Remove record by index
	 * @param index
	 */
	void remove(int index);

	/**
	 * Remove Records in journal
	 * @param fromIndex start index
	 * @param toIndex 	end index
	 */
	void remove(int fromIndex, int toIndex);

	/**
	 * Remove all Records in Journal
	 */
	void removeAll();

	/**
	 * Journal total size
	 * @return size of Journal
	 */
	int size();

	/**
	 * Filter by String s
	 * @param s input string
	 * @return filtered records
	 */
	Journal filter(String s);

	/**
	 * Filter by Date
	 * @param fromDate start date
	 * @param toDate	end date
	 * @return filtered records
	 */
	Journal filter(Date fromDate, Date toDate);

	/**
	 * Sort Journal by date
	 */
	void sortByDate();

	/**
	 * Sort by importance date
	 */
	void sortByImportanceDate();

	void sortByImportanceSortDate();

	void sortBySourceDate();

	/**
	 * Print all Records in Journal
	 */
	void printRecords();

	/**
	 * @return journal records
	 */
	ArrayList<Record> getRecords();

	/**
	 * Default method used for filter in both implementation
	 * is the same
	 * Using Pattern and Matcher to check if text contains
	 * subText 
	 * @param text 	input
	 * @param subText 	input subText condition
	 * @return	
	 */
	default boolean containsIgnoreCase(String text, String subText) {
		if (subText.equals("")) {
			return true;
		}
		if (text == null || subText == null || text.equals("")) {
			return false;
		}
		Pattern pattern = Pattern.compile(subText, Pattern.CASE_INSENSITIVE + Pattern.LITERAL);
		Matcher matcher = pattern.matcher(text);
		return matcher.find();
	}

}
