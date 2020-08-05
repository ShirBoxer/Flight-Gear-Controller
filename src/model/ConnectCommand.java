package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ConnectCommand implements Command {

	public static PrintWriter out;
	public static BufferedReader in;
	@SuppressWarnings("resource")
	@Override
	public double doCommand(String[] code) { //[ ip , port , ; ]
		try {
			Socket client = new Socket( InetAddress.getByName(code[0]), Integer.parseInt(code[1]));
			out = new PrintWriter(client.getOutputStream(),true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();}



		return 0.0;
	}


}
