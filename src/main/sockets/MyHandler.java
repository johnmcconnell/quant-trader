package main.sockets;

import java.io.IOException;
import java.net.ServerSocket;

public class MyHandler extends Thread {
	private ServerSocket myListener;
	private MyResponse myResponse;
	private boolean finished = false;
	
	public MyHandler(ServerSocket mySocket, MyResponse myResponse) {
		this.myListener = mySocket;
		this.myResponse = myResponse;
	}
	
	public void finish() {
		finished = true;
	}

	@Override
	public void run() {
		while(!finished) {
			try {
				new MyRequest(myListener.accept(), myResponse).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		while(!myListener.isClosed()) {
			try {
				myListener.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
	}
}
