package data;

import java.io.Serializable;
import java.util.Date;

public class Tick implements Serializable {
	private static final long serialVersionUID = -4085793666212688882L;
	private Date date;
	private int stockId;
	private float price;
	private int volume;
	
	public Tick(Date date, int stockId, float price, int volume) {
		this.stockId = stockId;
		this.date = date;
		this.price = price;
		this.volume = volume;
	}
	public Date getDate() {
		return date;
	}
	public float getPrice() {
		return price;
	}
	public int getVolume() {
		return volume;
	}
	public int getStockId() {
		return stockId;
	}
	
	@Override
	public String toString() {
		return "Tick [date=" + date + ", id="  + stockId + ", price=" + price + ", volume=" + volume
				+ "]";
	}
}
