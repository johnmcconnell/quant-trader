package world;

import java.io.Serializable;

import data.Action;

public class TradeAction implements Serializable {
	private static final long serialVersionUID = -1879092517715893656L;
	private int numShares;
	private int stockId;
	Action action;
	
	public TradeAction(int numShares, int stockId, Action action) {
		this.numShares = numShares;
		this.stockId = stockId;
		this.action = action;
	}
	
	public int getNumShares() {
		return this.numShares;
	}
	
	public int getStockId() {
		return this.stockId;
	}
	
	public Action getAction() {
		return this.action;
	}
	
	@Override
	public String toString() {
		return "Trade [numShares=" + numShares + ", stockId=" + stockId
				+ ", action=" + action + "]";
	}
	
}
