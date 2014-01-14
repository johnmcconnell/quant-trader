package world;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import data.Action;
import data.StockConverter;
import data.Tick;

public class Statistics {
	
	private int totalTrades;
	private int totalStocksExchanged;
	private float buyAmountSpent;
	private float sellAmountEarned;
	private StringBuffer tradeSummary;
	private float account;
	private Map<Integer,Queue<Float>> outstandingByStockId = new HashMap<Integer,Queue<Float>>(40);
	private Map<Integer,Queue<Float>> shortedByStockId	= new HashMap<Integer,Queue<Float>>(40);
	private World currWorld;
	
	
	public Statistics(World currWorld) {
		this.totalTrades = 0;
		this.totalStocksExchanged = 0;
		this.buyAmountSpent = 0;
		this.sellAmountEarned = 0;
		this.tradeSummary = new StringBuffer();
		this.account = 0;
		this.currWorld = currWorld;
		init(outstandingByStockId);
		init(shortedByStockId);
	}
	
	private void init(Map<Integer, Queue<Float>> map) {
		for (Integer id : StockConverter.getStockIds()) {
			map.put(id, new LinkedList<Float>());
		}
	}

	public int getTotalTrades() {
		return totalTrades;
	}
	
	public void incrementTotalTrades() {
		totalTrades++;
	}
	
	public float getBuyAmountSpent() {
		return buyAmountSpent;
	}
	
	public void setBuyAmountSpent(float newAmount) {
		this.buyAmountSpent += newAmount;
	}
	
	public float getSellAmountEarned() {
		return sellAmountEarned;
	}
	
	public void setSellAmountEarned(float newAmount) {
		this.sellAmountEarned += newAmount;
	}
	
	public float getProfit() {
		return this.sellAmountEarned - this.buyAmountSpent;
	}
	
	public float getNetWorth() {	
		
		float totalOutstanding = 0;
		float totalShorted = 0;
		Tick[] ticks = currWorld.getCurrTicksByStockId();
		
		/*
		 * netWorth = account 
		 * 			  + (foreach(s) that belongs to Outstanding(Shares(s)*Price(s))) 
		 *			  - (foreach(s) that belongs to Shorts(Shares(s)*Price(s)))
		*/
		for(Entry<Integer, Queue<Float>> trades : outstandingByStockId.entrySet()) {
			for (int x = 0; x < trades.getValue().size(); x++) {
				Float price = ticks[trades.getKey()].getPrice();
				totalOutstanding += price;
			}
		}
		
		for(Entry<Integer, Queue<Float>> trades : shortedByStockId.entrySet()) {
			for (int x = 0; x < trades.getValue().size(); x++) {
				Float price = ticks[trades.getKey()].getPrice();
				totalShorted += price;
			}
		}
		
		float netWorth = account + totalOutstanding - totalShorted;

		return netWorth;
	}
	
	/**
	 * @param totalCost the total cost of buying the current stock
	 * @param action the action to use of the stock
	 * @param price 
	 * @param currTick the tick information of the stock
	 * @return 1 on true and 0 on false
	 */
	public void update(int id, Action action, int amount, float price) {
		if (action == Action.BUY) {
			buyStock(id,amount,price);
			this.totalTrades++;
			this.totalStocksExchanged += amount;
		} else if (action == Action.SELL) {
			sellStock(id,amount,price);
			this.totalTrades++;
			this.totalStocksExchanged += amount;
		}
		tradeSummary.append("Amount: " + amount + 
				", Action: " + action + 
				", StockId: " + id +
				", Price: " + price + "\n");
	}
	
	private void sellStock(int id, int amount, float price) {
		System.out.println("sell stock id: " + id + ", a:" + amount + ", p:" +price);
		Queue<Float> outstanding = outstandingByStockId.get(id);
		while (!outstanding.isEmpty() && amount > 0) {
			outstanding.poll();
			amount--;
		}
		
		Queue<Float> shorted = shortedByStockId.get(id);
		while (amount > 0) {
			shorted.add(price);
			sellAmountEarned += price;
			account += price;
			amount--;
		}
	}

	private void buyStock(int id, int amount, float price) {
		System.out.println("buy stock id: " + id + ", a:" + amount + ", p:" + price);
		Queue<Float> shorted = shortedByStockId.get(id);
		while (!shorted.isEmpty() && amount > 0) {
			shorted.poll();
			amount--;
		}
		
		Queue<Float> outstanding = outstandingByStockId.get(id);
		while (amount > 0) {
			outstanding.add(price);
			account -= price;
			buyAmountSpent += price;
			amount--;
		}
	}

	/**
	 * @param filename the name one wishes to write out the statistics information
	 */
	public void writeOutputFile(String filename) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("./"+filename));
			String data = "Total Trade Count: " + totalTrades 
					+ "\nTotal Stocks Transacted: " + totalStocksExchanged
					+ "\nTotal Amount Bought: $" + buyAmountSpent
					+ "\nTotal Amount Sold: $" + sellAmountEarned 
					+ "\nNetworth: $" + getNetWorth() + "\nAccount: $" + account;
			data += "\nSummary of trades:\n" + tradeSummary.toString();
			writer.write(data);
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			if(writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					System.err.println(e);
				}
			}
		}
	}
	
}
