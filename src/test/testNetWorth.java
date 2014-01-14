package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import parse.CSVParseException;
import parse.CSVParser;
import data.ParseConverter;
import data.Tick;

import world.Statistics;
import world.World;

public class testNetWorth {

	CSVParser parser = new CSVParser();
	ParseConverter converter = new ParseConverter(13);
	List<Tick[]> ticksByDate = null;
	Statistics stat;
	
	@Before
	public void setUp() throws Exception {
		String[][] rows = null;
		try {
			rows = parser.parseFile("src" + File.separator + "test" + File.separator + "input" + File.separator + "test.csv");
			
		} catch (CSVParseException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		ticksByDate = converter.parseToTicksByStockId(rows);
		
		stat = new Statistics(new World(ticksByDate));
		
		//simulate trades
//		stat.update(2, Action.BUY, ticksByDate.get(4)[3]);
//		stat.update(2, Action.BUY, ticksByDate.get(2)[5]);
//		stat.update(2, Action.BUY, ticksByDate.get(4)[2]);
		
		//write updated stats to file
		stat.writeOutputFile("output.txt");
	}

	@Test
	public void test() {
		assertEquals((int)stat.getNetWorth(), 0);
	}

}
