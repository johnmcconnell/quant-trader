package main.sockets;

import java.net.ServerSocket;

import world.World;

public class TradeHandler extends MyHandler {

	public TradeHandler(ServerSocket mySocket, World world) {
		super(mySocket, new TradeResponse(world));
	}
	
}
