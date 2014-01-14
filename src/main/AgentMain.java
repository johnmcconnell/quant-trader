package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import agent.Agent;

import world.TradeAction;

import data.Tick;


public class AgentMain {
	private static final int TRADEPORT = 9000;
	private static final int TICKPORT = 9001;
	private static final Agent agent = new Agent();
	
	public static void main(String[] args) {
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		agent.think();
	}
	
	public static void sendTrade(TradeAction trade) {
    	try {
			Socket socket = new Socket("127.0.0.1",TRADEPORT);
			try {
				ObjectOutputStream ow = new ObjectOutputStream(socket.getOutputStream());
				ow.writeObject(trade);
			} catch (IOException e) {
				e.printStackTrace();
			}
			socket.close();
    	} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Tick[] getTick() {
    	try {
			Socket socket = new Socket("127.0.0.1",TICKPORT);
			Thread.sleep(10);
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			try {
				return (Tick[]) ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			socket.close();
    	} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	return null;
	}

	private static void init() throws IOException {
		System.setOut(new PrintStream(new FileOutputStream("log" + File.separator+ "agent.log")));
		System.setErr(new PrintStream(new FileOutputStream("log" + File.separator+ "agent.err.log")));
		System.out.println("Agent began life at " + new Date());
        connectToTick();
        connectToTrade();
	}
	
	private static void connectToTick() {
		boolean connected = false;
		while(!connected) {
    		try {
    			Socket s = new Socket("127.0.0.1",TICKPORT);
    			connected = s.isConnected();
    		} catch (UnknownHostException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        }
		System.out.println("Successfully connected to the tickport " + TICKPORT +
							" at time " + new Date());
	}
	
	private static void connectToTrade() {
		boolean connected = false;
		while(!connected) {
    		try {
    			Socket s = new Socket("127.0.0.1",TRADEPORT);
    			connected = s.isConnected();
    		} catch (UnknownHostException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        }
		System.out.println("Successfully connected to the tradeport " + TRADEPORT +
							" at time " + new Date());
	}
}
