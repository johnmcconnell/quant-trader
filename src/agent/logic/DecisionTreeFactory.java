package agent.logic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DecisionTreeFactory {
	
	public static DecisionTree buildTree(Values[][] values) {
		LinkedList<Integer> ids = new LinkedList<Integer>(); //add input parameters indexes;
		init_ids(values, ids);
		
		TreeNode parent = build(ids,values,values[0].length - 1);
		DecisionTree tree = new DecisionTree(parent);
		
		return tree;
	}

	private static TreeNode build(LinkedList<Integer> indexes, Values[][] parameters, int actionIndex) {
		if (parameters == null || indexes == null || indexes.isEmpty() || parameters.length == 0) return null; //guards
		
		
		Integer maxIndex = maxIndexByEntropy(indexes,parameters,actionIndex);
		
		if (maxIndex == null) {
			TreeNode node = new TreeNode(-1);
			node.setPlus(countPlus(parameters,actionIndex));
			node.setNeg(countMinus(parameters,actionIndex));
			return node;
		}
		
		TreeNode node = new TreeNode(maxIndex);
		
		if (!indexes.remove(maxIndex)) {
			System.out.println("HUGE PROBLEM!");
			System.exit(-1);
		}
		
		HashMap<Values, LinkedList<Values[]>> splits = splitparams(parameters,maxIndex);
		
		for (Values key : splits.keySet()) {
			Values[][] splitValue = toValArray(splits.get(key));
			TreeNode next = build(indexes,splitValue,actionIndex);
			node.addNode(key, next);
		}
		return node;
	}
	
	private static Integer countPlus(Values[][] parameters, int actionIndex) {
		Integer p = 0;
		for (Values[] timeslice : parameters) {
			if (PercentChange.isPOS(timeslice[actionIndex])) {
				p++;
			}
		}
		return p;
	}

	private static Integer countMinus(Values[][] parameters, int actionIndex) {
		Integer n = 0;
		for (Values[] timeslice : parameters) {
			if (PercentChange.isNeg(timeslice[actionIndex])) {
				n++;
			}
		}
		return n;
	}

	private static Values[][] toValArray(LinkedList<Values[]> list) {
		Values[][] value = new Values[list.size()][];
		for (int x = 0; x < value.length; x++) {
			value[x] = list.get(x);
		}
		return value;
	}
	
	private static HashMap<Values, LinkedList<Values[]>> splitparams(Values[][] parameters, Integer maxIndex) {
		HashMap<Values,LinkedList<Values[]>> splits = new HashMap<Values,LinkedList<Values[]>>(Values.values().length);
		initsplits(splits);
		
		for (Values[] timeslice : parameters) {
			splits.get(timeslice[maxIndex]).add(timeslice);
		}
		
		return splits;
	}

	private static void initsplits(HashMap<Values, LinkedList<Values[]>> splits) {
		for (Values v: Values.values()) {
			splits.put(v, new LinkedList<Values[]>());
		}
	}

	private static double entropy(Values[][] parameters, Integer index, Integer actionIndex) {
		double sum = 0.0f;
		for (Values v : Values.values()) {
			double p = prob(parameters,v,index,actionIndex);
			try {
				double change = Math.log(p) / Math.log(2);
				if (change == Double.NEGATIVE_INFINITY || change == Double.POSITIVE_INFINITY) {
					change = 0.0d;
				}
				sum += change;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return -sum;
	}

	private static float prob(Values[][] parameters, Values v, Integer index, Integer actionIndex) {
		float p = 0.0f;
		float n = 0.0f;
		for (Values[] timeslice : parameters) {
			if (timeslice[index] == v && PercentChange.isPOS(timeslice[actionIndex])) {
				p++;
			} else {
				n++;
			}
		}
		return p / (p + n);
	}

	private static Integer maxIndexByEntropy(LinkedList<Integer> indexes, Values[][] parameters,Integer actionIndex) {
		Integer max = null;
		double maxent = -0.0f;
		for (Integer x : indexes) {
			double ent = entropy(parameters, x, actionIndex);
			if (max == null) {
				max = x;
				maxent = ent;
			}
			if (ent > maxent) {
				max = x;
				maxent = ent;
			}
		}
		if (maxent <= 0.0f || maxent <= -0.0f) return null; //guard if less than zero
		return max;
	}

	private static void init_ids(Values[][] values, LinkedList<Integer> ids) {
		for (Integer x = 0; x < values[0].length - 1; x++) {
			ids.add(x);
		}
	}
}
