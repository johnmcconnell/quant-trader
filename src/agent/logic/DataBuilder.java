package agent.logic;

import java.util.Arrays;
import java.util.List;

import data.Tick;

public class DataBuilder {
	
	private static class Pair<T> {
		private T a = null;
		private T b = null;
	}
	
	public static Values[] parameterizePair(Tick[] a, Tick[] b) {
		Values[] pcs = new Values[a.length];
		
		for (int id = 0; id < a.length; id++) {
			pcs[id] = PercentChange.toValue(a[id], b[id]);
		}
		
		return pcs;
	}
	
	public static Values[][] parameterizeData(List<Tick[]> memory) {
		if (memory == null || memory.size() < 3) return null;
		Pair<Tick[]> p = new Pair<Tick[]>();
		
		Values[][] pData = new Values[memory.size() - 1][];
		
		p.a = memory.get(0);
		for(int x = 1; x < memory.size(); x++) {
			p.b = memory.get(x);
			
			Values[] pcs = parameterizePair(p.a,p.b);
			pData[x - 1] = pcs;
			
			p.a = p.b; //update
		}
		return pData;
	}
	
	public static Values[][][] quantifyDecision(Values[][] pData) {
		Values[][][] idToQData = new Values[pData[0].length][pData.length - 1][];
		
		for (int id = 0; id < idToQData.length; id++) {
			for (int timeslice = 0; timeslice < pData.length - 1; timeslice++) {
				idToQData[id][timeslice] = getQData(pData,id,timeslice);
			}
		}
		
		return idToQData;
	}

	private static Values[] getQData(Values[][] pData, int id, int timeslice) {
		//copy the values of the current index into a new array 
		//leaving the last element open for the decision
		Values[] qdata = Arrays.copyOf(pData[timeslice], pData[timeslice].length + 1);
		
		//assign the decision "look into the future" and find if it is 
		// pos or neg
		qdata[qdata.length - 1] = futureVal(pData,id,timeslice);
		
		return qdata;
	}
	
	private static Values futureVal(Values[][] pData,int id,int timeslice) {
		return pData[timeslice + 1][id];
	}
	
}
