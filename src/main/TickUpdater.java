package main;

import main.sockets.TickHandler;

import world.World;

public class TickUpdater extends Thread {
	private final int TICKPERIOD = 50;
	private final TickHandler tickHandler;
	private final World world;
	
	public TickUpdater(TickHandler tickHandler, World world) {
		super();
		this.tickHandler = tickHandler;
		this.world = world;
	}
	
	@Override
	public void run() {
		while(true) {
			waitForTick();
			tickHandler.setCurrentTick(world.tick());
		}
	}

	private void waitForTick() {
		while (true) {
			try {
				Thread.sleep(TICKPERIOD);
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
