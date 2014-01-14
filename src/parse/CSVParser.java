package parse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

public class CSVParser {
	/**
	 * @param filename, the filename to be read
	 * @return a table of String objects, indexed by the row, col of the info in the CSV file
	 * @throws CSVParseException
	 * @throws IOException
	 */
	public String[][] parseFile(String filename) throws CSVParseException, IOException {
		Collection<String[]> rows = new LinkedList<String[]>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				rows.add(line.split(","));
			}
		} catch (IOException e) {
			throw e;
		}
		
		String[][] parsed = collectionToArray(rows);
		
		return parsed;
	}

	private String[][] collectionToArray(Collection<String[]> rows) {
		String[][] parsed = new String[rows.size()][];
		int x = 0;
		for (String[] col : rows) {
			parsed[x] = col;
			x++;
		}
		return parsed;
	}
	
	/**
	 * @param text, the string representation of an csv file
	 * @return a table of String objects, indexed by the row, col of the info in the CSV text
	 */
	public String[][] parseText(String text) {
		
		String[] lines = text.split("\n");
		String[][] parsedText = new String[lines.length][];
		
		for (int x = 0; x < lines.length; x++) {
			parsedText[x] = lines[x].split(","); 
		}
		
		return parsedText;
	}
	
	public void printParse(String[][] rows) {
		if (rows == null) return;	//null check
		
		int x = 0;
		for (String[] cols : rows) {
			System.out.print("line " + x + ":");
			for (String entry : cols) {
				System.out.print("[" + entry + "]");
			}
			x++;
			System.out.println();
		}
	}
	
	/**
	 * @param rows
	 * @param rowCount, the maximum amount of lines you wish to print
	 */
	@Deprecated
	public void printParse(String[][] rows, int rowCount) {
		if (rows == null) return;	//null check
		
		int x = 0;
		for (String[] cols : rows) {
			System.out.print("line " + x + ":");
			for (String entry : cols) {
				System.out.print("[" + entry + "]");
			}
			x++;
			System.out.println();
			if (x > rowCount) return;
		}
	}

}
