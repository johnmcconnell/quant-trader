package main.sockets;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class MyResponse {

	protected void respond(InputStream inputStream, OutputStream outputStream) {
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		PrintWriter out = new PrintWriter(outputStream, true);
		out.println("Hello World!");
	}
}
