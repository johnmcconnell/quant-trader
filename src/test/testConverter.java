package test;

import static org.junit.Assert.*;
import org.junit.Test;

import data.StockConverter;

public class testConverter {

	StockConverter converter = new StockConverter();

	@Test
	public void testRegister() {
		int index = StockConverter.register("testStock");
		assertEquals(index, StockConverter.getId("testStock"));
	}

	@Test
	public void testGetName() {
		assertEquals("testStock", StockConverter.getName(StockConverter.getId("testStock")));
	}

}
