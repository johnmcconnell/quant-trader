package agent;

import java.util.ArrayList;
import java.util.List;

import agent.logic.DataBuilder;
import agent.logic.DecisionTree;
import agent.logic.DecisionTreeFactory;
import agent.logic.Values;

import main.AgentMain;

import world.TradeAction;

import data.Action;
import data.Tick;

public class Agent {
	private static final int TRAININDEX 	= 1200;
	private List<Tick[]> memory;
	private DecisionTree[] dtree;
	private boolean updated = false;
	private int lastsize = 0;
	
	public Agent() {
		this.memory = new ArrayList<Tick[]>(2000);
	}

	public List<Tick[]> getStockList() {
		return memory;
	}
	
	public void think() {
		while (true) {
			updated = updateTicks();
			if (memory.size() < TRAININDEX) {
				int size = memory.size();
				if (size % 100 == 0 && size != lastsize) {
					System.out.print(", [Size: " + size + ", Time: " + System.currentTimeMillis() + "]");
					lastsize = size;
				} else if (size % TRAININDEX == 0 && size != lastsize) {
					System.out.println(", [Size: " + size + ", Time: " + System.currentTimeMillis() + "]");
				}
				
			} else if (memory.size() > TRAININDEX && dtree == null) {
				
				Values[][] pData = DataBuilder.parameterizeData(memory);
				Values[][][] qDataById = DataBuilder.quantifyDecision(pData);
				
				dtree = new DecisionTree[qDataById.length];
				
				System.out.println();
				for (int x = 0; x < qDataById.length; x++) {
					dtree[x] = DecisionTreeFactory.buildTree(qDataById[x]);
					System.out.println("Tree for id:" + x + "," + dtree[x]);
				}
				
			} else if(updated && dtree != null) {
				int size = memory.size();
				Tick[] last = memory.get(size - 2);
				Tick[] current = memory.get(size - 1);
				for (int x = 0; x < dtree.length; x++) {
					Values val = dtree[x].poll(last,current);
					switch (val) {
					case VHIGHNEG:
						tradeStock(x,Action.SELL,10);
						break;
					case HIGHNEG:
						tradeStock(x,Action.SELL,5);
						break;
					case MEDNEG:
						tradeStock(x,Action.SELL,2);
						break;
					case LOWNEG:
						tradeStock(x,Action.SELL,1);
						break;
					case ZERO:
						break;
					case LOWPOS:
						tradeStock(x,Action.BUY,1);
						break;
					case MEDPOS:
						tradeStock(x,Action.BUY,2);
						break;
					case HIGHPOS:
						tradeStock(x,Action.BUY,5);
						break;
					case VHIGHPOS:
						tradeStock(x,Action.BUY,10);
						break;
					}
				}
			} else {
				//do nothing spin
			}
		}
	}
	
	/**
	 * Calls the getTickFromWorld function and adds its return value to thestockList array
	 */
	private boolean updateTicks() {
		Tick[] ticks = AgentMain.getTick();
		if (ticks == null) {
			return false;
		} else {
			memory.add(ticks);
			return true;
		}
	}

	/**
	 * Buys or sells a stock by contacting the world
	 * @param stockID
	 * @param action
	 * @return 1 if trade successful and 0 otherwise
	 */
	private boolean tradeStock(int stockID, Action action,int numShares) {
		TradeAction trade = new TradeAction(numShares, stockID, action);

		AgentMain.sendTrade(trade);
		return true;
	}
	
	
	
}
