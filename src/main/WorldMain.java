package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.util.Date;
import java.util.List;

import parse.CSVParseException;
import parse.CSVParser;

import data.ParseConverter;
import data.Tick;

import world.Statistics;
import world.World;

import main.sockets.TickHandler;
import main.sockets.TickResponse;
import main.sockets.TradeHandler;

public class WorldMain {
	private static final int TRADEPORT = 9000;
	private static final int TICKPORT = 9001;
	
	private static ServerSocket tickSocket;
	private static ServerSocket tradeSocket;
	private static TickHandler tickHandler;
	private static TradeHandler tradeHandler;
	private static TickUpdater updater;
	private static Statistics stats;
	private static World world;
	
	public static void main(String[] args) {
		//Change Print Writer
		try {
			System.setOut(new PrintStream(new FileOutputStream("log" + File.separator + "world.log")));
			System.setErr(new PrintStream(new FileOutputStream("log" + File.separator + "world.err.log")));
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		
		CSVParser parser = new CSVParser();
		ParseConverter converter = new ParseConverter(6000);
		String[][] rows = null;
		try {
			rows = parser.parseFile("data" + File.separator + "TICKDATA.csv");
		} catch (CSVParseException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		List<Tick[]> ticksByDate = converter.parseToTicksByStockId(rows);
		System.out.println("Tick Size: " + ticksByDate.size());
		world = new World(ticksByDate);
		stats = world.getStat();
		
		try {
			init();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(0);
		}
		
		tickHandler.setCurrentTick(world.getCurrTicksByStockId());
		
		//Start Servers
		tickHandler.start();
		tradeHandler.start();
		updater.start();
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
		
		while (true) {
			try {
				rd.readLine();
				stats.writeOutputFile("log" + File.separator + "stats.out");
				System.out.println("killed");
				System.exit(0);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
	
	
	
	private static void init() throws IOException {		
		System.out.println("The World has been created at " + new Date());
		
		//Create Tick Server
		tickSocket = new ServerSocket(TICKPORT);
		tickHandler = new TickHandler(tickSocket, new TickResponse());
		
		//Create Trade Server
		tradeSocket = new ServerSocket(TRADEPORT);
		tradeHandler = new TradeHandler(tradeSocket,world);
		
		updater = new TickUpdater(tickHandler, world);
	}
}
