package main;

import java.io.PrintStream;

public class QTShutdownHook extends Thread {
	private Process agent;
	private Process world;
	
	
	public QTShutdownHook(Process world, Process agent) {
		this.agent = agent;
		this.world = world;
	}
	
	@Override
	public synchronized void run() {
		System.out.print("killing agent...");
		agent.destroy();
		System.out.println(" complete!");
		
		System.out.print("killing world...");
		new PrintStream(world.getOutputStream()).println("world");
		System.out.println(" complete!");
		
			
	}
}
