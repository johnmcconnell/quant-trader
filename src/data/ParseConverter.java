package data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class ParseConverter {
	private int maxRows								= 13;
	private int nameIndex							= 0;
	private int dataIndex 							= 4;
	private int dateOffset							= 0;
	private int priceOffset							= 1;
	private int volumeOffset						= 3;
	
	public ParseConverter(int maxRows) {
		this.maxRows = maxRows;
	}

	/**
	 * @param parseData the data parsed from an csv file or a string table
	 * @return a list of ticksByStockId
	 */
	public List<Tick[]> parseToTicksByStockId(String[][] parseData) {
		registerNames(parseData[nameIndex]);
				
		return filter(parseData);
	}

	/**
	 * @param parseData the data parsed from an csv file or a string table
	 * @return list of ticksByStockId
	 */
	private List<Tick[]> filter(String[][] parseData) {
		DateFormat df = new SimpleDateFormat("mm/dd/yyyy kk:mm", Locale.ENGLISH);
		int length = parseData[dataIndex].length;
		
		List<Tick[]> ticks = new LinkedList<Tick[]>();
		int[] offsets = new int[(length / 6) + 1];
		
		for (int row = dataIndex; row < maxRows; row++) {
			if (updateOffsets(parseData, offsets, row, df)) {
				ticks.add(createTickArray(parseData, df, length, offsets, row));
			}
		}
		
		return ticks;
	}
	
	/**
	 * @param parseData the data parsed from an csv file or a string table
	 * @param offsets a array of offsets to keep track of the latest index for each stock
	 * @param row the current row to keep track of the number of entries
	 * @param df the date formatter to convert strings to dates
	 * @return whether the function was able to find a mutual minDate value within the allotted index
	 */
	private boolean updateOffsets(String[][] parseData, int[] offsets, int row, DateFormat df) {
		Date minDate = minDate(parseData, offsets, row, df);
		
		for(int x = 0; x < offsets.length; x++) {
			int offset = getOffset(parseData,minDate,offsets[x],row,Col(x),df);
			if (offset > 1500) {
				return false;
			} else {
				offsets[x] = offset;
			}
		}
		return true;
	}

	/**
	 * @param parseData the data parsed from an csv file or a string table
	 * @param minDate the minimum date that all three should have
	 * @param x the current row offset
	 * @param row the current row
	 * @param col the current column
	 * @param df the date formatter to convert strings to dates
	 * @return the new offset where the column contains the minDate
	 */
	private int getOffset(String[][] parseData, Date minDate, int x, int row, int col, DateFormat df) {
		int start = x;
		while(x < 1500 + start) {
			Date date = null;
			try {
				date = df.parse(parseData[row+x][col + dateOffset]);
				
			} catch (ParseException e) {
				e.printStackTrace();
				System.exit(0);
			}
			if (date == null) {
				return 2000;
			}
			
			if (date.before(minDate)) {
				x++;
			} else if (date.equals(minDate)){
				return x;
			} else {
				return 2000;
			}
		}
		return 2000;
	}

	/**
	 * @param parseData the data parsed from an csv file or a string table
	 * @param offsets the offsets of the row for each stockid
	 * @param row the current row
	 * @param df the date formatter to convert strings to dates
	 * @return Date the date is the maxDate for the given row
	 */
	private Date minDate(String[][] parseData, int[] offsets, int row, DateFormat df) {
		Date minDate = null;
		for(int x = 0; x < offsets.length; x++) {
			try {
				Date date = getDate(parseData, offsets[x], row, Col(x), df);
				if (minDate == null) minDate = date; //min guard
				
				if (!date.before(minDate)) { // this is minDate that they mutually have
					minDate = date;
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		return minDate;
	}
	
	/**
	 * @param parseData the data parsed from an csv file or a string table
	 * @param df the date formatter to convert strings to dates
	 * @param length the length the column of the parseDate
	 * @param offsets the offsets of the row for each stockid
	 * @param row the current row
	 * @return Tick[] the tickByStockId for a given time
	 */
	private Tick[] createTickArray(String[][] parseData, DateFormat df,
			int length, int[] offsets, int row) {
		Tick[] ticksByStockId = new Tick[offsets.length];
		
		for(int col = 0; col < length; col += 6) {
			
			String name = parseData[nameIndex][col];
			int id = StockConverter.getId(name);
			Date date = null;
			
			try {
				date = df.parse(parseData[row+offsets[col/6]][col + dateOffset]);
			} catch (ParseException e) {
				e.printStackTrace();
				System.exit(0);
			}
			
			float price = Float.parseFloat((parseData[row+offsets[col/6]][col + priceOffset]));
			int volume = Integer.parseInt((parseData[row+offsets[col/6]][col + volumeOffset]));

			ticksByStockId[col / 6] = new Tick(date,id,price,volume);
		}
		
		return ticksByStockId;
	}

	private Date getDate(String[][] parseData, int offset, int row, int col, DateFormat df) throws ParseException {
		return df.parse(parseData[row+offset][col + dateOffset]);
	}

	private void registerNames(String[] data) {
		for (int col = 0; col < data.length; col += 6) {
			StockConverter.register(data[col]);
		}
	}

	private int Col(int x) {
		return x * 6;
	}
	
	public void printTicksByDate(List<Tick[]> ticksByDate) {
		if (ticksByDate == null) return;	//null check
		
		for (Tick[] ticks : ticksByDate) {
			for (Tick tick : ticks) {
				System.out.print(tick);
			}
			System.out.println();
		}
	}
}
