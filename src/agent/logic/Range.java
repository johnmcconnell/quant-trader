package agent.logic;

public class Range {
	private float high;
	private float low;
	
	public Range(float high, float low) {
		this.high = high;
		this.low = low;
	}
	
	public boolean isWithin(float x) {
		return (low <= x && x <= high);
	}
	
	public float getHigh() {
		return high;
	}

	public float getLow() {
		return low;
	}
}
