package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author john and sid
 *
 */
public class QuantTrader {
	public static void main(String[] args) {
		Process world = null;
		Process agent = null;
		
		Runtime rt = Runtime.getRuntime();
		try {
			agent = rt.exec("java -classpath bin main.AgentMain");
			world = rt.exec("java -classpath bin main.WorldMain");
			
			Thread qtsh = new QTShutdownHook(world, agent);
			rt.addShutdownHook(qtsh);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		Timer timer = new Timer();
		timer.start();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
