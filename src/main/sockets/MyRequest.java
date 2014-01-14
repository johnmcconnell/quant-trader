package main.sockets;

import java.io.IOException;
import java.net.Socket;

public class MyRequest extends Thread {
	private Socket socket;
	private MyResponse response;
	
	public MyRequest(Socket accepted, MyResponse response) {
		this.socket = accepted;
		this.response = response;
	}
	
    @Override
	public void run() {
        try {
        	Thread.sleep(10);
            response.respond(socket.getInputStream(),socket.getOutputStream());
        } catch (IOException e) {
        	
        } catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Couldn't close a socket, what's going on?");
            }
        }
    }

}
