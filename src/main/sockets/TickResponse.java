package main.sockets;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import data.Tick;

public class TickResponse extends MyResponse {
	private Tick[] ticks = null;
	private boolean changed = false;
	
	public void setCurrentTick(Tick[] ticks) {
		if (ticks != null) {
			this.ticks = ticks;
			changed = true;
			//System.out.println("set to " + ticks[0]);
		} else {
			//System.out.println("set is still null");
		}
	}
	
	@Override
	protected void respond(InputStream inputStream, OutputStream outputStream) {
		try {
			ObjectOutputStream ow = new ObjectOutputStream(outputStream);
			if (changed) {
				ow.writeObject(ticks);
				changed = false;
			} else {
				ow.writeObject(null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
