package agent.logic;

import data.Tick;

public interface Parameter {
	public Values toValue(Tick t1, Tick t2);
}
