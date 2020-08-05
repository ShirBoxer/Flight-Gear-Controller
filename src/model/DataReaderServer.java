package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class DataReaderServer {
	public BufferedReader in;
	static volatile boolean stop = false;
	private int port;
	private int frequency;

	public DataReaderServer(int port, int freq) {
		in = null;
		stop = false;
		this.port = port;
		if(freq == 0)
			System.out.println("Problem with frequency");
		this.frequency = freq;
	}

	public void runServer() {
		try {
			ServerSocket server=new ServerSocket(port);
			server.setSoTimeout(60000);
				try{
					while(!stop){
					Socket client=server.accept();
					in = new BufferedReader(new InputStreamReader(client.getInputStream()));
					String line, name;
					String[] lines;
					double value;
					while((line = in.readLine()) != null){//to read 10 times per sec
						lines = line.split(",");
						if(lines.length != MyInterpreter.varList.size()){
							System.out.println("Connections Problems");
							continue;
						}
						for(int i =0; i< lines.length;i++){
							name = MyInterpreter.varList.get(i);
							value = Double.parseDouble(lines[i]);
							MyInterpreter.symbolTable.get(name).set(value);

						}

						try {Thread.sleep(1000/frequency);} catch (InterruptedException e) {e.printStackTrace();}
					}
					in.close();
					client.close();

					}
				}catch(SocketTimeoutException e){}
			server.close();
		} catch (IOException e) {}
	}

	public static void close() {
		DataReaderServer.stop = true;
	}
}
