package main.sockets;

import java.net.ServerSocket;

import data.Tick;

public class TickHandler extends MyHandler {
	private TickResponse response;
	
	public TickHandler(ServerSocket mySocket, TickResponse response) {
		super(mySocket, response);
		this.response = response;
	}
	
	public void setCurrentTick(Tick[] ticks) {
		response.setCurrentTick(ticks);
	}
}
