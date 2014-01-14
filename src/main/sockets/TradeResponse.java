package main.sockets;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;

import world.TradeAction;
import world.World;

public class TradeResponse extends MyResponse {
	private World world;
	
	public TradeResponse(World world) {
		super();
		this.world = world;
	}
	
	@Override
	protected void respond(InputStream inputStream, OutputStream outputStream) {
		try {
			ObjectInputStream ois = new ObjectInputStream(inputStream);
			TradeAction trade = (TradeAction) ois.readObject();
			System.out.println(trade);
			world.performTrade(trade);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
