package agent.logic;

import data.Tick;

public class PercentChange {
	private static final float NEG20PERCENT 	= -0.002f;
	private static final float NEG10PERCENT 	= -0.001f;
	private static final float NEG5PERCENT 		= -0.0005f;
	private static final float ZEROPERCENT 		= 0.0f;
	private static final float POS5PERCENT 		= 0.0005f;
	private static final float POS10PERCENT 	= 0.001f;
	private static final float POS20PERCENT 	= 0.002f;
	
	public static Values toValue(Tick t1, Tick t2) {
		float change = percentChange(t1.getPrice(),t2.getPrice());

		if (change < NEG20PERCENT) {
			return Values.VHIGHNEG;
		} else if (change < NEG10PERCENT) {
			return Values.HIGHNEG;
		} else if (change < NEG5PERCENT) {
			return Values.MEDNEG;
		} else if (change < ZEROPERCENT) {
			return Values.LOWNEG;
		} else if (change == ZEROPERCENT) {
			return Values.ZERO;
		} else if (change > ZEROPERCENT) {
			return Values.LOWPOS;
		} else if (change > POS5PERCENT) {
			return Values.MEDPOS;
		} else if (change > POS10PERCENT) {
			return Values.HIGHPOS;
		} else {
			return Values.VHIGHPOS;
		}
	}
	
	public static float percentChange(float a, float b) {
		return (b - a) / a;
	}
	
	public static boolean isNeg(Values v)	 {
		return v == Values.LOWNEG || v == Values.MEDNEG || v == Values.HIGHNEG;
	}
	
	public static boolean isPOS(Values v)	 {
		return v == Values.LOWPOS || v == Values.MEDPOS || v == Values.HIGHPOS;
	}
}
