package agent.logic;

import data.Tick;

public class DecisionTree {
	private TreeNode parent;

	public DecisionTree(TreeNode parent) {
		this.parent = parent;
	}
	
	public Values poll(Tick[] a, Tick[] b) {
		Values[] input = DataBuilder.parameterizePair(a, b);
	
		return travel(input,parent);
	}
	
	public Values travel(Values[] input, TreeNode node) {
		if (node == null) {
			return Values.ZERO;
		}
		Integer id = node.getId();
		if (id == -1) {
			Integer weight = node.getPlus() - node.getNeg();
			
			if (weight > 3) {
				return Values.VHIGHPOS;
			} else if (weight > 2) {
				return Values.HIGHPOS;
			} else if (weight > 1) {
				return Values.MEDPOS;
			} else if (weight > 0) {
				return Values.LOWPOS;
			} else if (weight == 0) {
				return Values.ZERO;
			} else if (weight < 0) {
				return Values.LOWNEG;
			} else if (weight < 1) {
				return Values.MEDNEG;
			} else if (weight < 2) {
				return Values.HIGHNEG;
			} else {
				return Values.VHIGHNEG;
			}
		}
		
		TreeNode next = node.decide(input[id]);
		return travel(input,next);
	}
	
	@Override
	public String toString() {
		return "DecisionTree [parent=" + parent + "]";
	}
}
