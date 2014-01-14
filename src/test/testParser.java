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

public class testParser {

	CSVParser parser = new CSVParser();
	ParseConverter converter = new ParseConverter(13);
	List<Tick[]> ticksByDate = null;
	
	
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
	}

	@Test
	public void testParseToTicksByStockId() {
		//converter.printTicksByDate(ticksByDate);
		assertEquals((int)ticksByDate.get(3)[0].getPrice(), (int)8.8502);
		assertEquals((int)ticksByDate.get(8)[3].getPrice(), (int)11.9998);
		assertEquals(ticksByDate.get(5)[2].getVolume(), 3400);
		assertEquals(ticksByDate.get(4)[4].getVolume(), 14439);
		assertEquals(ticksByDate.get(1)[1].getStockId(), 1);
		assertEquals(ticksByDate.get(2)[3].getStockId(), 3);
		assertEquals(ticksByDate.get(7)[7].getDate().toString(), "Wed Jan 02 08:43:00 CST 2013");
		assertEquals(ticksByDate.get(6)[8].getDate().toString(), "Wed Jan 02 08:42:00 CST 2013");
		
	}


}
