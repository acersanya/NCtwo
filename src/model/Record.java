package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author acersanya
 * This is Record class
 * In task the Record class should be 
 * immutable (final methods, no setters)
 * Class couldn't be final, it is extended 
 * by CellRecord class
 */
public  class Record {

	private Date date;
	private String messageSource;
	private ImportanceLevel importLevel;
	private String messageText;

	private static final String CHECK_IMPORTANCE_LEVEL = "No valied importance level. Please Check";

	public static enum ImportanceLevel {
		LEVEL_FOUR(4, "!!!!!"), LEVEL_THREE(3, "!!!"), LEVEL_TWO(2, "!"), LEVEL_ONE(1, ".");

		private final String text;
		private final int value;

		private ImportanceLevel(final int value, final String text) {
			this.text = text;
			this.value = value;
		}

		@Override
		public String toString() {
			return value + text;
		}
	}

	// REMAKE ONE STRING, PARSE STIRNG INTO
	public Record(Date date, String messageSource, ImportanceLevel level, String message) {
		this.date = date;
		this.messageSource = messageSource;
		this.importLevel = level;
		this.messageText = message;
	}

	// REMAKE ONE STRING, PARSE STIRNG INTO
	//Constructor which receives expression and 
	//parses it
	public Record(String text) {
		parseDate(text);
		parseAllNoDate(text);
	}

	/**
	 * Parsing date here
	 * validating date
	 * @param text input
	 */
	public void parseDate(String text) {
		Pattern clearPattern = Pattern.compile("[\\s]+");
		text = clearPattern.matcher(text).replaceAll(" ").trim();
		String regex = "^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			text = matcher.group(0);
			Date date = null;
			if (text == null) {
				throw new NullPointerException("Null data is invalid");
			}
			SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
			try {
				date = df.parse(text);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.date = date;
		}
	}

	/**
	 * Parse rest part of the input,
	 * ignoring date
	 * @param text
	 */
	public void parseAllNoDate(String text) {
		text = text.replaceAll("\\s+", " ").trim();
		ArrayList<String> temp = new ArrayList<>();
		for (String i : text.split(" ")) {
			temp.add(i);
		}
		System.out.println(temp);

		temp.remove(0);
		temp.remove(0);

		System.out.println(temp);

		for (ImportanceLevel e : ImportanceLevel.values()) {
			if (temp.get(0).equalsIgnoreCase(e.name())) {
				this.importLevel = e;
			}
		}
		//check here if Importance level matches importance level typed from keyboard
		if (this.importLevel == null) {
			throw new IllegalArgumentException(CHECK_IMPORTANCE_LEVEL);
		}

		this.messageSource = temp.get(1);
		this.messageText = temp.get(2);

	}

	@Override
	public   String toString() {
		return date + " " + messageSource;
	}

	/**
	 * Getter
	 * @return
	 */
	public final Date getDate() {
		return date;
	}

	/**
	 * Getter for messageSource
	 * @return
	 */
	public final String getMessageSource() {
		return messageSource;
	}

	public final ImportanceLevel getImportLevel() {
		return importLevel;
	}

	public final String getMessageText() {
		return messageText;
	}

}
