package gameoflife;

import java.util.Date;

import model.*;


/**
 * Child class for Record ancestor
 * 
 * @author acersanya
 *
 */
public class CellRecord extends Record {

	private static int counter = 1;
	private int id;
	private int coordinateX;
	private int coordinateY;

	public CellRecord(Date date, String messageSource, ImportanceLevel level, String message, int coordinateX,
			int coordinateY) {
		super(date, messageSource, level, null);
		this.coordinateX = coordinateX;
		this.coordinateY = coordinateY;
		id = counter++;
	}

	
	
	/**
	 * Get coordinate X
	 * @return x coordinate
	 */
	public int getCoordinateX() {
		return coordinateX;
	}



	/**
	 * Set coordinate X
	 * @param set coordinate X
	 */
	public void setCoordinateX(int coordinateX) {
		this.coordinateX = coordinateX;
	}


	/**
	 * Getter
	 * @return coordinate y
	 */

	public int getCoordinateY() {
		return coordinateY;
	}

	
	/**
	 * Setter 
	 * @param coordinate Y set
	 */


	public void setCoordinateY(int coordinateY) {
		this.coordinateY = coordinateY;
	}


	/**
	 * Overloaded method to String 
	 */

	@Override
	public String toString() {
		return "(x,y)" + "("+coordinateX+","+coordinateY +")"+ ", "+ getDate()+ ", Importance Level=" + getImportLevel() + "\n";
	}

}
