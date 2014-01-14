package agent.logic;

import java.util.HashMap;

public class TreeNode {
	private Integer id;
	private HashMap<Values,TreeNode> splits;
	private Integer pos;
	private Integer neg;
	
	public TreeNode(Integer maxIndex) {
		id = maxIndex;
		splits = new HashMap<Values,TreeNode>(Values.values().length);
	}
	
	public TreeNode decide(Values v) {
		return splits.get(v);
	}
	
	public void addNode(Values v, TreeNode node) {
		splits.put(v, node);
	}
	
	public Integer getId() {
		return id;
	}
	
	public Integer getPlus() {
		return pos;
	}

	public void setPlus(Integer pos) {
		this.pos = pos;
	}

	public Integer getNeg() {
		return neg;
	}

	public void setNeg(Integer neg) {
		this.neg = neg;
	}
	
	@Override
	public String toString() {
		if (id == -1) {
			return "Pos: " + pos +", " + "Neg: " + neg;
		}
	     StringBuilder sb = new StringBuilder();
	     sb.append("id:" + id +",\n");
	     for (Values v : splits.keySet()) {
	    	 TreeNode node = splits.get(v);
	    	 if (node == null) {
		    	 sb.append("[ ");
		    	 sb.append(v);
		    	 sb.append(' ');
		    	 sb.append("=> ");
		    	 sb.append(node);
		    	 sb.append(" ]\n");
	    	 } else if (node.getId() == -1) {
		    	 sb.append("[ ");
		    	 sb.append(v);
		    	 sb.append(' ');
		    	 sb.append("=>");
		    	 for (String line : node.toString().split("\n")) {
		    		 sb.append("\t" + line);
		    	 }
		    	 sb.append(" ]\n");
	    	 } else {
		    	 sb.append("[ ");
		    	 sb.append(v);
		    	 sb.append(' ');
		    	 sb.append("=>");
		    	 for (String line : node.toString().split("\n")) {
		    		 sb.append("\t" + line + "\n");
		    	 }
		    	 sb.append(" ]\n");
	    	 }
	     }
	     
	     return sb.toString();
	}

}
