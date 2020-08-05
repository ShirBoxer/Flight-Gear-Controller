package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;

import javafx.beans.property.StringProperty;

public class Model extends Observable{

	private double[][] mapHeights;
	private double maxHeight;
	private double minHeight;
	private double squareMeter;
	private double datumPointX;
	private double datumPointY;
	private String solution;


	public void connect(StringProperty ip, StringProperty port) {

		String[] argv = new String[3];
		argv[0] = "5400"; // port
		argv[1] = "10";	// frequency
		argv[2] = ";";
		MyInterpreter.commandMap.get("openDataServer").doCommand(argv);

		while( ( (odsCommand) MyInterpreter.commandMap.get("openDataServer")).drs.in == null){
			try {
				Thread.sleep(5000);
				System.out.println("waiting for client.");
			} catch (InterruptedException e) {e.printStackTrace();}
		}

		//load up the client
		argv[0] = ip.get();
		argv[1] = port.get();
		argv[2] = ";";
		 MyInterpreter.commandMap.get("connect").doCommand(argv);


		System.out.println("connections are up");//DEBUG

	}

	public void loadData(File f) {
		try {
			Scanner	s = new Scanner(f);
			String[] tmp = s.nextLine().split(",");
			setDatumPointX(Double.parseDouble(tmp[0]));
			setDatumPointY(Double.parseDouble(tmp[1]));
			MyInterpreter.symbolTable.get("positionX").set(Double.parseDouble(tmp[0]));
			MyInterpreter.symbolTable.get("positionY").set(Double.parseDouble(tmp[1]));
			tmp = s.nextLine().split(",");
			setSquareMeter(Double.parseDouble(tmp[0]));
			ArrayList<String[]> arr2D = new ArrayList<>();
			while(s.hasNextLine())
				arr2D.add(s.nextLine().split(","));


			setMaxHeight(0);
			setMinHeight(10000);// max realistic earth height
			double tmpD;
			setMapHeights(new double[arr2D.size()][arr2D.get(0).length]);

			for (int i = 0; i < arr2D.size(); i++)
				for (int j = 0; j < arr2D.get(0).length; j++){
					tmpD = Double.parseDouble(arr2D.get(i)[j]);
					getMapHeights()[i][j] = tmpD;
					if(tmpD < getMinHeight()) setMinHeight(tmpD);
					if(tmpD > getMaxHeight()) setMaxHeight(tmpD);
				}

			this.setChanged();
			this.notifyObservers(new String("mapVars"));

			s.close();

		}catch (FileNotFoundException e) {e.printStackTrace();}

	}

	public void drawPath(int startX, int startY,int desX, int desY) {
		int port = 37543;
		//connecting as a client
		try {
			//Thread.sleep(500);
			Socket clientSock = new Socket("127.0.0.1", port);
			PrintWriter outputClient = new PrintWriter(clientSock.getOutputStream());
			for (int i = 0; i < mapHeights.length; i++) {
				StringBuilder line = new StringBuilder();
				for (int j = 0; j < mapHeights[0].length; j++) {
					line.append(mapHeights[i][j]);
					line.append(",");
				}
				line.deleteCharAt(line.length()-1);

				outputClient.println(line.toString());
				outputClient.flush();
			}
			outputClient.println("end");
			outputClient.flush();


			// y for index i | x for index j
			outputClient.println(startY + ","+ startX);
			outputClient.flush();
			outputClient.println(desY + ","+ desX);
			outputClient.flush();
			BufferedReader inputClient = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
			while((solution = inputClient.readLine()).equals(null)){
				System.out.println("waiting for solution");
				Thread.sleep(1000);

			}
			if(solution != null){
				this.setChanged();
				this.notifyObservers("solution");
			}
			clientSock.close();
			inputClient.close();
			outputClient.close();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			}

	}

	public void interpret(String string) {
		String[] code = string.split("\n");
		new Thread(()->MyInterpreter.interpret(code)).start();
	}








	public double[][] getMapHeights() {
		return mapHeights;
	}

	public void setMapHeights(double[][] mapHeights) {
		this.mapHeights = mapHeights;
	}

	public double getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(double maxHeight) {
		this.maxHeight = maxHeight;
	}

	public double getMinHeight() {
		return minHeight;
	}

	public void setMinHeight(double minHeight) {
		this.minHeight = minHeight;
	}

	public double getSquareMeter() {
		return squareMeter;
	}

	public void setSquareMeter(double squareMeter) {
		this.squareMeter = squareMeter;
	}

	public double getDatumPointX() {
		return datumPointX;
	}

	public void setDatumPointX(double datumPointX) {
		this.datumPointX = datumPointX;
	}

	public double getDatumPointY() {
		return datumPointY;
	}

	public void setDatumPointY(double datumPointY) {
		this.datumPointY = datumPointY;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}




}
