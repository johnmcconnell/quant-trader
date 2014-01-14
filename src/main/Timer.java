package main;

public class Timer extends Thread {
	
	@Override
	public synchronized void run() {
		long lasttime = System.currentTimeMillis();
		int second = 1;
		while (true) {
			long current = System.currentTimeMillis();
			if (current > lasttime + 1000) {
				if (second % 10 == 0) {
					System.out.println("|" + second / 60 + ":" + second++ % 60 +"|");
				} else {
					System.out.print("|" + second / 60 + ": " + second++ % 60 + "|");
				}
				
				if (second > 60 * 3) {
					System.exit(0); //kill under time
				}
				
				lasttime = current;
			} else {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					//spin
				}
			}
		}
	}
}
