package world;

import java.util.Iterator;
import java.util.List;

import data.Action;
import data.Tick;

public class World {
	private List<Tick[]> tickList;
	private Iterator<Tick[]> it; //keeps track of which tick was last extracted
	private Tick[] currentTick;
	private Statistics stat;

	public World(List<Tick[]> tickList) {
		this.tickList = tickList;
		this.it = tickList.iterator();
		this.stat = new Statistics(this);
		this.currentTick = it.next();
	}
	
	public Tick[] tick() {
		this.currentTick = it.next();
		return this.currentTick;
	}
	
	public Tick[] getCurrTicksByStockId() {
		return currentTick;
	}
	
	public Statistics getStat() {
		return stat;
	}
	
	/**
	 * Buys or sells a stock by contacting the world
	 * @param currTick, the stock information to buy or sell
	 * @param action, to buy or sell the current stock
	 * @param numShares
	 * @return 1 if trade successful and 0 otherwise
	 */
	public boolean performTrade(TradeAction currTrade) {
		if (currentTick == null || currTrade == null) {
			return false;
		} else {
			int id = currTrade.getStockId();
			Action action = currTrade.getAction();
			int amount = currTrade.getNumShares();
			float price = currentTick[currTrade.getStockId()].getPrice();
			stat.update(id, action, amount, price);
			return true;
		}
	}
}
