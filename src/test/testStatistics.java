package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import world.Statistics;
import world.World;

public class testStatistics {

	Statistics stat = new Statistics(new World(null));
	@Before
	public void setUp() throws Exception {
		stat.setBuyAmountSpent((float)61296.72);
		stat.setSellAmountEarned(51092.363f);
		
	}

	@Test
	public void testGetTotalTrades() {
		assertEquals(0, stat.getTotalTrades());
	}

	@Test
	public void testGetBuyAmountSpent() {
		assertEquals((int)61296.72, (int)stat.getBuyAmountSpent());
	}


	@Test
	public void testGetSellAmountEarned() {
		assertEquals((int)51092.363, (int)stat.getSellAmountEarned());
	}

	@Test
	public void testGetProfit() {
		assertEquals((int)-10204.355, (int)stat.getProfit());
	}


}
